package raceagainst;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import raceagainst.input.Input;
import raceagainst.racecourse.RaceCourse;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.opengl.GL11.GL_TRUE;


public class Main {

    private int width = 300; //1280
    private int height = 300; //800

    private boolean running;

    private long window;
    private RaceCourse rc;

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
        window = glfwCreateWindow(300, 300, "Race Menu Screen", NULL, NULL);
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
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        //TODO: Set the projection matrices for all objects

        //Creates new race course
        rc = new RaceCourse();
    }

    public void run() {
        init();


        //For Later: Implement game play mode when player plays space
        /*
        if (Input.isKeyDown(GLFW_KEY_SPACE) {
            render new background and start timer.
        }
         */

        while (running) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the frame buffer

            if (Input.isKeyDown(GLFW_KEY_SPACE)) {
                glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
            }
            //Stops while loop if window closes.
            if (glfwWindowShouldClose(window)) {
                running = false;
            }
            glfwPollEvents();
            glfwSwapBuffers(window);
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
}
