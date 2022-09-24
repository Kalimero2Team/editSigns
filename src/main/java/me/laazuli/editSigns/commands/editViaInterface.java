package me.laazuli.editSigns.commands;

import me.laazuli.editSigns.EditSigns;
import me.laazuli.editSigns.commands.commandUtil.PositionTabCompletion;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class editViaInterface extends PositionTabCompletion implements CommandExecutor, TabCompleter  {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            Location signLocation;
            try{
                signLocation = new Location(p.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            }
            catch (Exception igonored) {
                sender.sendMessage(Component.text(ChatColor.DARK_RED + "Make sure to use the command in this way: /editsign <X> <Y> <Z>"));
                return false;
            }
            if (!(signLocation.getBlock().getState() instanceof Sign)){
                sender.sendMessage(Component.text(ChatColor.DARK_RED + "No sign found on given coordinates"));
                return false;
            }
            try {
                EditSigns.openSignInterface(p, signLocation.getBlock());
                return true;
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> returnList = new ArrayList<>();
        if (sender instanceof Player p) {
            positionTabComplete(p, args, returnList);
        }
        return returnList;
    }
}
