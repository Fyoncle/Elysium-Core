package net.fyoncle.elysiumcore.mixin.compatibility;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.fyoncle.elysiumcore.utility.other.DenseFlowersCompat;
import net.fyoncle.elysiumcore.utility.other.SnowRealMagicCompat;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

@Pseudo
@Mixin(targets = "tobinio.denseflowers.OffsetGenerator", remap = false)
public abstract class DenseFlowersOffsetGeneratorMixin {

    @ModifyReturnValue(method = "getNumberOfSurroundingFlowers", at = @At("RETURN"), require = 0)
    private static int elysiumcore$countSnowloggedFlowers(int original, BlockView world, BlockPos flowerPos) {
        int extra = 0;
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BlockPos neighbourPos = flowerPos.offset(dir);
            BlockState neighbour = world.getBlockState(neighbourPos);
            if (DenseFlowersCompat.isFlower(neighbour.getBlock())) {
                continue;
            }
            BlockState raw = SnowRealMagicCompat.unwrapSnowlogged(neighbour, world, neighbourPos);
            if (raw != neighbour && DenseFlowersCompat.isFlower(raw.getBlock())) {
                extra++;
            }
        }
        return original + extra;
    }
}