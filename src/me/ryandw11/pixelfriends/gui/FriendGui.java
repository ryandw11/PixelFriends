package me.ryandw11.pixelfriends.gui;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import me.ryandw11.pixelfriends.anvilgui.AnvilGUI;
import me.ryandw11.pixelfriends.api.DataFileManager;
import me.ryandw11.pixelfriends.api.status.FriendRequest;
import me.ryandw11.pixelfriends.api.status.RequestStatus;
import me.ryandw11.pixelfriends.headlib.HeadLib;

public class FriendGui implements Listener {
	private PixelFriends plugin;
	private SettingsManager sm;
	private DataFileManager dm;
	
	public FriendGui() {
		this.plugin = PixelFriends.plugin;
		sm = new SettingsManager(plugin);
		dm = new DataFileManager(plugin);
	}
	
	public void openMGUI(Player p, int page) {

	    Inventory inv = Bukkit.createInventory(null, 9 * 5, ChatColor.translateAlternateColorCodes('&', "&5Pixel Friends &7Page:" + page));

	    List<String> friends = plugin.data.getStringList(p.getUniqueId() + ".Friends");

	    int siz = friends.size();
	    int i = this.getMin(page);
	    int i2 = 9;
	    while (i < siz && i < this.getMax(page)) {
	      /*
	       *
	       * Edited GUI from UReport
	       *
	       */
	    	ItemStack item = new ItemStack(Material.PLAYER_HEAD);
	    	SkullMeta skMeta = (SkullMeta) item.getItemMeta();
	    	OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(friends.get(i)));
	    	skMeta.setOwningPlayer(op);
	    	skMeta.setDisplayName(ChatColor.AQUA + op.getName());
	    	List<String> lore = new ArrayList<>();
	    	if(!op.isOnline()) {
	    		DateFormat simple = new SimpleDateFormat(sm.getDateFormat());
	    		Date result = new Date(op.getLastPlayed());
	    		lore.add(ChatColor.BLUE + "Last Seen: " + simple.format(result));
	    	}
	    	else
	    		lore.add(ChatColor.GREEN + "Online");
	    	lore.add(ChatColor.AQUA + "Right Click this skull to remove the friend.");
	    	lore.add(sm.getNameOne());
	    	skMeta.setLore(lore);
	    	item.setItemMeta(skMeta);
	    	inv.setItem(i2, item);
	    	i = i + 1;
	    	i2++;
	    }
	    this.setBottom(inv, page, p);
	    this.setTop(inv);

