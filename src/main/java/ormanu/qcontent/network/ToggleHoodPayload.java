package ormanu.qcontent.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import ormanu.qcontent.QContent;

public record ToggleHoodPayload() implements CustomPacketPayload {

    public static final Type<ToggleHoodPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(QContent.MOD_ID, "toggle_hood"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleHoodPayload> CODEC =
            StreamCodec.unit(new ToggleHoodPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}