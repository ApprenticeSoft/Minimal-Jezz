package com.minimal.jezz.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.minimal.jezz.Couleurs;
import com.minimal.jezz.Donnees;
import com.minimal.jezz.LevelHandler;
import com.minimal.jezz.MyGdxGame;
import com.minimal.jezz.MyGestureListener;
import com.minimal.jezz.Point;
import com.minimal.jezz.Surface;
import com.minimal.jezz.Variables;
import com.minimal.jezz.body.Balle;
import com.minimal.jezz.body.Barre;
import com.minimal.jezz.enums.EtatBarre;
import com.minimal.jezz.enums.EtatPoint;
import com.minimal.jezz.table.TablesJeu;

public class GameScreen extends InputAdapter implements Screen {

    final MyGdxGame game;
    private OrthographicCamera camera;

    private World world;
    private Array<Barre> barres;
    private Array<Balle> balles;
    private Array<Point> points;
    private Array<Surface> surfaces;
    private TextureAtlas textureAtlas;
    private Skin skin;
    private int vies;
    private float airTotale;
    private float pourcentageAir;
    private TablesJeu tablesJeu;

    private Stage stage;
    private Label labelPourcentage;

    private MyGestureListener gestureListener;
    private InputMultiplexer inputMultiplexer;
    private LevelHandler level;
    private Couleurs couleurs;
    private Color couleurBarre;
    private Color couleurBalle;
    private Color couleurSurface;
    private Color couleurFond;
    private Sound sonContact;
    private Sound sonNiveauFini;
    private Sound sonPerdu;
    private boolean listenersBound;
    private boolean adBreakSent;
    private boolean bannerVisible;

    public GameScreen(final MyGdxGame gam) {
        game = gam;
        listenersBound = false;
        adBreakSent = false;
        bannerVisible = false;

        camera = new OrthographicCamera();
        camera.viewportHeight = Gdx.graphics.getHeight() * Variables.WORLD_TO_BOX;
        camera.viewportWidth = Gdx.graphics.getWidth() * Variables.WORLD_TO_BOX;
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f);
        camera.update();
        Variables.largeurBordure = camera.viewportHeight / 100f;

        couleurs = new Couleurs(Variables.couleurSelectionee);
        couleurBarre = new Color();
        couleurBalle = new Color();
        couleurSurface = new Color();
        couleurFond = new Color();
        couleurBalle = couleurs.getCouleur1();
        couleurFond = couleurs.getCouleur2();
        couleurBarre = couleurs.getCouleur3();
        couleurSurface = couleurs.getCouleur3();

        sonContact = game.assets.get("Sons/contact.mp3", Sound.class);
        sonNiveauFini = game.assets.get("Sons/gagne.mp3", Sound.class);
        sonPerdu = game.assets.get("Sons/perdu.mp3", Sound.class);

        textureAtlas = game.assets.get("Images.pack", TextureAtlas.class);
        skin = new Skin();
        skin.addRegions(textureAtlas);
        world = new World(new Vector2(0, 0), true);
        World.setVelocityThreshold(0);
        Variables.BOX_STEP = 0;
        Variables.pause = false;
        Variables.perdu = false;
        Variables.gagne = false;
        Variables.spawn = false;
        Variables.debut = true;
        vies = 3;

        barres = new Array<Barre>();
        balles = new Array<Balle>();
        points = new Array<Point>();
        surfaces = new Array<Surface>();
        gestureListener = new MyGestureListener(game, world, camera, barres, points, surfaces);
        inputMultiplexer = new InputMultiplexer();

        pourcentageAir = calculAirOccupee(barres, surfaces);

        Barre barreHaut = new Barre(game, world, camera, camera.viewportWidth / 2f, (camera.viewportHeight - Variables.posBordure * camera.viewportWidth), (0.5f - Variables.posBordure) * camera.viewportWidth - Variables.largeurBordure, Variables.largeurBordure);
        Barre barreBas = new Barre(game, world, camera, camera.viewportWidth / 2f, Variables.posBordure * camera.viewportWidth, (0.5f - Variables.posBordure) * camera.viewportWidth - Variables.largeurBordure, Variables.largeurBordure);
        Barre barreGauche = new Barre(game, world, camera, Variables.posBordure * camera.viewportWidth, camera.viewportHeight / 2f, Variables.largeurBordure, camera.viewportHeight / 2f);
        Barre barreDroite = new Barre(game, world, camera, (1 - Variables.posBordure) * camera.viewportWidth, camera.viewportHeight / 2f, Variables.largeurBordure, camera.viewportHeight / 2f);

