package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.cardinal_components.DreamingComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModDimensions;
import net.eman3600.dndreams.initializers.ModItems;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class IcyNeedle extends Item {

    public IcyNeedle(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world instanceof ServerWorld && !user.hasVehicle() && !user.hasPassengers()
                    && user.canUsePortals() && world.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY) {
            RegistryKey<World> registryKey = World.OVERWORLD;
            ServerWorld serverWorld = ((ServerWorld)world).getServer().getWorld(registryKey);
            if (serverWorld == null) {
                return TypedActionResult.fail(stack);
            }

            DreamingComponent component = EntityComponents.DREAMING.get(user);

            FabricDimensions.teleport(user, serverWorld, new TeleportTarget(component.returnPos(), Vec3d.ZERO, user.getYaw(), user.getPitch()));

            user.getItemCooldownManager().set(ModItems.SLEEPING_BREW, 200);
        } else {
            user.damage(DamageSource.FREEZE, 1);
        }

        user.getItemCooldownManager().set(this, 20);
        stack.decrement(1);
        return TypedActionResult.consume(stack);
    }
}
