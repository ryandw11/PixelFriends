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
import me.ryandw11.pixelfriends.api.status.FriendRequest;
import me.ryandw11.pixelfriends.headlib.HeadLib;

public class RequestGui implements Listener {
	private PixelFriends plugin;
	private SettingsManager sm;
	private DataFileManager dm;
	
	public RequestGui() {
		this.plugin = PixelFriends.plugin;
		sm = new SettingsManager(plugin);
		dm = new DataFileManager(plugin);
	}
	
	public void openMGUI(Player p, int page) {

	    Inventory inv = Bukkit.createInventory(null, 9 * 5, ChatColor.translateAlternateColorCodes('&', "&5Friend Requests &7Page:" + page));

	    List<FriendRequest> friends = dm.getRecievedRequests(p);

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
	    	OfflinePlayer op = Bukkit.getOfflinePlayer(friends.get(i).getSender());
	    	skMeta.setOwningPlayer(op);
	    	skMeta.setDisplayName(ChatColor.AQUA + op.getName());
	    	List<String> lore = new ArrayList<>();
	    	lore.add(ChatColor.BLUE + "This player wants to be you friend!");
	    	lore.add(ChatColor.BLUE + "Click to accept or deny this request.");
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

	  @EventHandler
	  public void onInventoryClickEvent(InventoryClickEvent e) {
	    if (e.getInventory().getName()
	        .contains(ChatColor.translateAlternateColorCodes('&', "&5Friend Requests &7Page:"))) {

	      Player p = (Player) e.getWhoClicked();
	      e.setCancelled(true);

	      List<FriendRequest> friends = dm.getRecievedRequests(p);
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

	    	  for(FriendRequest h : friends){
	        		if(((SkullMeta)item.getItemMeta()).getOwningPlayer().getUniqueId().equals(h.getSender())) {
	        			p.closeInventory();
	        			ConfirmFriendship cfgui = new ConfirmFriendship();
	        			cfgui.openConfirmGui(p, Bukkit.getOfflinePlayer(h.getSender()));
	        		}
	        	}

	      }//end of poseidon warp check.
	      else if (item.equals(friendItemStack())){
	    	  p.closeInventory();
	    	  FriendGui fgui = new FriendGui();
	    	  fgui.openMGUI(p, 1);
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
	    inv.setItem(4, def);
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
	    inv.setItem(37, friendItemStack());
	    inv.setItem(38, def);
	    inv.setItem(40, def);
	    inv.setItem(42, def);
	    inv.setItem(43, def);
	    inv.setItem(44, def);
	    

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
	  
	  
	  public ItemStack friendItemStack() {
		    List<String> ls = new ArrayList<>();
		    ls.add(sm.getNameTwo());
		    String[] array = ls.toArray(new String[ls.size()]); 
		    return HeadLib.WOODEN_ARROW_LEFT.toItemStack(1, ChatColor.translateAlternateColorCodes('&', "&cBack"), array);
		    
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
