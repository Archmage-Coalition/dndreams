package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.blocks.entities.SmokestackBlockEntity;
import net.eman3600.dndreams.mixin_interfaces.AbstractFurnaceBlockEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider, AbstractFurnaceBlockEntityAccess {
    @Shadow protected DefaultedList<ItemStack> inventory;

    @Shadow
    protected static boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        return false;
    }

    protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;getCookTime(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;)I"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void dndreams$tick(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci, boolean bl, boolean bl2, ItemStack itemStack, boolean bl3, boolean bl4, Recipe recipe, int i) {
        if (world.getBlockEntity(pos.up()) instanceof SmokestackBlockEntity entity && craftRecipeSafe(recipe, ((AbstractFurnaceBlockEntityAccess)blockEntity).getInventory(), 64)) {
            entity.receiveBurn(((AbstractFurnaceBlockEntityAccess)blockEntity).getInventory());
        }
    }

    public DefaultedList<ItemStack> getInventory() {
        return inventory;
    }

    @Unique
    private static boolean craftRecipeSafe(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        return recipe != null && canAcceptRecipeOutput(recipe, slots, count);
    }
}
