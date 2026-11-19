package net.fyoncle.elysiumcore.utility.other;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

public final class DenseFlowersCompat {

    private static final Logger LOGGER = LoggerFactory.getLogger("ElysiumCore/DenseFlowersCompat");
    private static final MethodHandle GET_FLOWER_OFFSETS;

    static {
        MethodHandle offsets = null;
        try {
            Class<?> cls = Class.forName("tobinio.denseflowers.OffsetGenerator", false,
                    DenseFlowersCompat.class.getClassLoader());
            for (Method m : cls.getMethods()) {
                if (Modifier.isStatic(m.getModifiers()) && m.getName().equals("getFlowerOffsets")
                        && m.getParameterCount() == 3) {
                    offsets = MethodHandles.publicLookup().unreflect(m);
                }
            }
        } catch (Throwable t) {
            LOGGER.warn("Dense Flowers OffsetGenerator not usable.", t);
        }
        if (offsets == null) {
            LOGGER.warn("Dense Flowers getFlowerOffsets not found; snowlogged flower density disabled.");
        }
        GET_FLOWER_OFFSETS = offsets;
    }

    private DenseFlowersCompat() {
    }

    /** Same rule Dense Flowers uses in this version: FlowerBlock only. */
    public static boolean isFlower(Block block) {
        return block instanceof FlowerBlock;
    }

    @SuppressWarnings("unchecked")
    public static List<Vec3d> getFlowerOffsets(BlockState state, BlockView world, BlockPos pos) {
        if (GET_FLOWER_OFFSETS == null)
            return Collections.emptyList();
        try {
            return (List<Vec3d>) GET_FLOWER_OFFSETS.invoke(state, world, pos);
        } catch (Throwable t) {
            return Collections.emptyList();
        }
    }
}