package com.tokyoghoul.rpg.network;

import com.tokyoghoul.rpg.TokyoGhoulRPG;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class NetworkHandler {
    public static final SimpleChannel CHANNEL=NetworkRegistry.newSimpleChannel(
        new ResourceLocation(TokyoGhoulRPG.MODID,"main"),
        ()->"2", s->true, s->true
    );
    private static int id=0;

    public static void init(){
        CHANNEL.registerMessage(id++,OriginPacket.class,OriginPacket::encode,OriginPacket::decode,OriginPacket::handle);
        CHANNEL.registerMessage(id++,UpgradePacket.class,UpgradePacket::encode,UpgradePacket::decode,UpgradePacket::handle);
        CHANNEL.registerMessage(id++,KagunePacket.class,KagunePacket::encode,KagunePacket::decode,KagunePacket::handle);
        CHANNEL.registerMessage(id++,OpenOriginPacket.class,OpenOriginPacket::encode,OpenOriginPacket::decode,OpenOriginPacket::handle);
        CHANNEL.registerMessage(id++,RagePacket.class,RagePacket::encode,RagePacket::decode,RagePacket::handle);
        CHANNEL.registerMessage(id++,SyncDataPacket.class,SyncDataPacket::encode,SyncDataPacket::decode,SyncDataPacket::handle);
        CHANNEL.registerMessage(id++,AbilityPacket.class,AbilityPacket::encode,AbilityPacket::decode,AbilityPacket::handle);
        CHANNEL.registerMessage(id++,GrapplePacket.class,GrapplePacket::encode,GrapplePacket::decode,GrapplePacket::handle);
    }
    private NetworkHandler(){}
}
