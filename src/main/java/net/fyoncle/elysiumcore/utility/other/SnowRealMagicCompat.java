package net.fyoncle.elysiumcore.utility.other;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

// Fixing https://github.com/ZsoltMolnarrr/BetterCombat/issues/505
public final class SnowRealMagicCompat {

    private static final Logger LOGGER = LoggerFactory.getLogger("ElysiumCore/SnowRealMagicCompat");

    private static final Class<?> SNOW_VARIANT;
    private static final MethodHandle GET_RAW;

    private static volatile boolean invokeFailureLogged = false;

    static {
        Class<?> variant = null;
        MethodHandle getRaw = null;
        try {
            variant = Class.forName("snownee.snow.block.SnowVariant", false,
                    SnowRealMagicCompat.class.getClassLoader());
            Method method = variant.getMethod("srm$getRaw", BlockState.class, BlockView.class, BlockPos.class);
            getRaw = MethodHandles.publicLookup().unreflect(method);
        } catch (ReflectiveOperationException | LinkageError e) {
            LOGGER.warn("Snow Real Magic SnowVariant#srm$getRaw not found; Better Combat swing-through fix disabled.",
                    e);
            variant = null;
            getRaw = null;
        }
        SNOW_VARIANT = variant;
        GET_RAW = getRaw;
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
            if (t instanceof Error error && !(t instanceof LinkageError)) {
                throw error;
            }
            if (!invokeFailureLogged) {
                invokeFailureLogged = true;
                LOGGER.warn("SnowVariant#srm$getRaw threw; falling back to the original block state.", t);
            }
            return state;
        }
    }
}