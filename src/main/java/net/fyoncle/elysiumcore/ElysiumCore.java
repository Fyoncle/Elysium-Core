package net.fyoncle.elysiumcore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fyoncle.elysiumcore.custommenus.RamWarningMenu;
import net.fyoncle.elysiumcore.utility.other.Ram;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;

public class ElysiumCore implements ClientModInitializer {

    public static final String MOD_ID = "elysiumcore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final float MIN_RECOMMENDED_RAM = 4.5f;

    private boolean ramWarningShown = false;

    @Override
    public void onInitializeClient() {
        ElysiumCoreConfig.init();
        registerRamWarning();
    }

    private void registerRamWarning() {
        float allocatedRam = Ram.getAllocatedRam();
        if (ElysiumCoreConfig.config.disableRamWarningScreen || allocatedRam >= MIN_RECOMMENDED_RAM) {
            return;
        }

        String formattedRam = String.format("%.1f", allocatedRam);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!ramWarningShown && client.currentScreen instanceof TitleScreen) {
                ramWarningShown = true;
                client.setScreen(new RamWarningMenu(Text.empty(), formattedRam));
            }
        });
    }
}