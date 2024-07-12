package me.przemo.skygenamulets.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.przemo.skygenamulets.SkyGenAmulets;
import me.przemo.skygenamulets.models.Amulet;
import me.przemo.skygenamulets.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Map;

@Command(name = "amulet")
public class AmuletCommand {
    private final SkyGenAmulets plugin;
    private final ConfigManager configManager;

    public AmuletCommand(SkyGenAmulets plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Execute
    @Permission("skygenamulets.admin")
    public void onAmuletCommand(@Context CommandSender sender, @Arg String[] args) {
        if (args.length < 4 || !args[0].equalsIgnoreCase("nadaj")) {
            sender.sendMessage(ChatColor.RED + "Niepoprawny alias komendy. Użyj: /amulet nadaj <nick> <poziom> <nazwa>");
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Komenda dostępna jest tylko dla graczy!");
            return;
        }

        Player player = (Player) sender;
        String targetName = args[1];
        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Niepoprawny poziom amuletu.");
            return;
        }
        String amuletName = args[3];

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Gracz " + targetName + " nie jest na serwerze bądź nie istnieje.");
            return;
        }

        if (!configManager.getAmulets().containsKey(amuletName)) {
            player.sendMessage(ChatColor.RED + "Nie znaleziono amuletu o nazwie " + amuletName + ".");
            return;
        }

        Map<Integer, Amulet> amuletLevels = configManager.getAmulets().get(amuletName);
        if (!amuletLevels.containsKey(level)) {
            player.sendMessage(ChatColor.RED + "Nie znaleziono amuletu o poziomie " + level + " który nazywa się " + amuletName + ".");
            return;
        }

        Amulet amulet = amuletLevels.get(level);
        ItemStack item = new ItemStack(amulet.getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(amulet.getName());
        meta.setLore(Collections.singletonList(amulet.getLore()));
        item.setItemMeta(meta);

        target.getInventory().addItem(item);
        player.sendMessage(ChatColor.GREEN + "Amulet " + amulet.getName() + " poziom " + level + " został nadany graczowi " + target.getName() + ".");
        target.sendMessage(ChatColor.GREEN + "Otrzymałeś amulet: " + amulet.getName() + " poziom " + level + ".");
    }
}
