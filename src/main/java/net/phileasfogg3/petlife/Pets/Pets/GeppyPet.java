package net.phileasfogg3.petlife.Pets.Pets;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.phileasfogg3.petlife.PetLife;
import net.phileasfogg3.petlife.Pets.Goals.PathFinderGoalPet;
import net.phileasfogg3.petlife.Pets.PetAttributes;
import net.phileasfogg3.petlife.Pets.Pet;
import net.phileasfogg3.petlife.Pets.Goals.OwnerHurtByTarget;
import net.phileasfogg3.petlife.Pets.Goals.OwnerHurtTarget;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;
import java.util.UUID;

public class GeppyPet extends Frog implements OwnableEntity, Pet {

    Player owner;

    public GeppyPet(Location loc, PetAttributes characteristics, org.bukkit.entity.Player petOwner) {
        super(EntityType.FROG, ((CraftWorld) loc.getWorld()).getHandle());
        this.owner = GetPlayerMinecraft(petOwner);

        PetLife.Instance.addPet(petOwner, this);

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(characteristics.getAttack());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(characteristics.getHearts());
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(characteristics.getSpeed());

        this.setCustomName(Component.nullToEmpty(petOwner.getName() + "'s Frog"));
        this.setCustomNameVisible(true);

        registerGoals();
    }

    private void initPathfinderGoals() {

        //this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, true));

        //this.goalSelector.addGoal(0, new FloatGoal(this));
        //this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
        //this.goalSelector.addGoal(1, new PathFinderGoalPet(this, 0.5D, 30));
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        //this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new PathFinderGoalPet(this, 1.0, 10.0F));
        //this.goalSelector.addGoal(7, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTarget(this, owner));
        this.targetSelector.addGoal(2, new OwnerHurtTarget(this, owner));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
    }

    @Override
    public LivingEntity getTarget() {
        return this.owner;
    }

    @Override
    public AttributeMap getAttributes() {
        AttributeMap attributeMap = new AttributeMap(Zombie.createAttributes().build());

        attributeMap.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(500);
        attributeMap.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
        attributeMap.getInstance(Attributes.MAX_HEALTH).setBaseValue(40.0);

        return attributeMap;
    }

    @Override
    public void targetAttacker(Entity attacker) {

    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.owner.getUUID();
    }
}
