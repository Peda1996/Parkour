package me.A5H73Y.Parkour.Other;

import java.io.Serializable;
import java.util.*;

import me.A5H73Y.Parkour.Parkour;
import me.A5H73Y.Parkour.Utilities.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class ParkourKit implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final List<String> validActions =
            Arrays.asList("death", "finish", "climb", "launch", "speed", "repulse", "norun", "nopotion", "bounce");

    private static Map<String, ParkourKit> loaded = new HashMap<>();

    // object attributes
    private String name;
    private List<Material> materials = new ArrayList<>();

    /**
     * ParkourKit
     * Each ParkourKit set has a unique name to refer to it, apart from the default set.
     * The format being ParkourKit.(name).MATERIAL.Action = "action"
     * If the Material provided is invalid, then it won't be added to our list of materials
     * Also if the Action provided is invalid, then it won't be added to our list of materials.
     * This is so ParkourKit remain safe while in use on a course.
     *
     * @param name
     */
    private ParkourKit(String name) {
        this.name = name;

        Set<String> rawMaterials = Parkour.getParkourConfig().getParkourKitData()
                .getConfigurationSection("ParkourKit." + name).getKeys(false);

        for (String rawMaterial : rawMaterials) {
            Material material = Material.getMaterial(rawMaterial);

            if (material == null) {
                Utils.log("Material " + rawMaterial + " is invalid.", 1);
                continue;
            }

            String action = Parkour.getParkourConfig().getParkourKitData()
                    .getString("ParkourKit." + name + "." + material.name() + ".Action").toLowerCase();

            if (!validActions.contains(action)) {
                Utils.log("Action " + action + " is invalid.", 1);
                continue;
            }

            // we only add the material once we know it's valid
            materials.add(material);
        }
    }

    /**
     * Get a list of all the action possibilities
     * @return List of actions
     */
    public static List<String> getValidActions() {
        return validActions;
    }

    /**
     * Get the materials that this ParkourKit is made up of
     * @return List<Material>
     */
    public List<Material> getMaterials() {
        return materials;
    }

    /**
     * Get the corresponding action for the material
     * @param material
     * @return corresponding action for material
     */
    public String getAction(Material material) {
        if (!materials.contains(material)) {
            return null;
        }

        return Parkour.getParkourConfig().getParkourKitData()
                .getString("ParkourKit." + name + "." + material.name() + ".Action").toLowerCase();
    }

    public Double getStrength(Material material) {
        if (!materials.contains(material)) {
            return 0.0;
        }

        return Parkour.getParkourConfig().getParkourKitData()
                .getDouble("ParkourKit." + name + "." + material.name() + ".Strength", 1);
    }

    public Integer getDuration(Material material) {
        if (!materials.contains(material)) {
            return null;
        }

        return Parkour.getParkourConfig().getParkourKitData()
                .getInt("ParkourKit." + name + "." + material.name() + ".Duration", 200);
    }

    /**
     * New point of accessing ParkourKit
     * If it's already loaded, then just return that, otherwise create the set and load it.
     * Hopefully this is better for performance
     * @param name
     * @return ParkourKit
     */
    public static ParkourKit getParkourKit(String name) {
        
    	name = name.toLowerCase();
    	
    	if (loaded.containsKey(name)) {
            return loaded.get(name);
        }

        if (!doesParkourKitExist(name)) {
            return null;
        }

        ParkourKit kit = new ParkourKit(name);
        loaded.put(name, kit);
        return kit;
    }

    public static Set<String> getAllParkourKits() {
        return Parkour.getParkourConfig().getParkourKitData()
                .getConfigurationSection("ParkourKit").getKeys(false);
    }

    public static boolean doesParkourKitExist(String name) {
        return getAllParkourKits().contains(name.toLowerCase());
    }

    public static void deleteKit(String argument) {
        clearMemory(argument);
        Parkour.getParkourConfig().getParkourKitData().set("ParkourKit." + argument, null);
        Parkour.getParkourConfig().saveParkourKit();
    }

    /**
     * The need to pass config as the plugin has not initialised fully at this stage
     * @param config
     * @param name
     */
    public static void createStandardKit(FileConfiguration config, String name) {
		config.set("ParkourKit." + name + ".SMOOTH_BRICK.Action", "death");
        config.set("ParkourKit." + name + ".HUGE_MUSHROOM_2.Action", "finish");
        config.set("ParkourKit." + name + ".BRICK.Action", "climb");
        config.set("ParkourKit." + name + ".BRICK.Strength", 0.4);
        config.set("ParkourKit." + name + ".EMERALD_BLOCK.Action", "launch");
        config.set("ParkourKit." + name + ".EMERALD_BLOCK.Strength", 1.2);
        config.set("ParkourKit." + name + ".MOSSY_COBBLESTONE.Action", "bounce");
        config.set("ParkourKit." + name + ".MOSSY_COBBLESTONE.Strength", (double) 5);
        config.set("ParkourKit." + name + ".MOSSY_COBBLESTONE.Duration", 200);
        config.set("ParkourKit." + name + ".OBSIDIAN.Action", "speed");
        config.set("ParkourKit." + name + ".OBSIDIAN.Strength", (double) 5);
        config.set("ParkourKit." + name + ".OBSIDIAN.Duration", 200);
        config.set("ParkourKit." + name + ".ENDER_STONE.Action", "repulse");
        config.set("ParkourKit." + name + ".ENDER_STONE.Strength", 0.4);
        config.set("ParkourKit." + name + ".GOLD_BLOCK.Action", "norun");
        config.set("ParkourKit." + name + ".HUGE_MUSHROOM_1.Action", "nopotion");
    }

    public static void clearMemory(String kitName) {
        loaded.remove(kitName);
    }
}
