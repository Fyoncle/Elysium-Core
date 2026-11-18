package net.fyoncle.elysiumcore.mixin.compatibility;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

// Fixing https://github.com/Snownee/SnowRealMagic/issues/496
@Mixin(targets = "snownee.snow.client.FabricRendererRenderAPI", remap = false)
public abstract class SnowRealMagicSodiumCullMixin {

    @Shadow
    @Final
    private BlockRenderView level;
    @Shadow
    @Final
    private BlockPos pos;
    @Shadow
    @Final
    private BlockState selfState;

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/api/renderer/v1/render/RenderContext;pushTransform(Lnet/fabricmc/fabric/api/renderer/v1/render/RenderContext$QuadTransform;)V"))
    private void elysiumcore$cullCamoWithOwnState(
            RenderContext context,
            RenderContext.QuadTransform original,
            Operation<Void> operation,
            @Local(argsOnly = true) BlockState renderedState) {
        if (renderedState == this.selfState || renderedState.getBlock() instanceof SnowBlock) {
            operation.call(context, original);
            return;
        }

        final BlockRenderView world = this.level;
        final BlockPos blockPos = this.pos;
        final BlockPos.Mutable neighbour = new BlockPos.Mutable();
        final int[] computed = { 0 };
        final int[] draw = { 0 };

        operation.call(context, (RenderContext.QuadTransform) quad -> {
            if (!original.transform(quad)) {
                return false;
            }

            Direction face = quad.cullFace();
            if (face == null) {
                return true;
            }

            int bit = 1 << face.getId();
            if ((computed[0] & bit) == 0) {
                computed[0] |= bit;
                if (Block.shouldDrawSide(renderedState, world, blockPos, face, neighbour.set(blockPos, face))) {
                    draw[0] |= bit;
                }
            }
            if ((draw[0] & bit) == 0) {
                return false;
            }

            Direction nominal = quad.nominalFace();
            quad.cullFace(null);
            quad.nominalFace(nominal);
            return true;
        });
    }
}