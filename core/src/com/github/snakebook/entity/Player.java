package com.github.snakebook.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.github.snakebook.locations.Location;
import com.github.snakebook.locations.Vector;
import com.github.snakebook.world.World;

import java.util.Iterator;
import java.util.LinkedList;

import static com.github.snakebook.world.World.Constant.WORLD_HEIGHT;
import static com.github.snakebook.world.World.Constant.WORLD_WIDTH;

public class Player extends SnakebookEntity {


    private static final String TEXTURE_FILE_NAME = "book1.png";
    private static final float DEFAULT_PLAYER_SPEED = 0.5f;
    private static final float CAMERA_MOTION_EASE = 0.1f;
    private static final float SPINNING_EASE_SPEED = 0.008f;
    private static final float BODY_DISTANCE_OFFSET = 6f;


    private Sprite sprite;
    private final Texture texture;
    private Vector2 direction;
    private float angle = 0;
    private float angleTarget = 0;
    private float energy = 100;
    private int scale = 1;
    private int length = 1;

    private final LinkedList<PlayerBodyPart> bodyParts = new LinkedList<>();

    private boolean isCameraXAxisLocked = true;
    private boolean isCameraYAxisLocked = true;

    private boolean isSprinting = false;

    private float spinVelocity = 0;
    private final float maxAcceleration = SPINNING_EASE_SPEED * 1.777777f;

    public Player(Location location) {
        super(location);
        this.direction = new Vector2(0, 0);
        this.texture = new Texture(Gdx.files.internal(TEXTURE_FILE_NAME));
    }

    @Override
    public void create(World world) {
        this.sprite = new Sprite(this.texture);

        PlayerBodyPart headPart = new PlayerBodyPart(this.getLocation(), getTexture());
        headPart.setHead(true);
        this.bodyParts.add(headPart);

        for (PlayerBodyPart bodyPart : this.bodyParts) {
            bodyPart.create(world);
        }
    }

    @Override
    public void render(World world) {
        this.renderInput(world);
        if (world.isOutOfBonds(this.getLocation())) {
            System.out.println("Dead");
            // TODO: Dead
        }

        this.length = this.bodyParts.size();

        // Translates towards direction
        this.getLocation().add(this.direction.x, this.direction.y);

        this.sprite.setX(getLocation().getX());
        this.sprite.setY(getLocation().getY());

        // Mouse rotation. Please, search on Google :( and found something similar
        // https://codepen.io/anon/pen/HGnDF/
        float dy = -(Gdx.input.getY() - (float) (Gdx.graphics.getHeight() - 32) / 2);
        float dx = Gdx.input.getX() - (float) (Gdx.graphics.getWidth() - 32) / 2;
        float theta = (float) Math.atan2(dy, dx);
        theta -= Math.PI / 2;
        float dTheta = (angleTarget - angle);
        if (dTheta > Math.PI) {
            angle += Math.PI * 2;
        } else if (dTheta < -Math.PI) {
            angle -= Math.PI * 2;
        }

        this.angleTarget = theta;
        float targetSpinVel = (angleTarget - angle) * SPINNING_EASE_SPEED;
        this.spinVelocity = this.clip(
                targetSpinVel,
                this.spinVelocity - this.maxAcceleration,
                this.spinVelocity + this.maxAcceleration
        );

        this.angle += spinVelocity;
        this.sprite.setRotation((float) Math.toDegrees(this.angle));
        this.isSprinting = Gdx.input.isKeyPressed(Input.Keys.SPACE);

        // Check player is sprinting
        float currentSpeed = DEFAULT_PLAYER_SPEED + 0.01f / scale;
        if (isSprinting && energy > 0) {
            currentSpeed = DEFAULT_PLAYER_SPEED * 1.2f;
            this.energy--;
        } else this.energy++;

        // Set direction
        this.direction = this.direction.set(
                -1f * (float) Math.sin(Math.toDegrees(this.angle)
                        * Gdx.graphics.getDeltaTime()) * currentSpeed,
                (float) Math.cos(Math.toDegrees(this.angle)
                        * Gdx.graphics.getDeltaTime()) * currentSpeed
        );

        // Camera boundaries
        this.isCameraXAxisLocked = !(this.getLocation().getX() > (float) WORLD_WIDTH / 2
                || this.getLocation().getX() < ((float) WORLD_WIDTH / 2) * -1);
        this.isCameraYAxisLocked = !(this.getLocation().getY() > (float) WORLD_HEIGHT / 2)
                && !(this.getLocation().getY() < ((float) WORLD_HEIGHT / 2) * -1);

        if (this.isCameraXAxisLocked) {
            world.getCamera().position.x
                    += (this.getLocation().x - world.getCamera().position.x) * CAMERA_MOTION_EASE;
        }
        if (this.isCameraYAxisLocked) {
            world.getCamera().position.y
                    += (this.getLocation().y - world.getCamera().position.y) * CAMERA_MOTION_EASE;
        }

        // Relocate the body with parts
        Iterator<PlayerBodyPart> bodyPartIterator = this.bodyParts.iterator();
        PlayerBodyPart previousBodyPart = null;
        PlayerBodyPart currentBodyPart;
        while (bodyPartIterator.hasNext()) {
            currentBodyPart = bodyPartIterator.next();
            if (currentBodyPart.isHead()) {
                currentBodyPart.setAngle(angle);
                currentBodyPart.setDirection(new Vector(this.direction.x, this.direction.y));
            }

            // Set location
            if (previousBodyPart != null) {
                float angle = (float) ((float) Math.atan2(
                        previousBodyPart.location.y - currentBodyPart.location.y,
                        previousBodyPart.location.x - currentBodyPart.location.x
                ) - (Math.PI / 2));
                currentBodyPart.setAngle(angle);
                float distance = (float) new Location(currentBodyPart.location.x, currentBodyPart.location.y)
                        .distance(previousBodyPart.location) - (32 + BODY_DISTANCE_OFFSET);
                currentBodyPart.setDirection(new Vector(
                                -1f * ((float) Math.sin(Math.toDegrees(angle) * Gdx.graphics.getDeltaTime())) * distance,
                                (float) Math.cos(Math.toDegrees(angle) * Gdx.graphics.getDeltaTime()) * distance
                        )
                );
            }

            currentBodyPart.render(world);
            previousBodyPart = currentBodyPart;
        }
    }

    private void renderInput(World w) {
    }

    private float clip(float x, float min, float max) {
        return x < min ? min : Math.min(x, max);
    }

    @Override
    public void dispose(World world) {
        this.texture.dispose();
    }

    @Override
    public Texture getTexture() {
        return this.texture;
    }

    public Location getHeadLocation() {
        Vector2 direction = new Vector2(this.direction).scl(3);
        return new Location(this.getLocation().x,
                this.getLocation().y).add(direction.x, direction.y);
    }

    public PlayerBodyPart createPart(World w) {
        Location location = new Location(
                -(this.getLocation().x - this.direction.x),
                -(this.getLocation().y - this.direction.y)
        );
        PlayerBodyPart playerBodyPart = new PlayerBodyPart(location, getTexture());
        playerBodyPart.setAngle((float) Math.atan2(
                this.getLocation().y - this.direction.y,
                this.getLocation().x - this.direction.x
        ));
        playerBodyPart.create(w);
        this.bodyParts.add(playerBodyPart);
        return playerBodyPart;
    }
}
