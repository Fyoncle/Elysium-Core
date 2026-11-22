package net.fyoncle.elysiumcore.mixin.cosmetic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.fyoncle.elysiumcore.ElysiumCoreConfig;
import net.fyoncle.elysiumcore.utility.constants.Textures;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;

@Mixin(LogoDrawer.class)
public class CustomMainMenuLogoMixin {

        @Unique
        private static final int ELYSIUMCORE$LOGO_W = 257;
        @Unique
        private static final int ELYSIUMCORE$LOGO_H = 87;
        @Unique
        private static final int ELYSIUMCORE$GAP = 6;
        @Unique
        private static final int ELYSIUMCORE$TOP_MARGIN = 4;

        @Unique
        private static int elysiumcore$singleplayerY(MinecraftClient client) {
                Screen screen = client.currentScreen;
                if (screen != null) {
                        for (Element child : screen.children()) {
                                if (child instanceof ButtonWidget button
                                                && button.getMessage().getContent() instanceof TranslatableTextContent c
                                                && c.getKey().equals("menu.singleplayer")) {
                                        return button.getY();
                                }
                        }
                }
                return client.getWindow().getScaledHeight() / 4 + 48;
        }

        @WrapOperation(method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
        private void elysiumcore$drawCustomLogo(DrawContext instance, Identifier resourceLocation,
                        int i, int j, float f, float g, int k, int l, int m, int n, Operation<Void> original) {
                if (!ElysiumCoreConfig.config.cosmetic.doCustomMainMenuLogo) {
                        original.call(instance, resourceLocation, i, j, f, g, k, l, m, n);
                        return;
                }

                int buttonTop = elysiumcore$singleplayerY(MinecraftClient.getInstance());

                int maxH = buttonTop - ELYSIUMCORE$GAP - ELYSIUMCORE$TOP_MARGIN;
                float scale = Math.max(0.3f, Math.min(1f, maxH / (float) ELYSIUMCORE$LOGO_H));
                int w = Math.round(ELYSIUMCORE$LOGO_W * scale);
                int h = Math.round(ELYSIUMCORE$LOGO_H * scale);

                int x = i + (ELYSIUMCORE$LOGO_W - w) / 2;
                int y = Math.min(j, buttonTop - ELYSIUMCORE$GAP - h);
                y = Math.max(y, ELYSIUMCORE$TOP_MARGIN);

                original.call(instance, Textures.MENU_LOGO, x, y, 0f, 0f, w, h, w, h);
        }

        @WrapOperation(method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
        private void elysiumcore$eraseEditionLogo(DrawContext instance, Identifier resourceLocation,
                        int i, int j, float f, float g, int k, int l, int m, int n, Operation<Void> original) {
                if (!ElysiumCoreConfig.config.cosmetic.doCustomMainMenuLogo) {
                        original.call(instance, resourceLocation, i, j, f, g, k, l, m, n);
                }
        }
}