package net.phileasfogg3.petlife.Managers;

import net.nexia.nexiaapi.Config;
import net.phileasfogg3.petlife.PetLife;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SessionManager {

    private final Config gameMgr;

    public SessionManager(Config gameMgr) {this.gameMgr = gameMgr;}

    public void sessionTimeManager() {

        int sessionLength = gameMgr.getData().getInt("session-timings.session-length");
        boolean sessionBreak = gameMgr.getData().getBoolean("session-timings.session-break");
        int sessionBreakLength = gameMgr.getData().getInt("session-timings.session-break-length");

        int sessionHalf = sessionLength / 2;

        if (sessionHalf% 60 !=0) {

            sessionHalf += (60-sessionHalf % 60); // Rounding up to nearest 60 if not already  multiple of 60.

        }

        if (sessionBreakLength % 60 !=0) {

            sessionBreakLength += (60- sessionBreakLength % 60); // Rounding up to nearest 60 if not already a multiple of 60.

        }

        if (sessionBreak) {

            // If there is a break
            int finalSessionBreakLength = sessionBreakLength;
            int finalSessionHalf = sessionHalf;

            // Counts down the first half of the session, until the break.
            runnable(new int[]{sessionHalf}, "Time left until the break: ", 1, () ->

                    // Counts down the time left of the break.
                    runnable(new int[]{finalSessionBreakLength}, "Time left of the break: ", 2, () ->

                            // Counts down the second half of the session, until the session's end.
                            runnable(new int[]{finalSessionHalf}, "Time left of the session: ", 3, null)

                    )
            );

        } else {

            // If there is no break
            runnable(new int[]{sessionLength}, "Time left of the session: ", 3, null);

        }

    }

    public void runnable(final int[] time, String tabListMessage, int actionID, Runnable onComplete) {

        // Updates yml and tablist
        sessionProgressUpdater(actionID, time[0]);
        updateTabList(tabListMessage, time[0]);

        new BukkitRunnable() {
            @Override
            public void run() {

                if (time[0] > 0) {
                    // Executes this code if there is more than 1 minute left of the session portion.

                    // Subtracts a minute from the session timer
                    time[0] -= 60;

                    // Updates yml and tablist
                    sessionProgressUpdater(actionID, time[0]);
                    updateTabList(tabListMessage, time[0]);

                    if (time[0] <= 0) {
                        // Executes this code if there is no time left of the session portion.

                        this.cancel();

                        // Requires confirmation to proceed
                        gameMgr.getData().set("confirmation.required", true);
                        gameMgr.save();

                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {

                            // Sends confirmation prompt to all online admins.
                            if (onlinePlayers.hasPermission("petlife.admin")) {

                                onlinePlayers.sendMessage("The session requires confirmation before it can proceed.");

                            }

                        }

                        // Waits for the session progression to be confirmed.
                        new BukkitRunnable() {
                            @Override
                            public void run() {

                                if (gameMgr.getData().getInt("confirmation.value") == 1) {
                                    // Executes this code if the session progression has been confirmed.

                                    this.cancel();

                                    gameMgr.getData().set("confirmation.value", 0);
                                    gameMgr.getData().set("confirmation.required", false);
                                    gameMgr.save();

                                    if (onComplete != null) {
                                        onComplete.run();
                                    }

                                    sessionAction(actionID);

                                }

                            }
                        }.runTaskTimer(PetLife.Instance, 0L, 20L);

                    }

                }

            }
        }.runTaskTimer(PetLife.Instance,1200L, 1200L);

    }

    public void sessionAction(int id) {

        switch (id) {

            case 1:
                // Do this after first half of the session
                gameMgr.getData().set("session-information.first-half-progress", -1);
                gameMgr.getData().set("break-active", true);
                gameMgr.save();

                break;
            case 2:
                // Do this when the break ends
                gameMgr.getData().set("session-information.break-progress", -1);
                gameMgr.getData().set("break-active", false);
                gameMgr.save();
                break;
            case 3:
                // Do this when the second half of the session ends (session should be over)
                gameMgr.getData().set("session-information.second-half-progress", -1);
                gameMgr.getData().set("session-active", false);
                gameMgr.save();
                break;

        }

    }

    public void sessionProgressUpdater(int id, int time) {
        // Updates the yml file appropriately so sessions can be resumed in the event of an unsafe crash.

        switch (id) {

            case 1:
                gameMgr.getData().set("session-information.first-half-progress", time);
                gameMgr.save();
                break;

            case 2:
                gameMgr.getData().set("session-information.break-progress", time);
                gameMgr.save();
                break;

            case 3:
                gameMgr.getData().set("session-information.second-half-progress", time);
                gameMgr.save();
                break;

        }

    }

    public void updateTabList(String body, int time) {
        // Updates the tablist

        String suffix;
        // Logic to make the suffix of the message singular when necessary (when 1 minute remains)

        if (time > 60 || time <=0) {

            suffix = " minutes";

        } else {

            suffix = " minute";

        }

        for (Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {

            onlinePlayers.setPlayerListFooter(body + time/60 + suffix); // Converts time to minutes.

        }

    }

}
