package net.eman3600.dndreams;

import net.eman3600.dndreams.initializers.*;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldMixinI;
import net.eman3600.dndreams.screen.WeavingScreen;
import net.eman3600.dndreams.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ModScreenHandlerTypes.WEAVING, WeavingScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DREAMWOOD_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DREAMWOOD_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHIMMERING_ICE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHIMMERING_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHIMMERING_GLASS_PANE, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SNOWBELL_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WATER_ARTICHOKE, RenderLayer.getCutout());

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_FLOWING_SPIRIT, ModFluids.FLOWING_FLOWING_SPIRIT,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0xA100F0F0
                ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                ModFluids.STILL_FLOWING_SPIRIT, ModFluids.FLOWING_FLOWING_SPIRIT);

        ModModelPredicateProvider.registerModModels();
    }


    public static boolean drawAether(ClientWorld world) {
        try {
            return (world instanceof ClientWorldMixinI accessedWorld) && (accessedWorld.getClient().player.hasStatusEffect(ModStatusEffects.AETHER)/* || world.getBiome(accessedWorld.getClient().player.getBlockPos()).matchesKey(BiomeKeys.DEEP_DARK)*/);
        } catch (NullPointerException e) {
            return false;
        }
    }
}
