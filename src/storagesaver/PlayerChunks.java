package storagesaver;

import java.util.UUID;

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
	
	public int incrementChunks()
	{
		iChunks++;
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
}