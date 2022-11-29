package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.cardinal_components.PermItemComponent;
import net.eman3600.dndreams.cardinal_components.StatBoonComponent;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.mixin_interfaces.ItemEntityAccess;
import net.eman3600.dndreams.recipe.TransmutationRecipe;
import net.eman3600.dndreams.util.ItemInFlowingSpiritCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;

public class ModCallbacks {

    public static Inventory DUMMY_INVENTORY = new SimpleInventory(1);

    public static boolean launchNewItem(World world, Vec3d pos, ItemStack base) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), base.copy());
        entity.addVelocity(0, 0.5D, 0);
        ItemEntityAccess.markTransmutable(entity, false);

        return world.spawnEntity(entity);
    }

    public static void registerCallbacks() {
        ItemInFlowingSpiritCallback.EVENT.register(item -> {
            if (item.world instanceof ServerWorld world && item instanceof ItemEntityAccess access && access.isTransmutable()) {
                access.setTransmutable(false);
                ItemStack stack = item.getStack();
                int amount = stack.getCount();

                ItemStack input = item.getStack().copy();
                input.setCount(1);

                DUMMY_INVENTORY.setStack(0, input);

                Optional<TransmutationRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.TRANSMUTATION, DUMMY_INVENTORY, world);
                if (optional.isPresent()) {
                    TransmutationRecipe recipe = optional.get();
                    ItemStack output = recipe.craft(DUMMY_INVENTORY);

                    if (!output.isEmpty()) {
                        for (int i = 0; i < amount; i++) {
                            if (launchNewItem(world, item.getPos(), output)) {
                                amount -= 1;
                                i--;
                            }
                        }

                        stack.decrement(stack.getCount() - amount);


                        if (amount <= 0) {
                            item.remove(Entity.RemovalReason.DISCARDED);
                        }
                    }
                }
            }
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            Map<EntityAttribute, EntityAttributeModifier> map = EntityComponents.STAT_BOON.get(oldPlayer).getAttributes();

            for (EntityAttribute attribute: map.keySet()) {
                EntityAttributeInstance instance = newPlayer.getAttributeInstance(attribute);
                instance.addTemporaryModifier(map.get(attribute));
            }

            newPlayer.setHealth(newPlayer.getMaxHealth());

            if (!alive) {
                PermItemComponent perms = EntityComponents.PERM_ITEM.get(oldPlayer);
                for (Item toRepair : PermItemComponent.resettables) {
                    perms.pair(toRepair);
                }

                TormentComponent torment = EntityComponents.TORMENT.get(newPlayer);
                torment.shield(false);
                torment.setSanity(Math.max(30, torment.getSanity() - 15));
            }
        });

        ServerPlayConnectionEvents.INIT.register((handler, server) -> EntityComponents.STAT_BOON.get(handler.player).reloadAttributes());

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            StatBoonComponent boons = EntityComponents.STAT_BOON.get(handler.player);
            try {
                WorldComponents.BOSS_STATE.get(server.getScoreboard()).setDifficulty();
            } catch (Exception e) {
                e.printStackTrace();
            }

            boons.reloadAttributes();
            if (handler.player.getMaxHealth() > 20) handler.player.setHealth(boons.hp);
        });
    }


}
