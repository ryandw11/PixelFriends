package me.ryandw11.pixelfriends.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ryandw11.pixelfriends.PixelFriends;
import me.ryandw11.pixelfriends.SettingsManager;
import me.ryandw11.pixelfriends.api.DataFileManager;
import me.ryandw11.pixelfriends.headlib.HeadLib;

public class SettingsGui implements Listener {
	private PixelFriends plugin;
	private SettingsManager sm;
	private DataFileManager dm;
	
	public SettingsGui() {
		this.plugin = PixelFriends.plugin;
		sm = new SettingsManager(plugin);
		dm = new DataFileManager(plugin);
	}
	
	public void openConfirmGui(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*2, ChatColor.translateAlternateColorCodes('&', "Settings"));
		
		ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta fillerm = filler.getItemMeta();
		fillerm.setDisplayName(" ");
		List<String> lore = new ArrayList<>();
		lore.add(sm.getNameTwo());
		fillerm.setLore(lore);
		filler.setItemMeta(fillerm);
		
		inv.setItem(0, filler);
		inv.setItem(1, filler);
		inv.setItem(2, filler);
		inv.setItem(3, requests());
		inv.setItem(12, button("requests", p));
		inv.setItem(4, filler);
		inv.setItem(5, teleport());
		inv.setItem(14, button("teleport", p));
		inv.setItem(6, filler);
		inv.setItem(7, filler);
		inv.setItem(8, filler);
		inv.setItem(9, filler);
		inv.setItem(10, filler);
		inv.setItem(11, filler);
		inv.setItem(13, filler);
		inv.setItem(15, filler);
		inv.setItem(16, filler);
		inv.setItem(17, back());
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClickEvent(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("Settings")) {
			return;
		}
		e.setCancelled(true);
		
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
			return;
		
		ItemStack item = e.getCurrentItem();
		
		if(!e.getInventory().contains(item))
			return;
		
		Player p = (Player) e.getWhoClicked();
		if(e.getRawSlot() == 14) {
			sm.setSetting(p, "teleport", !sm.getBoolSetting(p, "teleport"));
			e.getInventory().setItem(14, button("teleport", p));
		}else if(e.getRawSlot() == 12) {
			sm.setSetting(p, "requests", !sm.getBoolSetting(p, "requests"));
			e.getInventory().setItem(12, button("requests", p));
		}else if(item.equals(back())) {
			p.closeInventory();
			FriendGui fgui = new FriendGui();
			fgui.openMGUI(p, 1);
		}
		
	}
	
	public ItemStack teleport() {
		ItemStack tel = new ItemStack(Material.COMPASS);
		ItemMeta telM = tel.getItemMeta();
		telM.setDisplayName(ChatColor.GOLD + "Teleportation");
		List<String> lores = new ArrayList<String>();
		lores.add("If others could teleport to you.");
		telM.setLore(lores);
		tel.setItemMeta(telM);
		return tel;
	}
	
	public ItemStack requests() {
		ItemStack tel = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta telM = tel.getItemMeta();
		telM.setDisplayName(ChatColor.GREEN + "Requests");
		List<String> lores = new ArrayList<String>();
		lores.add("If others could request to be your friend.");
		telM.setLore(lores);
		tel.setItemMeta(telM);
		return tel;
	}
	
	public ItemStack back() {
		return HeadLib.WOODEN_ARROW_LEFT.toItemStack(1, ChatColor.RED + "Back", "Go Back", sm.getNameTwo());
	}
	
	public ItemStack button(String path, Player p) {
		ItemStack tel;
		if(sm.getBoolSetting(p, path))
			tel = new ItemStack(Material.LIME_DYE);
		else
			tel = new ItemStack(Material.GRAY_DYE);
		ItemMeta telM = tel.getItemMeta();
		if(sm.getBoolSetting(p, path))
			telM.setDisplayName(ChatColor.GREEN + "Enabled");
		else
			telM.setDisplayName(ChatColor.RED + "Disabled");
		List<String> lores = new ArrayList<String>();
		lores.add("Click to Enable/Disable!");
		telM.setLore(lores);
		tel.setItemMeta(telM);
		return tel;
	}
}
