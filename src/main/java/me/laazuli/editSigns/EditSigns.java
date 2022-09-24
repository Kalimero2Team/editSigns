package me.laazuli.editSigns;

import me.laazuli.editSigns.commands.editViaInterface;
import me.laazuli.editSigns.commands.setSignTextline;
import me.laazuli.editSigns.listener.SignListener;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class EditSigns extends JavaPlugin {

    private static final boolean aufChunkGeadded = true;

    @Override
    public void onEnable(){
        getLogger().info("Loading plugin");
        getServer().getPluginManager().registerEvents(new SignListener(this),this);
        Objects.requireNonNull(getCommand("setSign")).setExecutor(new setSignTextline());
        Objects.requireNonNull(getCommand("editSign")).setExecutor(new editViaInterface());
        getLogger().info("Plugin successfully loaded");
    }

    public static boolean openSignInterface(Player player, Block block) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (aufChunkGeadded) {
            BlockState probSign = block.getState();
            if (probSign instanceof Sign sign) {
                Method method = Player.class.getMethod("openSign", Sign.class);
                method.invoke(player, sign);
                return true;
            }
        }
        return false;
    }
}
