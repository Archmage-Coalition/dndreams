package net.eman3600.dndreams.blocks.spreadable;

import net.eman3600.dndreams.entities.misc.ShadeRiftEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class ShadeMossBlock extends Block {

    public static BooleanProperty TORMENTED = BooleanProperty.of("tormented");

    public static final int SEARCH_RANGE = 48;
    private static final Box SEARCH_BOX = new Box(-SEARCH_RANGE,-SEARCH_RANGE,-SEARCH_RANGE,SEARCH_RANGE,SEARCH_RANGE,SEARCH_RANGE);

    public ShadeMossBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(TORMENTED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);

        builder.add(TORMENTED);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {

        if (state.get(TORMENTED) && !moved) {

            if (newState.isAir()) ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), this.asItem().getDefaultStack());

            Box box = SEARCH_BOX.offset(pos);
            for (ShadeRiftEntity entity: world.getNonSpectatingEntities(ShadeRiftEntity.class, box)) {

                entity.updateMoss();
            }
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }
}
