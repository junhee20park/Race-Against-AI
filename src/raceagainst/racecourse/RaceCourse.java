package raceagainst.racecourse;

import raceagainst.graphics.Shader;
import raceagainst.graphics.Texture;
import raceagainst.graphics.VertexArray;
import raceagainst.math.Vector3f;

import java.util.*;

/** Creates a race course for the game and manages game updates. */
public class RaceCourse {

    public static float startingLineY = -5.6f;
    public static float width = 20.0f;
    public static float height = 11.25f;
    public static float halfWidth = width / 2.0f;
    public static float halfHeight = height / 2.0f;

    private VertexArray background;
    private Texture bgTexture;
    private Car playerCar;
    private Car nonPlayerCar;
    private Obstacle[] playerObs;
    private Obstacle[] nonPlayerObs;
    private Random random;
    private int obstacleNum = 9;
    private float obstacleInterval = 1.2f;

    /**
     * Constructs a race course.
     * Index 0 = bottom left corner
     * Index 1 = top left corner
     * Index 2 = top right corner
     * Index 3 = bottom right corner
     */
    public RaceCourse() {
        float[] vertices = new float[]{
                -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
                -10.0f, 10.0f * 9.0f / 16.0f, 0.0f,
                10.0f, 10.0f * 9.0f / 16.0f, 0.0f,
                10.0f, -10.0f * 9.0f / 16.0f, 0.0f
        };

        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = new float[]{
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        background = new VertexArray(vertices, indices, tcs);
        bgTexture = new Texture("res/racebg.png");

        playerCar = new Car("player");
        nonPlayerCar = new Car("npc");

        playerObs = new Obstacle[obstacleNum];
        java.util.Arrays.fill(playerObs, new Obstacle(0.0f, 0.0f, 0.0f));
        nonPlayerObs = new Obstacle[obstacleNum];
        java.util.Arrays.fill(nonPlayerObs, new Obstacle(0.0f, 0.0f, 0.0f));

        random = new Random();
        createObstacles();
        nonPlayerCar.npcPath = calculateNPCPath();
    }

    /**
     * Updates the race course.
     */
    public void update() {
        playerCar.update();
        nonPlayerCar.update();
        float carFront = playerCar.getY() + playerCar.carHeight / 2.0f;
        float npcCarFront = nonPlayerCar.getY() + nonPlayerCar.carHeight / 2.0f;
        if (carFront >= 10.0f * 9.0f / 16.0f && npcCarFront >= 10.0f * 9.0f / 16.0f) {
            System.out.println("It's a tie!");
        } else if (carFront >= 10.0f * 9.0f / 16.0f) {
            System.out.println("You win!");
        } else if (npcCarFront >= 10.0f * 9.0f / 16.0f) {
            System.out.println("AI won!");
        }
    }

    /**
     * Renders the race course, including background, cars and obstacles.
     */
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
        for (Obstacle o : playerObs) {
            o.render();
        }
        for (Obstacle o : nonPlayerObs) {
            o.render();
        }
    }

    /**
     * Creates obstacles with position in random x values,
     * with a set interval between the y values of obstacles.
     */
    public void createObstacles() {
        float yVal = -halfHeight + 1.5f; // y-value of the obstacle

        for (int i = 0; i < obstacleNum; i++) {
            // Player window width: [-10.0f, 0.0f]
            // Make sure x value - (blockWidth / 2.0f) > -10.0f (not outside left edge)
            //       and x value + (blockWidth / 2.0f) <   0.0f (not outside right edge)
            float xVal = -10.0f * random.nextFloat();
            boolean outOfScreen = true;

            while (outOfScreen) {
                if (xVal - (Obstacle.blockWidth / 2.0f) > -10.0f &&
                        xVal + (Obstacle.blockWidth / 2.0f) < 0.0f) {
                    outOfScreen = false;
                } else {
                    xVal = -10.0f * random.nextFloat();
                }
            }

            playerObs[i] = new Obstacle(xVal, yVal, 0.5f);
            yVal += obstacleInterval;
        }

        for (int i = 0; i < obstacleNum; i++) {
            // NPC window width: [0.0f, 10.0f] = player value shifted over by +10.0f
            Obstacle obs = playerObs[i];
            nonPlayerObs[i] = new Obstacle(obs.getX() + 10.0f, obs.getY(), obs.getZ());
        }

        playerCar.obstacles = playerObs;
        nonPlayerCar.obstacles = nonPlayerObs;
    }

    /** Returns an ArrayList of points that makes up the shortest path to the finish line
     * that the NPC car will take. */
    private ArrayList<Vector3f> calculateNPCPath() {
        ArrayList<Vector3f> nodes = new ArrayList<>();

        // Treats the racing course as a 0.01 x 0.01 square grid.
        // Adds all non-obstacles squares to the ArrayList nodes,
        //  with the z-value in vector as the heuristic to the finish line.
        for (float y = startingLineY + 0.1f; y < halfHeight; y += 0.1f) {
            for (float x = 0.0f; x < halfWidth; x += 0.1f) {
                if (!obsCollision(x, y) && !wallCollision(x, y)) {
                    nodes.add(new Vector3f(x, y, distToFinish(x, y)));
                }
            }
        }
        return AStar.pathSearch(nodes);
    }

    /** Checks if a position point would result in the NPC car colliding into an obstacle. */
    private Boolean obsCollision(float x, float y) {
        float carLeft = x - (nonPlayerCar.carWidth / 2.0f);
        float carRight = x + (nonPlayerCar.carWidth / 2.0f);
        float carFront = y + (nonPlayerCar.carHeight / 2.0f);
        float carBottom = y - (nonPlayerCar.carHeight / 2.0f);

        for (int i = 0; i < nonPlayerObs.length; i++) {
            float obsX = nonPlayerObs[i].getX();
            float obsY = nonPlayerObs[i].getY();

            float obsLeft = obsX - (Obstacle.blockWidth / 2.0f);
            float obsRight = obsX + (Obstacle.blockWidth / 2.0f);
            float obsTop = obsY + (Obstacle.blockHeight / 2.0f);
            float obsBottom = obsY - (Obstacle.blockHeight / 2.0f);

            // Checks for collision
            if (obsLeft < carRight && carLeft < obsRight) {
                if (carFront > obsBottom && carBottom < obsTop) {
                    return true;
                }
            }

            if (carLeft < obsRight && carRight > obsRight && carFront > obsBottom && carBottom < obsTop) {
                return true;
            }

            if (carRight > obsLeft && carLeft < obsLeft && carFront > obsBottom && carBottom < obsTop) {
                return true;
            }
        }
        return false;
    }

    /** Checks if a position point would result in the NPC car colliding into a wall. */
    private Boolean wallCollision(float x, float y) {
        float carLeft = x - (nonPlayerCar.carWidth / 2.0f);
        float carRight = x + (nonPlayerCar.carWidth / 2.0f);

        if (carLeft < 0.0f || carRight > 10.0f) {
            return true;
        }
        return false;
    }

    /** Calculates the distance to the finish point for the NPC car. */
    private float distToFinish(float x, float y) {
        float finX = halfWidth / 2.0f;
        float finY = halfHeight;

        return (float) Math.sqrt(Math.pow((finX - x), 2.0f) + Math.pow((finY - y), 2.0f));
    }
}
