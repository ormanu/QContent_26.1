package ormanu.qcontent.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import ormanu.qcontent.util.BoxedAccess;

import java.util.function.Consumer;

public class CardboardHideItem extends Item {
    public CardboardHideItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        if (!level.isClientSide()) {
            if (player.getCooldowns().isOnCooldown(this.getDefaultInstance())) return InteractionResult.FAIL;

            BoxedAccess a = (BoxedAccess)(Object) player;

            // ENTER only (do not toggle off here)
            if (!a.qcontent$isBoxed()) {
                a.qcontent$setBoxed(true);
            }
        }

        return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.translatable("itemTooltip.qcontent.cardboard_box").withStyle(ChatFormatting.GRAY));
    }
}