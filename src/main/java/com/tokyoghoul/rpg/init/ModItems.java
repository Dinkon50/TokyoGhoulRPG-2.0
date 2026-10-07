package com.tokyoghoul.rpg.init;

import com.tokyoghoul.rpg.TokyoGhoulRPG;
import com.tokyoghoul.rpg.entity.ModEntities;
import com.tokyoghoul.rpg.item.GhoulCoatItem;
import com.tokyoghoul.rpg.item.HalfGhoulOrganItem;
import com.tokyoghoul.rpg.item.KaguneItem;
import com.tokyoghoul.rpg.item.RCInjectorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, TokyoGhoulRPG.MODID);

    public static final RegistryObject<Item> RC_CELL =
        ITEMS.register("rc_cell",()->new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> RC_INJECTOR =
        ITEMS.register("rc_injector",()->new RCInjectorItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HALF_GHOUL_ORGAN =
        ITEMS.register("half_ghoul_organ",()->new HalfGhoulOrganItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SKILL_TOKEN =
        ITEMS.register("skill_token",()->new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> GHOUL_MASK =
        ITEMS.register("ghoul_mask",()->new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> QUINQUE_BLADE =
        ITEMS.register("quinque_blade",()->new SwordItem(Tiers.DIAMOND,4,-2.1f,new Item.Properties()));
    public static final RegistryObject<Item> KAGUNE =
        ITEMS.register("kagune",()->new KaguneItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GHOUL_COAT =
        ITEMS.register("ghoul_coat",()->new GhoulCoatItem(ArmorMaterials.NETHERITE,ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> INVESTIGATOR_COAT =
        ITEMS.register("investigator_coat",()->new GhoulCoatItem(ArmorMaterials.IRON,ArmorItem.Type.CHESTPLATE,new Item.Properties()));

    public static final RegistryObject<Item> GHOUL_SPAWN_EGG =
        ITEMS.register("ghoul_spawn_egg",()->new ForgeSpawnEggItem(ModEntities.GHOUL_NPC,0x5B0A12,0xD7193F,new Item.Properties()));
    public static final RegistryObject<Item> CCG_SPAWN_EGG =
        ITEMS.register("ccg_spawn_egg",()->new ForgeSpawnEggItem(ModEntities.CCG_NPC,0x173B5E,0x55B7E8,new Item.Properties()));

    private ModItems(){}
}
