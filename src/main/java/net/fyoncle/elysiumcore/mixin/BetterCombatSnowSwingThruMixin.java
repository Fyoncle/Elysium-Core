package net.fyoncle.elysiumcore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.fyoncle.elysiumcore.utility.other.SnowRealMagicCompat;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(value = MinecraftClient.class, priority = 1500)
public abstract class BetterCombatSnowSwingThruMixin {

    @WrapOperation(
            method = "isTargetingMineableBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/world/ClientWorld;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"
            )
    )
    private BlockState elysiumcore$seeThroughSnowloggedPlants(ClientWorld world, BlockPos pos, Operation<BlockState> original) {
        BlockState state = original.call(world, pos);

        if (state.contains(SnowBlock.LAYERS) && state.get(SnowBlock.LAYERS) >= 8) {
            return state;
        }
        return SnowRealMagicCompat.unwrapSnowlogged(state, world, pos);
    }
}