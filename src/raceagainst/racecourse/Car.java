package raceagainst.racecourse;

import raceagainst.graphics.Shader;
import raceagainst.graphics.Texture;
import raceagainst.graphics.VertexArray;
import raceagainst.input.Input;
import raceagainst.math.Matrix4f;
import raceagainst.math.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

/** Manages both the player and the NPC cars. */
public class Car {

    Obstacle[] obstacles;

    public float carWidth = 0.5f;
    public float carHeight = 0.8f;
    public ArrayList<Vector3f> npcPath;

    // Manages the speed of the NPC car.
    private int npcCarSpeed = 0;
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
                -carWidth / 2.0f, -carHeight / 2.0f, 0.5f,
                -carWidth / 2.0f,  carHeight / 2.0f, 0.5f,
                carWidth / 2.0f,  carHeight / 2.0f, 0.5f,
                carWidth / 2.0f, -carHeight / 2.0f, 0.5f
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
            position = new Vector3f(-RaceCourse.halfWidth / 2.0f, RaceCourse.startingLineY, 0.0f);
            isPlayer = true;
            npcPath = null;
        } else {
            position = new Vector3f(RaceCourse.halfWidth / 2.0f, RaceCourse.startingLineY, 0.0f);
            isPlayer = false;
        }
    }

    /** Updates the position of the car. */
    public void update() {
        float xDelta = 0.02f;
        float yDelta = 0.01f;

        if (isPlayer)
            playerUpdate(xDelta, yDelta);
        if (!isPlayer)
            npcUpdate();
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

    public float getY() {
        return position.y;
    }

    /** Helper method: Updates the position of the player car based on player input. */
    private void playerUpdate(float xDelta, float yDelta) {
        if (Input.isKeyDown(GLFW_KEY_LEFT)) {
            if (!wallCollision(-xDelta) && !obsCollision("left", -xDelta, 0.0f)) {
                position.x -= xDelta;
            }
        } else if (Input.isKeyDown(GLFW_KEY_RIGHT)) {
            if (!wallCollision(xDelta) && !obsCollision("right", xDelta, 0.0f)) {
                position.x += xDelta;
            }
        }
        if (!obsCollision("up", 0.0f, yDelta)) {
            position.y += yDelta;
        }
    }

    /** Helper method: Updates the position of the npc car. */
    private void npcUpdate() {
        if (npcPath.size() != 0 && npcCarSpeed == 7) {
            // Sets the npc car position to the next position in the npc path.
            Vector3f newPos = npcPath.remove(npcPath.size() - 1);
            position.x = newPos.getX();
            position.y = newPos.getY();
            npcCarSpeed = 0;
        } else {
            npcCarSpeed += 1;
        }
    }

    /** Helper method: Checks if car will collide into a wall
     * (edges of screen or middle of screen). */
    private Boolean wallCollision(float xDelta) {
        if (position.x + xDelta <= -9.5f) {
            return true;
        } else if (position.x + xDelta >= 0.0f) {
            return true;
        } else {
            return false;
        }
    }

    /** Helper method: Checks if car will collide into an obstacle. */
    private Boolean obsCollision(String checkType, float xDelta, float yDelta) {
        float carX = position.x;
        float carY = position.y;
        boolean skip;

        float carLeft = carX - (carWidth / 2.0f);
        float carRight = carX + (carWidth / 2.0f);
        float carFront = carY + (carHeight / 2.0f);
        float carBottom = carY - (carHeight / 2.0f);

        for (int i = 0; i < obstacles.length; i++) {
            float obsX = obstacles[i].getX();
            float obsY = obstacles[i].getY();

            float obsLeft = obsX - (Obstacle.blockWidth / 2.0f);
            float obsRight = obsX + (Obstacle.blockWidth / 2.0f);
            float obsTop = obsY + (Obstacle.blockHeight / 2.0f);
            float obsBottom = obsY - (Obstacle.blockHeight / 2.0f);

            // Skip the check on obstacles where car does not reach it.
            // Too much in front              OR         too behind
            if (carFront + yDelta < obsBottom || carBottom + yDelta > obsTop) {
                skip = true;
            } else {
                skip = false;
            }

            if (!skip) {
                // Checks if a move up would result in collision.
                if (checkType.equals("up")) {
                    if (obsLeft < carRight && carLeft < obsRight) {
                        if (carFront + yDelta > obsBottom && carBottom + yDelta < obsTop) {
                            return true;
                        }
                    }
                }

                // Checks if a move to the left would result in collision.
                if (checkType.equals("left")) {
                    if (carLeft + xDelta < obsRight && carRight > obsRight && carFront > obsBottom && carBottom < obsTop) {
                        return true;
                    }
                }

                // Checks if a move to the right would result in collision.
                if (checkType.equals("right")) {
                    if (carRight + xDelta > obsLeft && carLeft < obsLeft && carFront > obsBottom && carBottom < obsTop) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}