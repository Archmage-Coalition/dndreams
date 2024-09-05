package net.eman3600.dndreams.items;

import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class AscendItem extends TooltipItem {

    public AscendItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        InfusionComponent component = EntityComponents.INFUSION.get(user);

        if (component.getAscendState() <= 0 && canAscend(world, new BlockPos(user.getEyePos())) && !user.hasStatusEffect(ModStatusEffects.INSUBSTANTIAL)) {

            if (!world.isClient) {
                component.setAscending();
                if (!user.isCreative()) stack.damage(1, user, e -> e.sendToolBreakStatus(hand));
            }
            return TypedActionResult.success(stack);
        }


        return super.use(world, user, hand);
    }

    private boolean canAscend(World world, BlockPos pos) {

        boolean airClears = false;
        for (int y = pos.getY(); y < world.getTopY(); y++) {

            pos = pos.withY(y);
            BlockState state = world.getBlockState(pos);

            if (state.isIn(ModTags.SUBSTANTIAL)) return false;
            else if (state.getCollisionShape(world, pos).isEmpty() == airClears) {

                if (airClears && world.getBlockState(pos.up()).getCollisionShape(world, pos).isEmpty()) return true;
                airClears = true;
            }
        }
        return airClears;
    }

    public static boolean isInBlock(Entity entity) {

        float f = entity.getDimensions(entity.getPose()).width * 0.8f;
        Box box = Box.of(entity.getEyePos(), f, 1.0E-6, f);
        return BlockPos.stream(box).anyMatch(pos -> {
            BlockState blockState = entity.world.getBlockState(pos);
            return !blockState.isAir() && !blockState.getCollisionShape(entity.world, pos).isEmpty();
        });
    }
}
