import java.util.*;

public class SwiftCargo {

    static class Edge {
        int to;
        int cost;

        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    static class Graph {
        int vertices;
        List<List<Edge>> adj;

        Graph(int vertices) {
            this.vertices = vertices;
            adj = new ArrayList<>();

            for (int i = 0; i < vertices; i++) {
                adj.add(new ArrayList<>());
            }
        }

        void addEdge(int from, int to, int cost) {
            adj.get(from).add(new Edge(to, cost));
        }

        void updateEdge(int from, int to, int newCost) {
            for (Edge edge : adj.get(from)) {
                if (edge.to == to) {
                    edge.cost = newCost;
                    return;
                }
            }
        }

        void removeEdge(int from, int to) {
            adj.get(from).removeIf(edge -> edge.to == to);
        }
    }

    static class Result {
        int cost;
        List<Integer> path;

        Result(int cost, List<Integer> path) {
            this.cost = cost;
            this.path = path;
        }
    }

    static Result dynamicProgramming(
            Graph graph,
            int source,
            int destination,
            int[] stage) {

        int n = graph.vertices;

        int[] dp = new int[n];
        int[] next = new int[n];

        Arrays.fill(dp, Integer.MAX_VALUE);
        Arrays.fill(next, -1);

        dp[destination] = 0;

        Integer[] order = new Integer[n];

        for (int i = 0; i < n; i++) {
            order[i] = i;
        }

        Arrays.sort(order, (a, b) ->
                Integer.compare(stage[b], stage[a])
        );

        for (int u : order) {

            if (u == destination) {
                continue;
            }

            for (Edge edge : graph.adj.get(u)) {

                if (dp[edge.to] != Integer.MAX_VALUE &&
                        dp[edge.to] + edge.cost < dp[u]) {

                    dp[u] = dp[edge.to] + edge.cost;
                    next[u] = edge.to;
                }
            }
        }

        if (dp[source] == Integer.MAX_VALUE) {
            return new Result(
                    Integer.MAX_VALUE,
                    new ArrayList<>()
            );
        }

        List<Integer> path = new ArrayList<>();

        int current = source;

        while (current != -1) {

            path.add(current);

            if (current == destination) {
                break;
            }

            current = next[current];
        }

        return new Result(dp[source], path);
    }

    static Result dijkstra(
            Graph graph,
            int source,
            int destination) {

        int n = graph.vertices;

        int[] distance = new int[n];
        int[] parent = new int[n];

        Arrays.fill(
                distance,
                Integer.MAX_VALUE
        );

        Arrays.fill(parent, -1);

        PriorityQueue<int[]> pq =
                new PriorityQueue<>(
                        Comparator.comparingInt(a -> a[1])
                );

        distance[source] = 0;

        pq.add(new int[]{source, 0});

        while (!pq.isEmpty()) {

            int[] current = pq.poll();

            int u = current[0];
            int currentDistance = current[1];

            if (currentDistance != distance[u]) {
                continue;
            }

            if (u == destination) {
                break;
            }

            for (Edge edge : graph.adj.get(u)) {

                int newDistance =
                        distance[u] + edge.cost;

                if (newDistance < distance[edge.to]) {

                    distance[edge.to] = newDistance;
                    parent[edge.to] = u;

                    pq.add(
                            new int[]{
                                    edge.to,
                                    newDistance
                            }
                    );
                }
            }
        }

        if (distance[destination] ==
                Integer.MAX_VALUE) {

            return new Result(
                    Integer.MAX_VALUE,
                    new ArrayList<>()
            );
        }

        List<Integer> path =
                new ArrayList<>();

        int current = destination;

        while (current != -1) {

            path.add(current);
            current = parent[current];
        }

        Collections.reverse(path);

        return new Result(
                distance[destination],
                path
        );
    }

    static void printResult(
            String algorithm,
            Result result,
            String[] city) {

        System.out.println(
                "\n" + algorithm
        );

        System.out.println(
                "-------------------------"
        );

        if (result.cost ==
                Integer.MAX_VALUE) {

            System.out.println(
                    "No route available."
            );

            return;
        }

        System.out.println(
                "Minimum Cost: " +
                result.cost
        );

        System.out.print("Route: ");

        for (int i = 0;
             i < result.path.size();
             i++) {

            System.out.print(
                    city[result.path.get(i)]
            );

            if (i < result.path.size() - 1) {
                System.out.print(" -> ");
            }
        }

        System.out.println();
    }

    static void batchProcessing(
            Graph graph,
            int[][] requests,
            String[] city) {

        System.out.println(
                "\nBATCH PROCESSING"
        );

        System.out.println(
                "================"
        );

        for (int[] request : requests) {

            int source = request[0];
            int destination = request[1];

            Result result =
                    dijkstra(
                            graph,
                            source,
                            destination
                    );

            System.out.print(
                    city[source] +
                    " -> " +
                    city[destination] +
                    " : "
            );

            if (result.cost ==
                    Integer.MAX_VALUE) {

                System.out.println(
                        "No route"
                );

            } else {

                System.out.print(
                        "Cost = " +
                        result.cost +
                        ", Route = "
                );

                for (int i = 0;
                     i < result.path.size();
                     i++) {

                    System.out.print(
                            city[result.path.get(i)]
                    );

                    if (i <
                            result.path.size() - 1) {

                        System.out.print(
                                " -> "
                        );
                    }
                }

                System.out.println();
            }
        }
    }

    public static void main(String[] args) {

        String[] city = {
                "Warehouse",
                "Hub-A",
                "Hub-B",
                "Hub-C",
                "Hub-D",
                "Delivery-Center",
                "Destination"
        };

        Graph graph =
                new Graph(city.length);

        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 8);

        graph.addEdge(1, 3, 4);
        graph.addEdge(1, 4, 2);

        graph.addEdge(2, 3, 3);
        graph.addEdge(2, 4, 6);

        graph.addEdge(3, 5, 3);
        graph.addEdge(4, 5, 2);

        graph.addEdge(5, 6, 4);

        int[] stage = {
                1,
                2,
                2,
                3,
                3,
                4,
                5
        };

        int source = 0;
        int destination = 6;

        System.out.println(
                "SWIFTCARGO ROUTE OPTIMIZATION"
        );

        System.out.println(
                "============================="
        );

        Result dpResult =
                dynamicProgramming(
                        graph,
                        source,
                        destination,
                        stage
                );

        printResult(
                "Dynamic Programming",
                dpResult,
                city
        );

        Result dijkstraResult =
                dijkstra(
                        graph,
                        source,
                        destination
                );

        printResult(
                "Dijkstra's Algorithm",
                dijkstraResult,
                city
        );

        graph.updateEdge(
                1,
                4,
                10
        );

        System.out.println(
                "\nAfter Traffic Update:"
        );

        Result updatedResult =
                dijkstra(
                        graph,
                        source,
                        destination
                );

        printResult(
                "Updated Route",
                updatedResult,
                city
        );

        int[][] requests = {
                {0, 6},
                {0, 5},
                {1, 6},
                {2, 6}
        };

        batchProcessing(
                graph,
                requests,
                city
        );
    }
}
