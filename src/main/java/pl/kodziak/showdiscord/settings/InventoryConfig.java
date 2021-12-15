package pl.kodziak.showdiscord.settings;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.kodziak.showdiscord.utils.ChatUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Header("################################################################")
@Header("#                                                              #")
@Header("#          Konfiguracja gui /nagrody                           #")
@Header("#                                                              #")
@Header("################################################################")
public class InventoryConfig extends OkaeriConfig {

    @Comment("Nazwa gui")
    @Getter
    private String title = "&7Nagrody za &bSOCIAL-MEDIA";
    @Comment("Wielkość gui")
    @Getter
    private Integer size = 27;
    @CustomKey("gui-items")
    @Getter
    private Map<Integer, GuiItem> guiItems = ImmutableMap.<Integer, GuiItem>builder()
            .put(1, new GuiItem(Collections.singletonList(13),
                    Material.PLAYER_HEAD,
                    1,
                    "&bDISCORD",
                    Arrays.asList("",
                            "&7Odbierz nagrodę za dołączenie na dc!",
                            "&7Link do discorda znajdziesz",
                            "&7pod komendą &a/discord",
                            "",
                            "&7Nagroda&8:",
                            "   &8- &65x Legendarny Klucz",
                            "",
                            "&8□ &7Status&8: {STATUS}",
                            ""),
                    Collections.emptyMap(),
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg3M2MxMmJmZmI1MjUxYTBiODhkNWFlNzVjNzI0N2NiMzlhNzVmZjFhODFjYmU0YzhhMzliMzExZGRlZGEifX19"))
            .put(2, new GuiItem(Arrays.asList(0, 1, 7,8,9,17,18,19,25,26), Material.BLACK_STAINED_GLASS_PANE, 1, "&0", Collections.emptyList(), Collections.emptyMap(), ""))
            .put(3, new GuiItem(Arrays.asList(2,3,4,5,6,10,11,12,14,15,16,20,21,22,23,24), Material.GRAY_STAINED_GLASS_PANE, 1, "&0", Collections.emptyList(), Collections.emptyMap(), ""))
            .build();

    @AllArgsConstructor
    public class GuiItem extends OkaeriConfig{

        @Getter
        private List<Integer> slots;
        private Material material;
        private Integer amount;
        @CustomKey("display-name")
        private String displayName;
        private List<String> lore;
        private Map<Enchantment, Integer> enchantmentments;
        private String texture;

        public ItemStack getItem( int status ) {

            ItemStack itemStack = new ItemStack(material, amount);
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(ChatUtil.fixColor(displayName));
            itemMeta.setLore(lore.stream().map(rawText -> ChatUtil.fixColor(rawText.replace("{STATUS}", status == 0 ? "&eDołącz na discorda" : status == 1 ? "&aKliknij aby odebrać" : "&cJuż odebrano"))).collect(Collectors.toList()));
            enchantmentments.forEach(( ench, level ) -> itemMeta.addEnchant(ench, level, true));

            if ( material == Material.PLAYER_HEAD ) {

                SkullMeta skullMeta = ( SkullMeta ) itemMeta;
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
                gameProfile.getProperties().put("textures", new Property("textures", texture));

                Field profileField;
                try {
                    profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, gameProfile);
                } catch ( IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e ) {
                    e.printStackTrace();
                }

            }
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }

    }

}
