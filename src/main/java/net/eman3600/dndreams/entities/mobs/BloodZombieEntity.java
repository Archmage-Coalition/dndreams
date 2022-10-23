package net.eman3600.dndreams.entities.mobs;

import net.eman3600.dndreams.initializers.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class BloodZombieEntity extends ZombieEntity {

    public BloodZombieEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public BloodZombieEntity(World world) {
        super(ModEntities.BLOOD_ZOMBIE_ENTITY_TYPE, world);
    }


    public static boolean canSpawn(EntityType<BloodZombieEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return BloodZombieEntity.canSpawnInDark(type, world, spawnReason, pos, random) && (spawnReason == SpawnReason.SPAWNER || world.isSkyVisible(pos));
    }

    @Override
    protected void convertInWater() {

    }

    public static DefaultAttributeContainer.Builder createBloodZombieAttributes() {
        return createZombieAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 30).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected boolean canConvertInWater() {
        return false;
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}