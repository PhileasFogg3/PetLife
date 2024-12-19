package net.phileasfogg3.petlife.Pets.Goals;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class OwnerHurtByTarget extends TargetGoal {
    private final Mob mob;
    private final LivingEntity owner;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public OwnerHurtByTarget(Mob mob, LivingEntity owner) {
        super(mob, false);
        this.mob = mob;
        this.owner = owner;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        if (this.owner == null) {
            return false;
        } else {
            this.ownerLastHurtBy = this.owner.getLastHurtByMob();
            int i = this.owner.getLastHurtByMobTimestamp();
            return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT);
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy, TargetReason.TARGET_ATTACKED_OWNER, true);
        if (this.owner != null) {
            this.timestamp = this.owner.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}

/*
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.phileasfogg3.petlife.Pets.Pet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.EnumSet;

public class OwnerHurtByTarget extends TargetGoal {

    private final Pet pet;
    private LivingEntity ownerLastHurtBy;
    private int timeStamp;

    public OwnerHurtByTarget(Mob pet) {

        super(pet, false);
        this.pet = (Pet) pet;
        this.setFlags(EnumSet.of(Flag.TARGET));

    }

    @Override
    public boolean canUse() {

        Player player = Bukkit.getPlayer(this.pet.getOwnerUUID());
        if (player == null) {return false;}
        else {
            LivingEntity petOwner = (LivingEntity) ((CraftEntity) player).getHandle();
            this.ownerLastHurtBy = petOwner.getLastHurtByMob();
            int i = petOwner.getLastHurtByMobTimestamp();
            return i != this.timeStamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT);
        }

        return false;
    }
}
*/