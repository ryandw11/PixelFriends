package me.ryandw11.pixelfriends.utils;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ryandw11.pixelfriends.api.ComponentAddon;
import me.ryandw11.pixelfriends.api.event.PixelClickEvent;

public class Default implements ComponentAddon {

	@Override
	public boolean checkConditions(Player active, OfflinePlayer friend) {
		return true;
	}

	@Override
	public ItemStack customItem(Player active, OfflinePlayer friend) {
		ItemStack def = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta defM = def.getItemMeta();
		defM.setDisplayName(" ");
		def.setItemMeta(defM);
		return def;
	}

	@Override
	public void onClick(PixelClickEvent e) {
		// The default onClick will never be called.
	}

}
