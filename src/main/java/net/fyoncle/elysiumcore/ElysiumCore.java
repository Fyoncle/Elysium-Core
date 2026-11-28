package net.fyoncle.elysiumcore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fyoncle.elysiumcore.custommenus.RamWarningMenu;
import net.fyoncle.elysiumcore.custommenus.WelcomeMenu;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;

public class ElysiumCore implements ClientModInitializer {

    public static final String MOD_ID = "elysiumcore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final float MIN_RECOMMENDED_RAM = 5.0f;

    private boolean ramWarningHandled = false;
    private boolean welcomeHandled = false;

    @Override
    public void onInitializeClient() {
        ElysiumCoreConfig.init();
        registerStartupScreens();
    }

    private void registerStartupScreens() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.getOverlay() != null || !(client.currentScreen instanceof TitleScreen)) {
                return;
            }

            if (!ramWarningHandled) {
                ramWarningHandled = true;
                if (shouldShowRamWarning()) {
                    welcomeHandled = true;
                    client.setScreen(new RamWarningMenu(Text.empty()));
                    return;
                }
            }

            if (!welcomeHandled) {
                welcomeHandled = true;
                if (!ElysiumCoreConfig.config.seenWelcomeScreen) {
                    client.setScreen(new WelcomeMenu(Text.empty()));
                }
            }
        });
    }

    private boolean shouldShowRamWarning() {
        return !ElysiumCoreConfig.config.seenRamWarningScreen
                && RamWarningMenu.getAllocatedRam() < MIN_RECOMMENDED_RAM;
    }
}