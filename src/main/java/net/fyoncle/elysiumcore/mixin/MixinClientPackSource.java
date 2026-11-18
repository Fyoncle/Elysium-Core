package net.fyoncle.elysiumcore.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fyoncle.elysiumcore.resource.ElysiumCorePackProvider;
import net.minecraft.client.resource.DefaultClientResourcePackProvider;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.VanillaResourcePackProvider;

@Mixin(value = VanillaResourcePackProvider.class, priority = 1100)
public class MixinClientPackSource {
    @Unique
    private ElysiumCorePackProvider elysiumCorePackProvider;

    public MixinClientPackSource() {
        super();
    }

    @Inject(
            method = {"register"},
            at = {@At("RETURN")}
    )
    private void paxi_loadPaxiPacksClientFabric(Consumer<ResourcePackProfile> consumer, CallbackInfo callback) {
        if (this.thisIsClientPackProvider(this)) {
            if (this.elysiumCorePackProvider == null) {
                this.elysiumCorePackProvider = new ElysiumCorePackProvider();
            }

            this.elysiumCorePackProvider.register(consumer);
        }

    }

    @Unique
    private boolean thisIsClientPackProvider(Object obj) {
        return obj instanceof DefaultClientResourcePackProvider;
    }
}
