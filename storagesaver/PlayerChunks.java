package storagesaver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerChunks
{
	private Player player;
	private int iChunks;
	private Scoreboard board;
	
	public PlayerChunks(Player player, Scoreboard board)
	{
		this.player = player;
		this.iChunks = 0;
		this.board = board;
	}
	
	public int getChunks()
	{
		return iChunks;
	}
	
	public UUID getUUID()
	{
		return player.getUniqueId();
	}
	
	public boolean getPerms()
	{
		return player.hasPermission("Storagesaver.generate.builder");
	}
	
	public String getName()
	{
		return player.getName();
	}
	
	public Scoreboard getBoard()
	{
		return board;
	}
	
	public int incrementChunks(Chunk chunk, Main plugin, boolean bAdded)
	{
		iChunks++;
		//Do Database add here
		if (!bAdded)
			addToSecondaryRecord(player.getUniqueId().toString(), chunk.getX(), chunk.getZ());	
		return iChunks;
	}
	
	public void reduceChunks(int iReduction)
	{
		iChunks = iChunks - iReduction;
		if (iChunks < 0)
		{
			iChunks = 0;
		}
	}
	
	public void warn()
	{
		player.sendMessage(ChatColor.RED +"You have reached your chunk load limit for now");
	}
	
	public static void addToSecondaryRecord(String szCause, int XCoord, int ZCoord)
	{
		LocalDateTime tTimeStamp = LocalDateTime.now();
		
		String szOutput = "";
		
		szOutput = szOutput + szCause +",";
		szOutput = szOutput +XCoord +",";
		szOutput = szOutput +ZCoord +",";
		szOutput = szOutput +tTimeStamp.toString()+"\n";
		
		FileWriter fileWritter = null;
		BufferedWriter bw = null;
		
		try
		{
			File file = new File("regions.csv");
			fileWritter = new FileWriter(file, true);
			bw = new BufferedWriter(fileWritter);
			
			bw.write(szOutput);
		}
		catch (Exception e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"[Storagesaver] Error saving new chunk to file");
		}
		finally
		{
			//Try closing buffered writer
			try
			{
				if (bw != null)
					bw.close();
			}
			catch (Exception e)
			{
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"[Storagesaver] Error closing buffered writer");
			}
			
			//Try closing file writer
			try
			{
				fileWritter.close();
			}
			catch (Exception e)
			{
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"[Storagesaver] Error closing file writer");
			}
		}
	}
}
