package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class MindstrungArrowEntity extends PersistentProjectileEntity {

    public MindstrungArrowEntity(EntityType<? extends MindstrungArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public MindstrungArrowEntity(World world, LivingEntity owner) {
        super(ModEntities.MINDSTRUNG_ARROW, owner, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        if (!world.isClient) {
            kill();
        }
    }
}
