package net.phileasfogg3.petlife.Managers;

import net.nexia.nexiaapi.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerNameManager {

    private Config playerData;

    public PlayerNameManager(Config playerData) {this.playerData = playerData;}

    public void getPlayer(Player player){

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
                        colour = "Green";
                    case 2:
                        colour = "Yellow";
                    case 1:
                        colour = "Red";
                    case 0:
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
        ChatColor nameColour =ChatColor.valueOf(colour.toUpperCase());

        // Gives the player the correct colour in chat and tablist.
        player.setDisplayName(nameColour + player.getName());
        player.setPlayerListName(nameColour + player.getName());

        // Adds player to the correct team.
        sm.getTeam(colour).addEntry(player.getName());

    }

}
