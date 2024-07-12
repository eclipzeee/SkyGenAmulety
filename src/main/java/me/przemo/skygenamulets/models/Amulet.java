package me.przemo.skygenamulets.models;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

public class Amulet {
    private final Material material;
    private final String name;
    private final String lore;
    private final int effectLevel;
    private final PotionEffectType effect;

    public Amulet(Material material, String name, String lore, int effectLevel, PotionEffectType effect) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.effectLevel = effectLevel;
        this.effect = effect;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public int getEffectLevel() {
        return effectLevel;
    }

    public PotionEffectType getEffect() {
        return effect;
    }
}