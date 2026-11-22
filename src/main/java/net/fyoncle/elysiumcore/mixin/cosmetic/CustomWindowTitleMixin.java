package net.fyoncle.elysiumcore.mixin.cosmetic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fyoncle.elysiumcore.ElysiumCoreConfig;
import net.fyoncle.elysiumcore.utility.constants.Constants;
import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public class CustomWindowTitleMixin {
    @Inject(at = @At("RETURN"), method = "getWindowTitle", cancellable = true)
    private void getWindowTitle(CallbackInfoReturnable<String> cir) {
        if (!ElysiumCoreConfig.config.cosmetic.doCustomWindowTitle)
            return;
        cir.setReturnValue(Constants.Core.ELYSIUM_DAYS_WINDOW_TITLE);
    }
}
