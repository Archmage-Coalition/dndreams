package net.eman3600.dndreams.items;

import net.eman3600.dndreams.blocks.entities.BonfireBlockEntity;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class VitalSwordItem extends SwordItem implements AirSwingItem {
    public VitalSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof PlayerEntity player) && attacker.world instanceof ServerWorld world) {
            attacker.heal(4.0f);
            Vec3d vec = attacker.getPos();
            world.spawnParticles(ParticleTypes.HEART, vec.x, vec.y + .5, vec.z, 3, .5, .5, .5, 1);
        }

        return super.postHit(stack, target, attacker);
    }

    @Override
    public void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, @Nullable Entity hit) {
        if (user.getAttackCooldownProgress(0.5f) > 0.9f && hit != null) {
            user.heal(1.5f);
            Vec3d vec = user.getPos();
            world.spawnParticles(ParticleTypes.HEART, vec.x, vec.y + .5, vec.z, 3, .5, .5, .5, 1);
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = context.getStack();

        if (state.getBlock() instanceof CampfireBlock && !state.get(Properties.WATERLOGGED)) {
            Direction dir = state.get(Properties.HORIZONTAL_FACING);

            world.setBlockState(pos, ModBlocks.BONFIRE.getDefaultState().with(Properties.HORIZONTAL_FACING, dir), Block.NOTIFY_ALL);

            if (!context.getPlayer().isCreative()) stack.decrement(1);
            return ActionResult.SUCCESS;
        }


        return ActionResult.PASS;
    }
}
