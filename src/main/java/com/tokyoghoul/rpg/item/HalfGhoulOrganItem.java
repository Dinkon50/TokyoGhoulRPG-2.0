package com.tokyoghoul.rpg.item;
import com.tokyoghoul.rpg.capability.GhoulData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
public class HalfGhoulOrganItem extends Item { public HalfGhoulOrganItem(Properties p){super(p);} public InteractionResultHolder<ItemStack> use(Level l,Player p,InteractionHand h){ItemStack s=p.getItemInHand(h);if(!l.isClientSide){GhoulData.get(p).ifPresent(d->{if(d.getRace()==GhoulData.Race.HUMAN){d.setRace(GhoulData.Race.HALF_GHOUL);d.addRC(40);d.addPoints(10);d.sync(p);s.shrink(1);p.displayClientMessage(Component.literal("§cRC-перестройка завершена. Ты стал полугулем."),false);}});}return InteractionResultHolder.sidedSuccess(s,l.isClientSide);}}
