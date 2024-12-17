package net.eman3600.dndreams.items.dreadful;

import net.eman3600.dndreams.entities.projectiles.HemorrhageScarEntity;
import net.eman3600.dndreams.entities.projectiles.HemorrhageSlashEntity;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.eman3600.dndreams.items.magic_bow.MagicCrossbowItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HemorrhageItem extends SwordItem implements AirSwingItem, MagicDamageItem {
    private final int magicDamage;
    public static final int MAX_CHARGES = 2;

    public HemorrhageItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, int magicDamage, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.magicDamage = magicDamage;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (MagicCrossbowItem.isCharged(stack)) return true;

        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (MagicCrossbowItem.isCharged(stack)) return true;

        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, @javax.annotation.Nullable Entity hit) {
        if (user.getAttackCooldownProgress(0.5f) > 0.9f) {

            if (hit == null) {
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, user.getSoundCategory(), 1.0f, .8f);
            }

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_DROWNED_AMBIENT, user.getSoundCategory(), 1.0f, 2f);


            if (MagicCrossbowItem.isCharged(stack)) {

                MagicCrossbowItem.setCharges(stack, MagicCrossbowItem.getCharges(stack) - 1);

                HemorrhageSlashEntity slash = new HemorrhageSlashEntity(user, world);
                slash.initFromStack(stack);
                world.spawnEntity(slash);

                int damage = MAX_CHARGES - (hit == null ? 0 : 1);

                if (!MagicCrossbowItem.isCharged(stack) && !user.isCreative()) {
                    stack.damage(damage, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                }
            } else {

                HemorrhageScarEntity scar = new HemorrhageScarEntity(user, world);
                scar.initFromStack(stack);
                world.spawnEntity(scar);

                if (hit == null && !user.isCreative()) {
                    stack.damage(1, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);

        if (!MagicCrossbowItem.isCharged(stack)) {
            List<HemorrhageScarEntity> scars = world.getEntitiesByClass(HemorrhageScarEntity.class, user.getBoundingBox().expand(15), (hemorrhageScarEntity -> hemorrhageScarEntity.getOwner() == user));

            for (HemorrhageScarEntity scar : scars) {
                scar.slash();
            }

            if (!scars.isEmpty()) {
                if (!world.isClient) {
                    user.getItemCooldownManager().set(this, 10);
                    user.damage(BloodlustItem.CRIMSON_SACRIFICE, 6);

                    MagicCrossbowItem.setCharges(stack, MAX_CHARGES);
                }

                return TypedActionResult.consume(stack);
            }
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.0"));
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.1"));
        tooltip.add(Text.translatable("tooltip.dndreams.sacrifice"));
        tooltip.add(getTooltipMagicDamage(stack));
    }


    @Override
    public float getBaseMagicDamage() {
        return magicDamage;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {

        if (MagicCrossbowItem.isCharged(stack)) {
            int charges = MagicCrossbowItem.getCharges(stack);

            return charges * 13 / MAX_CHARGES;
        }

        return super.getItemBarStep(stack);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {

        if (MagicCrossbowItem.isCharged(stack)) {
            return 0xd30037;
        }

        return super.getItemBarColor(stack);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return super.isItemBarVisible(stack) || MagicCrossbowItem.isCharged(stack);
    }
}
