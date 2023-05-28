package net.eman3600.dndreams.staves.setup;

import net.eman3600.dndreams.items.MysticStaffItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class StaffProng {
    private final RegistryEntry.Reference<StaffProng> entry;

    public final Item material;

    public StaffProng(Item material) {
        this.material = material;

        this.entry = StaffProngRegistry.REGISTRY.createEntry(this);
    }

    public RegistryEntry.Reference<StaffProng> reference() {
        return entry;
    }

    public String toString() {
        return StaffProngRegistry.REGISTRY.getId(this).getPath();
    }

    public int getCooldown(int baseCost, int baseCooldown) {
        return baseCooldown;
    }
    public final int getBaseCost(ItemStack stack) {
        try {
            return getBaseCost(MysticStaffItem.getCore(stack), MysticStaffItem.getHandle(stack));
        } catch (NullPointerException e) {
            return 0;
        }
    }
    public final int getBaseCost(StaffCore core, StaffHandle handle) {
        return core.baseCost + handle.addedCost;
    }
    public int getDurability(int baseCost, int baseDurability) {
        return baseDurability;
    }
    public boolean canAffordSpecials(PlayerEntity player, int baseCost) {
        return true;
    }
    public void spendSpecials(PlayerEntity player, int baseCost) {}

    public abstract int getManaCost(int baseCost);
    public abstract float getSanityCost(int baseCost);

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        StaffCore core = MysticStaffItem.getCore(stack);
        StaffHandle handle = MysticStaffItem.getHandle(stack);

        int cost = getBaseCost(core, handle);
        int cooldown = core.baseCooldown;

        int manaCost = getManaCost(cost);
        if (manaCost > 0) {
            tooltip.add(Text.translatable("tooltip.dndreams.mana_cost", manaCost));
        }

        float sanityCost = getSanityCost(cost);
        if (sanityCost > 0) {
            tooltip.add(Text.translatable("tooltip.dndreams.sanity_cost", sanityCost));
        }

        cooldown = getCooldown(cost, cooldown);
        if (cooldown > 0) {
            tooltip.add(Text.translatable("tooltip.dndreams.cooldown", cooldown / 20f));
        }
    }

    public String getTranslationKey() {
        return Util.createTranslationKey("item", StaffProngRegistry.REGISTRY.getId(this));
    }
}
