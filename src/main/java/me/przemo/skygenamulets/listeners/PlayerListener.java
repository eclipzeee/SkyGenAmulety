package me.przemo.skygenamulets.listeners;

import me.przemo.skygenamulets.models.Amulet;
import me.przemo.skygenamulets.utils.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class PlayerListener implements Listener {
    private final ConfigManager configManager;

    public PlayerListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        int newSlot = event.getNewSlot();
        int previousSlot = event.getPreviousSlot();

        removeAmuletEffects(player);
        applyAmuletEffect(player, newSlot);

        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack currentItem = player.getInventory().getItem(newSlot);
                if (!isAmulet(currentItem) && !isAmulet(player.getInventory().getItemInOffHand())) {
                    removeAmuletEffects(player);
                }
            }
        }.runTaskLater(configManager.getPlugin(), 20L);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        removeAmuletEffects(player);
        applyAmuletEffect(player, event.getOffHandItem());

        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack currentItem = player.getInventory().getItemInOffHand();
                if (!isAmulet(currentItem) && !isAmulet(player.getInventory().getItemInMainHand())) {
                    removeAmuletEffects(player);
                }
            }
        }.runTaskLater(configManager.getPlugin(), 20L); 
    }

    private void applyAmuletEffect(Player player, ItemStack item) {
        if (item == null) {
            return;
        }

        for (Map<Integer, Amulet> levels : configManager.getAmulets().values()) {
            for (Amulet amulet : levels.values()) {
                if (item.getType() == amulet.getMaterial() &&
                        item.getItemMeta().getDisplayName().equals(amulet.getName())) {
                    player.addPotionEffect(new PotionEffect(amulet.getEffect(), Integer.MAX_VALUE, amulet.getEffectLevel()));
                    return;
                }
            }
        }
    }

    private void applyAmuletEffect(Player player, int slot) {
        ItemStack item = player.getInventory().getItem(slot);
        applyAmuletEffect(player, item);
    }

    private void removeAmuletEffects(Player player) {
        for (Amulet amulet : configManager.getAmulets().values().stream().flatMap(levels -> levels.values().stream()).toList()) {
            player.removePotionEffect(amulet.getEffect());
        }
    }

    private boolean isAmulet(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        for (Amulet amulet : configManager.getAmulets().values().stream().flatMap(levels -> levels.values().stream()).toList()) {
            if (item.getType() == amulet.getMaterial() && item.getItemMeta().getDisplayName().equals(amulet.getName())) {
                return true;
            }
        }
        return false;
    }
}
