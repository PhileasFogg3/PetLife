package net.phileasfogg3.petlife.Commands;

import com.google.common.collect.Lists;
import net.nexia.nexiaapi.Config;
import net.phileasfogg3.petlife.Managers.PlayerNameManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PetLifeAdminCommand implements CommandExecutor, TabCompleter {

    private Config playerData;
    private Config gameMgr;
    public PetLifeAdminCommand(Config playerData, Config gameMgr) {this.playerData = playerData;}


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length >=1) {

                switch (args[0]) {

                    case "sessionStart":

                        // Starts the session

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

                                    PlayerNameManager PNM = new PlayerNameManager(playerData);
                                    PNM.getPlayer(targetPlayer);

                                }

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

                        // Confirms an automatic action taken by the server

                }

            }

        } else {

            System.out.println("This command must be executed by a player");

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> arguments = Arrays.asList("","","");
        List<String> list = Lists.newArrayList();

        if(args.length == 1) {for (String s : arguments) {if (s.toLowerCase().startsWith(args[0].toLowerCase())) {return list;}}}

        return null;

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
