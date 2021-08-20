package raceagainst.racecourse;

import raceagainst.graphics.Shader;
import raceagainst.graphics.Texture;
import raceagainst.graphics.VertexArray;

import java.util.Random;

/** Creates a race course for the game and manages game updates. */
public class RaceCourse {

    public static float startingLineY = -5.6f;
    public static float width = 20.0f;
    public static float height = 11.25f;
    public static float halfWidth = width / 2;
    public static float halfHeight = height / 2;

    private VertexArray background;
    private Texture bgTexture;
    private Car playerCar;
    private Car nonPlayerCar;
    private Obstacle[] playerObstacles;
    private Obstacle[] nonPlayerObstacles;
    private Random random;
    private int obstacleNum = 10;
    private float obstacleInterval = 1.0f;

    /** Constructs a race course.
     * Index 0 = bottom left corner
     * Index 1 = top left corner
     * Index 2 = top right corner
     * Index 3 = bottom right corner */
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

        playerCar = new Car("player");
        nonPlayerCar = new Car("npc");

        playerObstacles = new Obstacle[obstacleNum];
        nonPlayerObstacles = new Obstacle[obstacleNum];
        random = new Random();
        createObstacles();
    }

    /** Updates the race course. */
    public void update() {
        playerCar.update();
        nonPlayerCar.update();
    }

    /** Renders the race course, including background, cars and obstacles. */
    public void render() {
        // Render the race background.
        Shader.BG.enable();
        bgTexture.bind();
        background.render();
        bgTexture.unbind();
        Shader.BG.disable();

        // Render the cars.
        playerCar.render();
        nonPlayerCar.render();

        // Render the obstacles.
        for (Obstacle o: playerObstacles) {
            o.render();
        }
        for (Obstacle o: nonPlayerObstacles) {
            o.render();
        }
    }

    /** Creates obstacles with position in random x values,
     * with a set interval between the y values of obstacles.
     */
    public void createObstacles() {
        float yVal = -halfHeight + 1.5f; // y-value of the obstacle

        for (int i = 0; i < obstacleNum; i++) {
            // Player window width: [-10.0f, 0.0f]
            // Make sure x value - (blockWidth / 2) > -10.0f (not outside left edge)
            //       and x value + (blockWidth / 2) <   0.0f (not outside right edge)
            float xVal = -10.0f * random.nextFloat();
            boolean outOfScreen = true;

            while (outOfScreen) {
                if (xVal - (Obstacle.blockWidth / 2) > -10.0f &&
                        xVal + (Obstacle.blockWidth / 2) < 0.0f) {
                    outOfScreen = false;
                } else {
                    xVal = -10.0f * random.nextFloat();
                }
            }

            playerObstacles[i] = new Obstacle(xVal, yVal, 0.5f);
            yVal += obstacleInterval;
        }

        for (int i = 0; i < obstacleNum; i++) {
            // NPC window width: [0.0f, 10.0f] = player value shifted over by +10.0f
            Obstacle playerObs = playerObstacles[i];
            nonPlayerObstacles[i] = new Obstacle(playerObs.getX() + 10.0f, playerObs.getY(), playerObs.getZ());
        }
    }
}
