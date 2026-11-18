package net.fyoncle.elysiumcore.mixin.cosmetic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fyoncle.elysiumcore.utility.constants.Textures;
import net.minecraft.client.util.Icons;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;

@Mixin(Icons.class)
public class CustomIconMixin {

    @Unique
    private static InputSupplier<InputStream> loadIcon(String path) {
        try (InputStream stream = CustomIconMixin.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null)
                return null;

            byte[] data = IOUtils.toByteArray(stream);
            return () -> new ByteArrayInputStream(data);
        } catch (IOException e) {
            return null;
        }
    }

    @Inject(method = "getIcons", at = @At("HEAD"), cancellable = true)
    private void getIcons(ResourcePack resourcePack,
            CallbackInfoReturnable<List<InputSupplier<InputStream>>> cir) {
        InputSupplier<InputStream> icon = loadIcon(Textures.APP_ICON);

        if (icon != null) {
            cir.setReturnValue(List.of(icon));
        }
    }

    @Inject(method = "getMacIcon", at = @At("HEAD"), cancellable = true)
    private void getMacIcon(ResourcePack resourcePack,
            CallbackInfoReturnable<InputSupplier<InputStream>> cir) {
        InputSupplier<InputStream> icon = loadIcon(Textures.APP_ICON_MAC);

        if (icon != null) {
            cir.setReturnValue(icon);
        }
    }
}