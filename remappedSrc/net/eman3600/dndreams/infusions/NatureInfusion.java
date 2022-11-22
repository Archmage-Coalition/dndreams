package net.eman3600.dndreams.infusions;

import net.eman3600.dndreams.infusions.setup.Infusion;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class NatureInfusion extends Infusion {
    public NatureInfusion() {
        super(true, World.OVERWORLD, 0, 200, 0);
    }
}
