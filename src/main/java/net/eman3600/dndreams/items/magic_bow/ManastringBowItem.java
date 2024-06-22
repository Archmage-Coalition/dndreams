package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.entities.projectiles.ManatwineArrowEntity;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManastringBowItem extends MagicBowItem implements ManaCostItem {
    public ManastringBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getBaseManaCost() {
        return 6;
    }

    protected boolean canAfford(PlayerEntity player, ItemStack stack) {
        return canAffordMana(player, stack);
    }

    protected void payAmmo(PlayerEntity player, ItemStack stack) {
        if (player != null) {
            spendMana(player, stack);
        }
    }

    public int pullTime() {
        return 18;
    }

    @Override
    public PersistentProjectileEntity createDefaultArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new ManatwineArrowEntity(world, shooter);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(getTooltipMana(stack));
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.ECHO_SHARD);
    }
}
