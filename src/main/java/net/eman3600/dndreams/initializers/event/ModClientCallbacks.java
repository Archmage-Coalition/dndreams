package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.entities.renderers.features.ModElytraFeatureRenderer;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

@Environment(EnvType.CLIENT)
public class ModClientCallbacks {

    public static void registerClientCallbacks() {

        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register(player -> {

            ItemStack stack = player.getEquippedStack(EquipmentSlot.CHEST);
            return !stack.isOf(ModItems.CLOUD_WINGS) && !stack.isOf(ModItems.EVERGALE);
        });


        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {

            if (entityRenderer instanceof PlayerEntityRenderer r) {

                registrationHelper.register(new ModElytraFeatureRenderer<>(r, context.getModelLoader(), ModItems.CLOUD_WINGS, new Identifier(MODID, "textures/models/cloud_wings.png")));
                registrationHelper.register(new ModElytraFeatureRenderer<>(r, context.getModelLoader(), ModItems.EVERGALE, new Identifier(MODID, "textures/models/evergale.png")));
            }
        });
    }


}
