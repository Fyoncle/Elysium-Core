package net.fyoncle.elysiumcore.mixin;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.google.common.collect.ImmutableMap;

import net.fabricmc.loader.api.FabricLoader;

public final class ElysiumCoreMixinPlugin implements IMixinConfigPlugin {

    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "net.fyoncle.elysiumcore.mixin.compatibility.WitherSkeletonHorseModifierMixin", () -> FabricLoader.getInstance().isModLoaded("eternalnether"),
            "net.fyoncle.elysiumcore.mixin.compatibility.SuperbSteedEntityAttributesMixin", () -> FabricLoader.getInstance().isModLoaded("superbsteeds"),
            "net.fyoncle.elysiumcore.mixin.compatibility.SnowRealMagicSodiumCullMixin", () -> FabricLoader.getInstance().isModLoaded("snowrealmagic") && FabricLoader.getInstance().isModLoaded("sodium"),
            "net.fyoncle.elysiumcore.mixin.compatibility.BetterCombatSnowSwingThruMixin", () -> FabricLoader.getInstance().isModLoaded("snowrealmagic") && FabricLoader.getInstance().isModLoaded("bettercombat"),
            "net.fyoncle.elysiumcore.mixin.compatibility.SspbRegionsUnexploredPathMixin", () -> FabricLoader.getInstance().isModLoaded("sspb") && FabricLoader.getInstance().isModLoaded("sodium") && FabricLoader.getInstance().isModLoaded("regions_unexplored"),
            "net.fyoncle.elysiumcore.mixin.compatibility.ServerEntityManagerMixin", () -> FabricLoader.getInstance().isModLoaded("guardvillagers")
    );

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
