package com.tokyoghoul.rpg.network;

import com.tokyoghoul.rpg.capability.GhoulData;
import com.tokyoghoul.rpg.client.ClientGhoulData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record SyncDataPacket(
    int race, int points, int level, int rc,
    int strength, int speed, int kagune, int regen, int stealth,
    int investigation, int quinque, int tactics, int armor, int discipline,
    int rage, int rageTicks, boolean rageUnlocked, boolean kaguneActive,
    int kaguneType, int hunger, int bleedingTicks
){
    public SyncDataPacket(GhoulData d){
        this(d.getRace().ordinal(), d.getPoints(), d.getLevel(), d.getRC(),
            d.getStrength(), d.getSpeed(), d.getKagune(), d.getRegen(), d.getStealth(),
            d.getInvestigation(), d.getQuinque(), d.getTactics(), d.getArmor(), d.getDiscipline(),
            d.getRage(), d.getRageTicks(), d.isRageUnlocked(), d.isKaguneActive(),
            d.getKaguneType().ordinal(), d.getHunger(), d.getBleedingTicks());
    }
    public static void encode(SyncDataPacket p, FriendlyByteBuf b){
        b.writeInt(p.race); b.writeInt(p.points); b.writeInt(p.level); b.writeInt(p.rc);
        b.writeInt(p.strength); b.writeInt(p.speed); b.writeInt(p.kagune); b.writeInt(p.regen); b.writeInt(p.stealth);
        b.writeInt(p.investigation); b.writeInt(p.quinque); b.writeInt(p.tactics); b.writeInt(p.armor); b.writeInt(p.discipline);
        b.writeInt(p.rage); b.writeInt(p.rageTicks); b.writeBoolean(p.rageUnlocked); b.writeBoolean(p.kaguneActive);
        b.writeInt(p.kaguneType); b.writeInt(p.hunger); b.writeInt(p.bleedingTicks);
    }
    public static SyncDataPacket decode(FriendlyByteBuf b){
        return new SyncDataPacket(
            b.readInt(),b.readInt(),b.readInt(),b.readInt(),
            b.readInt(),b.readInt(),b.readInt(),b.readInt(),b.readInt(),
            b.readInt(),b.readInt(),b.readInt(),b.readInt(),b.readInt(),
            b.readInt(),b.readInt(),b.readBoolean(),b.readBoolean(),
            b.readInt(),b.readInt(),b.readInt()
        );
    }
    public static void handle(SyncDataPacket p, Supplier<NetworkEvent.Context> c){
        c.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientGhoulData.set(p)));
        c.get().setPacketHandled(true);
    }
}
