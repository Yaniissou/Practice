package fr.yanissou.practice.listeners;

import fr.yanissou.practice.Practice;
import fr.yanissou.practice.managers.DuelManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        DuelManager duelManager = Practice.getInstance().getDuelManager();
        Bukkit.getScheduler().runTaskLater(Practice.getInstance(), () -> {
            event.getEntity().spigot().respawn();
            duelManager.resetInventory(event.getEntity());
        }, 1L);

        if (duelManager.hasDuel(event.getEntity().getUniqueId())) {
            duelManager.removeDuel(event.getEntity().getUniqueId());
        }

        // Reset inventory of the killer
        if (event.getEntity().getKiller() != null) {
            final UUID killerUUID = event.getEntity().getKiller().getUniqueId();
            final Player killerPlayer = Bukkit.getPlayer(killerUUID);
            if (killerPlayer != null) {
                duelManager.resetInventory(killerPlayer);
                killerPlayer.teleport(new Location(killerPlayer.getWorld(), 0, -60, 0));
            }
        }

        // Reset inventory of the player who died



    }
}
