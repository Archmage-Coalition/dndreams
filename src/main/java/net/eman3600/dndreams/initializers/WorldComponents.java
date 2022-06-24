package net.eman3600.dndreams.initializers;

import dev.onyxstudios.cca.api.v3.component.ComponentContainer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WorldComponents implements WorldComponentInitializer {
    public static final ComponentKey<BloodMoonComponent> BLOOD_MOON = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "blood_moon"), BloodMoonComponent.class);




    public static void tickBloodMoon(World world) {
        BLOOD_MOON.get(world).tick(world.isNight());
    }



    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(BLOOD_MOON, BloodMoonComponent::new);
    }
}
