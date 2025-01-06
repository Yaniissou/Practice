package fr.yanissou.practice.managers;

import fr.yanissou.practice.Practice;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class DuelManager {
    private static final ItemStack DIAMOND_SWORD = getCustomSword();

    private final Map<UUID, UUID> duel;

    public DuelManager() {
        this.duel = new HashMap<>();
    }

    public void addDuel(UUID sender, UUID target) {
        this.duel.put(sender, target);
        setupDuel(sender, target);
    }

    public void removeDuel(UUID sender) {
        this.duel.remove(sender);

    }

    public UUID getTarget(UUID sender) {
        return this.duel.get(sender);
    }

    public boolean hasDuel(UUID sender) {
        return this.duel.containsKey(sender);
    }

    private void setupDuel(UUID sender, UUID target) {
        final Player senderPlayer = Bukkit.getPlayer(sender);
        final Player targetPlayer = Bukkit.getPlayer(target);

        if (senderPlayer == null || targetPlayer == null)
            return;

        final Practice practice = Practice.getInstance();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getUniqueId().equals(sender) || onlinePlayer.getUniqueId().equals(target))
                continue;

            senderPlayer.hidePlayer(practice, onlinePlayer);
            targetPlayer.hidePlayer(practice, onlinePlayer);
        }
        senderPlayer.teleport(new Location(senderPlayer.getWorld(), 53, -60, 1, 90, 0));
        targetPlayer.teleport(new Location(targetPlayer.getWorld(), 20, -60, 1, -90, 0));
        setupInventory(senderPlayer);
        setupInventory(targetPlayer);

    }

    private void setupInventory(Player player) {
        final PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setArmorContents(null);
        inventory.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        inventory.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        inventory.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        inventory.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        inventory.addItem(DIAMOND_SWORD);
        inventory.addItem(new ItemStack(Material.GOLDEN_APPLE, 16));
        inventory.addItem(new ItemStack(Material.BOW));
        inventory.addItem(new ItemStack(Material.ARROW, 8));
        inventory.addItem(new ItemStack(Material.COOKED_BEEF, 64));
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
    }

    public void resetInventory(Player player) {
        final PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setArmorContents(null);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
    }

    private static ItemStack getCustomSword() {
        // create the itemstack
        final ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);

        // set the right custom meta
        final ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());
        meta.setUnbreakable(true);

        // edit the "consumable" component
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey("minecraft", "consumable"), PersistentDataType.BYTE, (byte) 1);  // 1 signifie que c'est consumable
        container.set(new NamespacedKey("minecraft", "consume_seconds"), PersistentDataType.INTEGER, 2000000);
        container.set(new NamespacedKey("minecraft", "animation"), PersistentDataType.STRING, "block");
        container.set(new NamespacedKey("minecraft", "sound_id"), PersistentDataType.STRING, "");
        container.set(new NamespacedKey("minecraft", "has_consume_particles"), PersistentDataType.BYTE, (byte) 0);  // Pas de particules

        // update the itemmeta
        stack.setItemMeta(meta);
        return stack;
    }
}
