package ormanu.qcontent.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import ormanu.qcontent.datagen.QItemTagProvider;
import ormanu.qcontent.items.ModItems;

@Mixin(Player.class)
public abstract class PlayerReachMixin {

    @ModifyReturnValue(method = "entityInteractionRange", at = @At("RETURN"))
    private double qcontent$extendedEntityReach(double original) {
        Player self = (Player)(Object)this;
        ItemStack main = self.getMainHandItem();

        if (!main.is(QItemTagProvider.EXTENDED_REACH)) return original;

        double bonus;
        if (main.is(ModItems.Scythe)) {
            bonus = 1.0;
        } else if (main.is(ModItems.RefinedSword)) {
            bonus = 0.75;
        } else {
            bonus = 0.5; // fallback for anything else in the tag
        }

        return original + bonus;
    }
}