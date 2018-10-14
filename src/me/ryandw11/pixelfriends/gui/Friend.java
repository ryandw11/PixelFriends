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
import org.bukkit.inventory.meta.SkullMeta;

import me.ryandw11.pixelfriends.PixelFriends;
import me.ryandw11.pixelfriends.SettingsManager;
import me.ryandw11.pixelfriends.api.ComponentAddon;
//import me.ryandw11.pixelfriends.api.DataFileManager;
import me.ryandw11.pixelfriends.api.event.PixelClickEvent;
import me.ryandw11.pixelfriends.headlib.HeadLib;
import me.ryandw11.pixelfriends.teleport.TeleportComponenet;
import me.ryandw11.pixelfriends.utils.Default;

public class Friend implements Listener {
	private PixelFriends plugin;
	private SettingsManager sm;
	//private DataFileManager dm;
	
	public Friend() {
		this.plugin = PixelFriends.plugin;
		sm = new SettingsManager(plugin);
		//dm = new DataFileManager(plugin);
	}
	
	public void openConfirmGui(Player p, OfflinePlayer input) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "Your Friend: " + input.getName()));
		ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta fillerm = filler.getItemMeta();
		fillerm.setDisplayName(" ");
		List<String> lore = new ArrayList<>();
		lore.add(sm.getNameTwo());
		fillerm.setLore(lore);
		filler.setItemMeta(fillerm);
		
		ItemStack reqSkull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta reqM = (SkullMeta) reqSkull.getItemMeta();
		reqM.setOwningPlayer(input);
		reqM.setDisplayName(ChatColor.GOLD + input.getName());
		lore.clear();
		lore.add(ChatColor.LIGHT_PURPLE + "Your Friend.");
		lore.add(sm.getNameTwo());
		reqM.setLore(lore);
		reqSkull.setItemMeta(reqM);
		
		ComponentAddon addon = null;
		for(ComponentAddon ca : PixelFriends.ca) {
			if(ca.checkConditions(p, input)) {
				addon = ca;
				break;
			}
		}
		if(addon == null && new TeleportComponenet().checkConditions(p, input)) {
			addon = new TeleportComponenet();
		}else if(addon == null) {
			addon = new Default();
		}
		
		ItemStack it = addon.customItem(p, input);
		ItemMeta itM = it.getItemMeta();
		List<String> lores = itM.getLore();
		if(lores == null)
			lores = new ArrayList<String>();
		lores.add(sm.getNameTwo());
		itM.setLore(lores);
		it.setItemMeta(itM);
		
		inv.setItem(0, reqSkull);
		inv.setItem(1, filler);
		inv.setItem(2, filler);
		inv.setItem(3, filler);
		inv.setItem(4, it);
		inv.setItem(5, filler);
		inv.setItem(6, filler);
		inv.setItem(7, filler);
		inv.setItem(8, back());
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClickEvent(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("Your Friend:")) {
			return;
		}
		e.setCancelled(true);
		
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
			return;
		
		ItemStack item = e.getCurrentItem();
		if(!item.getItemMeta().hasLore())
			return;
		
		/*
		 *	The compoenent area. 
		 */
		
		if(!item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).equals(sm.getNameTwo()))
			return;
		Player p = (Player) e.getWhoClicked();
		SkullMeta im = (SkullMeta) e.getInventory().getItem(0).getItemMeta();
		OfflinePlayer op = im.getOwningPlayer();
		if(item.equals(back())) {
			p.closeInventory();
			FriendGui rgui = new FriendGui();
			rgui.openMGUI(p, 1);
		}
		else {
			for(ComponentAddon ca : PixelFriends.ca) {
				
				ItemStack it = ca.customItem(p, op);
				ItemMeta itM = it.getItemMeta();
				List<String> lores = itM.getLore();
				if(lores == null)
					lores = new ArrayList<String>();
				lores.add(sm.getNameTwo());
				itM.setLore(lores);
				it.setItemMeta(itM);
				
				if(item.equals(it)) {
					ca.onClick(new PixelClickEvent(p, op)); //When the player clicks it.
				}
			}
			ComponentAddon tel = new TeleportComponenet();
			ItemStack it = tel.customItem(p, op);
			ItemMeta itM = it.getItemMeta();
			List<String> lores = itM.getLore();
			if(lores == null)
				lores = new ArrayList<String>();
			lores.add(sm.getNameTwo());
			itM.setLore(lores);
			it.setItemMeta(itM);
			if(item.equals(it)) {
				tel.onClick(new PixelClickEvent(p, op));
			}
		}
		
	}
	
	public ItemStack back() {
		return HeadLib.WOODEN_ARROW_LEFT.toItemStack(1, ChatColor.RED + "Back", "Go Back", sm.getNameTwo());
	}
}
