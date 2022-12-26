package me.laazuli.editSigns.listener;


import me.laazuli.editSigns.EditSigns;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class SignListener implements Listener {

    private final Plugin plugin;

    public SignListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!event.getPlayer().isSneaking() && event.getClickedBlock() != null) {
                EditSigns.openSignInterface(event.getPlayer(), event.getClickedBlock());
            }
        }
    }

}
