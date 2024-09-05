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

public class ShamblerHiveGoal extends Goal {

    private final ShamblerEntity shambler;
    private int cooldown = 0;
    private int hiveSize = 0;
    private static final UUID uuid = UUID.fromString("378ee489-fc77-4af5-aa9b-88cc20f43af5");

    public ShamblerHiveGoal(ShamblerEntity shambler) {
        this.shambler = shambler;
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public void start() {
        super.start();

        updateHive(true);
    }

    private int getGroupRange(int partners) {
        return (8 + Math.min(2 * partners, 24));
    }

    public List<ShamblerEntity> getNearbyShamblers(int partners) {

        return shambler.world.getEntitiesByClass(ShamblerEntity.class, Box.from(Vec3d.of(shambler.getBlockPos())).expand(getGroupRange(partners)), entity -> entity.isAlive() && entity != this.shambler);
    }

    public List<ShamblerEntity> getNearbyShamblers() {

        return getNearbyShamblers(hiveSize);
    }

    @Override
    public void tick() {
        cooldown--;

        if (cooldown <= 0 && shambler.getTarget() != null) {

            updateHive(true);
        }
    }

    public void updateHive(boolean allowRally) {

        cooldown = 50;

        int size = hiveSize;

        List<ShamblerEntity> partners;
        do {
            partners = getNearbyShamblers(size);
        } while (size < (size = partners.size()));

        if (size > hiveSize && allowRally) {
            rally(partners);
        }

        hiveSize = size;

        updateAttributes(size);
    }

    public int getHiveSize() {
        return hiveSize;
    }

    private void rally(List<ShamblerEntity> partners) {
        for (ShamblerEntity entity : partners) {
            if (entity.hiveGoal.hiveSize < partners.size()) {
                entity.hiveGoal.hiveSize = partners.size();

                entity.hiveGoal.updateAttributes(partners.size());
            }
        }
    }

    private void updateAttributes(int partners) {
        EntityAttributeInstance attributes = shambler.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (attributes != null) {
            EntityAttributeModifier previous = attributes.getModifier(uuid);
            if (previous != null) {
                attributes.removeModifier(previous);
            }

            attributes.addTemporaryModifier(new EntityAttributeModifier(uuid, "Hive", speedValue(partners), EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        }

        attributes = shambler.getAttributes().getCustomInstance(EntityAttributes.GENERIC_FOLLOW_RANGE);
        if (attributes != null) {
            EntityAttributeModifier previous = attributes.getModifier(uuid);
            if (previous != null) {
                attributes.removeModifier(previous);
            }

            attributes.addTemporaryModifier(new EntityAttributeModifier(uuid, "Hive", followValue(partners), EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    private float speedValue(int partners) {
        return Math.min(partners * .15f, .9f);
    }

    private float followValue(int partners) {
        return Math.min(partners * .25f, 2.5f);
    }
}