	    p.openInventory(inv);

	  }

	  @SuppressWarnings("deprecation")
	@EventHandler
	  public void onInventoryClickEvent(InventoryClickEvent e) {
	    if (e.getInventory().getName()
	        .contains(ChatColor.translateAlternateColorCodes('&', "&5Pixel Friends &7Page:"))) {

	      Player p = (Player) e.getWhoClicked();
	      e.setCancelled(true);

	      List<String> friends = plugin.data.getStringList(p.getUniqueId() + ".Friends");
	      int page = getPage(e.getInventory().getName());
	      int siz = friends.size();

	      if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e
	          .getCurrentItem().hasItemMeta()) {
	        return;
	      }
	      //================
	      ItemStack item = e.getCurrentItem();
	      if (!item.getItemMeta().hasLore()) {
	        return;
	      }
	      
	      int loreSize = item.getItemMeta().getLore().size();
	      
	      if (item.getItemMeta().getLore().get(loreSize - 1).equals(sm.getNameOne())) {

	        if (e.isLeftClick()) {
	        	for(String h : friends){
	        		if(((SkullMeta)item.getItemMeta()).getOwningPlayer().getUniqueId().equals(UUID.fromString(h))) {
	        			Friend f = new Friend();
	        			f.openConfirmGui(p, ((SkullMeta) item.getItemMeta()).getOwningPlayer());
	        		}
	        	}
	        }
	        else if (e.isRightClick()) {
	        	for(String h : friends){
	        		if(((SkullMeta)item.getItemMeta()).getOwningPlayer().getUniqueId().equals(UUID.fromString(h))) {
	        			UUID player = ((SkullMeta)item.getItemMeta()).getOwningPlayer().getUniqueId();
	        			dm.removeFriend(p.getUniqueId(), player);
	        			p.sendMessage(ChatColor.GREEN + "The player was removed from your friends list");
	        		}
	        	}
	        }

	      }//end of poseidon warp check.
	      if (!item.getItemMeta().getLore().get(loreSize - 1).equals(sm.getNameTwo())) {
	      } else if (item.equals(friendItemStack())) { //Add a firend
	        p.closeInventory();
	        new AnvilGUI(plugin, p, "Who do you want to add?", (player, reply) -> {
	        	if(reply.equals(p.getName())) {
	        		p.sendMessage(ChatColor.RED + "You may not friend yourself!");
	        		return null;
	        	}
	        	if(!Bukkit.getOfflinePlayer(reply).hasPlayedBefore()) {
	        		p.sendMessage(ChatColor.RED + "That player has not played before!");
	        		return null;
	        	}
	        	
	        	if(dm.getRecievedRequests(p).contains(new FriendRequest(Bukkit.getOfflinePlayer(reply).getUniqueId(), p.getUniqueId()))) {
	        		p.sendMessage(ChatColor.RED + "You already recieved a friend request from that player!");
	        		return null;
	        	}
	        	
	        	if(dm.hasRequest(Bukkit.getOfflinePlayer(reply).getUniqueId(), p.getUniqueId(), RequestStatus.Sent)) {
	        		p.sendMessage(ChatColor.RED + "You already sent a friend request to that player!");
	        		return null;
	        	}
	        	
	        	if(!sm.getBoolSetting(Bukkit.getOfflinePlayer(reply), "requests")) {
	        		p.sendMessage(ChatColor.RED + "That player has requests disabled!");
	        		return null;
	        	}
	        	if(sm.getMaxRequests() != -1)
	        		if(dm.getRequestsNumber(Bukkit.getOfflinePlayer(reply).getUniqueId()) >= sm.getMaxRequests()) {
	        			p.sendMessage(ChatColor.RED + "That person has too many open friend requests!");
	        			return null;
	        		}
	        	
	        	if(sm.getMaxFriend() != -1)
	        		if(dm.numberOfFriends(Bukkit.getOfflinePlayer(reply).getUniqueId()) >= sm.getMaxFriend()) {
	        			p.sendMessage(ChatColor.RED + "That person has too many friends!");
	        			return null;
	        		}
	        	
	        	if(sm.getMaxFriend() != -1)
	        		if(dm.numberOfFriends(p.getUniqueId()) >= sm.getMaxFriend()) {
	        			p.sendMessage(ChatColor.RED + "You have too many friends!");
	        			return null;
	        		}
	        	
	        	if(dm.hasFriend(p.getUniqueId(), Bukkit.getOfflinePlayer(reply).getUniqueId())) {
	        		p.sendMessage(ChatColor.RED + "You are already friends with that person!");
	        		return null;
	        	}
	        	
	        	ConfirmRequest cr = new ConfirmRequest();
	        	cr.openConfirmGui(p, Bukkit.getOfflinePlayer(reply));
	        	
	          return null;
	        });
	      }
	      else if (item.equals(requestsItemStack(p))){
	    	  p.closeInventory();
	    	  RequestGui rgui = new RequestGui();
	    	  rgui.openMGUI(p, 1);
	      }
	      else if(item.getType() == Material.EMERALD) {
	    	  p.closeInventory();
	    	  SettingsGui sgui = new SettingsGui();
	    	  sgui.openConfirmGui(p);
	      }
	      else if (item.getType() == Material.RED_STAINED_GLASS_PANE) { //Previous page of friends.
	        if (siz <= this.getMax(page)) {
	          p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9This is the last page!"));
	        } else if (siz > this.getMax(page)) {
	          p.closeInventory();
	          openMGUI(p, page + 1);
	        }
	      } else if (item.getType() == Material.PURPLE_STAINED_GLASS_PANE) { //Next page of friends
	        p.closeInventory();
	        openMGUI(p, page - 1);
	      }
	    }
	  }

	  private void setTop(Inventory inv) {
	    ItemStack def = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
	    ItemMeta defm = def.getItemMeta();
	    defm.setDisplayName(ChatColor.translateAlternateColorCodes('&', " "));
	    List<String> lss = new ArrayList<>();
	    lss.add(sm.getNameTwo());
	    defm.setLore(lss);
	    def.setItemMeta(defm);

	   
	    inv.setItem(0, def);
	    inv.setItem(1, def);
	    inv.setItem(2, def);
	    inv.setItem(3, def);
	    inv.setItem(4, friendItemStack());
	    inv.setItem(5, def);
	    inv.setItem(6, def);
	    inv.setItem(7, def);
	    inv.setItem(8, def);

	  }

	  private void setBottom(Inventory inv, int page, Player p) {

	    ItemStack def = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
	    ItemMeta defm = def.getItemMeta();
	    defm.setDisplayName(ChatColor.translateAlternateColorCodes('&', " "));
	    List<String> ls = new ArrayList<>();
	    ls.add(sm.getNameTwo());
	    defm.setLore(ls);
	    def.setItemMeta(defm);
	    inv.setItem(36, def);
	    //inv.setItem(37, def);
	    inv.setItem(38, def);
	    inv.setItem(40, def);
	    inv.setItem(42, def);
	    //inv.setItem(43, def);
	    inv.setItem(44, def);
	    
	    
	    inv.setItem(43, requestsItemStack(p));
	    List<String> skLore = new ArrayList<>();
	    ItemStack settings = new ItemStack(Material.EMERALD);
	    ItemMeta settingMeta = settings.getItemMeta();
	    settingMeta.setDisplayName(ChatColor.GRAY + "Settings");
	    skLore.clear();
	    skLore.add(ChatColor.WHITE + "Your settings.");
	    skLore.add(sm.getNameTwo());
	    settingMeta.setLore(skLore);
	    settings.setItemMeta(settingMeta);
	    inv.setItem(37, settings);

	    ItemStack next = new ItemStack(Material.RED_STAINED_GLASS_PANE, page + 1);
	    ItemMeta nextm = next.getItemMeta();
	    ArrayList<String> lores = new ArrayList<>();
	    lores.add(sm.getNameTwo());
	    nextm.setLore(lores);
	    nextm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cNext Page >"));
	    next.setItemMeta(nextm);
	    inv.setItem(41, next);

	    if (page != 1) {
	      ItemStack prev = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, page - 1);
	      ItemMeta prevm = prev.getItemMeta();
	      ArrayList<String> lore = new ArrayList<>();
	      lore.add(sm.getNameTwo());
	      prevm.setLore(lore);
	      prevm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5< Previous Page"));
	      prev.setItemMeta(prevm);
	      inv.setItem(39, prev);
	    } else {
	      ArrayList<String> lore = new ArrayList<>();
	      lore.add(ChatColor.translateAlternateColorCodes('&', "&cYou are on the first page!"));
	      lore.add(sm.getNameTwo());
	      ItemStack prev = new ItemStack(Material.BARRIER);
	      ItemMeta prevm = prev.getItemMeta();
	      prevm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5< Previous Page"));
	      prevm.setLore(lore);
	      prev.setItemMeta(prevm);
	      inv.setItem(39, prev);
	    }
	  }
	  
	  public ItemStack requestsItemStack(Player p) {
		  	int amount = dm.getRecievedRequests(p).size();
		  	if(amount > 64) {
		  		amount = 64;
		  	}
		  	if (amount < 1) {
		  		amount = 1;
		  	}
		    List<String> skLore = new ArrayList<>();
		    skLore.add(ChatColor.BLUE + "Your friend requests.");
		    skLore.add(sm.getNameTwo());
		    
		    String[] array = skLore.toArray(new String[skLore.size()]); 
		    return HeadLib.WOODEN_F.toItemStack(amount, ChatColor.AQUA + "Friend Requests (" + dm.getRecievedRequests(p).size() + ")", array);
		    
	  }
	  
	  public ItemStack friendItemStack() {
		    List<String> ls = new ArrayList<>();
		    ls.add(ChatColor.translateAlternateColorCodes('&', "&fSend a friend request to someone."));
		    ls.add(sm.getNameTwo());
		    String[] array = ls.toArray(new String[ls.size()]); 
		    return HeadLib.WOODEN_PLUS.toItemStack(1, ChatColor.translateAlternateColorCodes('&', "&aAdd a friend"), array);
		    
	  }

	  private int getMax(int page) {
	    int head = 3 * page;
	    return 9 * head;
	  }

	  private int getMin(int page) {
	    int head = 3 * page;
	    int fun = 9 * head;
	    return fun - 9 * 3;
	  }

	  private int getPage(String s) {
	    String[] st;
	    st = s.split(":");
	    return Integer.valueOf(st[1]);
	  }
}
