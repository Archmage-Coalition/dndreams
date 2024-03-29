package net.eman3600.dndreams.initializers.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentContainer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.cardinal_components.BossStateComponent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WorldComponents implements WorldComponentInitializer, ScoreboardComponentInitializer {
    public static final ComponentKey<BloodMoonComponent> BLOOD_MOON = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "blood_moon"), BloodMoonComponent.class);
    public static final ComponentKey<BossStateComponent> BOSS_STATE = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "boss_state"), BossStateComponent.class);



    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(BLOOD_MOON, BloodMoonComponent::new);
    }

    @Override
    public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry) {
        registry.registerScoreboardComponent(BOSS_STATE, (scoreboard, server) -> server == null
                ? new BossStateComponent(scoreboard, null)
                : new BossStateComponent(scoreboard, server)
        );
    }
}
