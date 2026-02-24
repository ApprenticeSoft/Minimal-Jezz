package com.minimal.jezz.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
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
        if (button.getLabel() != null) {
            button.getLabel().setAlignment(Align.center);
        }
        if (button.getLabelCell() != null) {
            button.getLabelCell().expand().fill().center();
        }
    }
}
