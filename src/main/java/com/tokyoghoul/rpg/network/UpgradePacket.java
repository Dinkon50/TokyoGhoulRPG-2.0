package com.tokyoghoul.rpg.network;

import com.tokyoghoul.rpg.capability.GhoulData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record UpgradePacket(String stat){
    public static void encode(UpgradePacket p,FriendlyByteBuf b){b.writeUtf(p.stat);}
    public static UpgradePacket decode(FriendlyByteBuf b){return new UpgradePacket(b.readUtf(32));}
    public static void handle(UpgradePacket p,Supplier<NetworkEvent.Context> c){
        c.get().enqueueWork(() -> {
            var s=c.get().getSender();
            if(s!=null) GhoulData.get(s).ifPresent(d -> {
                if(d.upgrade(p.stat)) d.sync(s);
            });
        });
        c.get().setPacketHandled(true);
    }
}
