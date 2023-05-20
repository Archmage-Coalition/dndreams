package net.eman3600.dndreams.items.staff;

import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnapStaffItem extends TooltipItem implements ManaCostItem {

    public SnapStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getBaseManaCost() {
        return 6;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);
        if (canAffordMana(user, stack)) {
            HitResult result = user.raycast(20, 0, false);

            if (result.squaredDistanceTo(user) < 400) {
                if (!world.isClient) {

                    Vec3d target;
                    if (result instanceof BlockHitResult blockHit) {
                        target = Vec3d.ofCenter(blockHit.getBlockPos().offset(blockHit.getSide()));
                    } else {
                        target = result.getPos();
                    }

                    double lower = Math.min(user.getY(), target.y);
                    double upper = Math.max(user.getY(), target.y) + 1;
                    float horizontal = (float) MathHelper.atan2(target.z - user.getZ(), target.x - user.getX());

                    for (int i = 0; i < 16; ++i) {
                        double h = 1.25 * (double) (i + 1);
                        conjureFangs(user, user.getX() + (double) MathHelper.cos(horizontal) * h, user.getZ() + (double) MathHelper.sin(horizontal) * h, lower, upper, horizontal, i);
                    }
                }

                spendMana(user, stack);

                user.getItemCooldownManager().set(this, 26);
                if (!user.isCreative()) stack.damage(1, user, p -> p.sendToolBreakStatus(hand));

                return TypedActionResult.success(stack, world.isClient());
            }
        }

        return super.use(world, user, hand);
    }

    private void conjureFangs(PlayerEntity user, double x, double z, double maxY, double y, float yaw, int warmup) {
        BlockPos blockPos = new BlockPos(x, y, z);
        boolean bl = false;
        double d = 0.0;
        do {
            VoxelShape voxelShape;
            BlockPos blockPos2;
            if (!user.world.getBlockState(blockPos2 = blockPos.down()).isSideSolidFullSquare(user.world, blockPos2, Direction.UP)) continue;
            if (!user.world.isAir(blockPos) && !(voxelShape = user.world.getBlockState(blockPos).getCollisionShape(user.world, blockPos)).isEmpty()) {
                d = voxelShape.getMax(Direction.Axis.Y);
            }
            bl = true;
            break;
        } while ((blockPos = blockPos.down()).getY() >= MathHelper.floor(maxY) - 1);
        if (bl) {
            EvokerFangsEntity fang = new EvokerFangsEntity(user.world, x, (double)blockPos.getY() + d, z, yaw, warmup, user);

            user.world.spawnEntity(fang);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(getTooltipMana(stack));
    }
}
