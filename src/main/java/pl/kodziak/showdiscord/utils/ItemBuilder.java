package pl.kodziak.showdiscord.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ItemBuilder {

    private final List<String> lore;
    private Material mat;
    private int amount;
    private short data;
    private String title;
    private Map<Enchantment, Integer> enchants;
    private boolean hideEnchants;

    public ItemBuilder( Material mat, int amount ) {

        this(mat, amount, ( short ) 0);
    }

    public ItemBuilder( Material mat, int amount, short data ) {

        this.title = null;
        this.lore = new ArrayList();
        this.enchants = new HashMap();
        this.mat = mat;
        this.amount = amount;
        this.data = data;
        this.hideEnchants = false;
    }

    public ItemBuilder setTitle( String title ) {

        title = ChatColor.translateAlternateColorCodes('&', title);
        this.title = title;
        return this;
    }

    public ItemBuilder addLores( String... lores ) {

        this.lore.addAll(Arrays.asList(lores));
        return this;
    }

    public ItemStack build() {

        Material mat = this.mat;
        if ( mat == null ) {
            mat = Material.AIR;
            org.bukkit.Bukkit.getLogger().warning("Null material!");
        }
        ItemStack item = new ItemStack(this.mat, this.amount, this.data);
        ItemMeta meta = item.getItemMeta();
        if ( this.title != null ) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
        }
        if ( !this.lore.isEmpty() ) {
            meta.setLore(lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
        }
        if ( hideEnchants ) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        if ( mat == Material.ENCHANTED_BOOK ) {
            EnchantmentStorageMeta bookMeta = ( EnchantmentStorageMeta ) item.getItemMeta();
            for ( Entry<Enchantment, Integer> enchantments : this.enchants.entrySet() ) {
                bookMeta.addStoredEnchant(enchantments.getKey(), enchantments.getValue(), true);
            }
            item.setItemMeta(bookMeta);
        } else {
            item.addUnsafeEnchantments(this.enchants);
        }
        return item;
    }

}