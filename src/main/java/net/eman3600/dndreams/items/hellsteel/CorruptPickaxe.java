package net.eman3600.dndreams.items.hellsteel;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CorruptPickaxe extends PickaxeItem {
    public CorruptPickaxe(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    /*@Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (hasBloodlust(miner) && state.isIn(BlockTags.PICKAXE_MINEABLE) && state.getBlock().getBlastResistance() < 3f)
            world.createExplosion(null, DamageSource.explosion(miner), new ImmunityExplosionBehavior(),
                (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D,
                1.0F, false, Explosion.DestructionType.BREAK);

        return super.postMine(stack, world, state, pos, miner);
    }*/

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
    }

    /*@Override
    public float additionalMiningModifiers(ItemStack stack, PlayerEntity entity, BlockState state, World world) {
        if (hasBloodlust(entity) && state.isIn(BlockTags.PICKAXE_MINEABLE)) {
            return state.getBlock().getBlastResistance() < 3f ? .35f : 1.5f;
        }
        return 1;
    }*/
}
