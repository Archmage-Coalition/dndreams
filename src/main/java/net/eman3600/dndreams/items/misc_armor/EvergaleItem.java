package net.eman3600.dndreams.items.misc_armor;

import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class EvergaleItem extends TooltipItem implements FabricElytraItem, ManaCostItem {

    public static double ACCELERATION = .07;

    public EvergaleItem(Settings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(ModItems.LOST_DREAM);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
        ItemStack itemStack2 = user.getEquippedStack(equipmentSlot);
        if (itemStack2.isEmpty()) {
            user.equipStack(equipmentSlot, itemStack.copy());
            if (!world.isClient()) {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
            }
            itemStack.setCount(0);
            return TypedActionResult.success(itemStack, world.isClient());
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    @Nullable
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA;
    }

    @Override
    public void doVanillaElytraTick(LivingEntity entity, ItemStack chestStack) {

        if (entity instanceof PlayerEntity player) {
            InfusionComponent infusion = EntityComponents.INFUSION.get(player);

            if (infusion.isGaleBoosted()) {
                int nextRoll = entity.getRoll() + 1;

                if (!entity.world.isClient) {
                    if (nextRoll % 3 == 0) {
                        chestStack.damage(1, entity, p -> p.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                    }

                    if (nextRoll % 10 == 0) entity.emitGameEvent(GameEvent.ELYTRA_GLIDE);
                }

                return;
            }
        }

        FabricElytraItem.super.doVanillaElytraTick(entity, chestStack);
    }

    public static boolean isUsing(PlayerEntity player) {
        return player.isFallFlying() && player.getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.EVERGALE);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClient && stack.getDamage() > 0 && entity instanceof PlayerEntity player && !player.isFallFlying() && player.isOnGround() && player.getEquippedStack(EquipmentSlot.CHEST) == stack && canAffordMana(player, stack)) {

            stack.setDamage(stack.getDamage() - 2);
            spendMana(player, stack);

            if (!canAffordMana(player, stack)) {
                EntityComponents.INFUSION.get(player).setGaleCooling();
            }
        }
    }

    @Override
    public int getBaseManaCost() {
        return 1;
    }

    @Override
    public boolean canAffordMana(PlayerEntity player, ItemStack stack) {
        return ManaCostItem.super.canAffordMana(player, stack) && EntityComponents.INFUSION.get(player).canGaleRepair();
    }
}
