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

    private static final float GAMEPLAY_SPEED_SCALE = 0.5f;

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

        maxSpeed = Variables.vitesseBalle * GAMEPLAY_SPEED_SCALE * camera.viewportHeight;

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

        float initY = MathUtils.random(-200, 200);
        float initX = MathUtils.random(-200, 200);
        body.applyLinearImpulse(initY / 100f, initX / 100f, body.getPosition().x, body.getPosition().y, true);
    }

    public void active() {
        maxSpeed = Variables.vitesseBalle * GAMEPLAY_SPEED_SCALE * camera.viewportHeight;
        vectorSpeed = body.getLinearVelocity();
        speed = vectorSpeed.len();
        if (speed > maxSpeed) {
            body.setLinearVelocity(vectorSpeed.limit(maxSpeed));
        } else if (speed < 0.8f * maxSpeed) {
            Vector2 n = vectorSpeed.nor();
            body.applyLinearImpulse(n.x, n.y, body.getPosition().x, body.getPosition().y, true);
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
        maxSpeed = vitesseBalles * GAMEPLAY_SPEED_SCALE * camera.viewportHeight;
    }
}
