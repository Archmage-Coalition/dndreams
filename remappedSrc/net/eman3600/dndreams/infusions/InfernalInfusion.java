package net.eman3600.dndreams.infusions;

import net.eman3600.dndreams.infusions.setup.Infusion;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class InfernalInfusion extends Infusion {
    public InfernalInfusion() {
        super(true, World.NETHER, 200, 0, 0);
    }

    @Override
    public boolean resistantTo(float damage, DamageSource source, PlayerEntity player) {
        return source.isFire() || source == BloodlustItem.CRIMSON_SACRIFICE;
    }
}
