package net.phileasfogg3.petlife.Managers;

import net.nexia.nexiaapi.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;

public class PlayerNameManager {

    private Config playerData;
    private Config gameMgr;

    public PlayerNameManager(Config playerData, Config gameMgr) {
        this.playerData = playerData;
        this.gameMgr = gameMgr;
    }

    public void getPlayer(Player player){
        TeamsManager tM = new TeamsManager(gameMgr);
        Map<Integer, String> teamsMap = tM.getTeamsFromYML();
        // Gets all the players in playerData.yml
        playerData.getData().getConfigurationSection("players").getKeys(false).forEach(key -> {
            // Finds the player whose name we wish to change.
            if (player.getUniqueId().toString().equals(key)) {
                // Gets how many lives the player in question has.
                int lives = playerData.getData().getInt("players." + key + ".Lives");
                String colour = "";
                // Assigns the colour variable to match the number of lives the player has
                switch (lives) {
                    case 3:
                        colour = teamsMap.get(3);
                        player.setGameMode(GameMode.SURVIVAL);
                        break;
                    case 2:
                        colour = teamsMap.get(2);
                        player.setGameMode(GameMode.SURVIVAL);
                        break;
                    case 1:
                        colour = teamsMap.get(1);
                        player.setGameMode(GameMode.SURVIVAL);
                        break;
                    default:
                        colour = teamsMap.get(0);
                        player.setGameMode(GameMode.SPECTATOR);
                }
                changeName(player, colour);
            }
        });
    }

    public void changeName(Player player, String colour){
        // Gets the scoreboard
        Scoreboard sm = Bukkit.getScoreboardManager().getMainScoreboard();
        // Gets the chat colour object based on the String colour.
        // Gives the player the correct colour in chat and tablist.
        player.setDisplayName(ChatColor.valueOf(colour.toUpperCase()) + player.getName() + ChatColor.RESET);
        player.setPlayerListName(ChatColor.valueOf(colour.toUpperCase()) + player.getName() + ChatColor.RESET);
        // Adds player to the correct team.
        sm.getTeam(colour).addEntry(player.getName());
    }
}
