package net.eman3600.dndreams.items.edge_series;

import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SlumberingSwordItem extends SwordItem implements ManaCostItem, MagicDamageItem {
    public SlumberingSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && canAffordMana(player, stack)) {
            spendMana(player, stack);

            target.timeUntilRegen = 1;
            target.damage(DamageSource.magic(player, player), getMagicDamage(stack));
        }

        return super.postHit(stack, target, attacker);
    }

    @Override
    public float getBaseMagicDamage() {
        return 2;
    }

    @Override
    public int getBaseManaCost() {
        return 4;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(getTooltipMana(stack));
        tooltip.add(getTooltipMagicDamage(stack));
    }
}
