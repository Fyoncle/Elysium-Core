package net.fyoncle.elysiumcore.mixin.compatibility;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fyoncle.elysiumcore.utility.other.DenseFlowersCompat;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

@Mixin(targets = "snownee.snow.client.FabricRendererRenderAPI", remap = false)
public abstract class SnowRealMagicDenseFlowersMixin {

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/api/renderer/v1/model/FabricBakedModel;emitBlockQuads(Lnet/minecraft/class_1920;Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;Ljava/util/function/Supplier;Lnet/fabricmc/fabric/api/renderer/v1/render/RenderContext;)V"), require = 0)
    private void elysiumcore$denseSnowloggedFlowers(
            FabricBakedModel model, BlockRenderView view, BlockState state, BlockPos pos,
            Supplier<Random> random, RenderContext context, Operation<Void> original) {
        original.call(model, view, state, pos, random, context);

        if (!DenseFlowersCompat.isFlower(state.getBlock())) {
            return;
        }
        Vec3d base = state.getModelOffset(view, pos);
        for (Vec3d extra : DenseFlowersCompat.getFlowerOffsets(state, view, pos)) {
            final float dx = (float) (extra.x - base.x);
            final float dy = (float) (extra.y - base.y);
            final float dz = (float) (extra.z - base.z);
            context.pushTransform(quad -> {
                for (int i = 0; i < 4; i++) {
                    quad.pos(i, quad.x(i) + dx, quad.y(i) + dy, quad.z(i) + dz);
                }
                return true;
            });
            try {
                original.call(model, view, state, pos, random, context);
            } finally {
                context.popTransform();
            }
        }
    }
}