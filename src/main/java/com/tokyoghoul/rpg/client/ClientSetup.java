package com.tokyoghoul.rpg.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.tokyoghoul.rpg.TokyoGhoulRPG;
import com.tokyoghoul.rpg.capability.GhoulData;
import com.tokyoghoul.rpg.entity.ModEntities;
import com.tokyoghoul.rpg.network.AbilityPacket;
import com.tokyoghoul.rpg.network.KagunePacket;
import com.tokyoghoul.rpg.network.NetworkHandler;
import com.tokyoghoul.rpg.network.RagePacket;
import com.tokyoghoul.rpg.network.GrapplePacket;
import com.tokyoghoul.rpg.screen.ProgressionScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public final class ClientSetup {
    public static KeyMapping KAGUNE, PROGRESSION, RAGE, ABILITY_ONE, ABILITY_TWO, ABILITY_THREE;

    public static void init(){
        var modBus=net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ClientSetup::renderers);
        modBus.addListener(ClientSetup::keys);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(ClientSetup.class);
    }

    @SubscribeEvent
    public static void renderers(EntityRenderersEvent.RegisterRenderers e){
        e.registerEntityRenderer(ModEntities.GHOUL_NPC.get(),
            ctx -> new SimpleHumanoidRenderer<>(ctx, new ResourceLocation("minecraft","textures/entity/player/wide/steve.png")));
        e.registerEntityRenderer(ModEntities.CCG_NPC.get(),
            ctx -> new SimpleHumanoidRenderer<>(ctx, new ResourceLocation("minecraft","textures/entity/player/wide/alex.png")));
    }

    @SubscribeEvent
    public static void keys(RegisterKeyMappingsEvent e){
        KAGUNE=new KeyMapping("key.tokyoghoulrpg.kagune",InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_R,"key.categories.tokyoghoulrpg");
        PROGRESSION=new KeyMapping("key.tokyoghoulrpg.progression",InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_K,"key.categories.tokyoghoulrpg");
        RAGE=new KeyMapping("key.tokyoghoulrpg.rage",InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_U,"key.categories.tokyoghoulrpg");
        ABILITY_ONE=new KeyMapping("key.tokyoghoulrpg.ability_one",InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_Q,"key.categories.tokyoghoulrpg");
        ABILITY_TWO=new KeyMapping("key.tokyoghoulrpg.ability_two",InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_E,"key.categories.tokyoghoulrpg");
        ABILITY_THREE=new KeyMapping("key.tokyoghoulrpg.ability_three",InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F,"key.categories.tokyoghoulrpg");
        e.register(KAGUNE); e.register(PROGRESSION); e.register(RAGE);
        e.register(ABILITY_ONE); e.register(ABILITY_TWO); e.register(ABILITY_THREE);
    }

    @SubscribeEvent
    public static void input(InputEvent.Key e){
        if(e.getAction()!=GLFW.GLFW_PRESS) return;
        if(KAGUNE!=null&&KAGUNE.matches(e.getKey(),e.getScanCode())) NetworkHandler.CHANNEL.sendToServer(new KagunePacket());
        if(PROGRESSION!=null&&PROGRESSION.matches(e.getKey(),e.getScanCode())) Minecraft.getInstance().setScreen(new ProgressionScreen());
        if(RAGE!=null&&RAGE.matches(e.getKey(),e.getScanCode())) NetworkHandler.CHANNEL.sendToServer(new RagePacket());
        if(ABILITY_ONE!=null&&ABILITY_ONE.matches(e.getKey(),e.getScanCode())) NetworkHandler.CHANNEL.sendToServer(new AbilityPacket(0));
        if(ABILITY_TWO!=null&&ABILITY_TWO.matches(e.getKey(),e.getScanCode())) NetworkHandler.CHANNEL.sendToServer(new GrapplePacket());
        if(ABILITY_THREE!=null&&ABILITY_THREE.matches(e.getKey(),e.getScanCode())) NetworkHandler.CHANNEL.sendToServer(new AbilityPacket(2));
    }

    @SubscribeEvent
    public static void overlay(RenderGuiOverlayEvent.Post e){
        if(e.getOverlay()!=VanillaGuiOverlay.HOTBAR.type()) return;
        Minecraft mc=Minecraft.getInstance();
        if(mc.player==null || ClientGhoulData.race()==GhoulData.Race.HUMAN) return;

        GuiGraphics g=e.getGuiGraphics();
        int x=mc.getWindow().getGuiScaledWidth()/2-105;
        int y=mc.getWindow().getGuiScaledHeight()-58;
        int w=210,h=12;

        g.fill(x-1,y-1,x+w+1,y+h+1,0xFF111111);
        g.fill(x,y,x+w,y+h,0xFF333333);
        int fill=(int)(w*(ClientGhoulData.rage()/100.0f));
        if(fill>0) g.fill(x,y,x+fill,y+h,
            ClientGhoulData.race()==GhoulData.Race.CCG ? 0xFF2580C8 : 0xFFE51C3A);

        String race = switch(ClientGhoulData.race()){
            case GHOUL -> "ГУЛЬ";
            case HALF_GHOUL -> "ПОЛУГУЛЬ";
            case CCG -> "CCG";
            default -> "ЧЕЛОВЕК";
        };
        String meter = ClientGhoulData.rageActive()
            ? (ClientGhoulData.race()==GhoulData.Race.CCG ? "БОЕВОЙ ДУХ — " : "ЯРОСТЬ — ")
                +Math.max(0,ClientGhoulData.rageTicks()/20)+"с"
            : (ClientGhoulData.race()==GhoulData.Race.CCG ? "Боевой дух: " : "Ярость: ")+ClientGhoulData.rage()+"%";

        g.drawString(mc.font,race+"  |  Уровень "+ClientGhoulData.level(),x,y-23,0xFFFFFFFF,true);
        g.drawString(mc.font,meter,x,y-11,0xFFFFFFFF,true);
        int hunger=ClientGhoulData.hunger();
        g.drawString(mc.font,"Голод: "+hunger+"%",x,y+15,0xFFE0C0C0,false);
        if(ClientGhoulData.bleedingTicks()>0){
            int by=y-58;
            int bw=(int)(w*Math.max(0,Math.min(1,ClientGhoulData.bleedingTicks()/(30.0f*20.0f))));
            g.fill(x-1,by-1,x+w+1,by+11,0xFF111111);
            g.fill(x,by,x+bw,by+10,0xFFB01828);
            g.drawString(mc.font,"КРОВОТЕЧЕНИЕ — "+(ClientGhoulData.bleedingTicks()/20+1)+"с",x,by-12,0xFFFF6666,true);
        }
        g.drawString(mc.font,"Q: способность  E: кагуне-рывок  F: восстановление",x,y+29,0xFFDDDDDD,false);
    }

    private ClientSetup(){}
}
