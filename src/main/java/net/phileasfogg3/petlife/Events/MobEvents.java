package net.phileasfogg3.petlife.Events;

import net.nexia.nexiaapi.Config;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MobEvents implements Listener {

    private Config gameMgr;
    private Config playerData;
    private Config messagesData;

    public MobEvents(Config gameMgr, Config playerData, Config messagesData) {
        this.gameMgr = gameMgr;
        this.playerData = playerData;
        this.messagesData = messagesData;
    }

    private final double dropChance = gameMgr.getData().getDouble("spawn-egg-drop-chance");

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Check if the entity was killed by a player
        if (!(event.getEntity().getKiller() instanceof Player)) {
            return;
        }

        // Get the entity type
        EntityType entityType = event.getEntityType();

        // Determine if a spawn egg should drop
        if (new Random().nextDouble() < dropChance) {
            Material spawnEggMaterial = getSpawnEggMaterial(entityType);

            if (spawnEggMaterial != null) {
                // Drop the spawn egg at the location of the entity
                ItemStack spawnEgg = new ItemStack(spawnEggMaterial, 1);
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), spawnEgg);
            }
        }
    }

    private Material getSpawnEggMaterial(EntityType entityType) {
        // Dynamically find the spawn egg material
        String spawnEggName = entityType.name() + "_SPAWN_EGG";
        return Material.matchMaterial(spawnEggName);
    }
}
