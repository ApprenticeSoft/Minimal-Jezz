package com.minimal.jezz.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.minimal.jezz.ActionBouton;
import com.minimal.jezz.Couleurs;
import com.minimal.jezz.Donnees;
import com.minimal.jezz.MyGdxGame;
import com.minimal.jezz.Variables;
import com.minimal.jezz.ui.UiActorUtils;

public class NiveauxScreen implements Screen {

    private static final float RETOUR_MIN_SAFE_SCREEN_HEIGHT_RATIO = 0.12f;

    final MyGdxGame game;
    OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    private TextButtonStyle textButtonStyle;
    private TextButtonStyle textButtonStyleInactif;
    private Array<TextButton> niveaux;
    private TextButton retourBouton;
    private Image transitionImage;
    private Table tableNiveaux;
    private ActionBouton action;
    private Couleurs couleur;
    private Label titre;
    private boolean listenersBound;
    private final float baseScreenWidth;
    private final float baseScreenHeight;

    public NiveauxScreen(final MyGdxGame gam) {
        game = gam;
        listenersBound = false;
        baseScreenWidth = Math.max(1f, Gdx.graphics.getWidth());
        baseScreenHeight = Math.max(1f, Gdx.graphics.getHeight());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        couleur = new Couleurs(1);

        action = new ActionBouton();

        stage = new Stage();
        skin = new Skin();
        TextureAtlas textureAtlas = game.assets.get("Images.pack", TextureAtlas.class);
        skin.addRegions(textureAtlas);

        LabelStyle titreStyle = new LabelStyle(game.assets.get("fontTitre.ttf", BitmapFont.class), Color.WHITE);
        titre = new Label(gam.langue.choisirNiveau, titreStyle);
        titre.setAlignment(Align.center);
        titre.getStyle().font.setUseIntegerPositions(false);
        updateTitleBounds();

        textButtonStyle = new TextButtonStyle();
        if (Gdx.graphics.getHeight() > 1000) {
            textButtonStyle.up = skin.getDrawable("Bouton");
            textButtonStyle.down = skin.getDrawable("BoutonChecked");
        } else {
            textButtonStyle.up = skin.getDrawable("BoutonPetit");
            textButtonStyle.down = skin.getDrawable("BoutonPetitChecked");
        }
        textButtonStyle.font = game.assets.get("font1.ttf", BitmapFont.class);
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = new Color(35 / 256f, 59 / 256f, 95 / 256f, 1);

        textButtonStyleInactif = new TextButtonStyle();
        if (Gdx.graphics.getHeight() > 1000) {
            textButtonStyleInactif.up = skin.getDrawable("BoutonInactif");
        } else {
            textButtonStyleInactif.up = skin.getDrawable("BoutonInactifPetit");
        }
        textButtonStyleInactif.font = game.assets.get("font1.ttf", BitmapFont.class);
        textButtonStyleInactif.fontColor = new Color().set(0, 0, 0, 0.7f);

        tableNiveaux = new Table();
        tableNiveaux.defaults().width(Gdx.graphics.getWidth() / 7f).height(Gdx.graphics.getWidth() / 7f).space(Gdx.graphics.getWidth() / 30f);

        niveaux = new Array<TextButton>();
        for (int i = 0; i < Variables.nombreNiveaux; i++) {
            TextButton textButton = new TextButton(String.valueOf(i + 1), textButtonStyle);
            niveaux.add(textButton);
            if (i + 1 == 5 || i + 1 == 10 || i + 1 == 15 || i + 1 == 20) {
                tableNiveaux.add(textButton).row();
            } else {
                tableNiveaux.add(textButton);
            }
        }

        transitionImage = new Image(skin.getDrawable("Barre"));
        transitionImage.setWidth(Gdx.graphics.getWidth());
        transitionImage.setHeight(Gdx.graphics.getHeight());
        transitionImage.setColor(couleur.getCouleur2());
        transitionImage.setX(-Gdx.graphics.getWidth());
        transitionImage.setY(0);
        transitionImage.addAction(Actions.alpha(0));

        tableNiveaux.setX(Gdx.graphics.getWidth() / 2f);
        tableNiveaux.setY(45 * Gdx.graphics.getHeight() / 100f);

        stage.addActor(tableNiveaux);
        stage.act();
        stage.draw();

        retourBouton = new TextButton("<", textButtonStyle);
        retourBouton.setWidth(Gdx.graphics.getWidth() / 7f);
        retourBouton.setHeight(Gdx.graphics.getWidth() / 7f);
        retourBouton.setX(niveaux.get(0).localToStageCoordinates(new Vector2(0, 0)).x);
        updateRetourButtonBounds();

        stage.addActor(titre);
        stage.addActor(retourBouton);
        stage.addActor(transitionImage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(couleur.getCouleur2().r, couleur.getCouleur2().g, couleur.getCouleur2().b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < niveaux.size; i++) {
            if ((i + 1) <= Donnees.getNiveau()) {
                niveaux.get(i).setStyle(textButtonStyle);
                niveaux.get(i).setTouchable(Touchable.enabled);
            } else {
                niveaux.get(i).setStyle(textButtonStyleInactif);
                niveaux.get(i).setTouchable(Touchable.disabled);
            }
        }

        retourBouton.setStyle(textButtonStyle);
        retourBouton.setTouchable(Touchable.enabled);
        updateTitleBounds();
        updateRetourButtonBounds();

        stage.act();
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchKey(Keys.BACK, true);
        game.actionResolver.showBanner();
        if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.WebGL) {
            UiActorUtils.centerTextButtons(stage.getRoot());
        }

        if (listenersBound) {
            return;
        }
        listenersBound = true;

        for (int i = 0; i < niveaux.size; i++) {
            action.niveauListener(game, niveaux.get(i), i + 1);
        }

        retourBouton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                transitionImage.addAction(Actions.moveTo(0, 0));
                transitionImage.addAction(Actions.sequence(
                        Actions.alpha(1, 0.2f),
                        Actions.run(() -> game.setScreen(new MainMenuScreen(game)))
                ));
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
        updateTitleBounds();
        updateRetourButtonBounds();
        if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.WebGL) {
            UiActorUtils.centerTextButtons(stage.getRoot());
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void updateRetourButtonBounds() {
        if (retourBouton == null) {
            return;
        }
        float bottomMargin = Gdx.graphics.getWidth() / 35f;
        float preferredY = retourBouton.getX();
        float bannerHeightPx = Math.max(game.hauteurBanniere, game.actionResolver.getBannerHeightPx());
        float minimumSafeY = bannerHeightPx + bottomMargin;
        float fallbackSafeY = Gdx.graphics.getHeight() * RETOUR_MIN_SAFE_SCREEN_HEIGHT_RATIO;
        retourBouton.setY(Math.max(preferredY, Math.max(minimumSafeY, fallbackSafeY)));
    }

    private void updateTitleBounds() {
        if (titre == null) {
            return;
        }
        float widthScale = Gdx.graphics.getWidth() / baseScreenWidth;
        float heightScale = Gdx.graphics.getHeight() / baseScreenHeight;
        float baseScale = Math.min(widthScale, heightScale);
        titre.setFontScale(baseScale);
        titre.invalidateHierarchy();
        float titleWidth = titre.getPrefWidth();
        float targetWidth = Gdx.graphics.getWidth() * 0.8f;
        if (titleWidth > 0f) {
            float fitScale = targetWidth / titleWidth;
            titre.setFontScale(baseScale * fitScale);
            titre.invalidateHierarchy();
            titleWidth = titre.getPrefWidth();
        }
        float titleHeight = titre.getPrefHeight();
        titre.setSize(titleWidth, titleHeight);
        titre.setPosition(
                Gdx.graphics.getWidth() / 2f - titleWidth / 2f,
                83 * Gdx.graphics.getHeight() / 100f - titleHeight / 2f
        );
    }
}
