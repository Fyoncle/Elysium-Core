package net.fyoncle.elysiumcore.mixin.compatibility;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.world.ServerWorld;

// Fixing https://github.com/mrsterner/GuardVillagers/issues/171
@Pseudo
@Mixin(targets = "dev.sterner.guardvillagers.GuardVillagers")
public abstract class GuardVillagersEntityLoadSpawnMixin {

    @WrapOperation(method = "lambda$onInitialize$2", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_3218;method_30771(Lnet/minecraft/class_1297;)V", remap = false), remap = false, require = 0)
    private static void elysiumcore$deferGuardSpawn(ServerWorld world, Entity guard, Operation<Void> original) {
        MinecraftServer server = world.getServer();
        server.send(new ServerTask(server.getTicks(), () -> {
            if (!guard.isRemoved()) {
                original.call(world, guard);
            }
        }));
    }
}