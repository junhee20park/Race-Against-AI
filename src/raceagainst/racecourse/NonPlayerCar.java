package raceagainst.racecourse;

import raceagainst.graphics.Shader;
import raceagainst.graphics.Texture;
import raceagainst.graphics.VertexArray;
import raceagainst.math.Matrix4f;
import raceagainst.math.Vector3f;

public class NonPlayerCar {

    private float CAR_SIZE = 3.0f;
    private VertexArray car;
    private Texture carTexture;
    private Vector3f position;

    /** Constructs a player car.
     * Index 0 = bottom left corner
     * Index 1 = top left corner
     * Index 2 = top right corner
     * Index 3 = bottom right corner */
    public NonPlayerCar() {
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
        position = new Vector3f(5.0f, -5.60f, 0.0f);
    }

    public void update() {
        float xDelta = 0.05f;
        float yDelta = 0.02f;
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
