package raceagainst;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import raceagainst.graphics.Shader;
import raceagainst.input.Input;
import raceagainst.math.Matrix4f;
import raceagainst.racecourse.RaceCourse;
import raceagainst.racecourse.StartMenu;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.opengl.GL11.GL_TRUE;


public class Main {

    private int width = 1280;
    private int height = 800;

    private boolean running = false;
    private boolean initWindowOpened = false;

    private long window;
    private RaceCourse rc;
    private StartMenu startMenu;

    public void init() {
        // Initialize the glfw library, and handle errors.
        //  GLFW is an open source library for OpenGL development on the desktop.
        if (!glfwInit())
            throw new IllegalStateException("Failed to initialize the glfw library.");

        // Configures GLFW.
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        running = true;

        // Creates window.
        window = glfwCreateWindow(width, height, "Race Menu Screen", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the window.");

        // Gets the resolution of the primary monitor.
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Centers the window. (upper left corner)
        glfwSetWindowPos(
                window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        //Creates key callback class to the window.
        glfwSetKeyCallback(window, new Input());

        // Makes the OpenGL context current.
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);
        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);

        Shader.loadAllShaders();

        //TODO: Set the projection matrices for all objects
        Shader.BG.enable();
        Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BG.setUniform1i("tex", 1);
        Shader.BG.disable();

        Shader.PLAYER_CAR.enable();
        Shader.PLAYER_CAR.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.PLAYER_CAR.setUniform1i("tex", 1);
        Shader.PLAYER_CAR.disable();

        //TODO: Draw the game start screen
        startMenu = new StartMenu();
        //glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    }

    /** While game is running, updates and renders. */
    public void run() {
        init();
        initWindowOpen();

        //TODO: Later - Set up timer, count down to race start

        while (running) {
            //Stops while loop if window closes.
            if (glfwWindowShouldClose(window)) {
                running = false;
            }

            update();
            render();

            int i = glGetError();
            if (i != GL_NO_ERROR) {
                System.out.println(i);
            }
        }
    }

    public void update() {
        glfwPollEvents();
        rc.update();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clears the frame buffer.
        rc.render();
        glfwSwapBuffers(window);
    }

    /** Renders the start game screen continuously until the player
     * presses SPACE. After SPACE presses, creates a new RaceCourse.
     */
    private void initWindowOpen() {
        while (!initWindowOpened) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (Input.isKeyDown(GLFW_KEY_SPACE)) {
                //TODO: Draw the race start screen
                rc = new RaceCourse();
                initWindowOpened = true;
            }

            //Stops while loop if window closes.
            if (glfwWindowShouldClose(window)) {
                running = false;
            }

            glfwPollEvents();
            startMenu.render();
            glfwSwapBuffers(window);
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
}
