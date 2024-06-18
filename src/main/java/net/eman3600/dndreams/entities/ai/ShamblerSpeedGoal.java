package net.eman3600.dndreams.entities.ai;

import net.eman3600.dndreams.entities.mobs.ShamblerEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.UUID;

public class ShamblerSpeedGoal extends Goal {

    private final ShamblerEntity shambler;
    private int cooldown = 0;
    private int previousPartners = 0;
    private static final UUID uuid = UUID.fromString("378ee489-fc77-4af5-aa9b-88cc20f43af5");

    public ShamblerSpeedGoal(ShamblerEntity shambler) {
        this.shambler = shambler;
    }

    @Override
    public boolean canStart() {
        return shambler.getTarget() != null;
    }

    @Override
    public void start() {
        super.start();

        updateSpeed(true);
    }

    private int getGroupRange(int partners) {
        return (8 + Math.min(2 * partners, 24));
    }

    public List<ShamblerEntity> getNearbyShamblers(int partners) {

        return shambler.world.getEntitiesByClass(ShamblerEntity.class, Box.from(Vec3d.of(shambler.getBlockPos())).expand(getGroupRange(partners)), entity -> entity.isAlive() && entity != this.shambler);
    }

    public List<ShamblerEntity> getNearbyShamblers() {

        return getNearbyShamblers(previousPartners);
    }

    @Override
    public void tick() {
        cooldown--;

        if (cooldown <= 0 && shambler.getTarget() != null) {

            updateSpeed(true);
        }
    }

    public void updateSpeed(boolean allowRally) {

        cooldown = 50;

        int size = previousPartners;

        List<ShamblerEntity> partners;
        do {
            partners = getNearbyShamblers(size);
        } while (size < (size = partners.size()));

        if (size > previousPartners && allowRally) {
            rally(partners);
        }

        previousPartners = size;

        EntityAttributeInstance attributes = shambler.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (attributes != null) {
            EntityAttributeModifier previous = attributes.getModifier(uuid);
            if (previous != null) {
                attributes.removeModifier(previous);
            }

            attributes.addTemporaryModifier(new EntityAttributeModifier(uuid, "Shamble", speedValue(size), EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    private void rally(List<ShamblerEntity> partners) {
        for (ShamblerEntity entity : partners) {
            if (entity.speedGoal.previousPartners < partners.size()) {
                entity.speedGoal.previousPartners = partners.size();

                EntityAttributeInstance attributes = entity.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if (attributes != null) {
                    EntityAttributeModifier previous = attributes.getModifier(uuid);
                    if (previous != null) {
                        attributes.removeModifier(previous);
                    }

                    attributes.addTemporaryModifier(new EntityAttributeModifier(uuid, "Shamble", speedValue(partners.size()), EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
                }
            }
        }
    }

    private float speedValue(int partners) {
        return Math.min(partners * .15f, .9f);
    }
}
