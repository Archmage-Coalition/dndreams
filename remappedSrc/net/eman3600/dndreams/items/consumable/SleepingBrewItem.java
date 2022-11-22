package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class SleepingBrewItem extends Item {
    public SleepingBrewItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world instanceof ServerWorld && user instanceof PlayerEntity player && !user.hasVehicle() && !user.hasPassengers()
                && user.canUsePortals() && world.getRegistryKey() == World.OVERWORLD) {
            RegistryKey<World> registryKey = ModDimensions.DREAM_DIMENSION_KEY;
            ServerWorld serverWorld = ((ServerWorld)world).getServer().getWorld(registryKey);
            if (serverWorld == null) {
                return stack;
            }

            if (!player.isCreative()) {
                player.setStackInHand(user.getActiveHand(), ItemUsage.exchangeStack(stack, player, Items.GLASS_BOTTLE.getDefaultStack()));
            }
            FabricDimensions.teleport(user, serverWorld, new TeleportTarget(user.getPos(), Vec3d.ZERO, user.getYaw(), user.getPitch()));

            player.getItemCooldownManager().set(ModItems.SLEEPING_BREW, 200);
            player.getItemCooldownManager().set(ModItems.ICY_NEEDLE, 200);
            player.getItemCooldownManager().set(ModItems.MATERIALIZE_TOME, 200);

        }

        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.getRegistryKey() == World.OVERWORLD) return ItemUsage.consumeHeldItem(world, user, hand);

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
