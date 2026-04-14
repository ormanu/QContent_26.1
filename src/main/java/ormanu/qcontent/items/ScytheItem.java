package ormanu.qcontent.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import ormanu.qcontent.sound.ModSounds;

import java.util.function.Consumer;

public class ScytheItem extends Item {
    public ScytheItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // Leaves fast (like shears-ish)
        if (state.is(BlockTags.LEAVES)) {
            return 15.0F;
        }

        // Plants/grass/vines fast
        if (state.is(Blocks.GRASS_BLOCK) ||
                state.is(Blocks.DIRT) ||
                state.is(Blocks.TALL_GRASS) ||
                state.is(Blocks.FERN) ||
                state.is(Blocks.LARGE_FERN) ||
                state.is(Blocks.DEAD_BUSH) ||
                state.is(Blocks.VINE) ||
                state.is(Blocks.GLOW_LICHEN)) {
            return 12.0F;
        }

        return super.getDestroySpeed(stack, state);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        ItemStack stack = ctx.getItemInHand();

        // must have air above (like hoe)
        if (!level.getBlockState(pos.above()).isAir()) return InteractionResult.PASS;

        var state = level.getBlockState(pos);

        // basic tilling targets
        boolean tillable =
                state.is(Blocks.DIRT) ||
                        state.is(Blocks.GRASS_BLOCK) ||
                        state.is(Blocks.DIRT_PATH) ||
                        state.is(Blocks.ROOTED_DIRT);

        if (!tillable) return InteractionResult.PASS;

        if (!level.isClientSide()) {
            level.setBlock(pos, Blocks.FARMLAND.defaultBlockState(), 11);
            level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (ctx.getPlayer() != null) {
                stack.hurtAndBreak(1, ctx.getPlayer(), ctx.getHand());
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);

        Level level = attacker.level();
        if (level.isClientSide()) return;

        float pitch = 0.65F;

        level.playSound(
                null,                       // null = everyone nearby hears it
                target.blockPosition(),      // play at target location
                ModSounds.ScytheHit,
                SoundSource.PLAYERS,
                0.65F,                        // volume
                pitch
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.translatable("itemTooltip.qcontent.scythe").withStyle(ChatFormatting.RED));
    }
}