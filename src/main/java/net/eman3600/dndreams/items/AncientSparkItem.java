package net.eman3600.dndreams.items;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.items.interfaces.RitualRemainItem;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SculkShriekerBlock;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class AncientSparkItem extends Item implements RitualRemainItem {
    private static Random random = new Random();

    public AncientSparkItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();

        if (spreadCharge(world, pos, 0) || spreadDischarge(world, pos, 0)) {
            world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 4.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

            stack.damage(1, player, (p) -> {
                p.sendToolBreakStatus(context.getHand());
            });
            return ActionResult.SUCCESS;
        } else if (world.getBlockState(pos).getBlock() instanceof SculkShriekerBlock) {
            BlockState state = world.getBlockState(pos);

            if (!state.get(SculkShriekerBlock.CAN_SUMMON)) {
                world.setBlockState(pos, state.with(SculkShriekerBlock.CAN_SUMMON, true));

                world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 4.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

                stack.damage(1, player, (p) -> {
                    p.sendToolBreakStatus(context.getHand());
                });
                return ActionResult.SUCCESS;
            }
        } else if (world.getBlockState(pos).getBlock() == ModBlocks.WORLD_FOUNTAIN) {
            if (context.getPlayer() instanceof ServerPlayerEntity player2) {
                EntityComponents.GATEWAY.get(player2).enterGateway(0, true);
            }
            stack.damage(1, player, (p) -> {
                p.sendToolBreakStatus(context.getHand());
            });
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private boolean spreadCharge(World world, BlockPos pos, int iterations) {
        if (world.getBlockState(pos).isOf(Blocks.REINFORCED_DEEPSLATE)) {
            world.setBlockState(pos, ModBlocks.CHARGED_DEEPSLATE.getDefaultState());
            world.updateNeighborsAlways(pos, ModBlocks.CHARGED_DEEPSLATE);

            world.addParticle(ParticleTypes.SONIC_BOOM, pos.getX() + random.nextDouble(-0.7, 0.7), pos.getY() + random.nextDouble(-0.7, 0.7), pos.getZ() + random.nextDouble(-0.7, 0.7), 0, 0, 0);

            iterations++;

            if (iterations < 80) {
                spreadCharge(world, pos.up(), iterations);
                spreadCharge(world, pos.down(), iterations);
                spreadCharge(world, pos.north(), iterations);
                spreadCharge(world, pos.east(), iterations);
                spreadCharge(world, pos.south(), iterations);
                spreadCharge(world, pos.west(), iterations);
            }

            return true;
        }
        return false;
    }

    private boolean spreadDischarge(World world, BlockPos pos, int iterations) {
        if (world.getBlockState(pos).isIn(ModTags.DEEPSLATE_FRAME)) {
            world.setBlockState(pos, Blocks.REINFORCED_DEEPSLATE.getDefaultState());
            world.updateNeighborsAlways(pos, Blocks.REINFORCED_DEEPSLATE);

            world.addParticle(ParticleTypes.SONIC_BOOM, pos.getX() + random.nextDouble(-0.2, 1.2), pos.getY() + random.nextDouble(-0.2, 1.2), pos.getZ() + random.nextDouble(-0.2, 1.2), 0, 0, 0);

            iterations++;

            if (iterations < 80) {
                spreadDischarge(world, pos.up(), iterations);
                spreadDischarge(world, pos.down(), iterations);
                spreadDischarge(world, pos.north(), iterations);
                spreadDischarge(world, pos.east(), iterations);
                spreadDischarge(world, pos.south(), iterations);
                spreadDischarge(world, pos.west(), iterations);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getRitualRemains(AbstractRitual ritual, SoulCandleBlockEntity entity, ItemStack stack) {
        ItemStack clone = stack.copy();
        clone.setDamage(clone.getDamage() + 8);

        if (clone.getDamage() >= clone.getMaxDamage()) return ItemStack.EMPTY;

        return clone;
    }
}
