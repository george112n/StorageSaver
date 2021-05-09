
/**
 * @author 
 * @date 30 Jan 2021
 * @time 11:18:21
 */

package storagesaver.listeners;

import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

import storagesaver.Main;
import storagesaver.PlayerChunks;

public class ChunkLoading implements Listener
{
	private final Main plugin;
	
	public ChunkLoading(Main plugin)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "ChunkLoading loaded");
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ChunkLoading ready");
	}
	
	@EventHandler
	public void Chunk(ChunkLoadEvent event)
	{
		int i, j;
		
		if (!event.isNewChunk())
		{
			return;
		}
		
		//Get players who generated chunk
		List<Player> players = event.getWorld().getPlayers();
		
		Player player;
		
		double iDistance;
		
		//Stores whether the record was added or not
		boolean bAdded = false;
		
		//Go through list of online players
		for (i = 0; i < players.size(); i++)
		{
			//Store player locally
			player = players.get(i);
			
			//Calculate distance of player from loaded chunk
			iDistance = Math.sqrt((player.getLocation().getBlockX() - event.getChunk().getX())^2 +(player.getLocation().getBlockZ() - event.getChunk().getZ())^2);
			
			//If the distance is less than 200 blocks, assume them to have conspired in the chunk loading
			//Allowing multiple players to have loaded a chunks stops groups of people being able to increase
			//The amount of chunks that they can load
			if (iDistance < plugin.getConfig().getInt("DistanceInvolved"))
			{
				//Find the chunk record of the player in question
				for (j = 0 ; j < plugin.getChunkRecord().size() ; j++)
				{
					//Test UUID of player in chunk record against the player found near the new chunk
					if (plugin.getChunkRecord().get(j).getUUID() == player.getUniqueId())
					{
						//If one of the players found near the chunk has already met their limit...

						//For guests
						if (!plugin.getChunkRecord().get(j).getPerms())
						{
							if (plugin.getChunkRecord().get(j).getChunks() > plugin.getConfig().getInt("ChunksLimitGuest"))
							{
								//No output - Already above
								//Then increment
								plugin.getChunkRecord().get(j).incrementChunks(event.getChunk(), plugin, bAdded);
								Score score = plugin.getChunkRecord().get(j).getBoard().getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.GREEN + "Your Chunks: ");
								score.setScore(plugin.getChunkRecord().get(j).getChunks());
							}
							else if (plugin.getChunkRecord().get(j).incrementChunks(event.getChunk(), plugin, bAdded) > plugin.getConfig().getInt("ChunksLimitGuest"))
							{
								notifyStaff(player);
								Score score = plugin.getChunkRecord().get(j).getBoard().getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.GREEN + "Your Chunks: ");
								score.setScore(plugin.getChunkRecord().get(j).getChunks());
							}
							else
							{
								Score score = plugin.getChunkRecord().get(j).getBoard().getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.GREEN + "Your Chunks: ");
								score.setScore(plugin.getChunkRecord().get(j).getChunks());
							}
						}

						//Builders
						else if (plugin.getChunkRecord().get(j).getChunks() > plugin.getConfig().getInt("ChunksLimitBuilder"))
						{
							//No output - Already above
							//Then increment
							plugin.getChunkRecord().get(j).incrementChunks(event.getChunk(), plugin, bAdded);								
							Score score = plugin.getChunkRecord().get(j).getBoard().getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.GREEN + "Your Chunks: ");
							score.setScore(plugin.getChunkRecord().get(j).getChunks());
						}
						else if (plugin.getChunkRecord().get(j).incrementChunks(event.getChunk(), plugin, bAdded) > plugin.getConfig().getInt("ChunksLimitBuilder"))
						{
							notifyStaff(player);
							Score score = plugin.getChunkRecord().get(j).getBoard().getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.GREEN + "Your Chunks: ");
							score.setScore(plugin.getChunkRecord().get(j).getChunks());
						}
						else
						{
							Score score = plugin.getChunkRecord().get(j).getBoard().getObjective(DisplaySlot.SIDEBAR).getScore(ChatColor.GREEN + "Your Chunks: ");
							score.setScore(plugin.getChunkRecord().get(j).getChunks());
						}
					}
				}
				//Tells the loop that it has been added by a player being near
				bAdded = true;
			}
		}
		
		//If no player was found close by when the new chunk loaded
		if (!bAdded)
		{
			PlayerChunks.addToSecondaryRecord("Unknown", event.getChunk().getX(), event.getChunk().getZ());
		}
	}
	
	public void notifyStaff(Player szPlayer)
	{
		final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		
		//Bypass warning messages if the player has bypass
		if (szPlayer.hasPermission("Storagesaver.generate.bypass"))
		{
			return;
		}
		
		for (Player player: players)
		{
			if (player.hasPermission("Storagesaver.notify"))
			{
				player.sendMessage(ChatColor.AQUA +"[Storage Saver] Issue Detected. Early Warning:");
				player.sendMessage(ChatColor.GOLD + szPlayer.getName() +ChatColor.YELLOW +" Has exceeded the chunk load threshold");
			}
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA +"[Storage Saver] Issue Detected. Early Warning:");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + szPlayer.getName() +ChatColor.YELLOW +" Has exceeded the chunk load threshold");
	}
	
} //End Class

//Created by Bluecarpet in London