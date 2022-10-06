package me.laazuli.editSigns;

import me.laazuli.editSigns.commands.SignCommand;
import me.laazuli.editSigns.listener.SignListener;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class EditSigns extends JavaPlugin {

    private static final boolean aufChunkGeadded = true;

    @Override
    public void onEnable(){
        getLogger().info("Loading plugin");
        getServer().getPluginManager().registerEvents(new SignListener(this),this);
        Objects.requireNonNull(getCommand("sign")).setExecutor(new SignCommand(this));
        getLogger().info("Plugin successfully loaded");
    }

    public static boolean openSignInterface(Player player, Block block) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (aufChunkGeadded) {
            if (block.getState() instanceof Sign sign) {
                player.openSign(sign);
                return true;
            }
        }
        return false;
    }
}
