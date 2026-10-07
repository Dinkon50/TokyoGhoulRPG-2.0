package com.tokyoghoul.rpg.event;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.tokyoghoul.rpg.capability.GhoulData;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class V2Commands {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent e){
        e.getDispatcher().register(Commands.literal("tg")
            .then(Commands.literal("skillpoints")
                .requires(s -> s.hasPermission(2))
                .then(Commands.argument("amount", IntegerArgumentType.integer(1,45))
                    .executes(ctx -> {
                        var player=ctx.getSource().getPlayerOrException();
                        int amount=IntegerArgumentType.getInteger(ctx,"amount");
                        GhoulData.get(player).ifPresent(d -> d.addPoints(amount));
                        GhoulData.get(player).ifPresent(d -> d.sync(player));
                        ctx.getSource().sendSuccess(() -> Component.literal("§aTokyoGhoulRPG: добавлено очков: "+amount), true);
                        return 1;
                    })))
            .then(Commands.literal("level")
                .executes(ctx -> {
                    var player=ctx.getSource().getPlayerOrException();
                    int level=GhoulData.get(player).map(GhoulData::getLevel).orElse(1);
                    ctx.getSource().sendSuccess(() -> Component.literal("§eУровень TokyoGhoulRPG: "+level+"/30"), false);
                    return level;
                }))
            .then(Commands.literal("quests")
                .executes(ctx -> { ctx.getSource().sendSuccess(() -> Component.literal("§6Система заданий 2.0 готовится к подключению UI."), false); return 1; }))
        );
    }
    private V2Commands(){}
}
