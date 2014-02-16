package me.broswen.bnotify;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener{
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		
		if(BNotify.hashmap1.containsKey(player.getName())){
			BNotify.hashmap1.remove(player.getName());
		}
		
	}

}
