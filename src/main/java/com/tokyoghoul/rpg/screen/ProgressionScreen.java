package com.tokyoghoul.rpg.screen;

import com.tokyoghoul.rpg.capability.GhoulData;
import com.tokyoghoul.rpg.client.ClientGhoulData;
import com.tokyoghoul.rpg.network.NetworkHandler;
import com.tokyoghoul.rpg.network.UpgradePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ProgressionScreen extends Screen {
    public ProgressionScreen(){super(Component.literal("Древо развития"));}

    @Override protected void init(){
        int x=width/2-150;
        if(ClientGhoulData.race()==GhoulData.Race.CCG){
            String[] stats={"investigation","quinque","tactics","armor","discipline","rage"};
            String[] names={"Расследование","Квинке","Тактика","Защита","Дисциплина","Боевой дух — УЛЬТА (4 очка)"};
            for(int i=0;i<stats.length;i++){
                final String stat=stats[i];
                addRenderableWidget(Button.builder(Component.literal("+  "+names[i]),
                    b->NetworkHandler.CHANNEL.sendToServer(new UpgradePacket(stat)))
                    .bounds(x,70+i*38,300,30).build());
            }
        } else {
            String[] stats={"strength","speed","kagune","regen","stealth","rage"};
            String[] names={"Сила","Скорость","Кагуне","Регенерация","Скрытность","Ярость — УЛЬТА (5 очков)"};
            for(int i=0;i<stats.length;i++){
                final String stat=stats[i];
                addRenderableWidget(Button.builder(Component.literal("+  "+names[i]),
                    b->NetworkHandler.CHANNEL.sendToServer(new UpgradePacket(stat)))
                    .bounds(x,70+i*38,300,30).build());
            }
        }
    }

    @Override public void render(GuiGraphics g,int x,int y,float pt){
        renderBackground(g);
        g.drawCenteredString(font,title,width/2,25,0xFFDDDD);
        g.drawCenteredString(font,Component.literal(
            "Путь: "+switch(ClientGhoulData.race()){
                case GHOUL -> "Гуль";
                case HALF_GHOUL -> "Полугуль";
                case CCG -> "CCG";
                default -> "Человек";
            }),width/2,40,0xFFFFFF);
        g.drawCenteredString(font,Component.literal(
            "Уровень: "+ClientGhoulData.level()+"  |  Очки навыков: "+ClientGhoulData.points()
        ),width/2,54,0xFFFF55);
        String spirit=ClientGhoulData.race()==GhoulData.Race.CCG ? "Боевой дух" : "Ярость";
        g.drawCenteredString(font,Component.literal(
            spirit+": "+ClientGhoulData.rage()+"%  |  Ульта: "+(ClientGhoulData.rageUnlocked()?"ОТКРЫТА":"ЗАКРЫТА")
        ),width/2,304,ClientGhoulData.rageUnlocked()?0xFFFF5555:0xFFAAAAAA);
        g.drawCenteredString(font,Component.literal(
            "U — активировать на 100%  |  Q/E/F — боевые способности"
        ),width/2,320,0xFFBBBBBB);
        super.render(g,x,y,pt);
    }
}
