package pl.kodziak.showdiscord.discord;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.kodziak.showdiscord.ShowDiscord;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class DiscordListener extends ListenerAdapter {

    private ShowDiscord showDiscord;

    @Override
    public void onReady( @NotNull ReadyEvent event ) {

        if(!showDiscord.getConfiguration().getMessages().isInfoEmbed()) return;

        executeAsync(() -> {
            for ( Guild guild : event.getJDA().getGuilds() ) {
                final TextChannel textChannelById = guild.getTextChannelById(showDiscord.getConfiguration().getDiscordChannel());
                if ( textChannelById == null ) continue;
                textChannelById.getHistoryFromBeginning(10).queue(history -> {
                    for ( Message message : history.getRetrievedHistory() ) {
                        message.delete().queue();
                    }
                });
                final var embedBuilder = new EmbedBuilder()
                        .setTitle(showDiscord.getConfiguration().getMessages().getInfoEmbedTitle())
                        .setColor(Color.DARK_GRAY)
                        .setDescription(showDiscord.getConfiguration().getMessages().getInfoEmebedDescription())
                        .setFooter(showDiscord.getConfiguration().getMessages().getEmbedFooter(), !Objects.equals(showDiscord.getConfiguration().getMessages().getEmbedFooterImage(), "") ? showDiscord.getConfiguration().getMessages().getEmbedFooterImage() : null);
                if(showDiscord.getConfiguration().getMessages().isEmbedTimestamp()) embedBuilder.setTimestamp(Instant.now());
                textChannelById.sendMessageEmbeds(embedBuilder
                        .build()).queue();
            }
        });

    }

    @Override
    public void onMessageReceived( @NotNull MessageReceivedEvent event ) {

        if ( event.getAuthor().isBot() ) return;
        if ( !event.getChannel().getId().equals(showDiscord.getConfiguration().getDiscordChannel()) ) return;

        final Message message = event.getMessage();
        executeAsync(() -> message.delete().queue());

        if ( message.getContentRaw().split("//s+").length > 1 ) return;

        final String playerName = message.getContentRaw();
        if ( showDiscord.getDataManager().isDiscordRedeemed(event.getAuthor().getIdLong()) ) {
            executeAsync(() -> {
                if(showDiscord.getConfiguration().getMessages().isEmbed()){
                    final EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setColor(Color.DARK_GRAY)
                            .setDescription(showDiscord.getConfiguration().getMessages().getDiscordRedeemed().replace("{NICKNAME}", playerName))
                            .setFooter(showDiscord.getConfiguration().getMessages().getEmbedFooter(), !Objects.equals(showDiscord.getConfiguration().getMessages().getEmbedFooterImage(), "") ? showDiscord.getConfiguration().getMessages().getEmbedFooterImage() : null);
                    if(showDiscord.getConfiguration().getMessages().isEmbedTimestamp()) embedBuilder.setTimestamp(Instant.now());
                    event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
                }else event.getChannel().sendMessage(showDiscord.getConfiguration().getMessages().getDiscordRedeemed().replace("{NICKNAME}", playerName)).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
            });
            return;
        }

        Player player = Bukkit.getPlayerExact(playerName);
        if ( player == null ) {
            executeAsync(() -> {
                if(showDiscord.getConfiguration().getMessages().isEmbed()){
                    final EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setColor(Color.DARK_GRAY)
                            .setDescription(showDiscord.getConfiguration().getMessages().getPlayerNotFound().replace("{NICKNAME}", playerName))
                            .setFooter(showDiscord.getConfiguration().getMessages().getEmbedFooter(), !Objects.equals(showDiscord.getConfiguration().getMessages().getEmbedFooterImage(), "") ? showDiscord.getConfiguration().getMessages().getEmbedFooterImage() : null);
                    if(showDiscord.getConfiguration().getMessages().isEmbedTimestamp()) embedBuilder.setTimestamp(Instant.now());
                    event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
                }else event.getChannel().sendMessage(showDiscord.getConfiguration().getMessages().getPlayerNotFound().replace("{NICKNAME}", playerName)).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
            });
            return;
        }

        if ( showDiscord.getDataManager().getPlayerStatus(player.getUniqueId()) != 0 ) {
            executeAsync(() -> {
                if(showDiscord.getConfiguration().getMessages().isEmbed()){
                    final EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setColor(Color.DARK_GRAY)
                            .setDescription(showDiscord.getConfiguration().getMessages().getNicknameRedeemed().replace("{NICKNAME}", player.getName()))
                            .setFooter(showDiscord.getConfiguration().getMessages().getEmbedFooter(), !Objects.equals(showDiscord.getConfiguration().getMessages().getEmbedFooterImage(), "") ? showDiscord.getConfiguration().getMessages().getEmbedFooterImage() : null);
                    if(showDiscord.getConfiguration().getMessages().isEmbedTimestamp()) embedBuilder.setTimestamp(Instant.now());
                    event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
                }else event.getChannel().sendMessage(showDiscord.getConfiguration().getMessages().getNicknameRedeemed().replace("{NICKNAME}", player.getName())).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
            });
            return;
        }

        showDiscord.getDataManager().addDiscordRedeemed(event.getAuthor().getIdLong(), player.getDisplayName());
        showDiscord.getDataManager().updatePlayerStatus(player.getUniqueId(), ( short ) 1);
        executeAsync(() -> {
            if(showDiscord.getConfiguration().getMessages().isEmbed()){
                final EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setColor(Color.DARK_GRAY)
                        .setDescription(showDiscord.getConfiguration().getMessages().getRedeemSuccessful().replace("{NICKNAME}", player.getName()))
                        .setFooter(showDiscord.getConfiguration().getMessages().getEmbedFooter(), !Objects.equals(showDiscord.getConfiguration().getMessages().getEmbedFooterImage(), "") ? showDiscord.getConfiguration().getMessages().getEmbedFooterImage() : null);
                if(showDiscord.getConfiguration().getMessages().isEmbedTimestamp()) embedBuilder.setTimestamp(Instant.now());
                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
            }else event.getChannel().sendMessage(showDiscord.getConfiguration().getMessages().getRedeemSuccessful().replace("{NICKNAME}", player.getName())).queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
        });

    }

    private void executeAsync( Runnable runnable ) {

        showDiscord.getExecutorService().execute(runnable);
    }

}
