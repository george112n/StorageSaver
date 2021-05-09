package storagesaver;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import storagesaver.UpdateCall;
import storagesaver.listeners.*;

public class Main extends JavaPlugin
{
	static FileConfiguration config;

	static Main instance;
	
	private ArrayList<PlayerChunks> ChunkRecord = new ArrayList<PlayerChunks>();
	
	public ScoreboardManager manager;
	
	@Override
	public void onEnable()
	{
		Main.instance = this;
		Main.config = this.getConfig();
		saveDefaultConfig();
		
		//Listeners
		
		//TP Listener
		new TPEvent(this);
		new MoveEvent(this);
		new ChunkLoading(this);
		new JoinEvent(this);
		
		manager = Bukkit.getScoreboardManager();
		
		int minute = (int) 1200L;
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				UpdateCall up = new UpdateCall(instance);
				up.run();
			}
		}, 0L, minute * config.getInt("ReductionInterval"));
	}
	
	public ArrayList<PlayerChunks> getChunkRecord()
	{
		return ChunkRecord;
	}
}
