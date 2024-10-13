package net.phileasfogg3.petlife.Managers;

import net.nexia.nexiaapi.Config;
import net.phileasfogg3.petlife.PetLife;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SessionManager {

    private Config gameMgr;

    public SessionManager(Config gameMgr) {this.gameMgr = gameMgr;}

    public void sessionTimeManager() {

        int sessionLength = gameMgr.getData().getInt("session-timings.session-length");
        boolean sessionBreak = gameMgr.getData().getBoolean("session-timings.session-break");
        int sessionBreakLength = gameMgr.getData().getInt("session-timings.session-break-length");

        // Update tablist every minute
        // Format like this :
        // Time To Break: <>
        // Time Left of Break: <>
        // Time to End of Session: <>
        // Wait for admin confirm command before moving on.

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

            runnable(new int[]{sessionHalf}, "Time left until the break: ", 1, () ->

                    runnable(new int[]{finalSessionBreakLength}, "Time left of the break: ", 2, () ->

                            runnable(new int[]{finalSessionHalf}, "Time left of the session: ", 3, null)

                    )
            );

        } else {

            // If there is no break

        }

    }

    public boolean runnable(final int time[], String tabListMessage, int actionID, Runnable onComplete) {

        updateTabList(tabListMessage, time[0]);

        new BukkitRunnable() {
            @Override
            public void run() {

                if (time[0] > 0) {

                    time[0] -= 60;

                    updateTabList(tabListMessage, time[0]);

                    if (time[0] <= 0) {

                        this.cancel();

                        if (onComplete != null) {
                            onComplete.run();
                        }

                        sessionAction(actionID);

                    }

                }

            }
        }.runTaskTimer(PetLife.Instance,1200L, 1200L);

        return true;
    }

    public void sessionAction(int id) {

        if (id == 1) {

            // Do this after first half of the session


        }

        if (id == 2) {

            // Do this when the break ends

        }

        if (id == 3) {

            // Do this when the second half of the session ends (session should be over)

        }

    }

    public void updateTabList(String s, int time) {

        for (Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {

            onlinePlayers.setPlayerListFooter(s + time/60); // Converts time to minutes.

        }

    }

}
