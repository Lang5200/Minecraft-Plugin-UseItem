package org.useitem;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class UseRunnable implements Runnable {
	private UseItem main = UseItem.main;

	@Override
	public void run() {
		for(String i : UseItem.use.keySet()) {
			if(Bukkit.getPlayerExact(i) == null) {
				continue;
			}
			Player p = Bukkit.getPlayerExact(i);
			Use use = UseItem.use.get(i);
			if(use.time-1 > 0) {
				use.time-=1;
				p.sendActionBar("正在使用中..." + use.time + "s");
			}else {
				if(main.getConfig().contains("item." + use.itemid + ".endSound") && Sound.valueOf(main.getConfig().getString("item." + use.itemid + ".endSound")) != null) {
					Sound sound = Sound.valueOf(main.getConfig().getString("item." + use.itemid + ".endSound"));
					p.playSound(p.getLocation(), sound, 1, 1);
				}
				if(main.getConfig().contains("item." + use.itemid + ".isConsume") && main.getConfig().getBoolean("item." + use.itemid + ".isConsume")) {
					p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
				}
				List<String> cmd = main.getConfig().getStringList("item." + use.itemid + ".cmd");
				for(String c : cmd) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.replace("%player%", p.getName()));
				}
				UseItem.use.remove(i);
				p.sendActionBar("§r");
			}
		}
	}
}
