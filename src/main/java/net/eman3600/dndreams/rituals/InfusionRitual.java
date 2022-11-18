package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.infusions.setup.Infusion;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class InfusionRitual extends AbstractRitual {
    private final Infusion infusion;

    public InfusionRitual(int cost, Infusion infusion, Ring... rings) {
        super(cost, rings);
        this.infusion = infusion;
    }

    @Override
    public boolean onCast(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, new Box(-5, -1, -5, 5, 1, 5).offset(pos));
        if (players.size() > 0) {
            int count = 0;
            for (PlayerEntity player: players) {
                try {
                    InfusionComponent component = EntityComponents.INFUSION.get(player);
                    if (component.getInfusion() != infusion) {
                        component.setInfusion(infusion);
                        count++;
                    }
                } catch (Exception ignored) {}
            }

            return count > 0;
        }

        return false;
    }
}
