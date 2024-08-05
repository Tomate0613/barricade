package net.modgarden.barricade.platform;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Collection;

public interface BarricadePlatformHelper {

    /**
     * Gets the current platform
     *
     * @return An enum value representing the current platform.
     */
    Platform getPlatform();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    <T> Collection<T> fixSeamsOnNeoForge(Collection<T> collection, Object textureAtlasSprite);
}