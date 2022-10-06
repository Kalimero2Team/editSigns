package me.laazuli.editSigns.listener;


import me.laazuli.editSigns.EditSigns;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SignListener implements Listener {

    private final Plugin plugin;
    private final ArrayList<Location> signLocations = new ArrayList<>();

    public SignListener(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignRightClick(PlayerInteractEvent event) throws Exception {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!event.getPlayer().isSneaking()) {
                EditSigns.openSignInterface(event.getPlayer(), event.getClickedBlock());
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Block block = event.getBlock();
        if(block.getState() instanceof Sign){
            Location location = block.getLocation();
            this.signLocations.add(location);
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event){
        Sign sign = (Sign) event.getBlock().getState();
        Location signLocation = sign.getLocation();
        if (this.signLocations.contains(signLocation)) {
            this.signLocations.remove(signLocation);
        }
        else{
            List<Component> lines = sign.lines();
            this.plugin.getLogger().log(Level.INFO, "Edited " + sign.getType().name() + " at " + signLocation
                    + " by " + event.getPlayer().getName() + " - " + "New Text: " + "1: " + lines.get(0) + " - 2: "
                    + lines.get(1) + " - 3: " + lines.get(2) + " - 4: " + lines.get(3));
        }
    }
}
