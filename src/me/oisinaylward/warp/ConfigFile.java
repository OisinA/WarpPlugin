package me.oisinaylward.warp;

import java.io.File;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ConfigFile {
	
	private File file;
	private FileConfiguration config;
	
	public ConfigFile(File f, FileConfiguration conf) {
		this.file = f;
		this.config = conf;
	}
	
	public void addWarp(Player player, String name) {
		try {
			config.set("warps." + name + ".world", player.getLocation().getWorld().getName());
			config.set("warps." + name + ".x", player.getLocation().getX());
			config.set("warps." + name + ".y", player.getLocation().getY());
			config.set("warps." + name + ".z", player.getLocation().getZ());
			config.set("warps." + name + ".pitch", player.getLocation().getPitch());
			config.set("warps." + name + ".yaw", player.getLocation().getYaw());
			config.save(file);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Location getWarp(Player player, String name) {
		try {
			World world = Warp.get().getServer().getWorld((String) config.get("warps." + name.toLowerCase() + ".world"));
			double x = (double) config.get("warps." + name.toLowerCase() + ".x");
			double y = (double) config.get("warps." + name.toLowerCase() + ".y");
			double z = (double) config.get("warps." + name.toLowerCase() + ".z");
			double pitch = (double) config.get("warps." + name.toLowerCase() + ".pitch");
			double yaw = (double) config.get("warps." + name.toLowerCase() + ".yaw");
			Location loc = new Location(world, x, y, z);
			loc.setPitch((float) pitch);
			loc.setYaw((float) yaw);
			return loc;
		} catch(Exception e) {
		}
		return null;
	}
	
	public void deleteWarp(String name) {
		try {
			config.set("warps." + name, null);
			config.save(file);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Set<String> getWarps() {
		Set<String> warps = config.getConfigurationSection("warps").getKeys(false);
		return warps;
	}
	
	public static ConfigFile load(Player player) {
		try {
			File file = new File(Warp.get().getDataFolder(), player.getUniqueId().toString() + ".yml");
			if(!file.exists()) {
				file.createNewFile();
			}
			YamlConfiguration yml = new YamlConfiguration();
			yml.load(file);
			return new ConfigFile(file, yml);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
