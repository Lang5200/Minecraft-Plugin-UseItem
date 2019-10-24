package org.useitem.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.useitem.Use;
import org.useitem.UseItem;
import fr.xephi.authme.api.v3.AuthMeApi;


public class BukkitListener implements Listener{
	private UseItem main = UseItem.main;
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(UseItem.isAuthme) {
			if(!AuthMeApi.getInstance().isAuthenticated(p)) {
				return;
			}
		}
		if(e.getHand() != EquipmentSlot.HAND) {
			return;
		}
		ItemStack item = e.getItem();
		if(main.isItem(item) != null) {
			String itemid = main.isItem(item);
			if(main.getConfig().contains("item." + itemid + ".isCancelEvents") && main.getConfig().getBoolean("item." + itemid + ".isCancelEvents")) {
				e.setCancelled(true);
			}
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				use(p, itemid);
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			ItemStack item = p.getItemInHand();
			if(main.isItem(item) != null) {
				String itemid = main.isItem(item);
				if(main.getConfig().contains("item." + itemid + ".isCancelEvents") && main.getConfig().getBoolean("item." + itemid + ".isCancelEvents")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		clear(p);
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(!(p.getLocation().getX() == e.getTo().getX() 
		   && p.getLocation().getY() == e.getTo().getY()
		   && p.getLocation().getZ() == e.getTo().getZ())
		) {
			clear(p);
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		clear(p);
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		clear(p);
	}
	
	@EventHandler
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		clear(p);
	}
	
	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer();
		clear(p);
	}
	
	public void clear(Player p) {
		if(UseItem.use.containsKey(p.getName())) {
			UseItem.use.remove(p.getName());
			p.sendActionBar("§r");
		}
	}
	public void use(Player p,String itemid) {
		if(!main.getConfig().contains("item." + itemid) || p == null) {
			return;
		}
		if(UseItem.use.containsKey(p.getName()) && UseItem.use.get(p.getName()).itemid.equals(itemid)) {
			return;
		}
		if(main.getConfig().contains("item." + itemid + ".time") && main.getConfig().getInt("item." + itemid + ".time") > 0) { // 需要消耗时间
			UseItem.use.put(p.getName(), new Use(itemid,main.getConfig().getInt("item." + itemid + ".time")));
			p.sendActionBar("正在使用中..." + main.getConfig().getInt("item." + itemid + ".time") + "s");
		}else { //无需消耗时间
			if(main.getConfig().contains("item." + itemid + ".isConsume") && main.getConfig().getBoolean("item." + itemid + ".isConsume")) {
				p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
			}
			List<String> cmd = main.getConfig().getStringList("item." + itemid + ".cmd");
			for(String c : cmd) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.replace("%player%", p.getName()));
			}
		}
		if(main.getConfig().contains("item." + itemid + ".useSound") && Sound.valueOf(main.getConfig().getString("item." + itemid + ".useSound")) != null) {
			Sound sound = Sound.valueOf(main.getConfig().getString("item." + itemid + ".useSound"));
			p.playSound(p.getLocation(), sound, 1, 1);
		}
	}
}
