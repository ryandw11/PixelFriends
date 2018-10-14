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

public class ConfirmFriendship implements Listener {
	private PixelFriends plugin;
	private SettingsManager sm;
	private DataFileManager dm;
	
	public ConfirmFriendship() {
		this.plugin = PixelFriends.plugin;
		sm = new SettingsManager(plugin);
		dm = new DataFileManager(plugin);
	}
	
	public void openConfirmGui(Player p, OfflinePlayer input) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "Accept friend request?"));
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
		lore.add(ChatColor.LIGHT_PURPLE + "Accept this player's request?");
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
		if(!e.getInventory().getName().contains("Accept friend request?")) {
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
			
			if(sm.getMaxFriend() != -1)
				if(dm.numberOfFriends(p.getUniqueId()) >= sm.getMaxFriend()) {
					p.sendMessage(ChatColor.RED + "You have too many friends!");
					p.closeInventory();
					return;
				}
			
			if(sm.getMaxFriend() != -1)
				if(dm.numberOfFriends(op.getUniqueId()) >= sm.getMaxFriend()) {
					p.sendMessage(ChatColor.RED + "They have too many friends!");
					p.closeInventory();
					return;
				}
			
			dm.removeRequest(op.getUniqueId(), p.getUniqueId());
			dm.addFriend(op.getUniqueId(), p.getUniqueId());
			
        	e.getWhoClicked().sendMessage(ChatColor.GREEN + "Successfully added " + op.getName() + " as a friend!");
        	if(op.isOnline())
        		Bukkit.getPlayer(op.getUniqueId()).sendMessage(ChatColor.GREEN + p.getName() + " has accepted your friend request!");
        	p.closeInventory();
        	RequestGui rgui = new RequestGui();
        	rgui.openMGUI(p, 1);
		}else if(item.equals(deny())) {
			p.sendMessage(ChatColor.RED + "Denied the request.");
			dm.removeRequest(op.getUniqueId(), p.getUniqueId());
			p.closeInventory();
			RequestGui rgui = new RequestGui();
			rgui.openMGUI(p, 1);
		}
		
	}
	
	public ItemStack confirm() {
		return HeadLib.CHECKMARK.toItemStack(1, ChatColor.GREEN + "Accept", "Accept the Request.", sm.getNameTwo());
	}
	
	public ItemStack deny() {
		return HeadLib.RED_X.toItemStack(1, ChatColor.RED + "Deny", "Deny the Request.", sm.getNameTwo());
	}
}
