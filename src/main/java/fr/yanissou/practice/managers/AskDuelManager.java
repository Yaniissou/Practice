package fr.yanissou.practice.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AskDuelManager {

    private final Map<UUID, UUID> askDuel;

    public AskDuelManager() {
        this.askDuel = new HashMap<>();
    }

    public void addAskDuel(UUID sender, UUID target) {
        this.askDuel.put(sender, target);
    }

    public void removeAskDuel(UUID sender) {
        this.askDuel.remove(sender);
    }

    public UUID getTarget(UUID sender) {
        return this.askDuel.get(sender);
    }

    public boolean hasAskDuel(UUID sender) {
        return this.askDuel.containsKey(sender);
    }

    public boolean hasAskDuel(UUID sender, UUID target) {
        return this.askDuel.containsKey(sender) && this.askDuel.get(sender).equals(target);
    }



}
