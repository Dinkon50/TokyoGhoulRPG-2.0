package com.tokyoghoul.rpg.item;
import com.tokyoghoul.rpg.capability.GhoulData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
public class RCInjectorItem extends Item { public RCInjectorItem(Properties p){super(p);} public InteractionResultHolder<ItemStack> use(Level l,Player p,InteractionHand h){ItemStack s=p.getItemInHand(h);if(!l.isClientSide)GhoulData.get(p).ifPresent(d->{d.addRC(20);d.addPoints(2);d.sync(p);p.displayClientMessage(Component.literal("§c+20 RC-клеток, +2 очка навыка"),true);s.shrink(1);});return InteractionResultHolder.sidedSuccess(s,l.isClientSide);}}
