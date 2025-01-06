package fr.yanissou.practice.listeners;

import fr.yanissou.practice.Practice;
import fr.yanissou.practice.managers.DuelManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.getDrops().clear();
        Bukkit.getScheduler().runTaskLaterAsynchronously(Practice.getInstance(), () -> event.getEntity().spigot().respawn(), 1L);
        DuelManager duelManager = Practice.getInstance().getDuelManager();
        if (duelManager.hasDuel(event.getEntity().getUniqueId())) {
            duelManager.removeDuel(event.getEntity().getUniqueId());
        }

        // Reset inventory of the killer
        if (event.getEntity().getKiller() != null) {
            final UUID killerUUID = event.getEntity().getKiller().getUniqueId();
            final Player killerPlayer = Bukkit.getPlayer(killerUUID);
            if (killerPlayer != null) {
                duelManager.resetInventory(killerPlayer);
            }
        }

        // Reset inventory of the player who died
        duelManager.resetInventory(event.getEntity());


    }
}
