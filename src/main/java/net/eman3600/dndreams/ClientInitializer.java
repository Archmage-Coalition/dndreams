package net.eman3600.dndreams;

import net.eman3600.dndreams.blocks.entities.CosmicPortalBlockEntityRenderer;
import net.eman3600.dndreams.blocks.renderer.BonfireBlockEntityRenderer;
import net.eman3600.dndreams.blocks.renderer.RefinedCauldronBlockEntityRenderer;
import net.eman3600.dndreams.entities.projectiles.GlowBoltEntity;
import net.eman3600.dndreams.entities.projectiles.SparkBoltEntity;
import net.eman3600.dndreams.entities.projectiles.StrifeEntity;
import net.eman3600.dndreams.entities.renderers.*;
import net.eman3600.dndreams.events.KeyInputHandler;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.basics.ModFluids;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.initializers.event.ModClientCallbacks;
import net.eman3600.dndreams.initializers.event.ModMessages;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.initializers.event.ModScreenHandlerTypes;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.eman3600.dndreams.screens.AttunementScreen;
import net.eman3600.dndreams.screens.RefineryScreen;
import net.eman3600.dndreams.screens.SmokestackScreen;
import net.eman3600.dndreams.screens.WeavingScreen;
import net.eman3600.dndreams.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class
ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlerTypes.WEAVING, WeavingScreen::new);
        HandledScreens.register(ModScreenHandlerTypes.ATTUNEMENT, AttunementScreen::new);
        HandledScreens.register(ModScreenHandlerTypes.SMOKESTACK, SmokestackScreen::new);
        HandledScreens.register(ModScreenHandlerTypes.REFINERY, RefineryScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DREAMWOOD_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SAKURA_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.JAPANESE_MAPLE_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PRISTINE_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHADE_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HAVEN_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PINE_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STAR_LEAVES, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DREAMWOOD_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SAKURA_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.JAPANESE_MAPLE_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PRISTINE_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHADE_SHROOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HAVEN_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PINE_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STAR_SAPLING, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLDEN_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHADE_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHADE_WEED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHADE_FERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HAVEN_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STAR_GRASS, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SPIRIT_ICE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CRYSTAL_SPIRIT_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WEAK_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HORDE_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HAVEN_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MORTAL_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COSMIC_PORTAL, RenderLayer.getEndPortal());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BONFIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STRIFE_FIRE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SNOWBELL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOTUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EMBER_MOSS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FRAZZLED_MOSS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.APPLETHORN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WITHER_BLOSSOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DRAGONFRUIT, RenderLayer.getCutout());

        BlockEntityRendererRegistry.register(ModBlockEntities.COSMIC_PORTAL_ENTITY, CosmicPortalBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.REFINED_CAULDRON_ENTITY, RefinedCauldronBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.BONFIRE_ENTITY, BonfireBlockEntityRenderer::new);

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_FLOWING_SPIRIT, ModFluids.FLOWING_FLOWING_SPIRIT,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0xA100F0F0
                ));
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_SORROW, ModFluids.FLOWING_SORROW,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0xDF1E1C32
                ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                ModFluids.STILL_FLOWING_SPIRIT, ModFluids.FLOWING_FLOWING_SPIRIT);
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                ModFluids.STILL_SORROW, ModFluids.FLOWING_SORROW);

        //1e1c32

        ModModelPredicateProvider.registerModModels();
        ModClientCallbacks.registerClientCallbacks();


        ModMessages.registerS2CPackets();

        ModParticles.registerParticleFactories();


        EntityRendererRegistry.register(ModEntities.SHADE_RIFT, EmptyEntityRenderer::new);

        EntityRendererRegistry.register(ModEntities.CROWNED_SLASH, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.CROWNED_BEAM, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TESLA_SLASH, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SPARK_BOLT, (EntityRendererFactory.Context context) -> new FlatEntityRenderer(context, SparkBoltEntity.TEXTURE));
        EntityRendererRegistry.register(ModEntities.GLOW_BOLT, (EntityRendererFactory.Context context) -> new FlatEntityRenderer(context, GlowBoltEntity.TEXTURE));
        EntityRendererRegistry.register(ModEntities.STRIFE, (EntityRendererFactory.Context context) -> new FlatEntityRenderer(context, StrifeEntity.TEXTURE));

        EntityRendererRegistry.register(ModEntities.BREW_SPLASH, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BREW_LINGERING, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BREW_GAS, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BREW_LIQUID, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SPRING_VIAL, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.RISING_BLOCK, FallingBlockEntityRenderer::new);

        EntityRendererRegistry.register(ModEntities.BLOOD_ZOMBIE, BloodZombieEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BLOOD_SKELETON, BloodSkeletonEntityRenderer::new);

        EntityRendererRegistry.register(ModEntities.WARDEN_RAGDOLL, WardenRagdollEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TORMENTOR, TormentorEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FACELESS, FacelessEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.DREAM_SHEEP, DreamSheepEntityRenderer::new);

        KeyInputHandler.registerBindings();
        KeyInputHandler.registerKeyInputs();
    }


    public static boolean drawAether(World world) {
        try {
            if ((world instanceof ClientWorldAccess access)) {
                try {

                    if (world.getRegistryKey() == ModDimensions.HAVEN_DIMENSION_KEY || world.getRegistryKey() == ModDimensions.GATEWAY_DIMENSION_KEY) return true;
                } catch (Exception ignored) {}

                return access.getClient().player.hasStatusEffect(ModStatusEffects.AETHER);
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
