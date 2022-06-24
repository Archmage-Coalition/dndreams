package net.eman3600.dndreams.initializers;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.minecraft.util.Identifier;

public class EntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<ManaComponent> MANA = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "mana"), ManaComponent.class);






    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(MANA, player -> new ManaComponent(), RespawnCopyStrategy.INVENTORY);
    }
}
