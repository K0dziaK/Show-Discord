package pl.kodziak.showdiscord.data;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import pl.kodziak.showdiscord.ShowDiscord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@AllArgsConstructor
public class DataManager {

    private final Map<UUID, Short> playerId = new HashMap<>();
    private final List<Long> discordRedeemed = new ArrayList<>();

    private final ShowDiscord showDiscord;

    public short getPlayerStatus( UUID uuid ) {

        return playerId.getOrDefault(uuid, ( short ) 0);
    }

    @SneakyThrows
    public void loadData() {

        showDiscord.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `showdiscord_players` (`uuid` varchar(64) PRIMARY KEY NOT NULL, `status` int(1) NOT NULL)").executeUpdate();
        showDiscord.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `showdiscord_discord` (`id` int(11) PRIMARY KEY auto_increment NOT NULL, `discordID` bigint(22) NOT NULL, `nickname` varchar(16) NOT NULL )").executeUpdate();

        final ResultSet playersResultSet = showDiscord.getConnection().prepareStatement("SELECT * FROM showdiscord_players").executeQuery();

        while ( playersResultSet.next() ) {

            playerId.put(UUID.fromString(playersResultSet.getString("uuid")), playersResultSet.getShort("status"));

        }

        final ResultSet discordsResultSet = showDiscord.getConnection().prepareStatement("SELECT * FROM showdiscord_discord").executeQuery();

        while ( discordsResultSet.next() ) {

            discordRedeemed.add(discordsResultSet.getLong("discordID"));

        }

    }

    @SneakyThrows
    public void updatePlayerStatus( UUID uuid, short status ) {
        if(!playerId.containsKey(uuid)){
            final PreparedStatement preparedStatement = showDiscord.getConnection().prepareStatement("INSERT INTO showdiscord_players(uuid, status) VALUES (?, ?)");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setShort(2, status);
            preparedStatement.executeUpdate();
        }else{
            final PreparedStatement preparedStatement = showDiscord.getConnection().prepareStatement("UPDATE showdiscord_players SET status=? WHERE uuid=?");
            preparedStatement.setShort(1, status);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        }
        playerId.put(uuid, status);
    }

    @SneakyThrows
    public void addDiscordRedeemed( long discordId, String nickname ) {
        final PreparedStatement preparedStatement = showDiscord.getConnection().prepareStatement("INSERT INTO showdiscord_discord(discordID, nickname) VALUES (?, ?)");
        preparedStatement.setLong(1, discordId);
        preparedStatement.setString(2, nickname);
        preparedStatement.executeUpdate();
        discordRedeemed.add(discordId);
    }

    public boolean isDiscordRedeemed( long discordId ) {

        return discordRedeemed.contains(discordId);
    }

}
