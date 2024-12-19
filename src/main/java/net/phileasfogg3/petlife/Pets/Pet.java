package net.phileasfogg3.petlife.Pets;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public interface Pet {

    void targetAttacker(Entity attacker);
    UUID getOwnerUUID();

    default org.bukkit.entity.Player GetPlayerBukkit(Player player) {
        return Bukkit.getPlayer(player.getUUID());
    }

    default Player GetPlayerMinecraft(org.bukkit.entity.Player player) {
        return (Player) ((CraftEntity) player).getHandle();
    }

}
