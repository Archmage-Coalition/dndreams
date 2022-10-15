package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.ComposterBlockAccess;
import net.minecraft.block.Block;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ComposterBlock.class)
public abstract class ComposterBlockMixin extends Block implements ComposterBlockAccess, InventoryProvider {
    @Shadow
    private static void registerCompostableItem(float levelIncreaseChance, ItemConvertible item) {
    }

    public ComposterBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void registerCompostable(float levelIncreaseChance, ItemConvertible item) {
        registerCompostableItem(levelIncreaseChance, item);
    }
}
