package net.fyoncle.elysiumcore;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class PortalWarn implements ModInitializer {
    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!player.getStackInHand(hand).isOf(Items.FLINT_AND_STEEL))
                return ActionResult.PASS;
            if (!world.getBlockState(hitResult.getBlockPos()).isOf(Blocks.OBSIDIAN))
                return ActionResult.PASS;

            if (!world.isClient) {
                player.sendMessage(Text.translatable("elysiumcore.portal_denied"), true);
            }
            return ActionResult.PASS;
        });
    }
}