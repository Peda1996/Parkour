package me.A5H73Y.Parkour.Events;

import org.bukkit.entity.Player;

public class PlayerLeaveCourseEvent extends ParkourEvent {

    public PlayerLeaveCourseEvent(final Player player, final String courseName) {
        super(player, courseName);
    }
}
