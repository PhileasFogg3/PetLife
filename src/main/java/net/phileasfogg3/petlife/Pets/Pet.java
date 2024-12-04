package net.phileasfogg3.petlife.Pets;

import org.bukkit.entity.Entity;

import java.util.UUID;

public interface Pet {

    void targetAttacker(Entity attacker);
    UUID getOwnerUUID();

}
