package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.misc_tool.TaglockItem;
import net.eman3600.dndreams.rituals.setup.Ritual;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class NightTerrorRitual extends Ritual {
    public NightTerrorRitual() {
        super(1500, new Ring(Ring.MIDDLE_RING, CandleTuning.NETHER));
    }

    @Override
    public boolean onCast(ServerWorld world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        ItemStack stack;
        if ((stack = blockEntity.getInventoryTaglock()) != null && !TaglockItem.isUnboundTaglock(stack) && TaglockItem.getBoundEntity(stack, world) instanceof PlayerEntity player) {
            TormentComponent component = EntityComponents.TORMENT.get(player);
            if (component.getSanity() > 5f) {
                component.setSanity(0);
                return true;
            }
        }
        return false;
    }
}
