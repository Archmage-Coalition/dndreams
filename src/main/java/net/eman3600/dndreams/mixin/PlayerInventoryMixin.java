package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.items.managold.ManagoldArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow
    @Final
    public DefaultedList<ItemStack> armor;

    @Shadow
    @Final
    public PlayerEntity player;

    @ModifyVariable(method = "damageArmor", at = @At(value = "HEAD"), argsOnly = true)
    public int[] injectDamageArmor(int[] slots, DamageSource damageSource, float amount, int[] slots2) {
        if (!(amount <= 0.0F)) {
            amount /= 4.0F;
            if (amount < 1.0F) {
                amount = 1.0F;
            }

            int[] foundArmors = {};

            int[] var4 = slots;
            int var5 = slots.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                int i = var4[var6];
                ItemStack itemStack = (ItemStack)this.armor.get(i);
                if ((!damageSource.isFire() || !itemStack.getItem().isFireproof()) && itemStack.getItem() instanceof ManagoldArmor) {
                    foundArmors = Arrays.copyOf(foundArmors, foundArmors.length + 1);
                    foundArmors[foundArmors.length - 1] = i;

                    ManaComponent component = EntityComponents.MANA.get(player);
                    if (component.getMana() >= (int)amount) {
                        component.useMana((int)amount);
                    } else {
                        itemStack.damage((int)amount, this.player, (player) -> {
                            player.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i));
                        });
                    }


                }
            }

            int[] newSlots = new int[0];
            for (int i = 0; i < slots.length; i++) {
                if (!isInside(foundArmors, slots[i])) {
                    newSlots = Arrays.copyOf(newSlots, newSlots.length + 1);
                    newSlots[newSlots.length - 1] = slots[i];
                }
            }

            slots = newSlots;

        }

        return slots;
    }

    @Unique
    private boolean isInside(int[] foundArmors, int slot) {
        for (int foundArmor: foundArmors) {
            if (slot == foundArmor) {
                return true;
            }
        }

        return false;
    }
}
