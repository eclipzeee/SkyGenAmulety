package me.przemo.skygenamulets;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import me.przemo.skygenamulets.commands.AmuletCommand;
import me.przemo.skygenamulets.listeners.PlayerListener;
import me.przemo.skygenamulets.utils.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyGenAmulets extends JavaPlugin {
    private ConfigManager configManager;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        liteCommands = LiteBukkitFactory
                .builder("skygenamulets", this)
                .commands(new AmuletCommand(this, configManager))
                .build();

        getServer().getPluginManager().registerEvents(new PlayerListener(configManager), this);
    }

    @Override
    public void onDisable() {
        if (liteCommands != null) {
            liteCommands.unregister();
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
