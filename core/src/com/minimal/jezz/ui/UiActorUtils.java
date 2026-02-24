package com.minimal.jezz.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;

public final class UiActorUtils {

    private UiActorUtils() {
    }

    public static void centerTextButtons(Actor root) {
        if (root == null) {
            return;
        }
        if (root instanceof TextButton) {
            centerTextButton((TextButton) root);
        }
        if (root instanceof Group) {
            SnapshotArray<Actor> children = ((Group) root).getChildren();
            for (int i = 0; i < children.size; i++) {
                centerTextButtons(children.get(i));
            }
        }
    }

    private static void centerTextButton(TextButton button) {
        button.pad(0f);
        float horizontalPad = Math.max(1f, button.getWidth() * 0.05f);
        float topPad = Math.max(1f, button.getHeight() * 0.12f);
        float bottomPad = Math.max(1f, button.getHeight() * 0.18f);
        Label label = button.getLabel();
        if (label != null) {
            label.setAlignment(Align.center, Align.center);
            label.setWrap(false);
            float lineGap = Math.max(0f, label.getStyle().font.getLineHeight() - label.getStyle().font.getCapHeight());
            float descent = Math.abs(label.getStyle().font.getDescent());
            bottomPad += (0.5f * lineGap) + (0.5f * descent);
        }
        if (button.getLabelCell() != null) {
            button.getLabelCell().padLeft(horizontalPad).padRight(horizontalPad).padTop(topPad).padBottom(bottomPad).center();
        }
        button.invalidateHierarchy();
    }
}
