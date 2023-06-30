package net.eman3600.dndreams.initializers.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.util.Identifier;

public class EntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<ManaComponent> MANA = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "mana"), ManaComponent.class);
    public static final ComponentKey<TormentComponent> TORMENT = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "torment"), TormentComponent.class);
    public static final ComponentKey<DreamingComponent> DREAMING = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "dreaming"), DreamingComponent.class);
    public static final ComponentKey<InfusionComponent> INFUSION = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "infusion"), InfusionComponent.class);
    public static final ComponentKey<StatBoonComponent> STAT_BOON = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "stat_boon"), StatBoonComponent.class);
    public static final ComponentKey<PermItemComponent> PERM_ITEM = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "perm_item"), PermItemComponent.class);
    public static final ComponentKey<GatewayComponent> GATEWAY = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "gateway"), GatewayComponent.class);
    public static final ComponentKey<ReviveComponent> REVIVE = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "revive"), ReviveComponent.class);
    public static final ComponentKey<ShockComponent> SHOCK = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "shock"), ShockComponent.class);
    public static final ComponentKey<WardenComponent> WARDEN = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "warden"), WardenComponent.class);
    public static final ComponentKey<RotComponent> ROT = ComponentRegistry.getOrCreate(new Identifier(Initializer.MODID, "rot"), RotComponent.class);






    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(MANA, ManaComponent::new, RespawnCopyStrategy.INVENTORY);
        registry.registerForPlayers(TORMENT, TormentComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(DREAMING, DreamingComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(INFUSION, InfusionComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(GATEWAY, GatewayComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(STAT_BOON, StatBoonComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(PERM_ITEM, PermItemComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(REVIVE, ReviveComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerFor(LivingEntity.class, SHOCK, ShockComponent::new);
        registry.registerFor(LivingEntity.class, ROT, RotComponent::new);

        registry.registerForPlayers(WARDEN, WardenComponent::new, RespawnCopyStrategy.NEVER_COPY);
        registry.registerFor(RaiderEntity.class, WARDEN, WardenComponent::new);
        registry.registerFor(MerchantEntity.class, WARDEN, WardenComponent::new);
    }
}
