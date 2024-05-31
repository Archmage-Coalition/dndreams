package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class ManatwineArrowEntity extends PersistentProjectileEntity {

    public ManatwineArrowEntity(EntityType<? extends ManatwineArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public ManatwineArrowEntity(World world, LivingEntity owner) {
        super(ModEntities.MANATWINE_ARROW, owner, world);
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
