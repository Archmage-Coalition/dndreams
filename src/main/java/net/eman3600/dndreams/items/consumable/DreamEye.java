package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.initializers.ModDimensions;
import net.eman3600.dndreams.initializers.ModItems;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class DreamEye extends Item {
    private static final int USE_TIME = 20;

    public DreamEye(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (getMaxUseTime(stack) - remainingUseTicks >= USE_TIME) {
            if (world instanceof ServerWorld && !user.hasVehicle() && !user.hasPassengers()
                    && user.canUsePortals() && world.getRegistryKey() == World.OVERWORLD) {
                RegistryKey<World> registryKey = ModDimensions.DREAM_DIMENSION_KEY;
                ServerWorld serverWorld = ((ServerWorld)world).getServer().getWorld(registryKey);
                if (serverWorld == null) {
                    return;
                }

                stack.decrement(1);
                FabricDimensions.teleport(user, serverWorld, new TeleportTarget(user.getPos(), Vec3d.ZERO, user.getYaw(), user.getPitch()));

                if (user instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity)user;
                    player.getItemCooldownManager().set(ModItems.DREAM_EYE, 100);
                    player.getItemCooldownManager().set(ModItems.ICY_NEEDLE, 100);
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.getRegistryKey() == World.OVERWORLD) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        } else {
            return TypedActionResult.fail(stack);
        }
    }
}
