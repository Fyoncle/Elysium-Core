package net.fyoncle.elysiumcore.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingStatus;

@Mixin(ServerEntityManager.class)
public abstract class ServerEntityManagerMixin<T extends EntityLike> {

    @Unique
    private int elysiumcore$statusUpdateDepth;

    @Unique
    private final List<T> elysiumcore$deferredAdds = new ArrayList<>();

    @Shadow
    protected abstract boolean addEntity(T entity, boolean existing);
    
    @WrapMethod(method = "updateTrackingStatus(Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/entity/EntityTrackingStatus;)V")
    private void elysiumcore$deferAddsDuringStatusUpdate(ChunkPos chunkPos, EntityTrackingStatus status, Operation<Void> original) {
        this.elysiumcore$statusUpdateDepth++;
        try {
            original.call(chunkPos, status);
        } finally {
            this.elysiumcore$statusUpdateDepth--;
        }
        if (this.elysiumcore$statusUpdateDepth == 0 && !this.elysiumcore$deferredAdds.isEmpty()) {
            List<T> pending = new ArrayList<>(this.elysiumcore$deferredAdds);
            this.elysiumcore$deferredAdds.clear();
            for (T entity : pending) {
                this.addEntity(entity, false);
            }
        }
    }

    @WrapMethod(method = "addEntity(Lnet/minecraft/world/entity/EntityLike;Z)Z")
    private boolean elysiumcore$queueFreshAddsDuringStatusUpdate(T entity, boolean existing, Operation<Boolean> original) {
        if (this.elysiumcore$statusUpdateDepth > 0 && !existing) {
            this.elysiumcore$deferredAdds.add(entity);
            return true;
        }
        return original.call(entity, existing);
    }
}