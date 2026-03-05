package com.minimal.jezz.body;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.minimal.jezz.Variables;

public class Balle extends CircleShape {

    private static final float MIN_SPEED_RATIO = 0.8f;

    public Body body;
    private BodyDef bodyDef;
    private float rayon;
    private final World world;
    Camera camera;
    private Vector2 vectorSpeed;
    private float maxSpeed;
    private float speed;

    public Balle(World world, Camera camera, float posX, float posY) {
        super();
        this.world = world;
        this.camera = camera;
        rayon = camera.viewportWidth / 50f;

        maxSpeed = computeTargetMaxSpeed();

        bodyDef = new BodyDef();
        this.setRadius(rayon);

        bodyDef.position.set(new Vector2(posX, posY));
        bodyDef.type = BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = this;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1;
        body.createFixture(fixtureDef);
        body.setUserData("Balle");

        Vector2 launchDirection = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
        if (launchDirection.isZero()) {
            launchDirection.set(0f, 1f);
        }
        body.setLinearVelocity(launchDirection.nor().scl(maxSpeed));
    }

    public void active() {
        maxSpeed = computeTargetMaxSpeed();
        vectorSpeed = body.getLinearVelocity();
        speed = vectorSpeed.len();
        if (speed > maxSpeed) {
            body.setLinearVelocity(vectorSpeed.limit(maxSpeed));
        } else if (speed < MIN_SPEED_RATIO * maxSpeed) {
            Vector2 direction = new Vector2(vectorSpeed);
            if (direction.isZero()) {
                direction.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
                if (direction.isZero()) {
                    direction.set(0f, 1f);
                }
            }
            body.setLinearVelocity(direction.nor().scl(MIN_SPEED_RATIO * maxSpeed));
        }

        if (body.getPosition().x - rayon < 0) {
            body.setTransform(rayon, body.getPosition().y, 0);
        }
        if (body.getPosition().x + rayon > camera.viewportWidth) {
            body.setTransform(camera.viewportWidth - rayon, body.getPosition().y, 0);
        }
        if (body.getPosition().y + rayon > camera.viewportHeight) {
            body.setTransform(body.getPosition().x, camera.viewportHeight - rayon, 0);
        }
    }

    public void drawOmbre(SpriteBatch batch, TextureRegion textureRegion) {
        batch.setColor(0, 0, 0, 0.2f);
        batch.draw(textureRegion,
                Variables.BOX_TO_WORLD * (this.body.getPosition().x - this.rayon) + Gdx.graphics.getWidth() / 80f,
                Variables.BOX_TO_WORLD * (this.body.getPosition().y - this.rayon) - Gdx.graphics.getWidth() / 68f,
                Variables.BOX_TO_WORLD * 2 * this.rayon,
                Variables.BOX_TO_WORLD * 2 * this.rayon);

    }

    public void draw(SpriteBatch batch, TextureRegion textureRegion, Color couleur) {
        batch.setColor(couleur);
        batch.draw(textureRegion,
                Variables.BOX_TO_WORLD * (this.body.getPosition().x - this.rayon),
                Variables.BOX_TO_WORLD * (this.body.getPosition().y - this.rayon),
                Variables.BOX_TO_WORLD * 2 * this.rayon,
                Variables.BOX_TO_WORLD * 2 * this.rayon);

    }

    public void setVitesse(float vitesseBalles) {
        float vitesseRatio = Variables.vitesseBalleNormale == 0f ? 1f : vitesseBalles / Variables.vitesseBalleNormale;
        maxSpeed = computeBaseVerticalSpeed() * vitesseRatio * Variables.vitesseBalleScale;
    }

    private float computeTargetMaxSpeed() {
        float vitesseRatio = Variables.vitesseBalleNormale == 0f ? 1f : Variables.vitesseBalle / Variables.vitesseBalleNormale;
        return computeBaseVerticalSpeed() * vitesseRatio * Variables.vitesseBalleScale;
    }

    private float computeBaseVerticalSpeed() {
        return camera.viewportHeight / Variables.BASE_BALL_CROSS_SCREEN_SECONDS;
    }
}