        barres.add(barreHaut);
        barres.add(barreBas);
        barres.add(barreGauche);
        barres.add(barreDroite);

        points.add(new Point(barreGauche.barre1.getPosition().x + Variables.largeurBordure, barreHaut.barre1.getPosition().y - Variables.largeurBordure, EtatPoint.HG));
        points.add(new Point(barreDroite.barre1.getPosition().x - Variables.largeurBordure, barreHaut.barre1.getPosition().y - Variables.largeurBordure, EtatPoint.HD));
        points.add(new Point(barreGauche.barre1.getPosition().x + Variables.largeurBordure, barreBas.barre1.getPosition().y + Variables.largeurBordure, EtatPoint.BG));
        points.add(new Point(barreDroite.barre1.getPosition().x - Variables.largeurBordure, barreBas.barre1.getPosition().y + Variables.largeurBordure, EtatPoint.BD));

        airTotale = (barreDroite.barre1.getPosition().x - Variables.largeurBordure - barreGauche.barre1.getPosition().x + Variables.largeurBordure)
                * (barreHaut.barre1.getPosition().y - Variables.largeurBordure - barreBas.barre1.getPosition().y + Variables.largeurBordure);

        level = new LevelHandler(world, camera, balles);
        level.loadLevel(Variables.niveauSelectione);

        stage = new Stage();

        LabelStyle labelStylePourcentage = new LabelStyle(game.assets.get("fontPourcentage.ttf", BitmapFont.class), new Color(0, 0, 0, 0.5f));
        labelPourcentage = new Label((int) pourcentageAir + "%", labelStylePourcentage);
        labelPourcentage.setX(Gdx.graphics.getWidth() / 2f - labelPourcentage.getWidth() / 2f);
        labelPourcentage.setY(Gdx.graphics.getHeight() / 2f - labelPourcentage.getHeight() / 2f);
        labelPourcentage.addAction(Actions.alpha(0));

