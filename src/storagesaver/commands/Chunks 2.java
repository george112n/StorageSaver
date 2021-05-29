package storagesaver.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import storagesaver.Main;

public class Chunks implements CommandExecutor
{
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e)
	{
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		//Check is command sender is a player
		if (!(sender instanceof Player))
		{
			sender.sendMessage("&cYou cannot add a player to a region!");
			return true;
		}
				
		//Convert sender to player
		Player p = (Player) sender;
		
		if (!(p.hasPermission("Spacesaver.display")))
		{
			return true;
		}
		
		if (args.length != 1)
		{
			p.sendMessage(ChatColor.RED +"Use: /chunks [on/off]");
		}
		else if (args[0].equalsIgnoreCase("on"))
		{
			for (int i = 0 ; i < Main.getInstance().getChunkRecord().size() ; i++)
			{
				if (Main.getInstance().getChunkRecord().get(i).getUUID() == p.getUniqueId())
				{
					p.setScoreboard(Main.getInstance().getChunkRecord().get(i).getBoard());
					return true;
				}
			}
		}
		else if (args[0].equalsIgnoreCase("off"))
		{
			p.setScoreboard(Main.getInstance().manager.getMainScoreboard());
		}
		else
		{
			p.sendMessage(ChatColor.RED +"Use: /chunks [on/off]");
		}
		return true;
	}
}
