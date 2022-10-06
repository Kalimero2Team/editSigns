package me.laazuli.editSigns.commands.commandUtil;

import org.bukkit.Location;

@FunctionalInterface
public interface SignFunction {
    boolean doAction(Location signLocation, int lineNum);
}
