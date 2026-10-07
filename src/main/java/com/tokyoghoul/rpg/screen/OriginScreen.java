package com.tokyoghoul.rpg.screen;

import com.tokyoghoul.rpg.network.NetworkHandler;
import com.tokyoghoul.rpg.network.OriginPacket;
import com.tokyoghoul.rpg.v2.KaguneType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OriginScreen extends Screen {
    private int stage=0;
    private int selectedRace=0;
    private KaguneType selectedKagune=KaguneType.NONE;
    public OriginScreen(){ super(Component.literal("Tokyo Ghoul RPG — Создание персонажа")); }

    @Override protected void init(){ rebuild(); }
    private void rebuild(){
        clearWidgets();
        int cx=width/2;
        if(stage==0){
            addRenderableWidget(card(cx-210,85,190,42,"§cГУЛЬ","Kagune и RC",1));
            addRenderableWidget(card(cx+20,85,190,42,"§dПОЛУГУЛЬ","2 типа Kagune",2));
            addRenderableWidget(card(cx-210,145,190,42,"§fЧЕЛОВЕК","Без выбора Kagune",0));
            addRenderableWidget(card(cx+20,145,190,42,"§bCCG","Quinque + 5 очков",3));
        } else {
            KaguneType[] types=selectedRace==2 ? new KaguneType[]{KaguneType.RINKAKU,KaguneType.KAGERO} : new KaguneType[]{KaguneType.RINKAKU,KaguneType.UKAKU,KaguneType.KOUKAKU,KaguneType.BIKAKU};
            int y=80;
            for(KaguneType type:types){
                addRenderableWidget(Button.builder(Component.literal(type.displayName()),b->{selectedKagune=type; finish();})
                    .bounds(cx-150,y,300,34).build()); y+=44;
            }
            addRenderableWidget(Button.builder(Component.literal("← Назад"),b->{stage=0;rebuild();}).bounds(cx-150,y+8,300,28).build());
        }
    }
    private Button card(int x,int y,int w,int h,String title,String sub,int race){
        return Button.builder(Component.literal(title+"  §7"+sub),b->{
            if(race==1||race==2){selectedRace=race;stage=1;rebuild();} else {selectedRace=race;selectedKagune=KaguneType.NONE;finish();}
        }).bounds(x,y,w,h).build();
    }
    private void finish(){ NetworkHandler.CHANNEL.sendToServer(new OriginPacket(selectedRace,selectedKagune.ordinal())); onClose(); }
    @Override public void render(GuiGraphics g,int x,int y,float pt){
        renderBackground(g);
        g.fill(0,0,width,height,0xA80A0A0E);
        g.fill(width/2-245,30,width/2+245,height-25,0xE0121218);
        g.drawCenteredString(font,title,width/2,45,0xFFFFFFFF);
        if(stage==0){
            g.drawCenteredString(font,Component.literal("ВЫБЕРИ СВОЮ СУДЬБУ"),width/2,62,0xFFE0B0B8);
            g.drawCenteredString(font,Component.literal("Этот выбор определяет развитие персонажа."),width/2,215,0xFFAAAAAA);
        } else {
            String name=selectedRace==2?"ПОЛУГУЛЬ":"ГУЛЬ";
            g.drawCenteredString(font,Component.literal("ВЫБОР KAGUNE — "+name),width/2,62,0xFFFFFFFF);
            g.drawCenteredString(font,Component.literal("Выбери боевой стиль. Это нельзя будет сменить бесплатно."),width/2,75,0xFFAAAAAA);
        }
        super.render(g,x,y,pt);
    }
}
