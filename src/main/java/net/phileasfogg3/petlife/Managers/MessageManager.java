package net.phileasfogg3.petlife.Managers;

import net.nexia.nexiaapi.Config;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MessageManager {
    private Config gameMgr;
    private Config messagesData;

    public MessageManager(Config gameMgr, Config messagesData) {
        this.gameMgr = gameMgr;
        this.messagesData = messagesData;
    }

    public void determineMessageLocation(String messageLocation, String message, String type, Player player) {
        switch (messageLocation) {
            case "chat":
                sendMessageChat(message, player);
                break;
            case "title":
                sendMessageTitle(message, player);
                break;
            case "action":
                sendMessageAction(message, player);
                break;
            default:
                System.out.println("Invalid Message Type");
        }
        determineMessageType(type, player);
    }

    public void determineMessageType(String type, Player player) {
        switch (type) {
            case "admin":
                player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_5, 1.0F, 1.0F);
                break;
            case "timing":
                player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 1.0F, 1.0F);
                break;
            case "sessionInfo":
                player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_1, 1.0F, 1.0F);
                break;
            default:
                System.out.println("Invalid Message Type");
        }
    }

    public void sendMessageChat(String message, Player player) {

        String colouredMessage = ChatColor.translateAlternateColorCodes('&', message);
        String colouredPrefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(messagesData.getData().getString("message-prefix")));

        player.sendMessage(colouredPrefix + colouredMessage);

    }

    public void sendMessageTitle(String message, Player player) {

    }

    public void sendMessageAction(String message, Player player) {

    }

}
