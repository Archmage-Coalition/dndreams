package net.eman3600.dndreams.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.interfaces.ActivateableToolItem;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstrumentOfTruthItem extends Item implements ActivateableToolItem, AirSwingItem {

    public InstrumentOfTruthItem(Settings settings) {
        super(settings);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (isActive(stack) && state.getBlock().getHardness() > 0f) {
            return 0.46875f * state.getBlock().getHardness() * (isHyper(stack) ? 100 : 30);
        }

        return 1;
    }

    @Override
    public boolean isSuitableFor(ItemStack stack, BlockState state) {
        return isActive(stack);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? getForm(stack).attributeModifiers : super.getAttributeModifiers(stack, slot);
    }

    public boolean isActive(ItemStack stack) {
        return getForm(stack).isActive();
    }

    public InstrumentForm getForm(ItemStack stack) {
        if (stack.hasNbt()) {
            return InstrumentForm.stateMap.getOrDefault(stack.getNbt().getInt("Form"), InstrumentForm.INACTIVE);
        } else {
            return InstrumentForm.INACTIVE;
        }
    }

    public void setForm(ItemStack stack, InstrumentForm form) {
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putInt("Form", form.id);

        if (!form.isActive()) {
            nbt.putBoolean("Hyper", false);
        }
    }

    public float getMagicDamage(ItemStack stack) {
        return getForm(stack).magicDamage;
    }

    /**
     * Determines if the stack has instamine enabled.
     * @param stack The ItemStack in question.
     * @return If the stack has instamine enabled.
     */
    public boolean isHyper(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().getBoolean("Hyper");
    }

    public void setHyper(ItemStack stack, boolean hyper) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!hyper || getForm(stack).miningTool) {
            nbt.putBoolean("Hyper", hyper);
        }
    }

    @Override
    public boolean allowContinuingBlockBreaking(PlayerEntity player, ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        try {
            if (world instanceof ClientWorldAccess access && access.getPlayer() != null) {
                if (access.getPlayer().getInventory().contains(stack)) {
                    tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.0"));
                    tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.1"));
                } else {
                    tooltip.add(Text.literal("§3§kYou may not yet see the truth"));
                    tooltip.add(Text.literal("§3§kof which you spurn."));
                }
            }
        } catch (NullPointerException ignored) {}

        if (getMagicDamage(stack) > 0) {
            tooltip.add(Text.translatable("tooltip.dndreams.magic_damage", "§2" + (int) getMagicDamage(stack)));
        }
    }

    @Override
    public void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, @Nullable Entity hit) {

        if (hit instanceof LivingEntity target && getForm(stack) == InstrumentForm.KATANA && user.getAttackCooldownProgress(0.5f) > 0.9f) {

            target.damage(DamageSource.magic(user, user), getMagicDamage(stack));
            target.timeUntilRegen = 0;
        } else if (getForm(stack) != InstrumentForm.AXE && getForm(stack).miningTool) {

            setForm(stack, InstrumentForm.KATANA);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && isActive(stack) && entity instanceof PlayerEntity player) {
            TormentComponent torment = EntityComponents.TORMENT.get(player);

            if (!torment.isTruthActive()) {
                setForm(stack, InstrumentForm.INACTIVE);
                torment.setTruthActive(false);
            }
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return getForm(stack) == InstrumentForm.STAFF ? UseAction.BOW : UseAction.NONE;
    }

    public InstrumentForm getBestForm(World world, PlayerEntity player, InstrumentForm currentForm, @Nullable BlockState state) {

        EntityHitResult cast = AirSwingItem.castWithDistance(player, 6, entity -> !entity.isSpectator() && entity.canHit());

        if (cast != null && cast.getEntity() instanceof LivingEntity && cast.getEntity().getType() != EntityType.ARMOR_STAND) {

            return currentForm == InstrumentForm.AXE ? currentForm : InstrumentForm.KATANA;
        }

        HitResult blockHit = player.raycast(6, 0, false);

        if (blockHit instanceof BlockHitResult hit && state == null) {

            state = world.getBlockState(hit.getBlockPos());
        }

        if (state != null && !state.isAir()) {
            return state.isIn(BlockTags.PICKAXE_MINEABLE) ? InstrumentForm.PICKAXE : state.isIn(BlockTags.AXE_MINEABLE) || state.isIn(BlockTags.HOE_MINEABLE) ? InstrumentForm.AXE : state.isIn(BlockTags.SHOVEL_MINEABLE) ? InstrumentForm.SHOVEL : InstrumentForm.PICKAXE;
        }

        return InstrumentForm.STAFF;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        TormentComponent torment = EntityComponents.TORMENT.get(user);

        if (!isActive(stack) && !torment.isTruthActive()) {

            torment.setTruthActive(true);
            setForm(stack, getBestForm(world, user, InstrumentForm.INACTIVE, null));

            return TypedActionResult.success(stack);
        } else if (getForm(stack).miningTool && user.isSneaking()) {

            setHyper(stack, !isHyper(stack));

            return TypedActionResult.success(stack);
        }

        return super.use(world, user, hand);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (isActive(stack) && miner instanceof PlayerEntity player) {

            setForm(stack, getBestForm(world, player, getForm(stack), state));
            return true;
        }

        return false;
    }

    public enum InstrumentForm {
        INACTIVE(0, false, 0),
        STAFF(1, false, 18),
        KATANA(2, false, 13, -2.4f, 10),
        PICKAXE(3, true, 5, -2.8f, 0),
        AXE(4, true, 15, -3f, 10),
        SHOVEL(5, true, 5.5f, -3f, 0);

        public final int id;
        public final float magicDamage;
        public final boolean miningTool;
        public final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
        public static final Map<Integer, InstrumentForm> stateMap;

        InstrumentForm(int id, boolean miningTool, float magicDamage) {
            this.id = id;
            this.magicDamage = magicDamage;
            this.miningTool = miningTool;
            attributeModifiers = ImmutableMultimap.of();
        }

        InstrumentForm(int id, boolean miningTool, double attackDamage, double attackSpeed, float magicDamage) {
            this.id = id;
            this.miningTool = miningTool;
            this.magicDamage = magicDamage;

            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();

            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", attackDamage, EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));

            attributeModifiers = builder.build();
        }

        public boolean isActive() {
            return this != INACTIVE;
        }

        static {
            stateMap = new HashMap<>();

            for (InstrumentForm state: values()) {
                stateMap.put(state.id, state);
            }
        }
    }
}
