package net.eman3600.dndreams.items.pericharite;

import net.eman3600.dndreams.entities.projectiles.FallingStarEntity;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.DivineWeaponItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PerichariteSwordItem extends SwordItem implements DivineWeaponItem, MagicDamageItem, ManaCostItem, AirSwingItem {
    private static final double DISTANCE = 24;
    private static final float PREDICTION_DENOMINATOR = 2.0f;
    private static final int MIN_SPACING = 2;
    private static final int MAX_SPACING = 5;


    public PerichariteSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, @Nullable Entity hit) {
        if (user.getAttackCooldownProgress(0.5f) > 0.9f && canAffordMana(user, stack)) {

            Vec3d target;
            Vec3d prediction;
            if (hit == null) {
                HitResult raycast;
                raycast = (raycast = AirSwingItem.castWithDistance(user, DISTANCE, entity -> !entity.isSpectator() && entity.canHit())) == null ? (user.raycast(DISTANCE, 0, false)) : raycast;
                if (raycast != null && raycast.getType() != HitResult.Type.MISS) {


                    if (raycast instanceof EntityHitResult entityHit) {
                        target = raycast.getPos();
                        prediction = entityHit.getEntity().getVelocity();
                    } else {
                        target = raycast.getPos();
                        prediction = Vec3d.ZERO;
                    }
                } else {
                    target = user.getPos().add(AirSwingItem.rayZVector(user.getYaw(), user.getPitch()).multiply(DISTANCE));
                    prediction = Vec3d.ZERO;
                }

                stack.damage(1, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            } else {
                target = hit.getPos();
                prediction = AirSwingItem.rayZVector(user.getYaw(), 0).multiply(.2d).add(user.getVelocity());
            }

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, user.getSoundCategory(), 1.0f, 1.5f);


            int count = world.random.nextInt(3) + 2;
            float distance = FallingStarEntity.randomlyDistance(world);
            spendMana(user, stack);

            for (int i = 0; i < count; i++) {
                float yaw = world.random.nextInt(360) - 180;
                float pitch = FallingStarEntity.randomlyPitch(world);

                FallingStarEntity star = new FallingStarEntity(user, world);
                star.initFromStack(stack, yaw, pitch, count, distance, target.add(prediction.multiply(distance/PREDICTION_DENOMINATOR)));
                world.spawnEntity(star);

                distance += world.random.nextBetween(MIN_SPACING, MAX_SPACING);
            }
        }
    }

    @Override
    public float getBaseMagicDamage() {
        return 7;
    }

    @Override
    public int getBaseManaCost() {
        return 5;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
        tooltip.add(getTooltipMana(stack));
        tooltip.add(getTooltipMagicDamage(stack));
    }
}
