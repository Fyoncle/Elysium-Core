package net.fyoncle.elysiumcore.mixin.compatibility;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.fyoncle.elysiumcore.utility.other.RegionsUnexploredPaths;
import net.minecraft.block.DirtPathBlock;

// Fixing an issue on SSPB where Silt and Peat dirt paths from Regions Unexplored doesn't get shadow when the config option "only_affect_path_blocks" is set to true.
@Mixin(
        targets = "net.caffeinemc.mods.sodium.client.model.light.smooth.SmoothLightPipeline",
        priority = 1500,
        remap = false
)
public abstract class SspbRegionsUnexploredPathMixin {

    @Definition(id = "DirtPathBlock", type = DirtPathBlock.class)
    @Expression("? instanceof DirtPathBlock")
    @WrapOperation(
            method = "sspb$getModifiedAOWeight",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean elysiumcore$treatRuPathsAsDirtPaths(Object block, Operation<Boolean> original) {
        return original.call(block) || RegionsUnexploredPaths.isRuPath(block);
    }
}