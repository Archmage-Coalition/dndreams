package net.eman3600.dndreams.items;

import com.google.common.collect.ImmutableList;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.eman3600.dndreams.items.interfaces.SanityCostItem;
import net.eman3600.dndreams.staves.setup.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MysticStaffItem extends Item implements ManaCostItem, SanityCostItem {

    public MysticStaffItem(Settings settings) {
        super(settings);
    }


    public static ItemStack createStaff(StaffHandle handle, StaffCore core, StaffProng prongs, Item... enhancements) {

        ItemStack stack = new ItemStack(ModItems.MYSTIC_STAFF);
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putString("staff_handle", StaffHandleRegistry.REGISTRY.getId(handle).toString());
        nbt.putString("staff_core", StaffCoreRegistry.REGISTRY.getId(core).toString());
        nbt.putString("staff_prongs", StaffProngRegistry.REGISTRY.getId(prongs).toString());

        NbtList list = new NbtList();
        for (Item item: enhancements) {
            list.add(NbtString.of(Registry.ITEM.getId(item).toString()));
        }

        nbt.put("staff_enhancements", list);

        return stack;
    }

    @Nullable
    public static StaffCore getCore(ItemStack stack) {

        if (stack.hasNbt()) {
            NbtCompound nbt = stack.getNbt();

            return StaffCoreRegistry.REGISTRY.get(Identifier.tryParse(nbt.getString("staff_core")));
        }

        return null;
    }

    @Nullable
    public static StaffHandle getHandle(ItemStack stack) {

        if (stack.hasNbt()) {
            NbtCompound nbt = stack.getNbt();

            return StaffHandleRegistry.REGISTRY.get(Identifier.tryParse(nbt.getString("staff_handle")));
        }

        return null;
    }

    @Nullable
    public static StaffProng getProngs(ItemStack stack) {

        if (stack.hasNbt()) {
            NbtCompound nbt = stack.getNbt();

            return StaffProngRegistry.REGISTRY.get(Identifier.tryParse(nbt.getString("staff_prongs")));
        }

        return null;
    }

    public static List<Item> getEnhancements(ItemStack stack) {
        List<Item> enhancements = new ArrayList<>();

        if (stack.hasNbt()) {
            NbtCompound nbt = stack.getNbt();

            NbtList list = nbt.getList("staff_enhancements", NbtList.STRING_TYPE);

            for (NbtElement element: list) {
                if (element instanceof NbtString str) {
                    enhancements.add(Registry.ITEM.get(Identifier.tryParse(str.asString())));
                }
            }
        }

        return ImmutableList.copyOf(enhancements);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    public int getStaffMaxDamage(ItemStack stack) {
        try {
            StaffCore core = getCore(stack);

            return getProngs(stack).getDurability(core.baseCost, (int)(core.durability * getHandle(stack).durabilityMult));

        } catch (NullPointerException e) {
            return getMaxDamage();
        }
    }

    @Override
    public int getBaseManaCost() {
        return 0;
    }

    @Override
    public float getBaseSanityCost() {
        return 0;
    }

    @Override
    public int getManaCost(ItemStack stack) {
        try {

            StaffCore core = getCore(stack);
            StaffHandle handle = getHandle(stack);
            StaffProng prongs = getProngs(stack);

            return prongs.getManaCost(core.baseCost + handle.addedCost);
        } catch (NullPointerException e) {

            return ManaCostItem.super.getManaCost(stack);
        }
    }

    @Override
    public float getSanityCost(ItemStack stack) {
        try {

            StaffCore core = getCore(stack);
            StaffHandle handle = getHandle(stack);
            StaffProng prongs = getProngs(stack);

            return prongs.getSanityCost(core.baseCost + handle.addedCost);
        } catch (NullPointerException e) {

            return SanityCostItem.super.getSanityCost(stack);
        }
    }

    @Override
    public boolean isSanityPermanent(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isSanityOptional(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        try {
            return getCore(stack).getChargeTime(stack);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return getMaxUseTime(stack) > 0 ? UseAction.BOW : UseAction.NONE;
    }

    public boolean canAfford(PlayerEntity player, ItemStack stack) {
        try {
            StaffProng prongs = getProngs(stack);

            return canAffordMana(player, stack) && canAffordSanity(player, stack) && prongs.canAffordSpecials(player, prongs.getBaseCost(stack));

        } catch (NullPointerException e) {
            return false;
        }
    }
}
