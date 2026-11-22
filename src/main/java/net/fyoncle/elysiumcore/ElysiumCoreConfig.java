package net.fyoncle.elysiumcore;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.minecraft.util.Identifier;

public class ElysiumCoreConfig extends Config {
    public static ElysiumCoreConfig config = ConfigApiJava.registerAndLoadConfig(ElysiumCoreConfig::new,
            RegisterType.CLIENT);

    public Cosmetic cosmetic = new Cosmetic();
    public Compatibility compatibility = new Compatibility();

    public ElysiumCoreConfig() {
        super(Identifier.of(ElysiumCore.MOD_ID, "config"));
    }

    public boolean disableRamWarningScreen = false;
    public boolean registerElysiumCorePack = true;

    public static class Cosmetic extends ConfigSection {
        public boolean doCustomTaskbarIcon = true;
        public boolean doCustomMainMenuLogo = true;
        public boolean doCustomMainMenuButtons = true;
        public boolean doCustomWindowTitle = true;
    }

    public static class Compatibility extends ConfigSection {
        public boolean particleInteractionsZFightFix = true;
        public boolean superbSteedAttributesFix = true;
        public boolean witherSkeletonHorseModifier = true;
    }

    public static void init() {
    }
}