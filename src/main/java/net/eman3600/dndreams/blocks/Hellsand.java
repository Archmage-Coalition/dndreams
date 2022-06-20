package net.eman3600.dndreams.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;

public class Hellsand extends SoulSandBlock {
    private final IntProvider experienceDropped;

    public Hellsand(Settings settings, IntProvider experience) {
        super(settings);
        this.experienceDropped = experience;
    }

    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, stack, dropExperience);
        if (dropExperience) {
            this.dropExperienceWhenMined(world, pos, stack, this.experienceDropped);
        }

    }
}
