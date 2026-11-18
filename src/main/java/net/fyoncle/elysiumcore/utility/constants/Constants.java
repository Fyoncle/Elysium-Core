package net.fyoncle.elysiumcore.utility.constants;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.MinecraftVersion;

public class Constants {
    public static class Links {
        public static final String DISCORD_LINK = "https://discord.gg/WFpDr7zY8Z";
        public static final String WIKI_LINK = "https://fyoncle.github.io/Elysium-Days/gettingstarted/home/";
        public static final String RAM_GUIDE_LINK = "https://fyoncle.github.io/Elysium-Days/guides/memoryallocation/";
    }

    public static final String MOD_VERSION = FabricLoader.getInstance()
            .getModContainer("elysiumcore")
            .orElseThrow()
            .getMetadata()
            .getVersion()
            .getFriendlyString();

    public static class Core {
        public static final String CURRENT_MINECRAFT_VERSION = MinecraftVersion.CURRENT.getName();
        public static final String ELYSIUM_DAYS_WINDOW_TITLE = "Elysium Days " + MOD_VERSION + " - "
                + CURRENT_MINECRAFT_VERSION;
    }
}