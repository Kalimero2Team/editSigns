package me.laazuli.editSigns.commands.commandUtil;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class PositionTabCompletion {

    protected void positionTabComplete(Player player, String[] args, ArrayList<String> returnList){
        String x;
        String y;
        String z;
        if (Objects.requireNonNull(player.getTargetBlock(4)).getState() instanceof Sign){
            x = Integer.toString(Objects.requireNonNull(player.getTargetBlock(4)).getX());
            y = Integer.toString(Objects.requireNonNull(player.getTargetBlock(4)).getY());
            z = Integer.toString(Objects.requireNonNull(player.getTargetBlock(4)).getZ());
        }
        else {
            x = Integer.toString((int) player.getLocation().getX());
            y = Double.toString((int) player.getLocation().getY());
            z = Double.toString((int) player.getLocation().getZ());
        }
        positionTabComplete(x, y, z, args, returnList);
    }

    protected void positionTabComplete(String x, String y, String z, String[] args, ArrayList<String> returnList) {
        if (args.length == 1) {
            returnList.add(x);
            returnList.add(x + " " + y);
            returnList.add(x + " " + y + " " + z);
        } else if (args.length == 2) {
            returnList.add(y);
            returnList.add(y + " " + z);
        } else if (args.length == 3) {
            returnList.add(z);
        }
    }
}
