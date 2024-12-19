package net.phileasfogg3.petlife.Pets.Goals;

import java.util.EnumSet;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class OwnerHurtTarget extends TargetGoal {
    private final Mob mob;
    private final LivingEntity owner;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public OwnerHurtTarget(Mob mob, LivingEntity owner) {
        super(mob, false);
        this.mob = mob;
        this.owner = owner;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        if (this.owner == null) {
            return false;
        } else {
            this.ownerLastHurt = this.owner.getLastHurtMob();
            int i = this.owner.getLastHurtMobTimestamp();
            return i != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT);
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurt, TargetReason.OWNER_ATTACKED_TARGET, true);
        if (this.owner != null) {
            this.timestamp = this.owner.getLastHurtMobTimestamp();
        }

        super.start();
    }
}
