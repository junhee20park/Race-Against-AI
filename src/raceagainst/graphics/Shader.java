package raceagainst.graphics;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import raceagainst.math.Matrix4f;
import raceagainst.math.Vector3f;
import raceagainst.utils.ShaderUtils;


/** Creates shader programs.
 * @source Adapted from TheCherno's Flappy tutorial Shader class.
 *         https://www.youtube.com/watch?v=527bR2JHSR0&t=1266s
 */

public class Shader {

    public static final int VERTEX_ATTRIB = 0;
    public static final int TCOORD_ATTRIB = 1;

    public static Shader BG, CAR, OBSTACLE;

    private int ID;
    private Map<String, Integer> locationCache = new HashMap<String, Integer>();

    public Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
    }

    public static void loadAllShaders() {
        BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
        CAR = new Shader("shaders/car.vert", "shaders/car.frag");

        //TODO: write the vertex and fragment shader for obstacle
        OBSTACLE = new Shader("shaders/car.vert", "shaders/car.frag");
    }

    public int getUniform(String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        }
        int result = glGetUniformLocation(ID, name);

        if (result == -1) {
            System.err.println("Could not find uniform variable '" + name + "'!");
        } else {
            locationCache.put(name, result);
        }
        return result;
    }

    public void setUniform1i(String name, int value) {
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float x, float y) {
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform3f(String name, Vector3f vector) {
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(ID);
    }

    public void disable() {
        glUseProgram(0);
    }
}
