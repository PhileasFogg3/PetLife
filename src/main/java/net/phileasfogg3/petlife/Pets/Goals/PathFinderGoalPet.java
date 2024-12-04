package net.phileasfogg3.petlife.Pets.Goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class PathFinderGoalPet extends Goal {
    private final Mob pet; // our pet
    private LivingEntity owner; // owner
    private final double speed; // pet's speed
    private final float distance; // distance between owner & pet
    private double x; // x
    private double y; // y
    private double z; // z


    public PathFinderGoalPet(Mob mob, double speed, float distance) {
        this.pet = mob;
        this.speed = speed;
        this.distance = distance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET, Flag.JUMP)); // what it will do in the goal
    }

    @Override
    public boolean canUse() {
        this.owner = this.pet.getTarget();
        //null checks below
        if (this.owner == null) return false;
        else if (this.pet.getDisplayName() == null) return false;
        else if (!this.pet.getDisplayName().getString().contains(this.owner.getName().getString()))
            return false; // check if the pet is owner pet by checking name
        else if (this.owner.distanceToSqr(this.pet) > (double) (this.distance * this.distance)) {
            // if the distance is bigger than distance given(in blocks), teleport it
            pet.getBukkitEntity().teleport(this.owner.getBukkitEntity().getLocation());
            return false;
        } else {
            this.x = this.owner.getX();
            this.y = this.owner.getY();
            this.z = this.owner.getZ();
            return true;//return true so it call start()
        }
    }

    @Override
    public void start() {
        this.pet.getNavigation().moveTo(this.x, this.y, this.z, this.speed); // move to owner with given speed
    }

    @Override
    public boolean canContinueToUse() {
        return !this.pet.getNavigation().isDone() && this.owner.distanceToSqr(this.pet) < (double) (this.distance * this.distance); // see if it has already moved to the owner and distance between owner and pet more than it given
    }

    @Override
    public void stop() {
        this.owner = null; //set it to null so it is called again in canUse()
    }
}
