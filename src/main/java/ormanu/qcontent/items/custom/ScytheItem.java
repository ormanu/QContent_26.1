package ormanu.qcontent.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import ormanu.qcontent.sound.ModSounds;

import java.util.function.Consumer;

public class ScytheItem extends Item {
    public ScytheItem(Properties properties) {
        super(properties);
    }

    /* =========================
       HOE MINING SPEED
       ========================= */

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // Fast on leaves (like shears)
        if (state.is(BlockTags.LEAVES)) {
            return 15.0F;
        }

        // Fast on hoe-mineable blocks (grass, plants, vines, etc.)
        if (state.is(BlockTags.MINEABLE_WITH_HOE)) {
            return 12.0F;
        }

        return super.getDestroySpeed(stack, state);
    }

    /* =========================
       HOE TILLING (right-click)
       ========================= */

    @Override
    public @NonNull InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        ItemStack stack = ctx.getItemInHand();
        BlockState state = level.getBlockState(pos);

        // Don't till if clicking the bottom face or if there's a block on top
        if (ctx.getClickedFace() == Direction.DOWN || !level.getBlockState(pos.above()).isAir()) {
            return InteractionResult.PASS;
        }

        BlockState resultState = null;
        boolean isCoarseDirt = false;

        // Determine what to convert to
        if (state.is(Blocks.GRASS_BLOCK) ||
                state.is(Blocks.DIRT_PATH) ||
                state.is(Blocks.DIRT) ||
                state.is(Blocks.ROOTED_DIRT)) {
            resultState = Blocks.FARMLAND.defaultBlockState();
        }
        else if (state.is(Blocks.COARSE_DIRT)) {
            resultState = Blocks.DIRT.defaultBlockState();
            isCoarseDirt = true;
        }

        if (resultState == null) {
            return InteractionResult.PASS;
        }

        // Play sound
        level.playSound(player, pos,
                isCoarseDirt ? SoundEvents.GRAVEL_BREAK : SoundEvents.HOE_TILL,
                SoundSource.BLOCKS, 1.0F, 1.0F);

        if (!level.isClientSide()) {
            level.setBlock(pos, resultState, 11);

            // Damage the item
            if (player != null) {
                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
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