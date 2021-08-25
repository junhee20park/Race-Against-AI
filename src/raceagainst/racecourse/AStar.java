package raceagainst.racecourse;

import raceagainst.math.Vector3f;

import java.util.*;

/**
 * @source
 */
public class AStar {

    private static class Node implements Comparable {
        float x;
        float y;
        float g; // Cost from start to current node
        float h; // Cost from current node to finish
        Node parent;

        public Node() {

        }

        public Node(Node parent, float x, float y, float g, float h) {
            this.parent = parent;
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
        }

        // Compares nodes using the f (g+h) values.
        @Override
        public int compareTo(Object o) {
            Node other = (Node) o;
            if (this.g + this.h > (other.g + other.h)) {
                return 1;
            } else if (this.g + this.h == (other.g + other.h)) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public static ArrayList<Vector3f> pathSearch(ArrayList<Vector3f> nodesInput) {
        TreeSet<Node> open = new TreeSet<>();
        HashSet<Node> closed = new HashSet<>();
        ArrayList<Vector3f> path = new ArrayList<>();
        Node currNode = new Node();

        float startNodeX = 5.0f; //RaceCourse.halfWidth / 2.0f;
        float startNodeY = -5.6f; // RaceCourse.startingLineY;
        float finishLineY = RaceCourse.halfHeight - 0.4f;
        Node startNode = new Node(null, startNodeX, startNodeY, 11.25f, 0.0f);
        open.add(startNode);

        while (!open.isEmpty()) {
            currNode = open.first();

            if (currNode.y >= finishLineY) {
                break;
            }

            HashSet<Node> neighbors = getNeighbors(currNode, nodesInput);
            for (Node n: neighbors) {
                float fromCurrCost = currNode.g + distFromNode(currNode, n.x, n.y);
                if (closed.contains(n)) {
                    // Ignore
                } else {
                    if (!open.contains(n)) {
                        n.parent = currNode;
                        n.g = fromCurrCost;
                        open.add(n);
                    } else {
                        if (fromCurrCost < n.g) {
                            n.parent = currNode;
                            n.g = fromCurrCost;
                        }
                    }
                }
            }

            open.remove(currNode);
            closed.add(currNode);
        }

        while (currNode.parent != null) {
            path.add(new Vector3f(currNode.x, currNode.y, 0.5f));
            currNode = currNode.parent;
        }

        return path;
    }

    private static HashSet<Node> getNeighbors(Node curr, ArrayList<Vector3f> nodesInput) {
        HashSet<Node> neighbors = new HashSet<>();
        for (Vector3f vec: nodesInput) {
            if (Math.abs(curr.x - vec.getX()) <= 0.2f) {
                if (vec.getY() > curr.y && Math.abs(curr.y - vec.getY()) <= 0.2f) {
                    Node n = new Node(curr, vec.getX(), vec.getY(), 0.0f, vec.getZ());
                    neighbors.add(n);
                }
            }
        }
        return neighbors;
    }

    /** Calculates the distance from given node to the given x and y coordinates. */
    private static float distFromNode(Node given, float x, float y) {
        return (float) Math.sqrt(Math.pow((given.x - x), 2.0f) + Math.pow((given.y - y), 2.0f));
    }
}
