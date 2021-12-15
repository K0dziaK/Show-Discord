package pl.kodziak.showdiscord.commands;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import pl.kodziak.showdiscord.ShowDiscord;
import pl.kodziak.showdiscord.settings.InventoryConfig;
import pl.kodziak.showdiscord.utils.ChatUtil;

@AllArgsConstructor
public class RewardCommand implements CommandExecutor {

    private final ShowDiscord showDiscord;

    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args ) {

        Inventory inventory = Bukkit.createInventory(null, showDiscord.getInventoryConfig().getSize(), ChatUtil.fixColor(showDiscord.getInventoryConfig().getTitle()));

        final short status = showDiscord.getDataManager().getPlayerStatus(( ( Player ) sender ).getUniqueId());
        for ( InventoryConfig.GuiItem guiItem : showDiscord.getInventoryConfig().getGuiItems().values() ) {

            for ( Integer slot : guiItem.getSlots() ) {

                inventory.setItem(slot, guiItem.getItem(status));

            }

        }

        ( ( Player ) sender ).openInventory(inventory);

        return true;
    }

}
