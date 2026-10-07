package com.tokyoghoul.rpg.network;

import com.tokyoghoul.rpg.capability.GhoulData;
import com.tokyoghoul.rpg.v2.KaguneType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record OriginPacket(int race, int kaguneType){
    public OriginPacket(int race){ this(race, 0); }
    public static void encode(OriginPacket p,FriendlyByteBuf b){b.writeInt(p.race);b.writeInt(p.kaguneType);}
    public static OriginPacket decode(FriendlyByteBuf b){return new OriginPacket(b.readInt(),b.readInt());}
    public static void handle(OriginPacket p,Supplier<NetworkEvent.Context> c){
        c.get().enqueueWork(() -> {
            var s=c.get().getSender();
            if(s==null) return;
            GhoulData.get(s).ifPresent(d -> {
                GhoulData.Race r = switch(p.race){
                    case 1 -> GhoulData.Race.GHOUL;
                    case 2 -> GhoulData.Race.HALF_GHOUL;
                    case 3 -> GhoulData.Race.CCG;
                    default -> GhoulData.Race.HUMAN;
                };
                KaguneType[] types=KaguneType.values();
                KaguneType type=p.kaguneType>=0&&p.kaguneType<types.length?types[p.kaguneType]:KaguneType.NONE;
                if(r==GhoulData.Race.GHOUL && (type==KaguneType.NONE||type==KaguneType.KAGERO)) type=KaguneType.RINKAKU;
                if(r==GhoulData.Race.HALF_GHOUL && (type==KaguneType.NONE||type==KaguneType.UKAKU||type==KaguneType.KOUKAKU||type==KaguneType.BIKAKU)) type=KaguneType.RINKAKU;
                d.choose(r,type); d.sync(s);
                s.displayClientMessage(net.minecraft.network.chat.Component.literal(
                    switch(r){
                        case GHOUL -> "§cПуть гуля выбран: "+type.displayName();
                        case HALF_GHOUL -> "§dПуть полугулю выбран: "+type.displayName();
                        case CCG -> "§bТы вступил в CCG. Получено +5 стартовых очков.";
                        default -> "§fТы остался человеком.";
                    }), false);
            });
        });
        c.get().setPacketHandled(true);
    }
}
