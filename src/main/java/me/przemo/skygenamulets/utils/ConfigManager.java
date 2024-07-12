package me.przemo.skygenamulets.utils;

import me.przemo.skygenamulets.models.Amulet;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigManager {
    private final JavaPlugin plugin;
    private final Map<String, Map<Integer, Amulet>> amulets = new HashMap<>();

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();

        FileConfiguration config = plugin.getConfig();
        if (config.contains("amulets")) {
            ConfigurationSection amuletsConfig = config.getConfigurationSection("amulets");
            for (String name : amuletsConfig.getKeys(false)) {
                ConfigurationSection levelsConfig = amuletsConfig.getConfigurationSection(name);
                Map<Integer, Amulet> levels = new HashMap<>();
                for (String levelStr : levelsConfig.getKeys(false)) {
                    int level = Integer.parseInt(levelStr);
                    ConfigurationSection amuletConfig = levelsConfig.getConfigurationSection(levelStr);
                    Material material = Material.matchMaterial(amuletConfig.getString("material"));
                    String amuletName = amuletConfig.getString("name");
                    String lore = amuletConfig.getString("lore");
                    int effectLevel = amuletConfig.getInt("effect-level");
                    String effectName = amuletConfig.getString("effect");
                    PotionEffectType effect = PotionEffectType.getByName(effectName);
                    if (effect == null) {
                        plugin.getLogger().log(Level.WARNING, "Nieprawidłowy efekt: " + effectName + " dla amuletu " + amuletName);
                    } else {
                        Amulet amulet = new Amulet(material, amuletName, lore, effectLevel, effect);
                        levels.put(level, amulet);
                    }
                }
                amulets.put(name, levels);
            }
        }
    }

    public Map<String, Map<Integer, Amulet>> getAmulets() {
        return amulets;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
