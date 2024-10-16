package net.eman3600.dndreams.entities.mobs;

import net.eman3600.dndreams.entities.ai.ShamblerHiveGoal;
import net.eman3600.dndreams.entities.ai.WanderTowardsHiveGoal;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class ShamblerEntity extends ZombieEntity {

    public final ShamblerHiveGoal hiveGoal = new ShamblerHiveGoal(this);

    public ShamblerEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);

        goalSelector.add(0, hiveGoal);
    }

    public ShamblerEntity(World world) {
        super(ModEntities.SHAMBLER, world);

        goalSelector.add(0, hiveGoal);
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        goalSelector.add(6, new WanderTowardsHiveGoal(this, 1d));
    }

    public static boolean canSpawn(EntityType<ShamblerEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return ShamblerEntity.canSpawnInDark(type, world, spawnReason, pos, random) && (spawnReason == SpawnReason.SPAWNER || world.isSkyVisible(pos));
    }

    @Override
    protected void convertInWater() {

    }

    public static DefaultAttributeContainer.Builder createShamblerAttributes() {
        return createZombieAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 30).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.21f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0).add(EntityAttributes.GENERIC_ARMOR, 6);
    }

    @Override
    protected boolean canConvertInWater() {
        return false;
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = super.tryAttack(target);

        if (bl && target instanceof LivingEntity entity) {
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.HEARTBLEED, 240, 1));
        }

        return bl;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        for (ShamblerEntity entity: hiveGoal.getNearbyShamblers()) {
            if (entity.getTarget() != null) {
                entity.hiveGoal.updateHive(false);
            }
        }

        super.onDeath(damageSource);
    }
}
