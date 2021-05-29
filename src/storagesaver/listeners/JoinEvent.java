
/**
 * @author 
 * @date 4 Jan 2021
 * @time 17:55:24
 */
package storagesaver.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import storagesaver.Main;
import storagesaver.PlayerChunks;

public class JoinEvent implements Listener
{
	private final Main plugin;
	
	public JoinEvent(Main plugin)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Storagesaver] JoinEvent loaded");
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event)
	{
		boolean bAlreadyConnected = false;
				
		//Checks whether there is already a chunk record for this player
		for (int i = 0 ; i < plugin.getChunkRecord().size() ; i++)
		{
			if (plugin.getChunkRecord().get(i).getUUID().equals(event.getPlayer().getUniqueId()))
			{
				bAlreadyConnected = true;
								
				Objective objective = plugin.getChunkRecord().get(i).getBoard().registerNewObjective("test", "dummy");
				objective.setDisplaySlot(DisplaySlot.SIDEBAR);
				 
				objective.setDisplayName("New Chunks Loaded");
				
				//Set their chunk count
				Score score = objective.getScore(ChatColor.GREEN + "Your Chunks: ");
				score.setScore(plugin.getChunkRecord().get(i).getChunks());
				
				//Add chunk limit to display
				Score Limit = objective.getScore(ChatColor.GREEN + "Limit: ");
				
				if (event.getPlayer().hasPermission("Storagesaver.generate.builder"))
					Limit.setScore(plugin.getConfig().getInt("ChunksLimitBuilder"));
				else
				{
					Limit.setScore(plugin.getConfig().getInt("ChunksLimitGuest"));
					//Assign the scoreboard to the player
				//	event.getPlayer().setScoreboard(plugin.getChunkRecord().get(i).getBoard());
				}
			}
		}
		
		//If there isnt a chunk record, create one and add it
		if (!bAlreadyConnected)
		{
			PlayerChunks newRecord = new PlayerChunks(event.getPlayer(), plugin.manager.getNewScoreboard());
			
			Objective objective = newRecord.getBoard().registerNewObjective("test", "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName("New Chunks Loaded");
			
			//Set their chunk count
			Score score = objective.getScore(ChatColor.GREEN + "Your Chunks: ");
			score.setScore(newRecord.getChunks());
			
			//Add chunk limit to display
			Score Limit = objective.getScore(ChatColor.GREEN + "Limit: ");
			
			if (event.getPlayer().hasPermission("Storagesaver.generate.builder"))
				Limit.setScore(plugin.getConfig().getInt("ChunksLimitBuilder"));
			else
			{
				Limit.setScore(plugin.getConfig().getInt("ChunksLimitGuest"));
				//Assign the scoreboard to the player
			//	event.getPlayer().setScoreboard(newRecord.getBoard());
			}
			
			//Add the new record to the chunk record
			plugin.getChunkRecord().add(newRecord);
		}
	}
}
//End Class

//Created by Bluecarpet in London