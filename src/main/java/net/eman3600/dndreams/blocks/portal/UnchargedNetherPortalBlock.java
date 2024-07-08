package net.eman3600.dndreams.blocks.portal;

import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UnchargedNetherPortalBlock extends GenericPortalBlock {

    private static final int MANA_COST = 10;

    public UnchargedNetherPortalBlock(Settings settings, Block frameBlock) {
        super(settings, frameBlock);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (!world.isClient && player != null) {
            ManaComponent mana = EntityComponents.MANA.get(player);

            if (mana.canAfford(MANA_COST)) {
                GenericPortalAreaHelper helper = new GenericPortalAreaHelper(world, pos, state.get(AXIS), frameBlock, this);

                if (helper.width > 1 && helper.height > 1) {
                    helper.createPortalOf(Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, state.get(AXIS)));

                    mana.useMana(MANA_COST);

                    world.playSound(null, pos, SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS, 1, 1);
                }
            } else {
                player.sendMessage(Text.translatable(getTranslationKey() + ".not_enough_mana"), true);
            }
        }

        return ActionResult.SUCCESS;
    }
}
