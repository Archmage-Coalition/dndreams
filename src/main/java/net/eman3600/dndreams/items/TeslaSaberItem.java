package net.eman3600.dndreams.items;

import net.eman3600.dndreams.cardinal_components.ShockComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.interfaces.PowerCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeslaSaberItem extends SwordItem implements PowerCostItem {
    public TeslaSaberItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        ShockComponent shock = EntityComponents.SHOCK.get(user);

        if (!shock.hasShock() && canAffordPower(user, stack)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }

        return TypedActionResult.pass(stack);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 20;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world instanceof ServerWorld server && user instanceof PlayerEntity player && canAffordPower(player, stack)) {
            spendPower(player, stack);

            ShockComponent shock = EntityComponents.SHOCK.get(player);
            shock.chargeShock(12.5f);

            LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(server);
            if (lightning != null) {
                lightning.setPosition(player.getPos());
                lightning.setChanneler((ServerPlayerEntity) player);
                server.spawnEntity(lightning);
            }
        }

        return stack;
    }

    @Override
    public float getBasePowerCost() {
        return 5f;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(getTooltipPower(world, stack));
    }
}
