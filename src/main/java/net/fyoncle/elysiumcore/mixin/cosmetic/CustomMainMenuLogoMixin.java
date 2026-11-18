package net.fyoncle.elysiumcore.mixin.cosmetic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.fyoncle.elysiumcore.utility.constants.Textures;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Mixin(LogoDrawer.class)
public class CustomMainMenuLogoMixin {
        private static final int ELYSIUMCORE$RAISE_THRESHOLD = 300;
        private static final int ELYSIUMCORE$RAISE_DIVISOR = 4;
        private static final int ELYSIUMCORE$MAX_RAISE = 25;

        @WrapOperation(method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
        public void elysiumCore$drawCustomLogo(DrawContext instance, Identifier resourceLocation,
                        int i, int j, float f,
                        float g, int k,
                        int l, int m, int n, Operation<Void> original) {
                int scaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
                int raise = MathHelper.clamp(
                                (ELYSIUMCORE$RAISE_THRESHOLD - scaledHeight) / ELYSIUMCORE$RAISE_DIVISOR,
                                0,
                                ELYSIUMCORE$MAX_RAISE);
                original.call(instance, Textures.MENU_LOGO, i, j - raise, 0f, 0f, 257, 87, 257, 87);
        }

        @WrapOperation(method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
        public void elysiumCore$eraseEditionLogo(DrawContext instance, Identifier resourceLocation,
                        int i, int j, float f,
                        float g, int k,
                        int l, int m, int n, Operation<Void> original) {
        }
}