package net.phileasfogg3.petlife.Pets.Goals;


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
