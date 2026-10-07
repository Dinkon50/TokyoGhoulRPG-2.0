package com.tokyoghoul.rpg.network;

import com.tokyoghoul.rpg.capability.GhoulData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record GrapplePacket(){
    public static void encode(GrapplePacket p,FriendlyByteBuf b){}
    public static GrapplePacket decode(FriendlyByteBuf b){return new GrapplePacket();}
    public static void handle(GrapplePacket p,Supplier<NetworkEvent.Context> c){
        c.get().enqueueWork(() -> {
            Player s=c.get().getSender(); if(s==null) return;
            GhoulData.get(s).ifPresent(d -> {
                if((d.getRace()!=GhoulData.Race.GHOUL && d.getRace()!=GhoulData.Race.HALF_GHOUL) || !d.isKaguneActive()) return;
                HitResult hit=s.pick(18.0D,0.0F,false);
                if(hit.getType()!=HitResult.Type.BLOCK) return;
                Vec3 target=((BlockHitResult)hit).getLocation().add(0,0.4,0);
                Vec3 delta=target.subtract(s.position());
                double len=delta.length();
                if(len<2.0 || len>18.0) return;
                Vec3 velocity=delta.normalize().scale(Math.min(2.6,0.55+len*0.11));
                s.setDeltaMovement(velocity);
                s.hurtMarked=true;
                d.setAbilityCooldown(12);
                d.sync(s);
            });
        });
        c.get().setPacketHandled(true);
    }
}
