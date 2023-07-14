package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.rituals.setup.Ritual;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CraftingRitual extends Ritual {
    private final Item item;

    public CraftingRitual(Item item, int cost, Ring... rings) {
        super(cost, rings);
        this.item = item;
    }

    @Override
    public boolean onCast(ServerWorld world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        ItemStack stack = new ItemStack(item);
        Vec3d vec = Vec3d.ofCenter(pos);

        ItemEntity entity = new ItemEntity(world, vec.x, vec.y + 1, vec.z, stack);

        world.spawnEntity(entity);
        return true;
    }
}
