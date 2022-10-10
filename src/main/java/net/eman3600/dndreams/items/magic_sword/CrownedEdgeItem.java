package net.eman3600.dndreams.items.magic_sword;

import net.eman3600.dndreams.entities.projectiles.CrownedSlashEntity;
import net.eman3600.dndreams.initializers.ModMessages;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.eman3600.dndreams.util.matrices.RotatedBox;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrownedEdgeItem extends SwordItem implements AirSwingItem, ManaCostItem {
    private final int magicDamage;

    public CrownedEdgeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, int magicDamage, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.magicDamage = magicDamage;
    }


    public int magicDamage() {
        return magicDamage;
    }


    @Override
    public void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, Entity hit) {
        if (canAffordMana(user) && user.getAttackCooldownProgress(0.5f) > 0.9f) {
            spendMana(user);

            if (hit == null) {
                stack.damage(1, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, user.getSoundCategory(), 1.0f, 1.5f);

            CrownedSlashEntity slash = new CrownedSlashEntity(user, world);
            slash.initFromStack(stack);

            world.spawnEntity(slash);

            /*Vec3d rot = AirSwingItem.rayZVector(user.getYaw(), user.getPitch());

            Vec3d userPos = user.getEyePos();

            Vec3d targetPos = userPos.add(rot.multiply(1.5));
            targetPos = targetPos.subtract(0, 0.25d, 0);

            RotatedBox box = new RotatedBox(targetPos, targetPos, user.getYaw()).expand(1.5d, 1d, 1.5d);
            Box fullBox = box.engulf();

            List<LivingEntity> entities = world.getNonSpectatingEntities(LivingEntity.class, box);

            for (LivingEntity livingEntity : entities) {
                if (livingEntity == user || user.isTeammate(livingEntity) || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity)livingEntity).isMarker() || !(user.squaredDistanceTo(livingEntity) < 9.0)) continue;
                livingEntity.takeKnockback(0.4f, MathHelper.sin(user.getYaw() * ((float)Math.PI / 180)), -MathHelper.cos(user.getYaw() * ((float)Math.PI / 180)));
                livingEntity.damage(DamageSource.magic(user, user), magicDamage);
            }

            for (double i = fullBox.minX; i <= fullBox.maxX; i += 0.25d) {
                for (double j = fullBox.minZ; j <= fullBox.maxZ; j+= 0.25d) {
                    if (box.contains(i, (box.minY + box.maxY) * .5, j)) {
                        PacketByteBuf packet = PacketByteBufs.create();

                        packet.writeDouble(i);
                        packet.writeDouble(box.minY);
                        packet.writeDouble(j);

                        ServerPlayNetworking.send(user, ModMessages.CROWNED_SLASH_ID, packet);
                    }
                }
            }*/
        }
    }

    @Override
    public int getManaCost() {
        return 5;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(getTooltipMana());
    }


}
