package net.fyoncle.elysiumcore.mixin.cosmetic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fyoncle.elysiumcore.utility.constants.Constants;
import net.fyoncle.elysiumcore.utility.constants.Textures;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

@Mixin(TitleScreen.class)
public class CustomMainMenuMixin extends Screen {

    @Unique
    private static final int ELYSIUMCORE$ICON_SIZE = 20;
    @Unique
    private static final int ELYSIUMCORE$ICON_GAP = 4;

    protected CustomMainMenuMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void elysiumcore$addLinkButtons(CallbackInfo ci) {
        ButtonWidget options = this.elysiumcore$findButton("menu.options");
        ButtonWidget quit = this.elysiumcore$findButton("menu.quit");

        if (options != null) {
            int x = options.getX() - 2 * (ELYSIUMCORE$ICON_GAP + ELYSIUMCORE$ICON_SIZE);
            this.elysiumCore$addLinkButton(x, options.getY(),
                    Textures.WIKI_BUTTON_UNFOCUSED_TEXTURE, Textures.WIKI_BUTTON_FOCUSED_TEXTURE,
                    Constants.Links.WIKI_LINK, "elysiumcore.tooltip.wiki");
        }
        if (quit != null) {
            int x = quit.getX() + quit.getWidth() + 2 * ELYSIUMCORE$ICON_GAP + ELYSIUMCORE$ICON_SIZE;
            this.elysiumCore$addLinkButton(x, quit.getY(),
                    Textures.DISCORD_BUTTON_UNFOCUSED_TEXTURE, Textures.DISCORD_BUTTON_FOCUSED_TEXTURE,
                    Constants.Links.DISCORD_LINK, "elysiumcore.tooltip.discord");
        }
    }

    @Unique
    private ButtonWidget elysiumcore$findButton(String translationKey) {
        for (Element child : this.children()) {
            if (child instanceof ButtonWidget button
                    && button.getMessage().getContent() instanceof TranslatableTextContent content
                    && content.getKey().equals(translationKey)) {
                return button;
            }
        }
        return null;
    }

    @Unique
    private void elysiumCore$addLinkButton(int x, int y, Identifier unfocused, Identifier focused,
            String url, String labelKey) {
        Text label = Text.translatable(labelKey);
        TexturedButtonWidget button = new TexturedButtonWidget(
                x, y,
                ELYSIUMCORE$ICON_SIZE, ELYSIUMCORE$ICON_SIZE,
                new ButtonTextures(unfocused, focused),
                pressed -> Util.getOperatingSystem().open(url),
                label);
        button.setTooltip(Tooltip.of(label));
        this.addDrawableChild(button);
    }
}