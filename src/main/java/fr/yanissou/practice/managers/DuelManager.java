package fr.yanissou.practice.managers;

import fr.yanissou.practice.Practice;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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

    /**
     * Creates a custom Sword capable of blocking
     * @return A sword capable of blocking;
     */
    private static ItemStack getCustomSword() {
        // create the itemstack
        final ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);

        // update the itemmeta
        final ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        stack.setItemMeta(meta);

        // edit the "consumable" component
        final net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(stack);
        final Consumable consumable = new Consumable(Float.MAX_VALUE, ItemUseAnimation.d, Holder.a(SoundEffects.xd), false, List.of());

        // Set the data component
        // Translates to DatacomponentMap.builder().set(DataComponents.CONSUMABLE, consumable).build();
        nmsItemStack.a(DataComponentPatch.a().a(DataComponents.x, consumable).a());

        // return the itemstack with the data component
        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }
}
