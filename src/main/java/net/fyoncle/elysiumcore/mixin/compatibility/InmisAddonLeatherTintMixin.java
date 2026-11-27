package net.fyoncle.elysiumcore.mixin.compatibility;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import draylar.inmis.client.TrinketBackpackRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

@Mixin(value = TrinketBackpackRenderer.class, priority = 500)
public abstract class InmisAddonLeatherTintMixin {

    private static final int DEFAULT_LEATHER_RGB = 9723195;

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true)
    private ItemStack elysiumcore$fixLeatherTint(ItemStack stack) {
        if (!stack.isIn(ItemTags.DYEABLE)) {
            return stack;
        }

        DyedColorComponent dyed = stack.get(DataComponentTypes.DYED_COLOR);
        int rgb = dyed != null ? dyed.rgb() : DEFAULT_LEATHER_RGB;
        int argb = 0xFF000000 | rgb;

        if (dyed != null && dyed.rgb() == argb) {
            return stack;
        }

        ItemStack copy = stack.copy();
        copy.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(argb, false));
        return copy;
    }
}