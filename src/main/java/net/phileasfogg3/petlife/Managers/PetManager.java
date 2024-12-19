package net.phileasfogg3.petlife.Managers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.phileasfogg3.petlife.PetLife;
import net.phileasfogg3.petlife.Pets.PetAttributes;
import net.phileasfogg3.petlife.Pets.Pets.GeppyPet;
import net.phileasfogg3.petlife.Pets.Pets.SheepPet;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.entity.Player;
import net.nexia.nexiaapi.Config;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PetManager {

    private Config gameMgr;
    private Config playerData;
    private Config petsData;

    private final Map<UUID, Entity> playerPets = new HashMap<>();

    public PetManager(Config gameMgr, Config playerData, Config petsData) {
        this.gameMgr = gameMgr;
        this.playerData = playerData;
        this.petsData = petsData;
    }

    public void summonPet(String petType, Player petOwner) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        PetAttributes characteristics = PetLife.Instance.getAttributes(petType);
        
        if (characteristics == null) {
            System.out.println("Invalid Pet type");
            return;
        }

        LivingEntity pet = null;
        ServerLevel world = ((CraftWorld) petOwner.getWorld()).getHandle();

        switch (petType) {
            
            case "Sheep":
                pet = new SheepPet(petOwner.getLocation(), characteristics, petOwner);
                break;
            case "Geppy":
                pet = new GeppyPet(petOwner.getLocation(), characteristics, petOwner);
                break;
            default:
                System.out.println(petType + " has not yet been implemented");
        }
        
        pet.setPos(petOwner.getLocation().getX(), petOwner.getLocation().getY(), petOwner.getLocation().getZ());
        world.addFreshEntity(pet);
        playerPets.put(petOwner.getUniqueId(), pet);
        System.out.println("Attack: " + characteristics.getAttack() + " Speed: " +characteristics.getSpeed() + " Health: " + characteristics.getHearts() + " Ability: " + characteristics.getAbility());

    }
}
