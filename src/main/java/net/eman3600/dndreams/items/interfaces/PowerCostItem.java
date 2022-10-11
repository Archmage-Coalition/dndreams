package net.eman3600.dndreams.items.interfaces;

import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldMixinI;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.world.World;

public interface PowerCostItem {
    float getPowerCost();
    default boolean canAffordPower(PlayerEntity player) {
        if (player != null)
            return EntityComponents.INFUSION.get(player).getPower() >= getPowerCost();
        return false;
    }

    default Text getTooltipPower(World world) {
        try {
            if (world instanceof ClientWorldMixinI access && !EntityComponents.INFUSION.get(access.getClient().player).infused()) {
                return Text.translatable("tooltip.dndreams.power_required");
            }
        } catch (ClassCastException | NullPointerException ignored) {}
        return Text.translatable("tooltip.dndreams.power_cost", "§d" + getPowerCost());
    }
}
