package raceagainst.racecourse;

import raceagainst.graphics.Shader;
import raceagainst.graphics.Texture;
import raceagainst.graphics.VertexArray;

public class StartMenu {

    private VertexArray background;
    private Texture bgTexture;

    public StartMenu() {
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
        bgTexture = new Texture("res/startbg.png");
    }

    public void render() {
        Shader.BG.enable();
        bgTexture.bind();
        background.render();
        bgTexture.unbind();
        Shader.BG.disable();
    }
}
