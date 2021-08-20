package raceagainst.racecourse;

import raceagainst.graphics.Shader;
import raceagainst.graphics.Texture;
import raceagainst.graphics.VertexArray;
import raceagainst.math.Matrix4f;
import raceagainst.math.Vector3f;

/** Represents the obstacles in the race course.*/
public class Obstacle {

    public static float blockWidth = 2.0f;
    public static float blockHeight = 0.2f;

    private VertexArray block;
    private Texture blockTexture;
    private Vector3f position;

    /** Constructs an obstacle -- a barrier-like object on the road.
     * Index 0 = bottom left corner
     * Index 1 = top left corner
     * Index 2 = top right corner
     * Index 3 = bottom right corner */
    public Obstacle(float x, float y, float z) {
        float[] vertices = {
                -blockWidth / 2, -blockHeight / 2, 0.5f,
                -blockWidth / 2,  blockHeight / 2, 0.5f,
                 blockWidth / 2,  blockHeight / 2, 0.5f,
                 blockWidth / 2, -blockHeight / 2, 0.5f
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

        block = new VertexArray(vertices, indices, textureCoordinates);
        //TODO: change this when I actually have an obstacle png
        blockTexture = new Texture("res/obstacle.jpeg");
        position = new Vector3f(x, y, z);
    }

    public void render() {
        Shader.OBSTACLE.enable();
        blockTexture.bind();
        Shader.OBSTACLE.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
        block.render();
        blockTexture.unbind();
        Shader.CAR.disable();
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getZ() {
        return position.z;
    }
}
