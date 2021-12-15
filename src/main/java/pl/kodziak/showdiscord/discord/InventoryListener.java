package pl.kodziak.showdiscord.discord;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.kodziak.showdiscord.ShowDiscord;
import pl.kodziak.showdiscord.utils.ChatUtil;

import java.util.HashMap;

@AllArgsConstructor
public class InventoryListener implements Listener {

    private ShowDiscord showDiscord;

    @EventHandler
    public void onClick( InventoryClickEvent event ) {

        if(!event.getView().getTitle().equals(ChatUtil.fixColor(showDiscord.getInventoryConfig().getTitle()))) return;

        event.setCancelled(true);
        Player player = ( Player ) event.getWhoClicked();

        if(event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.PLAYER_HEAD){

            if ( showDiscord.getDataManager().getPlayerStatus(player.getUniqueId()) == 0 ) {
                player.sendMessage(ChatUtil.fixColor(showDiscord.getConfiguration().getMessages().getNotAllowedToRedeemMessage()));
                player.closeInventory();
                return;
            }

            if ( showDiscord.getDataManager().getPlayerStatus(player.getUniqueId()) == 2 ) {
                player.sendMessage(ChatUtil.fixColor(showDiscord.getConfiguration().getMessages().getAlreadyRedeemedMessage()));
                player.closeInventory();
                return;
            }

            showDiscord.getDataManager().updatePlayerStatus(player.getUniqueId(), ( short ) 2);

            for ( String rewardCommand : showDiscord.getConfiguration().getRewardCommands() ) {
                showDiscord.getServer().dispatchCommand(showDiscord.getServer().getConsoleSender(), rewardCommand.replace("{NICKNAME}", player.getName()));
            }

            final HashMap<Integer, ItemStack> droppedItems = player.getInventory().addItem(showDiscord.getConfiguration().getRewardItems().toArray(new ItemStack[0]));
            droppedItems.forEach((amount, itemStack) -> {
                itemStack.setAmount(amount);
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            });

            player.sendMessage(ChatUtil.fixColor(showDiscord.getConfiguration().getMessages().getRewardRedeemMessage()));
            player.closeInventory();

        }

    }

}
