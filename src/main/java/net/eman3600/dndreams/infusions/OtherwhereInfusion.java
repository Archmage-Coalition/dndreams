package net.eman3600.dndreams.infusions;

import net.eman3600.dndreams.infusions.setup.Infusion;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class OtherwhereInfusion extends Infusion {
    public OtherwhereInfusion() {
        super(true, World.END, 200, 0, 200);
    }

    @Override
    public boolean resistantTo(float damage, DamageSource source, PlayerEntity player) {
        return source.isFromFalling() || source == DamageSource.IN_WALL || source == DamageSource.FLY_INTO_WALL;
    }
}
