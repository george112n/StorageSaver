
/**
 * @author 
 * @date 30 Jan 2021
 * @time 11:18:21
 */

package storagesaver.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import storagesaver.Main;

public class MoveEvent implements Listener
{
	private final Main plugin;
	
	public MoveEvent(Main plugin)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "MoveEvent loaded");
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MoveEvent ready");
	}
	
	@EventHandler
	public void FlyEvent(PlayerMoveEvent event)
	{
		//Convert sender to player
		Player player = event.getPlayer();
		
		if (player.hasPermission("Storagesaver.move.bypass"))
		{
			return;
		}
		
		double Y = event.getTo().getY();
		
		//Checks whether it is outside the designated range
		if (Y > plugin.getConfig().getInt("MaxHeight"))
		{
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED +"You have moved to a location which is too high!");
			player.sendMessage(ChatColor.RED +"Check Minecraft Server Rule 6 in the Discord server");
		}
		else if (Y < plugin.getConfig().getInt("MinHeight"))
		{
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED +"You have moved to a location which is too low!");
			player.sendMessage(ChatColor.RED +"Check Minecraft Server Rule 6 in the Discord server");
		}
	}
} //End Class

//Created by Bluecarpet in London