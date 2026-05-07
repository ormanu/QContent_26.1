package ormanu.qcontent.mixin.box;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import ormanu.qcontent.items.ModItems;
import ormanu.qcontent.util.BoxedAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerBoxSlotMixin {
    @Unique private int qcontent$boxSlot = -1;
    @Unique private boolean qcontent$wasBoxed = false;

    @Unique private static final int BOX_COOLDOWN_TICKS = 40;

    @Inject(method = "tick", at = @At("TAIL"))
    private void qcontent$boxSlotLogic(CallbackInfo ci) {
        Player self = (Player)(Object)this;
        if (self.level().isClientSide()) return;

        BoxedAccess a = (BoxedAccess)(Object) self;
        boolean boxed = a.qcontent$isBoxed();

        int selected = self.getInventory().getSelectedSlot();

        // detect transition: not boxed -> boxed
        if (boxed && !qcontent$wasBoxed) {
            qcontent$boxSlot = selected;
            qcontent$wasBoxed = true;
            return; // IMPORTANT: don't unbox in the same tick you entered
        }

        if (!boxed) {
            qcontent$boxSlot = -1;
            qcontent$wasBoxed = false;
            return;
        }

        // boxed and already initialized
        ItemStack stackInOriginalSlot = self.getInventory().getItem(qcontent$boxSlot);
        boolean stillHasBoxThere = stackInOriginalSlot.is(ModItems.CARDBOARD_HIDE);
        boolean slotChanged = selected != qcontent$boxSlot;

        if (slotChanged || !stillHasBoxThere) {
            a.qcontent$setBoxed(false);
            qcontent$boxSlot = -1;
            qcontent$wasBoxed = false;

            self.getCooldowns().addCooldown(ModItems.CARDBOARD_HIDE.getDefaultInstance(), BOX_COOLDOWN_TICKS);
        }
    }
}