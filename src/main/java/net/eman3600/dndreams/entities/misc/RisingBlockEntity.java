package net.eman3600.dndreams.entities.misc;

import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.mixin_interfaces.FallingBlockEntityAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class RisingBlockEntity extends FallingBlockEntity {

    public RisingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, World world) {
        super(entityType, world);
    }

    private RisingBlockEntity(World world, double x, double y, double z, BlockState block) {
        this(ModEntities.RISING_BLOCK, world);
        ((FallingBlockEntityAccess)this).setBlockState(block);
        this.intersectionChecked = true;
        this.setPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.setFallingBlockPos(this.getBlockPos());
    }

    @Override
    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movementType == MovementType.SELF ? movement.multiply(-1) : movement);
    }

    @Override
    public void tick() {
        super.tick();

        BlockPos blockPos = getBlockPos();
        boolean collisionOverhead = !world.getBlockState(blockPos.up()).getCollisionShape(world, blockPos.up()).isEmpty();

        if (!world.isClient && world.isOutOfHeightLimit(blockPos.getY()) && world.isOutOfHeightLimit(blockPos.getY() - 10)) {

            discard();
            return;
        }

        if (!world.isClient && !isRemoved() && collisionOverhead) {

            BlockState state = getBlockState();
            Block block = state.getBlock();
            BlockState currState = world.getBlockState(blockPos);
            this.setVelocity(getVelocity().multiply(.7, .5, .7));
            boolean replaceable = currState.canReplace(new AutomaticItemPlacementContext(world, blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
            boolean shouldPlace = state.canPlaceAt(world, blockPos) && !FallingBlock.canFallThrough(world.getBlockState(blockPos.up()));

            if (replaceable && shouldPlace) {

                if (world.setBlockState(blockPos, state, Block.NOTIFY_ALL)) {

                    ((ServerWorld)this.world).getChunkManager().threadedAnvilChunkStorage.sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, this.world.getBlockState(blockPos)));
                    this.discard();
                } else if (dropItem && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
                    this.discard();
                    this.onDestroyedOnLanding(block, blockPos);
                    this.dropItem(block);
                }
            } else {
                discard();
                if (dropItem && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
                    this.onDestroyedOnLanding(block, blockPos);
                    this.dropItem(block);
                }
            }
        }
    }

    public static RisingBlockEntity spawnFromBlock(World world, BlockPos pos, BlockState state) {
        RisingBlockEntity fallingBlockEntity = new RisingBlockEntity(world, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, state.contains(Properties.WATERLOGGED) ? state.with(Properties.WATERLOGGED, false) : state);
        fallingBlockEntity.timeFalling -= 1200;
        world.setBlockState(pos, state.getFluidState().getBlockState(), Block.NOTIFY_ALL);
        world.spawnEntity(fallingBlockEntity);
        return fallingBlockEntity;
    }
}
