package me.laazuli.editSigns;

import com.kalimero2.team.claims.api.ClaimsApi;
import com.kalimero2.team.claims.api.ClaimsChunk;
import me.laazuli.editSigns.listener.SignListener;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class EditSigns extends JavaPlugin {


    public static boolean openSignInterface(Player player, Block block) {
        Chunk chunk = block.getChunk();
        ClaimsChunk claimsChunk = ClaimsApi.getApi().getChunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getUID());
        if (claimsChunk.isClaimed() && (claimsChunk.getOwner().equals(player.getUniqueId()) || claimsChunk.getTrustedList().contains(player.getUniqueId()))) {
            BlockState probSign = block.getState();
            if (probSign instanceof Sign sign) {
                player.openSign(sign);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        getLogger().info("Loading plugin");
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        getLogger().info("Plugin successfully loaded");
    }
}
