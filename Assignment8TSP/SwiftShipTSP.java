
import java.util.*;

public class SwiftShipTSP {
    

    static int N = 4;
    static int[][] cost = {
        {0, 10, 15, 20},
        {10, 0, 35, 25},
        {15, 35, 0, 30},
        {20, 25, 30, 0}
    };

    static class Node {
        int city;
        int level;
        int totalCost;
        int bound;
        ArrayList<Integer> path;

        Node(int city, int level, int totalCost,
             ArrayList<Integer> path) {
            this.city = city;
            this.level = level;
            this.totalCost = totalCost;
            this.path = new ArrayList<>(path);
            this.bound = calculateBound(this);
        }
    }

    // Lower bound for the remaining route
    static int calculateBound(Node node) {
        int bound = node.totalCost;
        boolean[] visited = new boolean[N];

        for (int city : node.path) {
            visited[city] = true;
        }

        // Minimum outgoing edge for each unvisited city
        for (int i = 0; i < N; i++) {
            if (!visited[i]) {
                int min = Integer.MAX_VALUE;

                for (int j = 0; j < N; j++) {
                    if (i != j) {
                        min = Math.min(min, cost[i][j]);
                    }
                }

                bound += min;
            }
        }

        // Minimum edge to return to the starting city
        int minReturn = Integer.MAX_VALUE;
        for (int i = 1; i < N; i++) {
            minReturn = Math.min(minReturn, cost[i][0]);
        }

        bound += minReturn;
        return bound;
    }

    static void solve() {
        PriorityQueue<Node> pq = new PriorityQueue<>(
            (a, b) -> Integer.compare(a.bound, b.bound)
        );

        ArrayList<Integer> startPath = new ArrayList<>();
        startPath.add(0);

        Node start = new Node(0, 1, 0, startPath);
        pq.add(start);

        int minCost = Integer.MAX_VALUE;
        ArrayList<Integer> bestPath = new ArrayList<>();

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            // Prune paths that cannot improve the best solution
            if (current.bound >= minCost) {
                continue;
            }

            // All cities visited
            if (current.level == N) {
                int finalCost = current.totalCost
                              + cost[current.city][0];

                if (finalCost < minCost) {
                    minCost = finalCost;
                    bestPath = new ArrayList<>(current.path);
                    bestPath.add(0);
                }
                continue;
            }

            // Try visiting each unvisited city
            for (int next = 1; next < N; next++) {
                if (!current.path.contains(next)) {
                    ArrayList<Integer> newPath =
                        new ArrayList<>(current.path);
                    newPath.add(next);

                    int newCost = current.totalCost
                                + cost[current.city][next];

                    Node child = new Node(
                        next,
                        current.level + 1,
                        newCost,
                        newPath

                    );

                    if (child.bound < minCost) {
                        pq.add(child);
                    }
                }
            }
        }

        System.out.println("Minimum Cost: " + minCost);
        System.out.println("Optimal Route: " + bestPath);
    }

    public static void main(String[] args) {
        solve();
    }
}
