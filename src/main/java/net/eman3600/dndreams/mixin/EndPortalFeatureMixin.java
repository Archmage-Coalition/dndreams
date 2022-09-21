package net.eman3600.dndreams.mixin;

import com.mojang.serialization.Codec;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.EndPortalFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;

@Mixin(EndPortalFeature.class)
public abstract class EndPortalFeatureMixin extends Feature<DefaultFeatureConfig> {
    @Shadow
    @Final
    private boolean open;

    public EndPortalFeatureMixin(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        Iterator<BlockPos> var4 = BlockPos.iterate(new BlockPos(blockPos.getX() - 4, blockPos.getY() - 1, blockPos.getZ() - 4), new BlockPos(blockPos.getX() + 4, blockPos.getY() + 32, blockPos.getZ() + 4)).iterator();

        while(true) {
            BlockPos blockPos2;
            boolean bl;
            do {
                if (!var4.hasNext()) {
                    for(int i = 0; i < 4; ++i) {
                        if (open) {
                            if (i == 3) {
                                this.setBlockState(structureWorldAccess, blockPos.up(i), ModBlocks.WORLD_FOUNTAIN.getDefaultState());
                            } else {
                                this.setBlockState(structureWorldAccess, blockPos.up(i), ModBlocks.FLOWING_BEDROCK.getDefaultState());
                            }
                        } else {
                            this.setBlockState(structureWorldAccess, blockPos.up(i), Blocks.BEDROCK.getDefaultState());
                        }
                    }

                    BlockPos blockPos3 = blockPos.up(2);

                    for (Direction direction : Direction.Type.HORIZONTAL) {
                        this.setBlockState(structureWorldAccess, blockPos3.offset(direction), Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, direction));
                    }

                    return true;
                }

                blockPos2 = var4.next();
                bl = blockPos2.isWithinDistance(blockPos, 2.5D);
            } while(!bl && !blockPos2.isWithinDistance(blockPos, 3.5D));

            if (blockPos2.getY() < blockPos.getY()) {
                if (bl) {
                    this.setBlockState(structureWorldAccess, blockPos2, Blocks.BEDROCK.getDefaultState());
                } else if (blockPos2.getY() < blockPos.getY()) {
                    this.setBlockState(structureWorldAccess, blockPos2, Blocks.END_STONE.getDefaultState());
                }
            } else if (blockPos2.getY() > blockPos.getY()) {
                this.setBlockState(structureWorldAccess, blockPos2, Blocks.AIR.getDefaultState());
            } else if (!bl) {
                this.setBlockState(structureWorldAccess, blockPos2, Blocks.BEDROCK.getDefaultState());
            } else if (this.open) {
                this.setBlockState(structureWorldAccess, new BlockPos(blockPos2), Blocks.END_PORTAL.getDefaultState());
            } else {
                this.setBlockState(structureWorldAccess, new BlockPos(blockPos2), Blocks.AIR.getDefaultState());
            }
        }
    }
}
