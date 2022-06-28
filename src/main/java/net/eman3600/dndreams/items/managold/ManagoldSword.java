package net.eman3600.dndreams.items.managold;

import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ManagoldSword extends SwordItem {

    public ManagoldSword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.isPlayer()) {
            ManaComponent component = EntityComponents.MANA.get(attacker);
            if (component.getMana() >= 1) {
                component.useMana(1);
                return true;
            }
        }

        stack.damage(1, attacker, (e) -> {
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
            if (miner.isPlayer()) {
                ManaComponent component = EntityComponents.MANA.get(miner);
                if (component.getMana() >= 2) {
                    component.useMana(2);
                    return true;
                }
            }

            stack.damage(2, miner, (e) -> {
                e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }
}
