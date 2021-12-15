package pl.kodziak.showdiscord.commands;

import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.kodziak.showdiscord.ShowDiscord;
import pl.kodziak.showdiscord.utils.ChatUtil;

@AllArgsConstructor
public class DiscordCommand implements CommandExecutor {

    private final ShowDiscord showDiscord;

    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args ) {

        for ( String line : showDiscord.getConfiguration().getMessages().getDiscordMessage() ) {
            sender.sendMessage(ChatUtil.fixColor(line));
        }

        return true;
    }

}
