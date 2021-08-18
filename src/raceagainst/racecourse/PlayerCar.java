package raceagainst.racecourse;

import raceagainst.graphics.Shader;
import raceagainst.graphics.Texture;
import raceagainst.graphics.VertexArray;
import raceagainst.input.Input;
import raceagainst.math.Matrix4f;
import raceagainst.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerCar {

    private float CAR_SIZE = 3.0f;
    private VertexArray car;
    private Texture carTexture;
    private Vector3f position;

    public PlayerCar() {
        float[] vertices = {
                -CAR_SIZE / 2, -CAR_SIZE / 2, 0.5f,
                -CAR_SIZE / 2,  CAR_SIZE / 2, 0.5f,
                 CAR_SIZE / 2,  CAR_SIZE / 2, 0.5f,
                 CAR_SIZE / 2, -CAR_SIZE / 2, 0.5f
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
        carTexture = new Texture("res/playercar.png");
        position = new Vector3f(-5.0f, -5.60f, 0.0f);
    }

    public void update() {
        float xDelta = 0.05f;
        float yDelta = 0.02f;

        if (Input.isKeyDown(GLFW_KEY_LEFT)) {
            if (position.x - xDelta <= -9.5) {
                return;
            }
            position.x -= xDelta;
        } else if (Input.isKeyDown(GLFW_KEY_RIGHT)) {
            if (position.x + xDelta >= 0.0f) {
                return;
            }
            position.x += xDelta;
        }
        position.y += yDelta;
    }

    public void render() {
        Shader.PLAYER_CAR.enable();
        carTexture.bind();
        Shader.PLAYER_CAR.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
        car.render();
        carTexture.unbind();
        Shader.PLAYER_CAR.disable();
    }
}
