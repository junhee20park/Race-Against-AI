package raceagainst.racecourse;

import raceagainst.graphics.Shader;
import raceagainst.graphics.Texture;
import raceagainst.graphics.VertexArray;

public class RaceCourse {

    private VertexArray background;
    private Texture bgTexture;
    private PlayerCar playerCar;

    public RaceCourse() {
        float[] vertices = new float[] {
                -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
                -10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
                 10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
                 10.0f, -10.0f * 9.0f / 16.0f, 0.0f
        };

        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = new float[] {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        background = new VertexArray(vertices, indices, tcs);
        bgTexture = new Texture("res/racebg.png");
        playerCar = new PlayerCar();
    }

    public void update() {
        playerCar.update();
    }

    public void render() {
        // Render the race background.
        Shader.RACE_BG.enable();
        bgTexture.bind();
        background.render();
        bgTexture.unbind();
        Shader.RACE_BG.disable();

        // Render the player car.
        playerCar.render();
    }
}
