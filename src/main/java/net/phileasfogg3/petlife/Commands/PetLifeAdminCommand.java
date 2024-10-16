package net.phileasfogg3.petlife.Commands;

import net.nexia.nexiaapi.Config;
import net.phileasfogg3.petlife.Managers.PlayerNameManager;
import net.phileasfogg3.petlife.Managers.SessionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PetLifeAdminCommand implements CommandExecutor, TabCompleter {

    private Config playerData;
    private Config gameMgr;
    public PetLifeAdminCommand(Config playerData, Config gameMgr) {
        this.playerData = playerData;
        this.gameMgr = gameMgr;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >=1) {
                switch (args[0]) {
                    case "sessionStart":
                        // Starts the session
                        boolean sessionActive = gameMgr.getData().getBoolean("session-active");
                        int sessionNumber = gameMgr.getData().getInt("session-information.session-number");
                        // Checks to see if the player has permissions
                        if (player.hasPermission("petlife.admin")) {
                            if (sessionNumber == 0 && !sessionActive) {
                                // Do this if this is the first session.
                                System.out.println("Do this if this is the first session.");
                                int newSessionNumber = sessionNumber + 1;
                                gameMgr.getData().set("session-information.session-number", newSessionNumber);
                                gameMgr.getData().set("session-active", true);
                                gameMgr.save();
                                // Controls Session Timings
                                SessionManager sM = new SessionManager(gameMgr);
                                sM.sessionTimeInitializer();
                            } else if (sessionNumber > 0 && !sessionActive) {
                                // Do this if it is not the first session and the session has not already started.
                                System.out.println("Do this if it is not the first session and the session has not already started.");
                                int newSessionNumber = sessionNumber + 1;
                                gameMgr.getData().set("session-information.session-number", newSessionNumber);
                                gameMgr.getData().set("session-active", true);
                                gameMgr.save();
                            } else {
                                // Do this if the session has already started.
                                System.out.println("Do this if the session has already started.");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                        }
                        break;
                    case "setlife":
                        if (args.length ==3){
                            if (player.hasPermission("petlife.admin")){
                                String playerName = args[1];
                                int life = Integer.parseInt(args[2]);
                                Player targetPlayer = Bukkit.getServer().getPlayerExact(playerName);
                                if (targetPlayer !=null && targetPlayer.isOnline()) {
                                    // Sets the life of a specific value of this player
                                    Map<String, Object> playerDataMap = getPlayerValues(targetPlayer);
                                    playerDataMap.put("Lives", life);
                                    saveConfig(targetPlayer, playerDataMap);
                                    PlayerNameManager PNM = new PlayerNameManager(playerData, gameMgr);
                                    PNM.getPlayer(targetPlayer);
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                            }
                        }
                        break;
                    case "setpet":
                        if (args.length ==3){
                            String playerName = args[1];
                            String petName = args[2];
                            Player targetPlayer = Bukkit.getServer().getPlayerExact(playerName);
                            if (targetPlayer !=null && targetPlayer.isOnline()) {
                                // Sets the pet of a specific player
                            }
                        }
                        break;
                    case "cure":
                        if (args.length ==2){
                            String playerName = args[1];
                            Player targetPlayer = Bukkit.getServer().getPlayerExact(playerName);
                            if (targetPlayer !=null && targetPlayer.isOnline()) {
                                // Cures the specific player if they are the boogeyman
                            }
                        }
                        break;
                    case "confirm":
                        if (player.hasPermission("petlife.admin")){
                            // Confirms an automatic action taken by the server
                            if (gameMgr.getData().getBoolean("confirmation.required")) {
                                gameMgr.getData().set("confirmation.value", 1);
                                player.sendMessage("Confirmed!");
                            } else {
                                player.sendMessage("Nothing to confirm!");
                            }
                        } else {
                            player.sendMessage("You do not have permission to use this command!");
                        }
                }
            }
        } else {
            System.out.println("This command must be executed by a player");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 1) {
            arguments = Arrays.asList("sessionStart", "setlife", "setpet", "cure", "confirm");
        }
        // Filter results based on input
        List<String> filteredSuggestions = new ArrayList<>();
        for (String suggestion : arguments) {
            if (suggestion.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                filteredSuggestions.add(suggestion);
            }
        }
        return filteredSuggestions;
    }

    private Map<String, Object> getPlayerValues(Player player) {
        return playerData.getData().getConfigurationSection("players." + player.getUniqueId()).getValues(false);
    }

    private void saveConfig(Player player, Map<String, Object> playerDataMap) {
        // Method to save the playerData.yml file.
        playerData.getData().createSection("players." + player.getUniqueId(), playerDataMap);
        playerData.save();
    }
}