        stage.addActor(labelPourcentage);
        tablesJeu = new TablesJeu(gam, stage, skin, level, couleurFond, couleurBarre);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(couleurFond.r, couleurFond.g, couleurFond.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float currentAir = 100 * calculAirOccupee(barres, surfaces) / airTotale;
        if (pourcentageAir != currentAir && !Variables.spawn) {
            pourcentageAir = currentAir;
            labelPourcentage.setText((int) pourcentageAir + "%");
            labelPourcentage.addAction(Actions.sequence(
                    Actions.moveTo(Gdx.graphics.getWidth() / 2f - labelPourcentage.getPrefWidth() / 2f, labelPourcentage.getY()),
                    Actions.alpha(1),
                    Actions.delay(0.5f),
                    Actions.alpha(0, 0.6f, Interpolation.sineIn)
            ));
        }

        world.step(Variables.BOX_STEP, Variables.BOX_VELOCITY_ITERATIONS, Variables.BOX_POSITION_ITERATIONS);

        for (int i = 0; i < balles.size; i++) {
            balles.get(i).active();
        }

        if (!Variables.pause && !Variables.gagne && vies > 0) {
            for (Barre barre : barres) {
                barre.active(barres, points);
            }
        }

        Barre.detectSurface(points, balles, surfaces);

        if (100 * calculAirOccupee(barres, surfaces) / airTotale > Variables.objectif && !Variables.spawn) {
            if (!Variables.gagne) {
                Variables.gagne = true;
                if (Donnees.getSon()) {
                    Barre.sonSurface.stop();
                    sonNiveauFini.play();
                }
                if (!adBreakSent) {
                    adBreakSent = true;
                    game.actionResolver.onNaturalBreak();
                }
            }
            if (Variables.niveauSelectione == 25) {
                tablesJeu.jeuFini();
            } else {
                tablesJeu.gagne();
                if (Variables.niveauSelectione == Donnees.getNiveau()) {
                    Donnees.setNiveau(Donnees.getNiveau() + 1);
                }
            }
        }

        if (vies <= 0) {
            if (!Variables.perdu) {
                Variables.perdu = true;
                if (Donnees.getSon()) {
                    sonContact.stop();
                    sonPerdu.play();
                }
                if (!adBreakSent) {
                    adBreakSent = true;
                    game.actionResolver.onNaturalBreak();
                }
            }
            tablesJeu.perdu();
        }

        boolean shouldShowBanner = Variables.pause || Variables.gagne || Variables.perdu;
        if (shouldShowBanner != bannerVisible) {
            bannerVisible = shouldShowBanner;
            if (bannerVisible) {
                game.actionResolver.showBanner();
            } else {
                game.actionResolver.hideBanner();
            }
        }

        if (Gdx.input.isKeyJustPressed(Keys.BACK) && vies > 0) {
            if (Variables.debut || Variables.gagne) {
                game.setScreen(new MainMenuScreen(game));
            } else if (Variables.pause) {
                tablesJeu.pauseFinie();
            } else {
                tablesJeu.pause();
            }
        }

        game.batch.begin();
        for (int i = 0; i < balles.size; i++) {
            balles.get(i).drawOmbre(game.batch, textureAtlas.findRegion("Balle"));
        }

        for (int i = 0; i < barres.size; i++) {
            barres.get(i).drawOmbre(game.batch, textureAtlas.findRegion("Barre"));
        }

        for (int i = 0; i < barres.size; i++) {
            barres.get(i).draw(game.batch, textureAtlas.findRegion("Barre"), couleurBarre);
        }

        game.batch.setColor(couleurSurface);
        if (surfaces.size > 0) {
            for (int i = 0; i < surfaces.size; i++) {
                game.batch.draw(
                        textureAtlas.findRegion("Barre"),
                        Variables.BOX_TO_WORLD * surfaces.get(i).getX(),
                        Variables.BOX_TO_WORLD * surfaces.get(i).getY(),
                        Variables.BOX_TO_WORLD * surfaces.get(i).getWidth(),
                        Variables.BOX_TO_WORLD * surfaces.get(i).getHeight()
                );
            }
        }

        game.batch.draw(textureAtlas.findRegion("Barre"), 0, 0, Variables.posBordure * Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(textureAtlas.findRegion("Barre"), (1 - Variables.posBordure) * Gdx.graphics.getWidth(), 0, Variables.posBordure * Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(textureAtlas.findRegion("Barre"), 0, 0, Gdx.graphics.getWidth(), Variables.posBordure * Gdx.graphics.getWidth());
        game.batch.draw(textureAtlas.findRegion("Barre"), 0, (Gdx.graphics.getHeight() - Variables.posBordure * Gdx.graphics.getWidth()), Gdx.graphics.getWidth(), Variables.posBordure * Gdx.graphics.getWidth());
        game.batch.end();

        stage.act();
        stage.draw();

        game.batch.begin();
        for (int i = 0; i < balles.size; i++) {
            balles.get(i).draw(game.batch, textureAtlas.findRegion("Balle"), couleurBalle);
        }
        game.batch.end();
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Keys.BACK, true);

        if (!listenersBound) {
            inputMultiplexer.addProcessor(new GestureDetector(gestureListener));
            inputMultiplexer.addProcessor(stage);

            world.setContactListener(new ContactListener() {
                @Override
                public void beginContact(Contact contact) {
                    Body a = contact.getFixtureA().getBody();
                    Body b = contact.getFixtureB().getBody();

                    if ("Bordure".equals(a.getUserData()) && "Barre".equals(b.getUserData())) {
                        for (Barre barre : barres) {
                            if (barre.vertical) {
                                if (a.getPosition().y > b.getPosition().y) {
                                    if (b == barre.barre1) {
                                        if (barre.getEtat() != EtatBarre.contactHaut) {
                                            barre.setContactInit(a.getPosition().y - Variables.largeurBordure);
                                            barre.ajoutPoint(new Point(barre.barre1.getPosition().x - Variables.largeurBordure, a.getPosition().y - Variables.largeurBordure, EtatPoint.HD));
                                            barre.ajoutPoint(new Point(barre.barre1.getPosition().x + Variables.largeurBordure, a.getPosition().y - Variables.largeurBordure, EtatPoint.HG));
                                        }
                                    }
                                } else if (a.getPosition().y < b.getPosition().y) {
                                    if (b == barre.barre1) {
                                        if (barre.getEtat() != EtatBarre.contactBas) {
                                            barre.setContactInit(a.getPosition().y + Variables.largeurBordure);
                                            barre.ajoutPoint(new Point(barre.barre1.getPosition().x - Variables.largeurBordure, a.getPosition().y + Variables.largeurBordure, EtatPoint.BD));
                                            barre.ajoutPoint(new Point(barre.barre1.getPosition().x + Variables.largeurBordure, a.getPosition().y + Variables.largeurBordure, EtatPoint.BG));
                                        }
                                    }
                                }
                            } else {
                                if (a.getPosition().x > b.getPosition().x) {
                                    if (b == barre.barre1) {
                                        if (barre.getEtat() != EtatBarre.contactDroite) {
                                            barre.setContactInit(a.getPosition().x - Variables.largeurBordure);
                                            barre.ajoutPoint(new Point(a.getPosition().x - Variables.largeurBordure, barre.barre1.getPosition().y + Variables.largeurBordure, EtatPoint.BD));
                                            barre.ajoutPoint(new Point(a.getPosition().x - Variables.largeurBordure, barre.barre1.getPosition().y - Variables.largeurBordure, EtatPoint.HD));
                                        }
                                    }
                                } else if (a.getPosition().x < b.getPosition().x) {
                                    if (b == barre.barre1) {
                                        if (barre.getEtat() != EtatBarre.contactGauche) {
                                            barre.setContactInit(a.getPosition().x + Variables.largeurBordure);
                                            barre.ajoutPoint(new Point(a.getPosition().x + Variables.largeurBordure, barre.barre1.getPosition().y + Variables.largeurBordure, EtatPoint.BG));
                                            barre.ajoutPoint(new Point(a.getPosition().x + Variables.largeurBordure, barre.barre1.getPosition().y - Variables.largeurBordure, EtatPoint.HG));
                                        }
                                    }
                                }
                            }
                        }
                    } else if (("Barre".equals(a.getUserData()) && "Balle".equals(b.getUserData()))
                            || ("Balle".equals(a.getUserData()) && "Barre".equals(b.getUserData()))) {
                        for (Barre barre : barres) {
                            if (a == barre.barre1 || b == barre.barre1) {
                                barre.setEtat(EtatBarre.decroissante);
                                vies--;
                                if (Donnees.getSon()) {
                                    sonContact.play();
                                }
                            }
                        }
                    }
                }

                @Override
                public void endContact(Contact contact) {
                }

                @Override
                public void preSolve(Contact contact, Manifold oldManifold) {
                    Body a = contact.getFixtureA().getBody();
                    Body b = contact.getFixtureB().getBody();
                    if ("Barre".equals(a.getUserData()) && "Barre".equals(b.getUserData())) {
                        contact.setEnabled(false);
                    }
                }

                @Override
                public void postSolve(Contact contact, ContactImpulse impulse) {
                }
            });

            tablesJeu.boutonListener(world, barres);
            listenersBound = true;
        }

        Gdx.input.setInputProcessor(inputMultiplexer);
        game.actionResolver.hideBanner();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height * Variables.WORLD_TO_BOX;
        camera.viewportWidth = width * Variables.WORLD_TO_BOX;
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f);
        camera.update();
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
        game.actionResolver.hideBanner();
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        world.dispose();
    }

    public float calculAirOccupee(Array<Barre> barres, Array<Surface> surfaces) {
        float airOccupee = 0;

        for (int i = 0; i < surfaces.size; i++) {
            airOccupee += surfaces.get(i).getWidth() * surfaces.get(i).getHeight();
        }

        for (int i = 4; i < barres.size; i++) {
            airOccupee += 4 * barres.get(i).getWidth() * barres.get(i).getHeight();
        }

        return airOccupee;
    }
}
