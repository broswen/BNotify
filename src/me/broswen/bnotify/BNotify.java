package me.broswen.bnotify;

import java.util.HashMap;

import me.broswen.bnotify.PlayerListener;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class BNotify extends JavaPlugin{
	public static BNotify plugin;
	public final PlayerListener playerListener = new PlayerListener();
	public static HashMap<String, Long> hashmap = new HashMap<String, Long>();
	public static HashMap<String, Long> hashmap1 = new HashMap<String, Long>();
	
	@Override
	public void onEnable(){
		PluginDescriptionFile pdfFile = this.getDescription();
		getServer().getPluginManager().registerEvents(this.playerListener, this);
		getLogger().info("BNotify version " + pdfFile.getVersion() + " has been enabled!");
		loadConfig();
		this.plugin = this;
	}
	
	@Override
	public void onDisable(){
		getLogger().info("BNotify has been disabled!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		PluginDescriptionFile pdfFile = this.getDescription();
		
		if(cmd.getName().equalsIgnoreCase("bnotify")){
			if(sender.hasPermission("bnotify.info")){
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					player.sendMessage(ChatColor.GOLD + "######## "+ ChatColor.WHITE +"BNotify" + ChatColor.GOLD + " ########");
					player.sendMessage("Version: " + pdfFile.getVersion());
					player.sendMessage("Author: " + pdfFile.getAuthors());
					player.sendMessage("About: " + pdfFile.getDescription());
					player.sendMessage(ChatColor.GOLD + "######## "+ ChatColor.WHITE +"BNotify" + ChatColor.GOLD + " ########");
				}else{
					getLogger().info("######## BNotify ########");
					getLogger().info("Version: " + pdfFile.getVersion());
					getLogger().info("Author: " + pdfFile.getAuthors());
					getLogger().info("About: " + pdfFile.getDescription());
					getLogger().info("######## BNotify ########");
				}
				
			}else{
				sender.sendMessage(ChatColor.RED + "You cannot do that!");
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("poke")){
			if(sender.hasPermission("bnotify.poke")){
				if(sender instanceof Player){
					final Player player = (Player) sender;
					if(args.length == 1){
						Player targetPlayer = player.getServer().getPlayer(args[0]);
						if(targetPlayer != null){
							if(targetPlayer != player){
								if(!hashmap1.containsKey(targetPlayer.getName())){
									if(!targetPlayer.hasPermission("bnotify.exempt")){
										
										int commandDelay = plugin.getConfig().getInt("command-delay");
										int commandDelayInTicks = commandDelay * 20;
										
										if(!hashmap.containsKey(player.getName())){
											player.sendMessage(ChatColor.GOLD + "[BNotify] " + ChatColor.WHITE + "You poked " + targetPlayer.getName() + "!");
											targetPlayer.sendMessage(ChatColor.GOLD + "[BNotify] " + ChatColor.WHITE + "You were poked by " + player.getName() + "!");
											targetPlayer.playSound(targetPlayer.getLocation(), Sound.NOTE_PLING, 10, 5);
											
											hashmap.put(player.getName(), null);
											
											this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){

												@Override
												public void run() {
													if(hashmap.containsKey(player.getName())){
														hashmap.remove(player.getName());
													}
												}
												
											}, commandDelayInTicks);
											
										}else{
											player.sendMessage(ChatColor.GOLD + "[BNotify] " + ChatColor.RED + "You may only poke every " + commandDelay + " seconds!");
										}
										
									}else{
										player.sendMessage(ChatColor.GOLD + "[BNotify] " + ChatColor.RED + "That person may not be poked!");
									}
									
								}else{
									player.sendMessage(ChatColor.GOLD + "[BNotify] " + ChatColor.RED + "That person is currently blocking pokes!");
								}
								
							}else{
								player.sendMessage(ChatColor.GOLD + "[BNotify] " + ChatColor.RED + "You may not poke yourself!");
							}
							
						}else{
							player.sendMessage(ChatColor.GOLD + "[BNotify] " + ChatColor.RED + "That player is not online!");
						}
						
					}else{
						player.sendMessage(ChatColor.GOLD + "[BNotify] " + ChatColor.RED + "/poke <player>");
					}
					
				}else{
					getLogger().info("You must be a player to do that!");
				}
				
			}else{
				sender.sendMessage(ChatColor.RED + "You cannot do that!");
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("poketoggle")){
			
			if(sender.hasPermission("bnotify.toggle")){
			
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					if(!hashmap1.containsKey(player.getName())){
						hashmap1.put(player.getName(), null);
						player.sendMessage(ChatColor.GOLD + "[BNotify] " + ChatColor.RESET + "You are no longer pokable!");
					}else{
						hashmap1.remove(player.getName());
						player.sendMessage(ChatColor.GOLD + "[BNotify] " + ChatColor.RESET + "You are now pokable!");
					}
					
				}else{
					getLogger().info("You must be a player!");
				}
				
			}else{
				sender.sendMessage(ChatColor.RED + "You cannot do that!");
			}
		}
		
		return true;
	}
	
	public void loadConfig(){
		saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}
