package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LiquidVoidItem extends DrinkableItem {
    public LiquidVoidItem(Settings settings) {
        super(settings, false, new StatusEffectInstance(ModStatusEffects.BRAINFREEZE, 600));
    }



    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
    }
}
