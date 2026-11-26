package net.fyoncle.elysiumcore.mixin.cosmetic;

import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import draylar.inmis.Inmis;
import draylar.inmis.item.BackpackItem;
import net.fyoncle.elysiumcore.ElysiumCoreConfig;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;

@Mixin(PlayerEntity.class)
public abstract class HideCapeWithBackpackMixin {

    @Inject(method = "isPartVisible", at = @At("HEAD"), cancellable = true)
    private void elysiumcore$hideCape(PlayerModelPart part, CallbackInfoReturnable<Boolean> cir) {
        if (part != PlayerModelPart.CAPE)
            return;
        if (!((Object) this instanceof AbstractClientPlayerEntity player))
            return;
        if (!ElysiumCoreConfig.config.cosmetic.hideCapeWithBackpack)
            return;
        if (!Inmis.CONFIG.trinketRendering)
            return;

        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        if (component.isEmpty())
            return;

        Map<String, TrinketInventory> group = component.get().getInventory().get("chest");
        if (group == null)
            return;

        TrinketInventory back = group.get("back");
        if (back == null)
            return;

        for (int i = 0; i < back.size(); i++) {
            if (back.getStack(i).getItem() instanceof BackpackItem) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}