package net.eman3600.dndreams;

import net.eman3600.dndreams.blocks.entities.CosmicPortalBlockEntityRenderer;
import net.eman3600.dndreams.entities.renderers.BloodSkeletonEntityRenderer;
import net.eman3600.dndreams.entities.renderers.BloodZombieEntityRenderer;
import net.eman3600.dndreams.entities.renderers.WardenRagdollEntityRenderer;
import net.eman3600.dndreams.initializers.*;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.eman3600.dndreams.screen.WeavingScreen;
import net.eman3600.dndreams.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ModScreenHandlerTypes.WEAVING, WeavingScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DREAMWOOD_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DREAMWOOD_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHIMMERING_ICE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHIMMERING_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHIMMERING_GLASS_PANE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WEAK_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HORDE_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OVERHELL_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MORTAL_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COSMIC_PORTAL, RenderLayer.getEndPortal());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SNOWBELL_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WATER_ARTICHOKE, RenderLayer.getCutout());

        BlockEntityRendererRegistry.register(ModBlockEntities.COSMIC_PORTAL_ENTITY, CosmicPortalBlockEntityRenderer::new);

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_FLOWING_SPIRIT, ModFluids.FLOWING_FLOWING_SPIRIT,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0xA100F0F0
                ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                ModFluids.STILL_FLOWING_SPIRIT, ModFluids.FLOWING_FLOWING_SPIRIT);

        ModModelPredicateProvider.registerModModels();


        ModMessages.registerS2CPackets();

        ModParticles.registerParticleFactories();


        EntityRendererRegistry.register(ModEntities.CROWNED_SLASH_ENTITY_TYPE, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.CROWNED_BEAM_ENTITY_TYPE, EmptyEntityRenderer::new);

        EntityRendererRegistry.register(ModEntities.BLOOD_ZOMBIE_ENTITY_TYPE, BloodZombieEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BLOOD_SKELETON_ENTITY_TYPE, BloodSkeletonEntityRenderer::new);

        EntityRendererRegistry.register(ModEntities.WARDEN_RAGDOLL_ENTITY_TYPE, WardenRagdollEntityRenderer::new);
    }


    public static boolean drawAether(World world) {
        try {
            return (world instanceof ClientWorldAccess accessedWorld) && (accessedWorld.getClient().player.hasStatusEffect(ModStatusEffects.AETHER)/* || world.getBiome(accessedWorld.getClient().player.getBlockPos()).matchesKey(BiomeKeys.DEEP_DARK)*/);
        } catch (NullPointerException e) {
            return false;
        }
    }
}
