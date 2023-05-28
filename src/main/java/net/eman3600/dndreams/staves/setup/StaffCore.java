package net.eman3600.dndreams.staves.setup;

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

public class StaffCore {
    private final RegistryEntry.Reference<StaffCore> entry;

    public final Item material;
    public final int durability;
    public final int baseCost;
    public final int baseCooldown;
    public final float baseDamage;
    public final float baseRange;
    public final StaffHandle[] allowedHandles;

    public StaffCore(Item material, int durability, int baseCost, int baseCooldown, float baseDamage, float baseRange, StaffHandle... allowedHandles) {
        this.material = material;
        this.durability = durability;
        this.baseCost = baseCost;
        this.baseCooldown = baseCooldown;
        this.baseDamage = baseDamage;
        this.baseRange = baseRange;
        this.allowedHandles = allowedHandles;

        this.entry = StaffCoreRegistry.REGISTRY.createEntry(this);
    }

    public Item[] allowedEnhancements() {
        return new Item[]{};
    }

    public boolean usesSpeed(ItemStack stack) {
        return false;
    }

    public float getSpeed(float range) {
        return range;
    }

    public float getRange(ItemStack stack) {
        return baseRange;
    }

    public int getChargeTime(ItemStack stack) {
        return 0;
    }





    public RegistryEntry.Reference<StaffCore> reference() {
        return entry;
    }

    public String toString() {
        return StaffCoreRegistry.REGISTRY.getId(this).getPath();
    }

    public String getTranslationKey() {
        return Util.createTranslationKey("item", StaffCoreRegistry.REGISTRY.getId(this));
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {}

    public boolean use(PlayerEntity user, ItemStack stack) {
        return false;
    }

    public boolean release(PlayerEntity user, ItemStack stack, int useTicks) {
        return false;
    }
}
