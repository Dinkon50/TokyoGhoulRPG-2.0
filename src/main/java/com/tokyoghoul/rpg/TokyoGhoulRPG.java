package com.tokyoghoul.rpg;

import com.tokyoghoul.rpg.client.ClientSetup;
import com.tokyoghoul.rpg.entity.CCGNPC;
import com.tokyoghoul.rpg.entity.GhoulNPC;
import com.tokyoghoul.rpg.entity.ModEntities;
import com.tokyoghoul.rpg.init.ModItems;
import com.tokyoghoul.rpg.network.NetworkHandler;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TokyoGhoulRPG.MODID)
public class TokyoGhoulRPG {
    public static final String MODID = "tokyoghoulrpg";

    public TokyoGhoulRPG() {
        var bus=FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITIES.register(bus);
        ModItems.ITEMS.register(bus);
        NetworkHandler.init();
        bus.addListener(TokyoGhoulRPG::registerAttributes);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSetup::init);
    }

    private static void registerAttributes(EntityAttributeCreationEvent e){
        e.put(ModEntities.GHOUL_NPC.get(), GhoulNPC.createAttributes().build());
        e.put(ModEntities.CCG_NPC.get(), CCGNPC.createAttributes().build());
    }
}
