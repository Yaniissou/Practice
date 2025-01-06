package fr.yanissou.practice.listeners;

import fr.yanissou.practice.Practice;
import fr.yanissou.practice.managers.DuelManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
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
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.getUniqueId().equals(event.getEntity().getUniqueId()))
                    return;
                player.showPlayer(Practice.getInstance(), event.getEntity());
            });
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
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getUniqueId().equals(killerUUID))
                        continue;
                    killerPlayer.showPlayer(Practice.getInstance(), onlinePlayer);
                }
            }
        }
    }

    /**
     * Handles the player joining to give him the right attribute modifiers
     * @param event the event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final AttributeInstance attributeInstance = Objects.requireNonNull(player.getAttribute(Attribute.ATTACK_SPEED),
                        "AttributeInstance of type 'ATTACK_SPEED' shouldn't be null !");

        // does the player already have the attributes ?
        if (attributeInstance.getBaseValue() == 20)
            return;

        // set the right value
        attributeInstance.setBaseValue(20);
    }
}
