package me.A5H73Y.Parkour.Events;

import org.bukkit.entity.Player;

public class PlayerDeathEvent extends ParkourEvent {

    public PlayerDeathEvent(final Player player, final String courseName) {
        super(player, courseName);
    }

}
