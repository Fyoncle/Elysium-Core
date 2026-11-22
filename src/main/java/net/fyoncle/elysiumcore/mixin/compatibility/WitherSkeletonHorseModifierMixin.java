package net.fyoncle.elysiumcore.mixin.compatibility;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fuzs.eternalnether.world.entity.animal.horse.WitherSkeletonHorse;
import net.fyoncle.elysiumcore.ElysiumCoreConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

@Mixin(WitherSkeletonHorse.class)
public class WitherSkeletonHorseModifierMixin extends SkeletonHorseEntity {
    public WitherSkeletonHorseModifierMixin(EntityType<? extends SkeletonHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("RETURN"), method = "initAttributes")
    public void initAttributes(Random random, CallbackInfo ci) {
        if (!ElysiumCoreConfig.config.compatibility.witherSkeletonHorseModifier)
            return;

        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(32.0F);
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.21);
        this.getAttributeInstance(EntityAttributes.GENERIC_JUMP_STRENGTH).setBaseValue(0.8F);
    }
}