package raceagainst.racecourse;

import raceagainst.graphics.Shader;
import raceagainst.graphics.Texture;
import raceagainst.graphics.VertexArray;
import raceagainst.input.Input;
import raceagainst.math.Matrix4f;
import raceagainst.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Car {

    private float carWidth = 3.0f;
    private float carHeight = 4.0f;
    private boolean isPlayer;

    private VertexArray car;
    private Texture carTexture;
    private Vector3f position;

    /** Constructs a car.
     * Index 0 = bottom left corner
     * Index 1 = top left corner
     * Index 2 = top right corner
     * Index 3 = bottom right corner */
    public Car(String carType) {
        float[] vertices = {
                -carWidth / 2, -carHeight / 2, 0.5f,
                -carWidth / 2,  carHeight / 2, 0.5f,
                 carWidth / 2,  carHeight / 2, 0.5f,
                 carWidth / 2, -carHeight / 2, 0.5f
        };

        byte[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        float[] textureCoordinates = {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        car = new VertexArray(vertices, indices, textureCoordinates);
        carTexture = new Texture("res/car.png");

        if (carType.equals("player")) {
            position = new Vector3f(-RaceCourse.halfWidth / 2, RaceCourse.startingLineY, 0.0f);
            isPlayer = true;
        } else {
            position = new Vector3f(RaceCourse.halfWidth / 2, RaceCourse.startingLineY, 0.0f);
            isPlayer = false;
        }
    }

    /** Updates the position of the car. */
    public void update() {
        float xDelta = 0.05f;
        float yDelta = 0.02f;

        if (isPlayer)
            playerUpdate(xDelta, yDelta);
        if (!isPlayer)
            npcUpdate(xDelta, yDelta);
    }

    /** Renders the car, setting an ml_matrix that reflects updated positions. */
    public void render() {
        Shader.CAR.enable();
        carTexture.bind();
        Shader.CAR.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
        car.render();
        carTexture.unbind();
        Shader.CAR.disable();
    }

    /** Helper method: Updates the position of the player car based on player input. */
    private void playerUpdate(float xDelta, float yDelta) {
        if (Input.isKeyDown(GLFW_KEY_LEFT)) {
            if (!wallCollision(-xDelta)) {
                position.x -= xDelta;
            }
        } else if (Input.isKeyDown(GLFW_KEY_RIGHT)) {
            if (!wallCollision(xDelta)) {
                position.x += xDelta;
            }
        }
        position.y += yDelta;
    }

    /** Helper method: Updates the position of the npc car. */
    private void npcUpdate(float xDelta, float yDelta) {
        position.y += yDelta;
    }

    /** Helper method: Checks if car will collide into a wall
     * (edges of screen or middle of screen). */
    private Boolean wallCollision(float xDelta) {
        if (position.x + xDelta <= -9.5f) {
            return true;
        }
        if (position.x + xDelta >= 0.0f) {
            return true;
        }
        return false;
    }

    /* private Boolean obstacleCollision() {

    } */
}
