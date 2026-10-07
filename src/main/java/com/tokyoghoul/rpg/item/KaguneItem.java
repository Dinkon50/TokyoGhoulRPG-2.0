package com.tokyoghoul.rpg.item;
import com.tokyoghoul.rpg.capability.GhoulData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
public class KaguneItem extends Item { public KaguneItem(Properties p){super(p);} @Override public InteractionResultHolder<ItemStack> use(Level l,Player p,InteractionHand h){ if(!l.isClientSide){GhoulData.get(p).ifPresent(d->{if(d.getRace()==GhoulData.Race.GHOUL||d.getRace()==GhoulData.Race.HALF_GHOUL){d.setKaguneActive(!d.isKaguneActive());d.sync(p);p.displayClientMessage(Component.literal("§cКагуне: "+(d.isKaguneActive()?"активировано":"скрыто")),true);}});} return InteractionResultHolder.sidedSuccess(p.getItemInHand(h),l.isClientSide);} }
