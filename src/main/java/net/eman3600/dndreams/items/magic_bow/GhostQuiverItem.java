package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.entities.projectiles.GhostArrowEntity;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GhostQuiverItem extends MagicQuiverItem implements ManaCostItem {

    public GhostQuiverItem(Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new GhostArrowEntity(world, shooter);
    }

    @Override
    public boolean canAfford(PlayerEntity user, ItemStack stack) {
        return canAffordMana(user, stack);
    }

    @Override
    public void spendCost(PlayerEntity user, ItemStack stack) {
        spendMana(user, stack);
    }

    @Override
    public int getBaseManaCost() {
        return 3;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(getTooltipMana(stack));
    }
}
