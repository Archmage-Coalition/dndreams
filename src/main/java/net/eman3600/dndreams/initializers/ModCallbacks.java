package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.recipe.TransmutationRecipe;
import net.eman3600.dndreams.util.ItemInFlowingSpiritCallback;
import net.eman3600.dndreams.util.ItemEntityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

public class ModCallbacks {

    public static Inventory DUMMY_INVENTORY = new SimpleInventory(1);

    public static boolean launchNewItem(World world, Vec3d pos, ItemStack base) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), base.copy());
        entity.addVelocity(0, 0.5D, 0);

        return world.spawnEntity(entity);
    }

    public static void registerCallbacks() {
        ItemInFlowingSpiritCallback.EVENT.register(item -> {
            if (item.world instanceof ServerWorld world && item instanceof ItemEntityInterface entityInterface) {
                int windupTicks = entityInterface.getWindupTicks();
                if (windupTicks >= 60) {
                    entityInterface.setWindupTicks(0);
                    ItemStack stack = item.getStack();
                    int amount = stack.getCount();

                    ItemStack input = item.getStack().copy();
                    input.setCount(1);

                    DUMMY_INVENTORY.setStack(0, input);

                    Optional<TransmutationRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.TRANSMUTATION, DUMMY_INVENTORY, world);
                    if (optional.isPresent()) {
                        TransmutationRecipe craftingRecipe = optional.get();
                        ItemStack output = craftingRecipe.craft(DUMMY_INVENTORY);

                        if (!output.isEmpty()) {
                            for (int i = 0; i < amount; i++) {
                                if (launchNewItem(world, item.getPos(), output)) {
                                    amount -= 1;
                                }
                            }

                            stack.decrement(stack.getCount() - amount);


                            if (amount <= 0) {
                                item.remove(Entity.RemovalReason.DISCARDED);
                            }
                        }
                    }
                } else {
                    entityInterface.incrementWindupTicks(1);
                }

            }
        });
    }


}
