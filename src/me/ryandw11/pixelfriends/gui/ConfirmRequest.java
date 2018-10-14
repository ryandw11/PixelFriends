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
import me.ryandw11.pixelfriends.api.DataFileManager;
import me.ryandw11.pixelfriends.headlib.HeadLib;

public class ConfirmRequest implements Listener {
	private PixelFriends plugin;
	private SettingsManager sm;
	private DataFileManager dm;
	
	public ConfirmRequest() {
		this.plugin = PixelFriends.plugin;
		sm = new SettingsManager(plugin);
		dm = new DataFileManager(plugin);
	}
	
	public void openConfirmGui(Player p, OfflinePlayer input) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "Send request?"));
		inv.setItem(3, confirm());
		inv.setItem(5, deny());
		
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
		lore.add(ChatColor.LIGHT_PURPLE + "Send request to this player?");
		lore.add(sm.getNameTwo());
		reqM.setLore(lore);
		reqSkull.setItemMeta(reqM);
		
		inv.setItem(0, filler);
		inv.setItem(1, filler);
		inv.setItem(2, filler);
		inv.setItem(4, reqSkull);
		inv.setItem(6, filler);
		inv.setItem(7, filler);
		inv.setItem(8, filler);
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClickEvent(InventoryClickEvent e) {
		if(!e.getInventory().getName().contains("Send request?")) {
			return;
		}
		e.setCancelled(true);
		
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
			return;
		
		ItemStack item = e.getCurrentItem();
		if(!item.getItemMeta().hasLore())
			return;
		
		
		
		if(!item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).equals(sm.getNameTwo()))
			return;
		Player p = (Player) e.getWhoClicked();
		OfflinePlayer op = ((SkullMeta) e.getInventory().getItem(4).getItemMeta()).getOwningPlayer();
		if(item.equals(confirm())) {
			dm.sendRequest(p, op.getUniqueId());
        	e.getWhoClicked().sendMessage(ChatColor.GREEN + "Successfully sent a request to " + op.getName() + "!");
        	if(op.isOnline())
        		Bukkit.getPlayer(op.getUniqueId()).sendMessage(ChatColor.GREEN + p.getName() + " has asked to be your friend!");
        	p.closeInventory();
		}else if(item.equals(deny())) {
			p.sendMessage(ChatColor.RED + "Cancelled request.");
			p.closeInventory();
		}
		
	}
	
	public ItemStack confirm() {
		return HeadLib.CHECKMARK.toItemStack(1, ChatColor.GREEN + "YES", "Yes, I would like to send", "a friend request to", "this person.", sm.getNameTwo());
	}
	
	public ItemStack deny() {
		return HeadLib.RED_X.toItemStack(1, ChatColor.RED + "NO", "No, I would not like to", " send a friend request", "to this person.", sm.getNameTwo());
	}
}
