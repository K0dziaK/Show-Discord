package pl.kodziak.showdiscord;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.hjson.HjsonConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import pl.kodziak.showdiscord.commands.DiscordCommand;
import pl.kodziak.showdiscord.commands.RewardCommand;
import pl.kodziak.showdiscord.data.DataManager;
import pl.kodziak.showdiscord.discord.DiscordListener;
import pl.kodziak.showdiscord.discord.InventoryListener;
import pl.kodziak.showdiscord.settings.Config;
import pl.kodziak.showdiscord.settings.InventoryConfig;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ShowDiscord extends JavaPlugin implements CommandExecutor {

    @Getter
    private ExecutorService executorService;

    @Getter
    private Config configuration;

    @Getter
    private InventoryConfig inventoryConfig;

    @Getter
    private DataManager dataManager;

    @Getter
    private Connection connection;

    private JDA jda;

    @Override
    public void onEnable() {

        setupConfig();

        setupConnection();

        executorService = Executors.newSingleThreadExecutor();

        dataManager = new DataManager(this);
        dataManager.loadData();

        setupDiscordBot();

        getCommand("nagroda").setExecutor(new RewardCommand(this));
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
        if ( configuration.getMessages().isDiscordCommand() )
            getCommand("discord").setExecutor(new DiscordCommand(this));

    }

    private void setupDiscordBot() {

        if(configuration.getToken().length() == 0){
            Bukkit.getPluginManager().disablePlugin(this);
            throw new IllegalArgumentException("Token bota nie został ustawiony!");
        }

        try {
            this.jda = JDABuilder.createDefault(configuration.getToken()).build();
            jda.addEventListener(new DiscordListener(this));
        } catch ( LoginException e ) {
            e.printStackTrace();
        }
    }

    private void setupConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + configuration.getDatabase().getHost() + ":" + configuration.getDatabase().getPort() + "/" + configuration.getDatabase().getDatabase() + "?autoReconnect=true&useUnicode=yes&useSSL=false", configuration.getDatabase().getUsername(), configuration.getDatabase().getPassword());
        } catch ( SQLException | ClassNotFoundException e ) {
            Bukkit.getPluginManager().disablePlugin(this);
            throw new IllegalArgumentException("Nie udało się połączyć z bazą danych!");
        }
    }

    private void setupConfig() {

        configuration = ConfigManager.create(Config.class, ( it ) -> {
            it.withConfigurer(new HjsonConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(this.getDataFolder(), "config.json"));
            it.saveDefaults();
            it.load(true);
        });

        inventoryConfig = ConfigManager.create(InventoryConfig.class, ( it ) -> {
            it.withConfigurer(new HjsonConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(this.getDataFolder(), "inventory.json"));
            it.saveDefaults();
            it.load(true);
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        if ( jda == null ) return;
        jda.shutdownNow();

    }

}
