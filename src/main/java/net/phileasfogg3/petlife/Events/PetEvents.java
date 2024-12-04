package net.phileasfogg3.petlife.Events;

import net.minecraft.world.entity.Entity;
import net.phileasfogg3.petlife.PetLife;
import net.phileasfogg3.petlife.Pets.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class PetEvents implements Listener {

    @EventHandler
    public void onPlayerDamaged(EntityDamageByEntityEvent e) {
        System.out.println("0");
        if (e.getEntity() instanceof Player) {
            System.out.println("1");
            Player owner = ((Player) e.getEntity()).getPlayer();
            Pet pet = PetLife.Instance.petMap.get(owner.getUniqueId());
            if (pet != null && e.getDamager() != null) {
                System.out.println("2");
                pet.targetAttacker(e.getDamager());
            }
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent e) {

        if (e.getEntity().getKiller() instanceof Pet) {

            if (PetLife.Instance.petMap.containsValue(e.getEntity().getKiller())) {



            }

        }

    }
}
