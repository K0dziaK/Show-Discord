package pl.kodziak.showdiscord.utils;

import org.bukkit.ChatColor;

public class ChatUtil {

    public static String fixColor(String rawText){
        return ChatColor.translateAlternateColorCodes('&', rawText);
    }

}
