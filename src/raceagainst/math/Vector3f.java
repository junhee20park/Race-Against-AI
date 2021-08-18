package raceagainst.math;

/** Creates vertices.
 * @source Pulled from TheCherno's Flappy tutorial Vertex3f class.
 *         https://www.youtube.com/watch?v=527bR2JHSR0&t=1266s
 */
public class Vector3f {

    public float x, y, z;

    public Vector3f() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
