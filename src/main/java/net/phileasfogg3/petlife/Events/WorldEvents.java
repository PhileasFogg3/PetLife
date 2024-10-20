package net.phileasfogg3.petlife.Events;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.SculkShrieker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.CraftItemEvent;

public class WorldEvents implements Listener {
    @EventHandler
    public void onLightningStrike(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (e.getRecipe().getResult().getType() == Material.ENCHANTING_TABLE) {
            e.setCancelled(true);
            e.getWhoClicked().sendMessage("You cannot craft Enchanting Tables in Pet Life!");
        }
    }

    @EventHandler
    public void onShriekerPlaceEvent(BlockPlaceEvent e) {
        BlockState state = e.getBlock().getState();
        BlockData data = state.getBlockData();
        if (data instanceof SculkShrieker) {
            ((SculkShrieker) data).setCanSummon(true);
        }
        state.setBlockData(data);
        state.update();
    }
}
