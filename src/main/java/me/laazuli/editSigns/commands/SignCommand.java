package me.laazuli.editSigns.commands;

import me.laazuli.editSigns.EditSigns;
import me.laazuli.editSigns.commands.commandUtil.PositionTabCompletion;
import me.laazuli.editSigns.commands.commandUtil.SignFunction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SignCommand extends PositionTabCompletion implements CommandExecutor, TabCompleter {


    /** Determines how many arguments of the base command /sign are used to navigate to specific functions.
     * /sign setline [coords, line number, new text] --> 1 navigation argument **/

    private final Plugin plugin;

    private final Component signLengthErrorMsg = formatPluginMsgs("A sign only has 4 Lines!", ChatColor.DARK_RED);
    private final Component noSignFoundErrorMsg = formatPluginMsgs("No sign found on given coordinates", ChatColor.DARK_RED);

    public SignCommand(Plugin plugin) {
        this.plugin = plugin;
    }

   private Component incorrectUsageMsg(String commandName){
       return formatPluginMsgs("Make sure to use the command in this way: /sign " +
               commandName +  " <X> <Y> <Z>", ChatColor.DARK_RED);
   }

   private Component formatPluginMsgs(String msg, ChatColor chatColor){
       return (Component.text( chatColor+ msg)).decoration(TextDecoration.ITALIC, true);
   }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int lineNum = -1;
        int subCommands;
        World world;
        String commandName;
        SignFunction signAction = (signLocation, lineNum1) -> {
            throw new NullPointerException();
        };

        if (sender instanceof Player || sender instanceof CommandBlock){

            subCommands = 1;
            switch (args[0]) {
                case "setline" -> {
                    commandName = "setline";
                    StringBuilder newText = new StringBuilder();
                    try {
                        String s = args[subCommands + 4];
                        lineNum = Integer.parseInt(args[subCommands+3]);
                    } catch (Exception ignored) {
                        sender.sendMessage(incorrectUsageMsg(commandName).
                                append(formatPluginMsgs(" <linenumber> \"<Text>\"", ChatColor.DARK_RED)));
                        return false;
                    }
                    if (lineNum < 1 || lineNum > 4){
                        sender.sendMessage(signLengthErrorMsg);
                        return false;
                    }

                    signAction = (Location loc, int lnNum) -> {
                        newText.append(args[subCommands + 4]);
                        if (args.length >= subCommands + 6) {
                            for (int i = subCommands + 5; i < args.length; i++) {

                                newText.append(" ").append(args[i]);
                            }
                        }

                        if (!(args[subCommands + 4].startsWith("\"") && args[args.length - 1].endsWith("\""))) {
                            sender.sendMessage(incorrectUsageMsg(commandName).
                                    append(formatPluginMsgs(" <linenumber> \"<Text>\"", ChatColor.DARK_RED)));
                            return false;
                        }

                        if (newText.length() > 18) {
                            sender.sendMessage(formatPluginMsgs("Your text ist too long! Applied text: "
                                    + newText.substring(1, 17) + "[" + newText.substring(17, newText.length() - 1) + "]",
                                    ChatColor.GRAY));
                        }

                        if (loc.getBlock().getState() instanceof Sign sign) {
                            String substring;
                            if (newText.length() > 16) {
                                substring = newText.substring(1, 17);
                            } else {
                                substring = newText.substring(1, newText.length() - 1);
                            }
                            sign.line(lnNum - 1, Component.text(substring));
                            sign.update();
                            return true;
                        }

                        sender.sendMessage(noSignFoundErrorMsg);
                        return false;
                    };
                }
                case "getline" -> {
                    commandName = "getline";
                    try {
                        lineNum = Integer.parseInt(args[subCommands+3]);
                    } catch (Exception ignored) {
                        sender.sendMessage(incorrectUsageMsg(commandName).
                                append(formatPluginMsgs(" <linenumber>", ChatColor.DARK_RED)));
                        return false;
                    }
                    if (lineNum < 1 || lineNum > 4){
                        sender.sendMessage(signLengthErrorMsg);
                        return false;
                    }

                    signAction = (Location loc, int lnNum) -> {
                        System.out.println(loc);
                        if (loc.getBlock().getState() instanceof Sign sign) {
                            sender.sendMessage(formatPluginMsgs("Sign at " + sign.getX() + " " + sign.getY() + " " + sign.getZ() + ", Line " + lnNum + ": " + (sign.line((lnNum - 1))), ChatColor.GRAY));
                            return true;
                        }
                        sender.sendMessage(noSignFoundErrorMsg);
                        return false;
                    };
                }
                case "edit" -> {
                    if (sender instanceof Player player){
                        signAction = (Location loc, int ignored) -> {
                            if (loc.getBlock().getState() instanceof Sign) {
                                try {
                                    return EditSigns.openSignInterface(player, loc.getBlock());
                                } catch (Exception e) {
                                    plugin.getLogger().log(Level.INFO, "Internal Error: staticmethod EditSigns.openSignInterface() " + e);
                                    return false;
                                }
                            }
                            sender.sendMessage(Component.text(ChatColor.DARK_RED + "No sign found on given coordinates")
                                    .decoration(TextDecoration.ITALIC, true));
                            return false;
                        };
                    }
                }
                default -> {
                    sender.sendMessage(formatPluginMsgs("Invalid sub command", ChatColor.DARK_RED));
                    return false;
                }
            }
            if (sender instanceof Player player) {
                world = player.getWorld();
            }
            else {
                world = ((CommandBlock) sender).getWorld();
            }
        }
        else {
            return false;
        }

        Location signLocation;

        try {
            signLocation = new Location(world, Integer.parseInt(args[subCommands]), Integer.parseInt(args[subCommands+1]), Integer.parseInt(args[subCommands+2]));
            System.out.println(signLocation);
        }
        catch (Exception ignored){
            sender.sendMessage(incorrectUsageMsg("<subCommand>"));
            return false;
        }

        return signAction.doAction(signLocation, lineNum);
    }


        /*

        World world = null;
        int subCommands = Integer.parseInt(null);

        if (sender instanceof Player player) {
            sender.sendMessage("new code");

            String commandName;

            world = player.getWorld();

            switch (args[0]) {
                case "setline":
                    commandName = "setline";
                    subCommands = 1;

                    StringBuilder newText = new StringBuilder();

                    try {
                        String s = args[subCommands + 4];
                    } catch (Exception ignored) {
                        sender.sendMessage(incorrectUsageMsg(commandName));
                        return false;
                    }
                    if (!(args[subCommands + 4].startsWith("\"") && args[args.length - 1].endsWith("\""))) {
                        sender.sendMessage(incorrectUsageMsg("setline"));
                        return false;
                    }

                    newText.append(args[subCommands + 4]);
                    if (args.length >= subCommands + 6) {
                        for (int i = subCommands + 5; i < args.length; i++) {

                            newText.append(" ").append(args[i]);
                        }
                    }

                    if (newText.length() > 18) {
                        sender.sendMessage((Component.text(ChatColor.GRAY + "Your text ist too long! Applied text: "
                                + newText.substring(1, 17) + "[" + newText.substring(17, newText.length() - 1) + "]"))
                                .decoration(TextDecoration.ITALIC, true));
                    }

                    return doSignAction(args, subCommands, sender, world, commandName, (Location signLocation, int lineNum) -> {
                        if (signLocation.getBlock().getState() instanceof Sign sign) {
                            String substring;
                            if (newText.length() > 16) {
                                substring = newText.substring(1, 17);
                            } else {
                                substring = newText.substring(1, newText.length() - 1);
                            }
                            sign.line(lineNum - 1, Component.text(substring));
                            sign.update();
                            return true;
                        }
                        sender.sendMessage(Component.text(ChatColor.DARK_RED + "No sign found on given coordinates")
                                .decoration(TextDecoration.ITALIC, true));
                        return false;
                    });

                case "getline":
                    commandName = "getline";
                    subCommands = 1;

                    return doSignAction(args, subCommands, sender, world, commandName, (Location signLocation, int lineNum) -> {
                        if (signLocation.getBlock().getState() instanceof Sign sign) {
                            sign.line(lineNum);
                        }
                        sender.sendMessage(Component.text(ChatColor.DARK_RED + "No sign found on given coordinates")
                                .decoration(TextDecoration.ITALIC, true));
                        return false;
                    });


                case "edit":
                    break;

                default:
                    sender.sendMessage(Component.text(ChatColor.DARK_RED + "This subcommand does not exist!"));
                    return false;
            }
            // return doSignAction(args, subCommands, sender)
        }
        else if (sender instanceof CommandBlock commandBlock) {

        }
        else {
            return false;
        }

        try {
            Location signLocation = new Location(world, Integer.parseInt(args[subCommands]),
                    Integer.parseInt(args[subCommands+1]), Integer.parseInt(args[subCommands+2]));
        }


        return false;
    }
         */
        /*
         if (sender instanceof Player player){
            Location signLocation;
            int lineNum;

            switch (args[0]) {
                case "setline":
                    StringBuilder newText = new StringBuilder();
                    try {
                        signLocation = new Location(player.getWorld(), Integer.parseInt(args[navigationArgs]),
                                Integer.parseInt(args[navigationArgs + 1]), Integer.parseInt(args[navigationArgs + 2]));
                        lineNum = Integer.parseInt(args[navigationArgs + 3]);
                        String s = args[navigationArgs + 4];
                    } catch (Exception ignored) {
                        sender.sendMessage(incorrectUsageMsg("setline"));
                        return false;
                    }
                    if (!(lineNum > 0 && lineNum < 5)) {
                        sender.sendMessage((Component.text(ChatColor.DARK_RED + "A sign only has 4 Lines!"))
                                .decoration(TextDecoration.ITALIC, true));
                        return false;
                    }
                    if (!(args[navigationArgs + 4].startsWith("\"") && args[args.length - 1].endsWith("\""))) {
                        sender.sendMessage(incorrectUsageMsg("setline"));
                        return false;
                    }

                    newText.append(args[navigationArgs + 4]);
                    if (args.length >= navigationArgs + 6) {
                        for (int i = navigationArgs + 5; i < args.length; i++) {

                            newText.append(" ").append(args[i]);
                        }
                    }

                    if (newText.length() > 18){
                        sender.sendMessage((Component.text(ChatColor.GRAY + "Your text ist too long! Applied text: "
                                + newText.substring(1, 17) + "[" + newText.substring(17, newText.length()-1) +"]"))
                                .decoration(TextDecoration.ITALIC, true));
                    }

                    if (signLocation.getBlock().getState() instanceof Sign sign) {
                        String substring;
                        if (newText.length() > 16){
                            substring = newText.substring(1, 17);
                        }
                        else {
                            substring = newText.substring(1, newText.length()-1);
                        }
                        sign.line(lineNum-1, Component.text(substring));
                        sign.update();
                        return true;
                    }
                    sender.sendMessage(Component.text(ChatColor.DARK_RED + "No sign found on given coordinates")
                            .decoration(TextDecoration.ITALIC, true));
                    return false;
                case "getline":
                    try {
                        signLocation = new Location(player.getWorld(), Integer.parseInt(args[navigationArgs]),
                                Integer.parseInt(args[navigationArgs + 1]), Integer.parseInt(args[navigationArgs + 2]));
                        lineNum = Integer.parseInt(args[navigationArgs + 3]);
                        // String s = args[navigationArgs + 4];
                    } catch (Exception ignored) {
                        // abgeändert
                        sender.sendMessage(incorrectUsageMsg("getline"));
                        return false;
                    }
                    if (!(lineNum > 0 && lineNum < 5)) {
                        sender.sendMessage((Component.text(ChatColor.DARK_RED + "A sign only has 4 Lines!"))
                                .decoration(TextDecoration.ITALIC, true));
                        return false;
                    }
                    /*
                    if (!(args[navigationArgs + 4].startsWith("\"") && args[args.length - 1].endsWith("\""))) {
                        sender.sendMessage(incorrectUsageMsg("setline"));
                        return false;
                    }
                     ///*

                    /*
                    newText.append(args[navigationArgs + 4]);
                    if (args.length >= navigationArgs + 6) {
                        for (int i = navigationArgs + 5; i < args.length; i++) {

                            newText.append(" ").append(args[i]);
                        }
                    }

                    if (newText.length() > 18){
                        sender.sendMessage((Component.text(ChatColor.GRAY + "Your text ist too long! Applied text: "
                                + newText.substring(1, 17) + "[" + newText.substring(17, newText.length()-1) +"]"))
                                .decoration(TextDecoration.ITALIC, true));
                    } ///*

                    // abgeändert
                    if (signLocation.getBlock().getState() instanceof Sign sign) {
                        sign.line(lineNum);
                    }
                    sender.sendMessage(Component.text(ChatColor.DARK_RED + "No sign found on given coordinates")
                            .decoration(TextDecoration.ITALIC, true));
                    return false;

                case "edit":

                    break;
            }
        }


        return false;

                     */

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int subCommands = 1;
        ArrayList<String> returnList = new ArrayList<>();

        if (sender instanceof Player || sender instanceof CommandBlock) {

            if (args.length == subCommands) {
                returnList.add("setline");
                returnList.add("getline");
                if (sender instanceof Player) returnList.add("edit");
            }

            if (sender instanceof Player player) {
                positionTabComplete(player, args, returnList, subCommands);
            }
            else {
                CommandBlock commandBlock = (CommandBlock) sender;
                positionTabComplete(commandBlock.getX(), commandBlock.getY(), commandBlock.getZ(), args, returnList, subCommands);
            }


            if (args[0].equals("setline") || args[0].equals("getline")) {
                if (args.length == subCommands + 4) {
                    for (int i = 1; i < 5; i++) {
                        returnList.add(Integer.toString(i));
                    }
                }
            }
            if (args[0].equals("setline")) {
                if (args.length == subCommands + 5) {
                    returnList.add("\"\"");
                }
            }
        }


        return returnList;
    }
}
