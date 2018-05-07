package me.oisinaylward.warp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatColor;

public class Warp extends JavaPlugin {
	
	public final ChatColor PRIMARY = ChatColor.GOLD;
	public final ChatColor SECONDARY = ChatColor.WHITE;
	
	private static Warp warp;
	
	@Override
	public void onEnable() {
		warp = this;
		if(!loadConfig()) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private boolean loadConfig() {
		try {
			if(!getDataFolder().exists()) {
				getDataFolder().mkdirs();
			}
			getLogger().info(getDataFolder().listFiles().length + " player warps loaded.");
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	private String usage() {
		return PRIMARY + "[WARP]\n" + SECONDARY + "/warp [name]\n/warp set [name]\n/warp del [name]\n/warp list";
	}
	
	public static Warp get() {
		return warp;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("warp")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage(PRIMARY + "[WARP] " + SECONDARY + "Players only :(");
				return true;
			}
			
			Player player = (Player) sender;

			if(args.length == 0) {
				player.sendMessage(usage());
			} else if(args.length > 0) {
				if(args[0].equalsIgnoreCase("set")) {
					if(args.length != 2) {
						player.sendMessage(usage());
						return true;
					}
					ConfigFile config = ConfigFile.load(player);
					int warps = config.getWarps().toArray().length;
					if(warps >= 3) {
						player.sendMessage(PRIMARY + "[WARP] " + SECONDARY + "You already have 3 warps. Delete one before setting another.");
						return true;
					}
					config.addWarp(player, args[1].toLowerCase());
					player.sendMessage(PRIMARY + "[WARP] " + SECONDARY + "Set warp '" + args[1] + "' to this location.");
				} else if(args[0].equalsIgnoreCase("del")) {
					if(args.length != 2) {
						player.sendMessage(usage());
						return true;
					}
					ConfigFile config = ConfigFile.load(player);
					if(config.getWarp(player, args[1].toLowerCase()) == null) {
						player.sendMessage(PRIMARY + "[WARP] " + SECONDARY + "Warp doesn't exist.");
						return true;
					}
					config.deleteWarp(args[1].toLowerCase());
					player.sendMessage(PRIMARY + "[WARP] " + SECONDARY + "Warp '" + args[1] + "' deleted.");
				} else if(args[0].equalsIgnoreCase("list")) {
					ConfigFile config = ConfigFile.load(player);
					if(config.getWarps().toArray().length == 0) {
						player.sendMessage(PRIMARY + "[WARP] " + SECONDARY + "You have no warps. :(");
						return true;
					}
					player.sendMessage(PRIMARY + "[WARP] " + SECONDARY + "" + config.getWarps().toString());
				} else {
					ConfigFile config = ConfigFile.load(player);
					Location loc = config.getWarp(player, args[0]);
					if(loc == null) {
						player.sendMessage(PRIMARY + "[WARP] " + SECONDARY + "Warp doesn't exist.");
						return true;
					}
					player.teleport(loc);
					player.sendMessage(PRIMARY + "[WARP] " + SECONDARY + "Warping to '" + args[0] + "'.");
				}
			}
			return true;
		}
		return false;
	}
	
}
