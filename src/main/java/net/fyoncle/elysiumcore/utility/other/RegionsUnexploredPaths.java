package net.fyoncle.elysiumcore.utility.other;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

// Fixing an issue on SSPB where Silt and Peat dirt paths from Regions Unexplored doesn't get shadow when the config option "only_affect_path_blocks" is set to true.
public final class RegionsUnexploredPaths {

    private static final String NAMESPACE = "regions_unexplored";
    private static final String[] PATH_IDS = { "silt_dirt_path", "peat_dirt_path" };

    private static volatile Block[] paths;

    private RegionsUnexploredPaths() {
    }

    public static boolean isRuPath(Object obj) {
        if (!(obj instanceof Block block)) {
            return false;
        }
        Block[] resolved = paths;
        if (resolved == null) {
            resolved = resolve();
            paths = resolved;
        }
        for (Block path : resolved) {
            if (path == block) {
                return true;
            }
        }
        return false;
    }

    private static Block[] resolve() {
        List<Block> found = new ArrayList<>(PATH_IDS.length);
        for (String id : PATH_IDS) {
            Identifier identifier = Identifier.of(NAMESPACE, id);
            if (Registries.BLOCK.containsId(identifier)) {
                found.add(Registries.BLOCK.get(identifier));
            }
        }
        return found.toArray(new Block[0]);
    }
}