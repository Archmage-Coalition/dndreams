package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.blocks.VitalOreBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin extends ToolItem {

    @Shadow @Final private TagKey<Block> effectiveBlocks;

    public MiningToolItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Inject(method = "isSuitableFor", at = @At("HEAD"), cancellable = true)
    private void dndreams$isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {

        if (state.getBlock() instanceof VitalOreBlock && !state.get(VitalOreBlock.REVEALED)) {

            cir.setReturnValue(state.isIn(this.effectiveBlocks));
        }
    }
}
