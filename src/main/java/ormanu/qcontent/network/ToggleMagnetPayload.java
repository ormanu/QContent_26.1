package ormanu.qcontent.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import ormanu.qcontent.QContent;

public record ToggleMagnetPayload() implements CustomPacketPayload {

    public static final Type<ToggleMagnetPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(QContent.MOD_ID, "toggle_magnet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleMagnetPayload> CODEC =
            StreamCodec.unit(new ToggleMagnetPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}