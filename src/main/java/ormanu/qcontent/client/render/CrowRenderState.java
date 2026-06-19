package ormanu.qcontent.client.render;

import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CrowRenderState extends LivingEntityRenderState implements GeoRenderState {
    private final Map<DataTicket<?>, Object> dataMap = new HashMap<>();

    @Override
    public Map<DataTicket<?>, Object> getDataMap() {
        return dataMap;
    }

    public boolean hasBeakItem = false;
    public ItemStack beakItem = ItemStack.EMPTY;

    public final ItemStackRenderState beakItemState = new ItemStackRenderState();
}