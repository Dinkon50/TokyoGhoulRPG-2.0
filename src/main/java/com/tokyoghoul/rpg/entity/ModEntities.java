package com.tokyoghoul.rpg.entity;

import com.tokyoghoul.rpg.TokyoGhoulRPG;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TokyoGhoulRPG.MODID);

    public static final RegistryObject<EntityType<GhoulNPC>> GHOUL_NPC =
        ENTITIES.register("ghoul_npc", () ->
            EntityType.Builder.of(GhoulNPC::new, MobCategory.MONSTER)
                .sized(0.6F, 1.95F).clientTrackingRange(8).build("ghoul_npc"));

    public static final RegistryObject<EntityType<CCGNPC>> CCG_NPC =
        ENTITIES.register("ccg_npc", () ->
            EntityType.Builder.of(CCGNPC::new, MobCategory.MONSTER)
                .sized(0.6F, 1.95F).clientTrackingRange(8).build("ccg_npc"));

    private ModEntities(){}
}
