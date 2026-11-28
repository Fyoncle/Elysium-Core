package net.fyoncle.elysiumcore.custommenus;

import java.util.ArrayList;
import java.util.List;

import net.fyoncle.elysiumcore.ElysiumCoreConfig;
import net.fyoncle.elysiumcore.utility.constants.Constants;
import net.fyoncle.elysiumcore.utility.constants.Textures;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Util;

public class WelcomeMenu extends Screen {

    private static final int LOGO_TEX_W = 1028;
    private static final int LOGO_TEX_H = 350;
    private static final float LOGO_WIDTH_FRACTION = 0.55f;
    private static final int MAX_LOGO_W = 257;
    private static final int MIN_LOGO_H = 24;
    private static final int MAX_TEXT_WIDTH = 400;
    private static final int MAX_DIVIDER_W = 260;
    private static final int EDGE = 4;
    private static final int SECTION_GAP = 6;
    private static final int DIVIDER_PAD = 5;
    private static final int DIVIDER_SPACE = 2 * DIVIDER_PAD + 1;
    private static final int DIVIDER_COLOR = 0x66FFFFFF;
    private static final int LINE_HEIGHT = 11;
    private static final int BUTTON_W = 100;
    private static final int BUTTON_H = 20;
    private static final int BUTTON_GAP = 4;
    private static final int GOLD = 0xFFAA00;

    private final List<List<OrderedText>> sections = new ArrayList<>();
    private final List<Integer> sectionColors = new ArrayList<>();
    private int logoY;
    private int logoW;
    private int logoH;
    private int textY;
    private int dividerW;

    public WelcomeMenu(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();

        int maxWidth = Math.min(MAX_TEXT_WIDTH, this.width - 2 * EDGE - 16);
        this.dividerW = Math.min(MAX_DIVIDER_W, maxWidth);
        this.sections.clear();
        this.sectionColors.clear();
        addSection("elysiumcore.welcomescreen.thanks", GOLD, maxWidth);
        addSection("elysiumcore.welcomescreen.wiki", Colors.WHITE, maxWidth);
        addSection("elysiumcore.welcomescreen.accessibility", Colors.WHITE, maxWidth);
        addSection("elysiumcore.welcomescreen.multiplayer", Colors.WHITE, maxWidth);

        int textHeight = 0;
        for (List<OrderedText> section : this.sections) {
            textHeight += section.size() * LINE_HEIGHT;
        }
        textHeight += (this.sections.size() - 1) * DIVIDER_SPACE + SECTION_GAP;

        int buttonW = Math.min(BUTTON_W, this.width - 2 * EDGE);
        boolean sideBySide = this.width >= 2 * buttonW + BUTTON_GAP + 2 * EDGE;
        int buttonsHeight = sideBySide ? BUTTON_H : 2 * BUTTON_H + BUTTON_GAP;

        int desiredW = Math.min(MAX_LOGO_W, Math.round(this.width * LOGO_WIDTH_FRACTION));
        int desiredH = desiredW * LOGO_TEX_H / LOGO_TEX_W;
        int spaceForLogo = this.height - 2 * EDGE - textHeight - buttonsHeight - SECTION_GAP;
        this.logoH = Math.min(desiredH, spaceForLogo);
        if (this.logoH < MIN_LOGO_H) {
            this.logoH = 0;
        }
        this.logoW = this.logoH * LOGO_TEX_W / LOGO_TEX_H;

        int contentHeight = (this.logoH > 0 ? this.logoH + SECTION_GAP : 0) + textHeight + buttonsHeight;
        int top = Math.max(EDGE, (this.height - contentHeight) / 2);
        this.logoY = top;
        this.textY = this.logoH > 0 ? top + this.logoH + SECTION_GAP : top;

        int buttonY = this.textY + textHeight;
        int centerX = this.width / 2;

        int wikiX;
        int startedX;
        int startedY;
        if (sideBySide) {
            wikiX = centerX - buttonW - BUTTON_GAP / 2;
            startedX = centerX + BUTTON_GAP / 2;
            startedY = buttonY;
        } else {
            wikiX = centerX - buttonW / 2;
            startedX = wikiX;
            startedY = buttonY + BUTTON_H + BUTTON_GAP;
        }

        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("elysiumcore.welcomescreen.button_wiki"),
                button -> Util.getOperatingSystem().open(Constants.Links.WIKI_LINK))
                .dimensions(wikiX, buttonY, buttonW, BUTTON_H)
                .build());
        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("elysiumcore.welcomescreen.button_get_started"),
                button -> {
                    ElysiumCoreConfig.config.seenWelcomeScreen = true;
                    ElysiumCoreConfig.config.save();
                    this.close();
                })
                .dimensions(startedX, startedY, buttonW, BUTTON_H)
                .build());
    }

    private void addSection(String key, int color, int maxWidth) {
        this.sections.add(this.textRenderer.wrapLines(Text.translatable(key), maxWidth));
        this.sectionColors.add(color);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        if (this.logoH > 0) {
            context.drawTexture(Textures.MENU_LOGO, (this.width - this.logoW) / 2, this.logoY,
                    0f, 0f, this.logoW, this.logoH, this.logoW, this.logoH);
        }

        int y = this.textY;
        int last = this.sections.size() - 1;
        for (int i = 0; i <= last; i++) {
            for (OrderedText line : this.sections.get(i)) {
                context.drawCenteredTextWithShadow(this.textRenderer, line, this.width / 2, y,
                        this.sectionColors.get(i));
                y += LINE_HEIGHT;
            }
            if (i < last) {
                int lineY = y + DIVIDER_PAD;
                int x1 = (this.width - this.dividerW) / 2;
                context.fill(x1, lineY, x1 + this.dividerW, lineY + 1, DIVIDER_COLOR);
                y += DIVIDER_SPACE;
            }
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(new TitleScreen());
    }
}