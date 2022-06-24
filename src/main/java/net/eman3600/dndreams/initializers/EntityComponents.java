package net.eman3600.dndreams.initializers;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.minecraft.util.Identifier;

public class EntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<ManaComponent> MANA = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "mana"), ManaComponent.class);
    public static final ComponentKey<TormentComponent> TORMENT = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "torment"), TormentComponent.class);






    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(MANA, player -> new ManaComponent(player), RespawnCopyStrategy.INVENTORY);
        registry.registerForPlayers(TORMENT, player -> new TormentComponent(player), RespawnCopyStrategy.LOSSLESS_ONLY);
    }
}
