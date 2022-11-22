package net.eman3600.dndreams.infusions;

import net.eman3600.dndreams.infusions.setup.Infusion;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class SpiritInfusion extends Infusion {
    public SpiritInfusion() {
        super(true, ModDimensions.HAVEN_DIMENSION_KEY, 0, 200, 200);
    }

    @Override
    public boolean resistantTo(float damage, DamageSource source, PlayerEntity player) {
        return source == DamageSource.WITHER;
    }
}
