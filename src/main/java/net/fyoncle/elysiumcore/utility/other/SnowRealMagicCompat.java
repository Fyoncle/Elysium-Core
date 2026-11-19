package net.fyoncle.elysiumcore.utility.other;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public final class SnowRealMagicCompat {

    private static final Logger LOGGER = LoggerFactory.getLogger("ElysiumCore/SnowRealMagicCompat");

    private static final Class<?> SNOW_VARIANT;
    private static final MethodHandle GET_RAW;
    private static final MethodHandle GET_LAYERS;

    static {
        Class<?> variant = null;
        MethodHandle getRaw = null;
        MethodHandle getLayers = null;
        try {
            variant = Class.forName("snownee.snow.block.SnowVariant", false,
                    SnowRealMagicCompat.class.getClassLoader());
            Method method = variant.getMethod("srm$getRaw", BlockState.class, BlockView.class, BlockPos.class);
            getRaw = MethodHandles.publicLookup().unreflect(method);
        } catch (Throwable t) {
            LOGGER.warn("Snow Real Magic SnowVariant#srm$getRaw not found; Better Combat swing-through fix disabled.",
                    t);
            variant = null;
            getRaw = null;
        }
        if (variant != null) {
            try {
                Method method = variant.getMethod("srm$layers", BlockState.class, BlockView.class, BlockPos.class);
                getLayers = MethodHandles.publicLookup().unreflect(method);
            } catch (Throwable t) {
                LOGGER.warn(
                        "Snow Real Magic SnowVariant#srm$layers not found; full-snow check falls back to vanilla layers only.",
                        t);
            }
        }
        SNOW_VARIANT = variant;
        GET_RAW = getRaw;
        GET_LAYERS = getLayers;
    }

    private SnowRealMagicCompat() {
    }

    public static BlockState unwrapSnowlogged(BlockState state, BlockView world, BlockPos pos) {
        if (GET_RAW == null || !SNOW_VARIANT.isInstance(state.getBlock())) {
            return state;
        }
        try {
            BlockState raw = (BlockState) GET_RAW.invoke(state.getBlock(), state, world, pos);
            return raw == null || raw.isAir() ? state : raw;
        } catch (Throwable t) {
            return state;
        }
    }

    public static int getLayers(BlockState state, BlockView world, BlockPos pos) {
        if (GET_LAYERS == null || !SNOW_VARIANT.isInstance(state.getBlock())) {
            return 0;
        }
        try {
            return (int) GET_LAYERS.invoke(state.getBlock(), state, world, pos);
        } catch (Throwable t) {
            return 0;
        }
    }
}