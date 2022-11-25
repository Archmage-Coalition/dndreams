package net.eman3600.dndreams.initializers.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.*;
import net.minecraft.util.Identifier;

public class EntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<ManaComponent> MANA = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "mana"), ManaComponent.class);
    public static final ComponentKey<TormentComponent> TORMENT = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "torment"), TormentComponent.class);
    public static final ComponentKey<DreamingComponent> DREAMING = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "dreaming"), DreamingComponent.class);
    public static final ComponentKey<InfusionComponent> INFUSION = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "infusion"), InfusionComponent.class);
    public static final ComponentKey<StatBoonComponent> STAT_BOON = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "stat_boon"), StatBoonComponent.class);
    public static final ComponentKey<PermItemComponent> PERM_ITEM = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "perm_item"), PermItemComponent.class);
    public static final ComponentKey<GatewayComponent> GATEWAY = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "gateway"), GatewayComponent.class);






    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(MANA, ManaComponent::new, RespawnCopyStrategy.INVENTORY);
        registry.registerForPlayers(TORMENT, TormentComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(DREAMING, DreamingComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(INFUSION, InfusionComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(GATEWAY, GatewayComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(STAT_BOON, StatBoonComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(PERM_ITEM, PermItemComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
