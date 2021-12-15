package pl.kodziak.showdiscord.settings;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.Variable;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.kodziak.showdiscord.utils.ItemBuilder;

import java.util.Collections;
import java.util.List;

@Header("################################################################")
@Header("#                                                              #")
@Header("#    Witaj w konfiguarcji pluginu Show-Discord                 #")
@Header("#    Jeżeli posiadasz jakieś pytania, kieruj się               #")
@Header("#    na discord https://discord.gg/tgtNRYvgAd                  #")
@Header("#                                                              #")
@Header("#    Plugin wykonał: K0dziaK dla ShowCode.PL                   #")
@Header("#                                                              #")
@Header("################################################################")
public class Config extends OkaeriConfig {

    @Getter
    @Variable("APP_TOKEN")
    @Comment
    @Comment({"Token bota discord"})
    private String token = "";

    @Getter
    @Comment
    @Comment({"Id kanału na discordzie, na którym gracz wysyła nick aby otrzymać nagrodę"})
    @CustomKey("discord-channel")
    private String discordChannel = "0";

    @Getter
    @Comment
    @Comment("Przedmioty które otrzyma gracz za dołączenie na discorda")
    @CustomKey("reward-items")
    private List<ItemStack> rewardItems = Collections.singletonList(new ItemBuilder(Material.DIRT, 5).setTitle("&aExample Display Name").addLores("&7Example", "&fMultiline", "&7Lore").build());

    @Getter
    @Comment
    @Comment("Komendy które wykonają się gdy gracz odbierze nagrodę za dołączenie na discorda (użyj {NICKNAME} aby wykorzystać)")
    @CustomKey("reward-commands")
    private List<String> rewardCommands = Collections.emptyList();

    @Getter
    @Comment
    @Comment({"===========", "Wiadomości", "==========="})
    private MessagesConfig messages = new MessagesConfig();
    @Getter
    @Comment
    @Comment({"===========", "Baza Danych - MySQL", "==========="})
    private StorageConfig database = new StorageConfig();

    public class MessagesConfig extends OkaeriConfig {

        @Getter
        @Comment
        @Comment("Wiadomość którą otrzyma gracz podczas próby odebrania nagrody, przed dołączeniem na discorda")
        @CustomKey("not-allowed-to-redeem")
        private String notAllowedToRedeemMessage = "&7Nagrodę będziesz mógł odebrać dopiero po dołączeniu na naszego discorda &6https://discord.gg/tgtNRYvgAd &7i napisaniu swojego nicku na kanale &e#nagroda";

        @Getter
        @Comment
        @Comment("Wiadomość którą otrzyma gracz podczas próby odebrania nagrody, gdy już ją odebrał")
        @CustomKey("already-redeemed")
        private String alreadyRedeemedMessage = "&cOdebrałeś już nagrodę za dołączenie na discorda!";

        @Getter
        @Comment
        @Comment("Wiadomość którą otrzyma gracz przy pomyślnym odebraniu nagrody")
        @CustomKey("reward-redeem")
        private String rewardRedeemMessage = "&aOtrzymałeś nagrodę za dołączenie na discorda!";

        @Getter
        @Comment
        @Comment("Czy komenda /discord ma być włączona?")
        @CustomKey("discord-command")
        private boolean discordCommand = true;

        @Getter
        @Comment
        @Comment("Wiadomość którą otrzyma gracz po wpisaniu /discord")
        @CustomKey("discord-command-message")
        private List<String> discordMessage = Collections.singletonList("&7Wejdź na naszego discorda &6https://discord.gg/tgtNRYvgAd &7i odbierz nagrodę pisząc na kanale &e#nagroda");

        @Getter
        @Comment
        @Comment("Wiadomość którą otrzyma gracz, gdy próbuje 2 raz odebrać nagrodę z tego samego konta discord")
        @CustomKey("already-redeemed-discord")
        private String discordRedeemed = "Odebrałeś już nagrodę z tego konta discord!";

        @Getter
        @Comment
        @Comment("Wiadomość którą otrzyma gracz, gdy próbuje 2 raz odebrać nagrodę na to samo konto minecraft")
        @CustomKey("already-redeemed-nickname")
        private String nicknameRedeemed = "Na ten nick odebrana została już nagroda!";

        @Getter
        @Comment
        @Comment("Wiadomość którą otrzyma gracz, gdy próbuje 2 raz odebrać nagrodę z tego samego konta discord")
        @CustomKey("player-not-found")
        private String playerNotFound = "Taki gracz nie jest online na serwerze!";

        @Getter
        @Comment
        @Comment("Wiadomość którą otrzyma gracz, gdy próbuje 2 raz odebrać nagrodę z tego samego konta discord")
        @CustomKey("redeem-successful")
        private String redeemSuccessful = "{NICKNAME} odbierz nagrodę w grze za pomocą komendy **/nagroda**!";

        @Getter
        @Comment
        @Comment("Czy bot ma wysłać wiadomość informacyjną na kanale do odbierania nagrody?")
        @CustomKey("info-embed")
        private boolean infoEmbed = true;

        @Getter
        @Comment("Tytuł embeda informacyjnego")
        @CustomKey("info-embed-title")
        private String infoEmbedTitle = "Nagrody :gift:";

        @Getter
        @Comment("Opis embeda informacyjnego")
        @CustomKey("info-embed-description")
        private String infoEmebedDescription = "Chciałbyś odebrać specjalną nagrodę za dołączenie na serwer discord?\n" +
                "Napisz swój nick z serwera, aby ją odebrać\n" +
                "\n" +
                "Pamiętaj! Podczas odbierania musisz być na serwerze.\n" +
                "```Przykład: Notch```";

        @Getter
        @Comment
        @Comment("Czy bot odpisując na discordzie powinien używać embeda?")
        private boolean embed = true;

        @Getter
        @Comment
        @Comment("Poniższe opcje działają tylko gdy opcja \"embed\" jest włączona")
        @Comment("Tekst który znajduje się w tzw. footerze embedu (będzie w każdej wiadomości wysyłanej przez bota)")
        @CustomKey("embed-footer")
        private String embedFooter = "Show-Discord | System nagród";
        @Getter
        @Comment("Link do zdjęcia które znaleźć ma się w footerze (zostaw puste jeżeli ma się nie pojawić)")
        @CustomKey("embed-footer-image")
        private String embedFooterImage = "";
        @Getter
        @Comment("Czy w footerze ma znajdować się timestamp? (jest to czas wysłania wiadomości)")
        @CustomKey("embed-footer-timestamp")
        private boolean embedTimestamp = true;

    }

    public class StorageConfig extends OkaeriConfig {

        @Getter
        private String host = "localhost";
        @Getter
        private Integer port = 3306;
        @Getter
        private String database = "db";
        @Getter
        private String username = "username";
        @Getter
        private String password = "password";

    }

}


