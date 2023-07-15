package net.eman3600.dndreams.blocks.spreadable;

import net.eman3600.dndreams.entities.misc.ShadeRiftEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.WorldAccess;

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
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (state.get(TORMENTED)) {

            Box box = SEARCH_BOX.offset(pos);
            for (ShadeRiftEntity entity: world.getNonSpectatingEntities(ShadeRiftEntity.class, box)) {

                entity.updateMoss();
            }
        }

        super.onBroken(world, pos, state);
    }
}
