package net.fyoncle.elysiumcore.mixin.compatibility;

import java.util.concurrent.ThreadLocalRandom;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fyoncle.elysiumcore.ElysiumCoreConfig;
import net.minecraft.client.particle.Particle;

/**
 * Thread-safe replacement for Particle Interactions' z-fighting fix (MC-91873).
 * Original used level.random, which crashes/errors when particles tick
 * off-thread
 */
@Mixin(Particle.class)
public abstract class ParticleInteractionsFixMixin {
    @Shadow
    protected double y;
    @Shadow
    protected boolean collidesWithWorld;
    @Shadow
    private boolean stoppedByCollision;

    @Unique
    private boolean elysiumcore$hasLanded;

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void elysiumcore$adjustYWhenLanded(CallbackInfo ci) {
        if (!ElysiumCoreConfig.config.compatibility.particleInteractionsZFightFix)
            return;
        if (this.collidesWithWorld && this.stoppedByCollision && !this.elysiumcore$hasLanded) {
            this.elysiumcore$hasLanded = true;
            this.y += ThreadLocalRandom.current().nextDouble(0.0001, 0.0003);
        }
    }
}