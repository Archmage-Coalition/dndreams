package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.basics.ModEnchantments;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.MysticStaffItem;
import net.eman3600.dndreams.items.interfaces.UnbreakableItem;
import net.eman3600.dndreams.mixin_interfaces.ItemStackAccess;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackAccess {
    @Shadow
    public abstract boolean isDamageable();

    @Shadow public abstract Item getItem();

    @Shadow public abstract boolean isIn(TagKey<Item> tag);

    @Shadow public abstract boolean isDamaged();

    @Shadow public abstract void setDamage(int damage);

    @Shadow public abstract int getDamage();

    @Shadow public abstract NbtCompound getOrCreateNbt();

    @Shadow @Final private static String REPAIR_COST_KEY;
    @Unique
    private static final float SANITY_DIVISOR = 20f;

    @Unique
    private int repairTicks = 0;

    @Inject(at = @At("HEAD"), cancellable = true, method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z")
    public void dndreams$damage$boolean(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack)(Object)this;

        if (stack.getItem() instanceof UnbreakableItem) {
            cir.setReturnValue(false);
            return;
        }

        if (isDamageable() && stack.isIn(ModTags.MANA_BUFFERING_TOOLS)) {
            ManaComponent mana = EntityComponents.MANA.get(player);
            if (mana.canAfford(amount)) {
                if (amount > 0) {
                    int i = EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
                    int j = amount;

                    for(int k = 0; i > 0 && k < amount; ++k) {
                        if (UnbreakingEnchantment.shouldPreventDamage(stack, i, random)) {
                            j--;
                        }
                    }

                    if (j > 0) {
                        mana.useMana(j);
                    }
                }

                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void dndreams$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        PlayerEntity player = context.getPlayer();

        if (player != null && ModStatusEffects.shouldRestrict(player)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
    private void dndreams$getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        if (getItem() instanceof MysticStaffItem item) {
            cir.setReturnValue(item.getStaffMaxDamage((ItemStack)(Object)this));
        }
    }

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void dndreams$inventoryTick$autoRepair(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {

        if (isIn(ModTags.AUTO_REPAIRING_TOOLS) && isDamaged() && !world.isClient() && entity instanceof PlayerEntity player) {

            ItemStack stack = (ItemStack)(Object)this;

            repairTicks++;

            for (TagKey<Item> key: ItemStackAccess.repairPredicates.keySet()) {

                if (isIn(key)) {

                    int requiredTicks = ItemStackAccess.repairPredicates.get(key).apply(stack, player);

                    if (requiredTicks == -1) continue;

                    requiredTicks *= 1 - (EnchantmentHelper.getLevel(ModEnchantments.REPARATION, stack) * .15);

                    if (repairTicks >= requiredTicks) {

                        if (ItemStackAccess.repairCostConsumers.containsKey(key)) {
                            ItemStackAccess.repairCostConsumers.get(key).accept(stack, player);
                        }

                        repairTicks = 0;
                        setDamage(Math.max(getDamage() - 1, 0));
                        return;
                    }
                }
            }
        }
    }
}
