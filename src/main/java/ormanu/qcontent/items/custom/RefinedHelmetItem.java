package ormanu.qcontent.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class RefinedHelmetItem extends Item {

    public RefinedHelmetItem(Properties properties) {
        super(properties);
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(
                Component.literal("Toggle Hood ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(Component.literal("[")
                                .withStyle(ChatFormatting.DARK_GRAY))
                        .append(Component.keybind("key.qcontent.toggle_hood")
                                .withStyle(ChatFormatting.LIGHT_PURPLE))
                        .append(Component.literal("]")
                                .withStyle(ChatFormatting.DARK_GRAY))
        );
    }

}