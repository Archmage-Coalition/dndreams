package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {
    public BlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("TAIL"))
    private static void dndreams$getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> stackList = cir.getReturnValue();

        if (stack.isIn(ModTags.SMELTING_TOOLS)) {
            if (stack.getItem() instanceof BloodlustItem lustItem && entity instanceof LivingEntity living && lustItem.hasBloodlust(living)) return;
            RecipeManager manager = world.getRecipeManager();
            for (ItemStack drop: stackList) {
                SimpleInventory inventory = new SimpleInventory(drop);
                Optional<SmeltingRecipe> optional = manager.getFirstMatch(RecipeType.SMELTING, inventory, world);

                SmeltingRecipe recipe;
                if (optional.isPresent()) {
                    recipe = optional.get();

                    stackList.add(new ItemStack(recipe.getOutput().getItem(), drop.getCount()));
                    stackList.remove(drop);
                }
            }
        }
    }

    @Inject(method = "dropExperienceWhenMined", at = @At("HEAD"), cancellable = true)
    private void dndreams$dropExperienceWhenMined(ServerWorld world, BlockPos pos, ItemStack tool, IntProvider experience, CallbackInfo ci) {
        if (tool.isIn(ModTags.SILKY_TOOLS)) {
            ci.cancel();
        }
    }
}
