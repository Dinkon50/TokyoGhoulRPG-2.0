package com.tokyoghoul.rpg.network;

import com.tokyoghoul.rpg.capability.GhoulData;
import com.tokyoghoul.rpg.entity.GhoulNPC;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record AbilityPacket(int ability){
    public static void encode(AbilityPacket p,FriendlyByteBuf b){b.writeInt(p.ability);}
    public static AbilityPacket decode(FriendlyByteBuf b){return new AbilityPacket(b.readInt());}

    public static void handle(AbilityPacket p,Supplier<NetworkEvent.Context> c){
        c.get().enqueueWork(() -> {
            var s=c.get().getSender();
            if(s==null || p.ability<0 || p.ability>2) return;
            GhoulData.get(s).ifPresent(d -> {
                if(d.getRace()==GhoulData.Race.HUMAN){
                    s.displayClientMessage(Component.literal("§7У человека пока нет боевых способностей."), true);
                    return;
                }
                if(!d.useAbility()){
                    s.displayClientMessage(Component.literal("§7Способность перезаряжается."), true);
                    return;
                }

                if(p.ability==0){
                    float damage = d.getRace()==GhoulData.Race.CCG
                        ? 5.0f + d.getQuinque()*1.5f
                        : 5.0f + d.getKagune()*1.2f;
                    double radius = d.getRace()==GhoulData.Race.CCG ? 3.5 : 4.0;
                    for(LivingEntity target : s.level().getEntitiesOfClass(
                        LivingEntity.class, s.getBoundingBox().inflate(radius),
                        x -> x != s && x.isAlive()
                    )){
                        if(d.getRace()==GhoulData.Race.CCG){
                            boolean ghoulTarget = target instanceof GhoulNPC;
                            if(target instanceof net.minecraft.world.entity.player.Player pl){
                                ghoulTarget = GhoulData.get(pl)
                                    .map(td -> td.getRace()==GhoulData.Race.GHOUL)
                                    .orElse(false);
                            }
                            if(ghoulTarget) target.hurt(s.damageSources().playerAttack(s), damage);
                        } else {
                            target.hurt(s.damageSources().playerAttack(s), damage);
                        }
                    }
                    s.level().broadcastEntityEvent(s,(byte)60);
                    s.displayClientMessage(Component.literal(
                        d.getRace()==GhoulData.Race.CCG ? "§bКвинке-удар!" : "§cОсобая атака кагуне!"
                    ), true);
                    d.setAbilityCooldown(25);
                } else if(p.ability==1){
                    s.setDeltaMovement(s.getLookAngle().scale(1.15).add(0,0.25,0));
                    s.hurtMarked=true;
                    s.displayClientMessage(Component.literal("§eРывок!"), true);
                    d.setAbilityCooldown(35);
                } else {
                    float heal = d.getRace()==GhoulData.Race.CCG
                        ? 2.0f + d.getTactics()*0.35f
                        : 3.0f + d.getRegen()*0.5f;
                    s.heal(heal);
                    s.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        net.minecraft.world.effect.MobEffects.DAMAGE_RESISTANCE, 60,
                        d.getRace()==GhoulData.Race.CCG ? 0 : 1, false, false, true
                    ));
                    s.displayClientMessage(Component.literal("§aВосстановление: +"+String.format(java.util.Locale.ROOT,"%.1f",heal)+" HP"), true);
                    d.setAbilityCooldown(100);
                }
                d.sync(s);
            });
        });
        c.get().setPacketHandled(true);
    }
}
