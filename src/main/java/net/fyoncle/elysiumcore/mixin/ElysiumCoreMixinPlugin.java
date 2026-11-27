package net.fyoncle.elysiumcore.mixin;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.loader.api.FabricLoader;

public final class ElysiumCoreMixinPlugin implements IMixinConfigPlugin {

    private static final Map<String, List<String>> REQUIRED_MODS = Map.of(
            "compatibility.WitherSkeletonHorseModifierMixin", List.of("eternalnether"),
            "compatibility.SuperbSteedEntityAttributesMixin", List.of("superbsteeds"),
            "compatibility.ParticleInteractionsFixMixin", List.of("eg_particle_interactions"),
            "cosmetic.HideCapeWithBackpackMixin", List.of("inmis", "trinkets"),
            "compatibility.InmisAddonLeatherTintMixin", List.of("inmis", "trinkets", "inmisaddon"));

    private String mixinPackagePrefix = "";

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackagePrefix = mixinPackage.endsWith(".") ? mixinPackage : mixinPackage + ".";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!mixinClassName.startsWith(mixinPackagePrefix)) {
            return true;
        }
        List<String> requiredMods = REQUIRED_MODS.get(mixinClassName.substring(mixinPackagePrefix.length()));
        if (requiredMods == null) {
            return true;
        }
        FabricLoader loader = FabricLoader.getInstance();
        return requiredMods.stream().allMatch(loader::isModLoaded);
    }

    @Override
    public String getRefMapperConfig() {
        return null;
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