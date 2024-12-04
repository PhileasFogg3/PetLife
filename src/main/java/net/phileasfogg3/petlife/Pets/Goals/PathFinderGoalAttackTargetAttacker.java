package net.phileasfogg3.petlife.Pets.Goals;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.EnumSet;

public class PathFinderGoalAttackTargetAttacker extends Goal {

    private final Mob pet;
    private LivingEntity owner;
    private Entity target;
    private final double maxDistance;
    private double x; // x
    private double y; // y
    private double z; // z

    public PathFinderGoalAttackTargetAttacker(Mob mob, double maxDistance, Entity target) {

        this.target = target;
        System.out.println("-"+target);
        System.out.println("--" + this.target);
        this.pet = mob;
        this.maxDistance = maxDistance;
        this.setFlags(EnumSet.of(Flag.TARGET, Flag.MOVE, Flag.JUMP));

    }

    @Override
    public boolean canUse() {
        this.owner = getOwner(this.pet);
        System.out.println("0:" + this.owner);
        if (this.owner == null) {return false;}
        else if (this.pet.getDisplayName() == null) {return false;}
        else if (!this.pet.getDisplayName().getString().contains(this.owner.getName().getString())) {return false;}
        if (this.target != null && this.target.isDead()) {
            System.out.println("1:" + this.target);
            if (this.owner.distanceToSqr(this.target.getLocation().getX(), this.target.getLocation().getY(), this.target.getLocation().getY()) > 5) {
                return false;
            }
            System.out.println("The target for " + this.pet.getDisplayName() + " is " + this.target.getType());
            this.x = this.target.getLocation().getX();
            this.y = this.target.getLocation().getY();
            this.z = this.target.getLocation().getZ();
            System.out.println(this.target);
        }
        return true;
    }

    @Override
    public void start() {
        if (this.target == null) {System.out.println("ERRORRRRR");return;}
        LivingEntity targetMob = (LivingEntity) ((CraftEntity) this.target).getHandle();
        this.pet.setTarget(targetMob, EntityTargetEvent.TargetReason.CUSTOM, true);
        this.pet.goalSelector.addGoal(0, new MeleeAttackGoal((PathfinderMob) this.pet, 1.0D, true)  );
        //this.pet.getNavigation().moveTo(this.x, this.y, this.z, 1.0);
        this.pet.setAggressive(true);
        this.target.setGlowing(true);
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && !this.target.isDead() && this.target.equals(owner.getLastAttacker()) && this.pet.distanceToSqr(this.target.getLocation().getX(), this.target.getLocation().getY(), this.target.getLocation().getY()) >= maxDistance * maxDistance;
    }

    @Override
    public void stop() {
        if (this.target != null) {this.target.setGlowing(false);}
        this.pet.setAggressive(false);
        this.pet.setTarget(null);
        this.target = null;
        this.pet.setTarget((this.owner), EntityTargetEvent.TargetReason.CUSTOM, true);
        this.pet.goalSelector.addGoal(0, new FloatGoal(this.pet));
        this.pet.goalSelector.addGoal(2, new LookAtPlayerGoal(this.pet, net.minecraft.world.entity.player.Player.class, 8.0F));
        this.pet.goalSelector.addGoal(1, new PathFinderGoalPet(this.pet, 0.5D, 30));
    }

    public LivingEntity getOwner(Mob mob) {
        OwnableEntity pet = (OwnableEntity) mob;
        return pet.getOwner();
    }
}
