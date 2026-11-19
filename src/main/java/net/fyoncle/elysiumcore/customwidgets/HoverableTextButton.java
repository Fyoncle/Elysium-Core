package net.fyoncle.elysiumcore.customwidgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

public class HoverableTextButton extends TexturedButtonWidget {
    private static final int TEXT_MARGIN = 2;

    public HoverableTextButton(int x, int y, int width, int height,
            Text text,
            Identifier textureUnfocused,
            Identifier textureFocused, PressAction pressAction) {
        super(x, y, width, height, new ButtonTextures(textureUnfocused, textureFocused), pressAction, text);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        this.drawScrollableText(context, MinecraftClient.getInstance().textRenderer, TEXT_MARGIN, Colors.WHITE);
    }
}