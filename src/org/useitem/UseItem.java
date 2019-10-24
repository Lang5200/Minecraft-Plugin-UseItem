package org.useitem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.useitem.listener.BukkitListener;

public class UseItem extends JavaPlugin{
	public static UseItem main;
	public static boolean isAuthme = false;
	public static Map<String,Use> use = new HashMap<String,Use>();
	@Override
	public void onEnable() {
		this.main = this;
		saveDefaultConfig();
		if(getServer().getPluginManager().getPlugin("AuthMe") != null) {
			isAuthme = true;
			Bukkit.getLogger().info("[UseItem] 检测到Authme，未登录的玩家将不允许使用物品");
		}
		Bukkit.getPluginManager().registerEvents(new BukkitListener(),this);
		Bukkit.getScheduler().runTaskTimer(this,new UseRunnable(),0,20);
		Bukkit.getPluginCommand("useitem").setExecutor(new org.useitem.Command());
		Bukkit.getLogger().info("[UseItem] 插件已加载完成！");
	}
	
	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelAllTasks();
		Bukkit.getLogger().info("[UseItem] 插件已卸载完成！");
	}
	
	public String isItem(ItemStack item) {
		if(item == null || !item.hasItemMeta()) { 
			return null;
		}
		if(!item.getItemMeta().hasDisplayName() && !item.getItemMeta().hasLore()) { 
			return null;
		}
		ConfigurationSection cs = main.getConfig().getConfigurationSection("item");
		for(String i : cs.getKeys(false)) {
			if(!cs.contains(i + ".type") || !cs.contains(i + ".cmd") || !cs.contains(i + ".string")) {
				continue;
			}
			String string = cs.getString(i + ".string");
			if(cs.getString(i + ".type").equalsIgnoreCase("name")) {
				if(!item.getItemMeta().hasDisplayName()) {
					continue;
				}
				if(string.equals(item.getItemMeta().getDisplayName())) {
					return i;
				}
			}else if(cs.getString(i + ".type").equalsIgnoreCase("lore")) {
				if(!item.getItemMeta().hasLore()) {
					continue;
				}
				for(String line : item.getItemMeta().getLore()) {
					if(line.equals(string) || line.contains(string)) {
						return i;
					}
				}
			}else if(cs.getString(i + ".type").equalsIgnoreCase("wlore")) {
				if(!item.getItemMeta().hasLore()) {
					continue;
				}
				for(String line : item.getItemMeta().getLore()) {
					if(line.equals(string)) {
						return i;
					}
				}
			}
		}
		return null;
	}
}
