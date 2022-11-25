package net.eman3600.dndreams.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TaglockItem extends Item {
    public TaglockItem(Settings settings) {
        super(settings);
    }

    public static void bind(ItemStack stack, LivingEntity entity) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putUuid("bound", entity.getUuid());
        nbt.putString("bound_name", entity.getName().getString());
    }

    @Nullable
    public static LivingEntity getBoundEntity(ItemStack stack, ServerWorld world) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.contains("bound")) {
            Entity entity = world.getEntity(nbt.getUuid("bound"));
            return entity instanceof LivingEntity living ? living : null;
        }
        return null;
    }

    @Nullable
    public static String getBoundName(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.contains("bound_name")) {
            return nbt.getString("bound_name");
        }
        return null;
    }

    public static boolean isUnboundTaglock(ItemStack stack) {
        if (stack.getItem() instanceof TaglockItem) {
            return !stack.getOrCreateNbt().contains("bound");
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (nbt.contains("bound_name")) {
            tooltip.add(Text.translatable(getTranslationKey() + ".tooltip", "§d" + nbt.getString("bound_name")));
        }

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (isUnboundTaglock(stack)) {
            ItemStack newStack = new ItemStack(this);
            bind(newStack, entity);
            user.getInventory().insertStack(newStack);
            stack.decrement(1);
            return ActionResult.success(user.world.isClient);
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}
