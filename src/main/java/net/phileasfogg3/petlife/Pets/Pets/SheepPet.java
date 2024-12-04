package net.phileasfogg3.petlife.Pets.Pets;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Zombie;
import net.phileasfogg3.petlife.PetLife;
import net.phileasfogg3.petlife.Pets.Goals.PathFinderGoalAttackTargetAttacker;
import net.phileasfogg3.petlife.Pets.Goals.PathFinderGoalPet;
import net.phileasfogg3.petlife.Pets.Pet;
import net.phileasfogg3.petlife.Pets.PetAttributes;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

import javax.annotation.Nullable;
import java.util.UUID;

public class SheepPet extends Sheep implements OwnableEntity, Pet {

    UUID owner;
    PetAttributes petAttributes;

    public SheepPet(Location loc, PetAttributes characteristics, Player petOwner) {
        super(EntityType.SHEEP, ((CraftWorld) loc.getWorld()).getHandle());

        Wolf wolf;

        PetLife.Instance.addPet(petOwner, this);

        this.petAttributes = characteristics;
        this.owner = petOwner.getUniqueId();
        System.out.println(characteristics.getSpeed());
        System.out.println(characteristics.getHearts());
        System.out.println(characteristics.getAttack());

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(characteristics.getAttack());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(characteristics.getHearts());
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(characteristics.getSpeed());

        this.setCustomName(Component.nullToEmpty(petOwner.getName() + "'s Sheep"));
        this.setCustomNameVisible(true);

        initPathfinderGoals(characteristics.getAbility());

        this.setTarget(((CraftPlayer) petOwner).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
    }

    private void initPathfinderGoals(String ability) {

        if (ability == null) {System.out.println("Spawning a pet without a custom ability");}

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
        this.goalSelector.addGoal(1, new PathFinderGoalPet(this, 0.5D, 30));

    }


    @Override
    public AttributeMap getAttributes() {
        AttributeMap attributeMap = new AttributeMap(Zombie.createAttributes().build());

        attributeMap.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3);
        attributeMap.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
        attributeMap.getInstance(Attributes.MAX_HEALTH).setBaseValue(40.0);

        return attributeMap;
    }

    @Override
    public void targetAttacker(Entity attacker) {
        if (attacker !=null) {
            System.out.println("3");
            if (this.getTarget() == this.getOwner()) {
                System.out.println("4");
                System.out.println(attacker);
                this.setTarget((LivingEntity) ((CraftEntity) attacker).getHandle(), EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER, true);
                this.targetSelector.addGoal(0, new PathFinderGoalAttackTargetAttacker(this, 30, attacker));
            }
        }
    }

    @Override
    public UUID getOwnerUUID() {
        return owner;
    }
}
