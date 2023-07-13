package net.eman3600.dndreams.items.edge_series;

import net.eman3600.dndreams.entities.projectiles.CrownedBeamEntity;
import net.eman3600.dndreams.entities.projectiles.CrownedSlashEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

import javax.annotation.Nullable;

public class TrueCrownedEdgeItem extends CrownedEdgeItem {
    public TrueCrownedEdgeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, int magicDamage, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, magicDamage, settings);
    }


    @Override
    public int getBaseManaCost() {
        return 8;
    }

    @Override
    public void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, @Nullable Entity hit) {
        if (user.getAttackCooldownProgress(0.5f) > 0.9f) {
            if (hit == null) {
                stack.damage(1, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, user.getSoundCategory(), 1.0f, 1.5f);


            float roll = CrownedSlashEntity.randomlyRoll(world);
            CrownedSlashEntity slash = new CrownedSlashEntity(user, world);
            slash.initFromStack(stack, roll);
            world.spawnEntity(slash);

            if (canAffordMana(user, stack)) {
                spendMana(user, stack);

                CrownedBeamEntity beam = new CrownedBeamEntity(user, world);
                beam.initFromStack(stack, roll);
                world.spawnEntity(beam);

                slash.setDamaging(false);
            }
        }
    }
}
