package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.items.TaglockItem;
import net.eman3600.dndreams.rituals.setup.Ritual;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TaglockRitual extends Ritual {
    public TaglockRitual() {
        super(50);
    }

    @Override
    public boolean onCast(ServerWorld world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        ItemStack stack;
        Vec3d vec = Vec3d.ofCenter(pos);
        if ((stack = blockEntity.getInventoryTaglock()) != null && TaglockItem.isUnboundTaglock(stack)) {
            List<LivingEntity> mobs = world.getNonSpectatingEntities(LivingEntity.class, new Box(-5, -1, -5, 5, 1, 5).offset(pos));
            if (mobs.size() != 1) return false;
            ItemStack boundStack = new ItemStack(ModItems.TAGLOCK);
            TaglockItem.bind(boundStack, mobs.get(0));

            ItemEntity entity = new ItemEntity(world, vec.x, vec.y + 1, vec.z, boundStack);

            world.spawnEntity(entity);

            return true;
        }
        return false;
    }
}
