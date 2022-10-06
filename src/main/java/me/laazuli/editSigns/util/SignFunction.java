package me.laazuli.editSigns.util;

import org.bukkit.Location;

@FunctionalInterface
public interface SignFunction {
    boolean doAction(Location signLocation, int lineNum);
}
