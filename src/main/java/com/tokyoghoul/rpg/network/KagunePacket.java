package com.tokyoghoul.rpg.network;

import com.tokyoghoul.rpg.capability.GhoulData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record KagunePacket(){
    public static void encode(KagunePacket p,FriendlyByteBuf b){}
    public static KagunePacket decode(FriendlyByteBuf b){return new KagunePacket();}
    public static void handle(KagunePacket p,Supplier<NetworkEvent.Context> c){
        c.get().enqueueWork(() -> {
            var s=c.get().getSender();
            if(s==null) return;
            GhoulData.get(s).ifPresent(d -> {
                if(d.getRace()!=GhoulData.Race.GHOUL && d.getRace()!=GhoulData.Race.HALF_GHOUL) return;
                d.setKaguneActive(!d.isKaguneActive());
                d.sync(s);
                s.level().broadcastEntityEvent(s,(byte)60);
                if(d.isKaguneActive()){
                    float radius=3.0f+d.getKagune()*0.3f;
                    for(LivingEntity e:s.level().getEntitiesOfClass(
                        LivingEntity.class,s.getBoundingBox().inflate(radius),x->x!=s&&x.isAlive()
                    )){
                        e.hurt(s.damageSources().playerAttack(s),2.0f+d.getKagune()*1.5f);
                    }
                }
            });
        });
        c.get().setPacketHandled(true);
    }
}
