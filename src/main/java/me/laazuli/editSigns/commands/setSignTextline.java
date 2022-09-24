package me.laazuli.editSigns.commands;

import me.laazuli.editSigns.commands.commandUtil.PositionTabCompletion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class setSignTextline extends PositionTabCompletion implements CommandExecutor, TabCompleter {

    private void sendWrongUsageError(CommandSender sender){
        sender.sendMessage(Component.text(ChatColor.DARK_RED + "Make sure to use the command in this way: /editsign <X> <Y> <Z> <linenumber> \"<Text>\"").decorate(TextDecoration.ITALIC));
    }

    private boolean editSignAtLine(CommandSender sender, String[] args, Location location) {
        int lineNumber;
        try{
            lineNumber = Integer.parseInt(args[3]);
            args[4].length();
        } catch (Exception e) {
            sendWrongUsageError(sender);
            return false;
        }
        if (!(args[4].startsWith("\"") && args[args.length-1].endsWith("\""))) {
            sendWrongUsageError(sender);
            return false;
        }
        StringBuilder newText = new StringBuilder(args[4]);
        for (int i=5; i<args.length; i++){
            newText.append(" ").append(args[i]);
        }
        return executeEditSignAtLine(sender, location, lineNumber, newText.substring(1, newText.length() - 1));
    }
    private boolean executeEditSignAtLine(CommandSender sender, Location signLocation, int lineNumber, String newText){
        BlockState isThatASign = signLocation.getBlock().getState();
        if (isThatASign instanceof Sign sign){
            if (newText == null) newText = "";
            if (0 < lineNumber && lineNumber < 5) {
                sign.line(lineNumber - 1, Component.text(newText));
                sign.update();
                return true;
            }
            sender.sendMessage(Component.text(ChatColor.DARK_RED + "A sign only has 4 lines!"));
            return false;
        }
        sender.sendMessage(Component.text(ChatColor.DARK_RED + "No sign found on given coordinates"));
        return false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            Location signLocation;
            try{
                signLocation = new Location(player.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            }
            catch (Exception ignored){
                sendWrongUsageError(sender);
                return false;
            }
            return editSignAtLine(sender, args, signLocation);

        } else if (sender instanceof CommandBlock commandBlock) {
            Location signLocation;
            try{
                signLocation = new Location(commandBlock.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            }
            catch (Exception ignored){
                sendWrongUsageError(sender);
                return false;
            }
            return editSignAtLine(sender, args, signLocation);
        }

        return false;


        /*
        if (sender instanceof Player player){
            Location signLocation;
            int lineNumber;
            try{
                signLocation = new Location(player.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                lineNumber = Integer.parseInt(args[3]);
                args[4].length();
            } catch (Exception e) {
                sendWrongUsageError(sender);
                return false;
            }
            if (!(args[4].startsWith("\"") && args[args.length-1].endsWith("\""))) {
                sendWrongUsageError(sender);
                return false;
            }
            StringBuilder newText = new StringBuilder(args[4]);
            for (int i=5; i<args.length; i++){
                newText.append(" ").append(args[i]);
            }
            return executeEditSignAtLine(sender, signLocation, lineNumber, newText.substring(1, newText.length() - 1));
        }
        if (sender instanceof CommandBlock commandBlock){
            Location signLocation;
            int lineNumber;
            try{
                signLocation = new Location(commandBlock.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                lineNumber = Integer.parseInt(args[3]);
                args[4].length();
            } catch (Exception e) {
                sendWrongUsageError(sender);
                return false;
            }
            if (!(args[4].startsWith("\"") && args[args.length-1].endsWith("\""))) {
                sendWrongUsageError(sender);
                return false;
            }
            StringBuilder newText = new StringBuilder(args[4]);
            for (int i=5; i<args.length; i++){
                newText.append(" ").append(args[i]);
            }
            return executeEditSignAtLine(sender, signLocation, lineNumber, newText.substring(1, newText.length() - 1));
        }
         */

    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        ArrayList<String> returnList = new ArrayList<>();

        if (sender instanceof Player p) {
            positionTabComplete(p, args, returnList);
            if (args.length == 4) {
                for (int i = 4; i > 0; i--) {
                    returnList.add(Integer.toString(i));
                }
            } else if (args.length == 5) {
                returnList.add("\"\"");
            }
        }


        else if (sender instanceof CommandBlock c) {
            String blockX = Integer.toString(c.getX());
            String blockY = Integer.toString(c.getY());
            String blockZ = Integer.toString(c.getZ());
            positionTabComplete(blockX, blockY, blockZ, args, returnList);
            if (args.length == 4) {
                for (int i=4; i>0; i--) {
                    returnList.add(Integer.toString(i));
                }
            }
            else if (args.length == 5) {
                returnList.add("\"\"");
            }
        }


        return returnList;
    }
}
