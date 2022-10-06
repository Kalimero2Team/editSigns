package me.laazuli.editSigns.commands.commandUtil;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class PositionTabCompletion {

    protected void positionTabComplete(Player player, String[] args, ArrayList<String> returnList, int subCommands){
        int x;
        int y;
        int z;
        if (Objects.requireNonNull(player.getTargetBlock(4)).getState() instanceof Sign){
            x = Objects.requireNonNull(player.getTargetBlock(4)).getX();
            y = Objects.requireNonNull(player.getTargetBlock(4)).getY();
            z = Objects.requireNonNull(player.getTargetBlock(4)).getZ();
        }
        else {
            x = (int) player.getLocation().getX();
            y = (int) player.getLocation().getY();
            z = (int) player.getLocation().getZ();
        }
        positionTabComplete(x, y, z, args, returnList, subCommands);
    }

    protected void positionTabComplete(int x, int y, int z, String[] args, ArrayList<String> returnList, int subCommands) {
        if (args.length == subCommands + 1) {
            returnList.add(Integer.toString(x));
            returnList.add(x + " " + y);
            returnList.add(x + " " + y + " " + z);
        } else if (args.length == subCommands + 2) {
            returnList.add(Integer.toString(y));
            returnList.add(y + " " + z);
        } else if (args.length == subCommands + 3) {
            returnList.add(Integer.toString(z));
        }
    }
}
