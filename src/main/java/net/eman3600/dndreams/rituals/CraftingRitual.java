package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.BonfireBlockEntity;
import net.eman3600.dndreams.recipes.RitualRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class CraftingRitual extends Ritual {


    @Override
    public boolean onCast(ServerWorld world, BlockPos pos, BonfireBlockEntity blockEntity, RitualRecipe recipe) {

        ItemStack stack = recipe.getOutput().copy();
        NbtCompound nbt = blockEntity.getRitualFocus().getOrCreateNbt();
        stack.setNbt(nbt.copy());
        blockEntity.setRitualFocus(stack);

        return true;
    }
}
