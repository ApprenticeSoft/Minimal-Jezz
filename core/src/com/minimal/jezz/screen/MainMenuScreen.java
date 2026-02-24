package com.minimal.jezz.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.minimal.jezz.Couleurs;
import com.minimal.jezz.Donnees;
import com.minimal.jezz.MyGdxGame;
import com.minimal.jezz.Variables;
import com.minimal.jezz.table.TableNotation;

public class MainMenuScreen implements Screen {

    final MyGdxGame game;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private BitmapFont fontTitre;
    private GlyphLayout glyphLayout;
    private TextButton startBouton;
    private TextButton optionBouton;
    private TextButton rateBouton;
    private TextButton moreAppsBouton;
    private Image transitionImage;
    private TextButtonStyle textButtonStyle;
    private Couleurs couleur;
    private TableNotation tableNotation;
    private boolean listenersBound;

    public MainMenuScreen(final MyGdxGame gam) {
        game = gam;
        listenersBound = false;
        Variables.pause = true;

        if (!game.music.isPlaying()) {
            game.music.play();
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage();
        skin = new Skin();

        couleur = new Couleurs(1);

        fontTitre = game.assets.get("fontTitre.ttf", BitmapFont.class);

        TextureAtlas textureAtlas = game.assets.get("Images.pack", TextureAtlas.class);
        skin.addRegions(textureAtlas);
        glyphLayout = new GlyphLayout();
        glyphLayout.setText(fontTitre, "MINIMAL JEZZ");

        textButtonStyle = new TextButtonStyle();
        if (Gdx.graphics.getHeight() >= 1000) {
            textButtonStyle.up = skin.getDrawable("Bouton");
            textButtonStyle.down = skin.getDrawable("BoutonChecked");
        } else {
            textButtonStyle.up = skin.getDrawable("BoutonPetit");
            textButtonStyle.down = skin.getDrawable("BoutonPetitChecked");
        }
        textButtonStyle.font = game.assets.get("font1.ttf", BitmapFont.class);
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = new Color(35 / 256f, 59 / 256f, 95 / 256f, 1);

        startBouton = new TextButton(gam.langue.commencer, textButtonStyle);
        startBouton.setWidth(Gdx.graphics.getWidth() / 3f);
        startBouton.setHeight(Gdx.graphics.getHeight() / 11f);
        startBouton.setX(Gdx.graphics.getWidth() / 2f - startBouton.getWidth() / 2f);
        startBouton.setY(60 * Gdx.graphics.getHeight() / 100f - startBouton.getHeight() / 2f - game.hauteurBanniere);

        optionBouton = new TextButton(gam.langue.options, textButtonStyle);
        optionBouton.setWidth(Gdx.graphics.getWidth() / 3f);
        optionBouton.setHeight(Gdx.graphics.getHeight() / 11f);
        optionBouton.setX(startBouton.getX());
        optionBouton.setY(startBouton.getY() - 1.1f * optionBouton.getHeight());

        rateBouton = new TextButton(gam.langue.noter, textButtonStyle);
        rateBouton.setWidth(Gdx.graphics.getWidth() / 3f);
        rateBouton.setHeight(Gdx.graphics.getHeight() / 11f);
        rateBouton.setY(optionBouton.getY() - 1.1f * rateBouton.getHeight());

        moreAppsBouton = new TextButton(gam.langue.plusDApp, textButtonStyle);
        moreAppsBouton.setWidth(Gdx.graphics.getWidth() / 3f);
        moreAppsBouton.setHeight(Gdx.graphics.getHeight() / 11f);
        moreAppsBouton.setY(optionBouton.getY() - 1.1f * rateBouton.getHeight());

        if (!Donnees.getRate()) {
            rateBouton.setX(startBouton.getX());
            moreAppsBouton.setX(-Gdx.graphics.getWidth());
        } else {
            rateBouton.setX(-Gdx.graphics.getWidth());
            moreAppsBouton.setX(startBouton.getX());
        }

        transitionImage = new Image(skin.getDrawable("Barre"));
        transitionImage.setWidth(Gdx.graphics.getWidth());
        transitionImage.setHeight(Gdx.graphics.getHeight());
        transitionImage.setColor(couleur.getCouleur2());
        transitionImage.setX(-Gdx.graphics.getWidth());
        transitionImage.setY(0);
        transitionImage.addAction(Actions.alpha(0));

        stage.addActor(startBouton);
        stage.addActor(optionBouton);
        stage.addActor(rateBouton);
        stage.addActor(moreAppsBouton);
        stage.addActor(transitionImage);

        if (!Donnees.getRate() && Donnees.getRateCount() < 1) {
            tableNotation = new TableNotation(game, skin);
            tableNotation.draw(stage);
        }

        if (!Donnees.getRate()) {
            startBouton.addAction(Actions.parallel(
                    Actions.alpha(0),
                    Actions.addAction(Actions.alpha(0), optionBouton),
                    Actions.addAction(Actions.alpha(0), rateBouton)
            ));
            startBouton.addAction(Actions.sequence(
                    Actions.delay(0.1f),
                    Actions.alpha(1, 0.1f),
                    Actions.addAction(Actions.alpha(1, 0.1f), optionBouton),
                    Actions.delay(0.1f),
                    Actions.addAction(Actions.alpha(1, 0.1f), rateBouton)
            ));
        } else {
            startBouton.addAction(Actions.parallel(
                    Actions.alpha(0),
                    Actions.addAction(Actions.alpha(0), optionBouton),
                    Actions.addAction(Actions.alpha(0), moreAppsBouton)
            ));
            startBouton.addAction(Actions.sequence(
                    Actions.delay(0.1f),
                    Actions.alpha(1, 0.1f),
                    Actions.addAction(Actions.alpha(1, 0.1f), optionBouton),
                    Actions.delay(0.1f),
                    Actions.addAction(Actions.alpha(1, 0.1f), moreAppsBouton)
            ));
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(couleur.getCouleur2().r, couleur.getCouleur2().g, couleur.getCouleur2().b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        fontTitre.draw(game.batch, glyphLayout, Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2f, 85 * Gdx.graphics.getHeight() / 100f - game.hauteurBanniere);
        game.batch.end();

        stage.act();
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        game.actionResolver.showBanner();
        if (listenersBound) {
            return;
        }
        listenersBound = true;

        if (!Donnees.getRate() && Donnees.getRateCount() < 1) {
            tableNotation.action();
        }

        startBouton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Variables.couleurSelectionee = MathUtils.random(1, 15);
                transitionImage.addAction(Actions.moveTo(0, 0));
                transitionImage.addAction(Actions.sequence(
                        Actions.alpha(1, 0.2f),
                        Actions.run(() -> game.setScreen(new NiveauxScreen(game)))
                ));
            }
        });

        optionBouton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                transitionImage.addAction(Actions.moveTo(0, 0));
                transitionImage.addAction(Actions.sequence(
                        Actions.alpha(1, 0.2f),
                        Actions.run(() -> game.setScreen(new OptionScreen(game)))
                ));
            }
        });

        rateBouton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Donnees.setRate(true);
                Gdx.net.openURI(Variables.GOOGLE_PLAY_GAME_URL);
            }
        });

        moreAppsBouton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(Variables.GOOGLE_PLAY_STORE_URL);
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
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
}
