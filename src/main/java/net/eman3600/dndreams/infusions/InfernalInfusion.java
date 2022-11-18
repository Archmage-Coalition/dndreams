package net.eman3600.dndreams.infusions;

import net.eman3600.dndreams.infusions.setup.Infusion;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class InfernalInfusion extends Infusion {
    public InfernalInfusion() {
        super(true, World.NETHER, 200, 0, 0);
    }

    @Override
    public boolean resistantTo(float damage, DamageSource source, PlayerEntity player) {
        return source.isFire();
    }
}
