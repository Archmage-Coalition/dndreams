package net.eman3600.dndreams.items.cloud;

import net.eman3600.dndreams.entities.projectiles.CloudSlashEntity;
import net.eman3600.dndreams.entities.projectiles.CrownedBeamEntity;
import net.eman3600.dndreams.entities.projectiles.CrownedSlashEntity;
import net.eman3600.dndreams.items.enchantments.AliasedEnchantment;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CloudSwordItem extends SwordItem implements AirSwingItem, ManaCostItem {

    public CloudSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }


    @Override
    public void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, @javax.annotation.Nullable Entity hit) {
        if (user.getAttackCooldownProgress(0.5f) > 0.9f) {

            if (canAffordMana(user, stack)) {
                if (hit == null) {
                    stack.damage(1, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                }

                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, user.getSoundCategory(), 1.0f, 1.5f);


                float roll = CrownedSlashEntity.randomlyRoll(world);

                spendMana(user, stack);

                CloudSlashEntity beam = new CloudSlashEntity(user, world);
                beam.initFromStack(stack, roll);
                world.spawnEntity(beam);
            }
        }
    }

    @Override
    public int getBaseManaCost() {
        return 5;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
        tooltip.add(getTooltipMana(stack));
    }
}
