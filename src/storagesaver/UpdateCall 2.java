package storagesaver;

import java.util.ArrayList;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author 
 * @date 4 Jan 2021
 * @time 18:20:43
 */

public class UpdateCall extends BukkitRunnable
{
	private final Main plugin;
		
	public UpdateCall(Main plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void run()
	{
		ArrayList<PlayerChunks> players = plugin.getChunkRecord();
				
		for (PlayerChunks player: players)
		{
			player.reduceChunks(plugin.getConfig().getInt("ReductionAmount"));
		}
	}
} //End Class

//Created by Bluecarpet in London