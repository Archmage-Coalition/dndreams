package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.items.consumable.SpringVialItem;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class SpringVialEntity extends ThrownItemEntity implements FlyingItemEntity {
    public SpringVialEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public SpringVialEntity(double d, double e, double f, World world) {
        super(ModEntities.SPRING_VIAL, d, e, f, world);
    }

    public SpringVialEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.SPRING_VIAL, livingEntity, world);
    }

    @Override
    protected float getGravity() {
        return 0.05f;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.world.isClient) {
            return;
        }

        ItemStack stack = getStack();
        Fluid fluid;
        int color;
        if (stack.getItem() instanceof SpringVialItem item) {
            fluid = item.fluid;
            color = item.color;
        }
        else {
            fluid = Fluids.WATER;
            color = 3694022;
        }

        this.world.syncWorldEvent(WorldEvents.SPLASH_POTION_SPLASHED, this.getBlockPos(), color);
        if (world.getBlockState(getBlockPos()).isAir() || world.getBlockState(getBlockPos()).isIn(BlockTags.REPLACEABLE_PLANTS)) world.setBlockState(this.getBlockPos(), fluid.getDefaultState().getBlockState(), Block.NOTIFY_ALL);

        this.discard();
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.WATER_VIAL;
    }
}
