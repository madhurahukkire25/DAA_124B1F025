import java.util.*;

class SmartTrafficManagement {

    static class Edge {
        int destination;
        int time;

        Edge(int destination, int time) {
            this.destination = destination;
            this.time = time;
        }
    }

    static class Node implements Comparable<Node> {
        int vertex;
        int distance;

        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    static int V;
    static ArrayList<Edge>[] graph;

    @SuppressWarnings("unchecked")
    SmartTrafficManagement(int vertices) {
        V = vertices;
        graph = new ArrayList[V];

        for (int i = 0; i < V; i++) {
            graph[i] = new ArrayList<>();
        }
    }

    // Add a road in both directions
    void addRoad(int u, int v, int time) {
        graph[u].add(new Edge(v, time));
        graph[v].add(new Edge(u, time));
    }

    // Update travel time of an existing road
    void updateRoad(int u, int v, int newTime) {
        boolean found = false;

        for (Edge e : graph[u]) {
            if (e.destination == v) {
                e.time = newTime;
                found = true;
                break;
            }
        }

        for (Edge e : graph[v]) {
            if (e.destination == u) {
                e.time = newTime;
                break;
            }
        }

        if (found) {
            System.out.println("Traffic updated for road "
                    + u + " <-> " + v);
        } else {
            System.out.println("Road not found!");
        }
    }

    // Dijkstra's algorithm
    void dijkstra(int source, boolean[] hospitals) {
        int[] dist = new int[V];
        int[] parent = new int[V];

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        PriorityQueue<Node> pq = new PriorityQueue<>();
        dist[source] = 0;
        pq.offer(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex;

            if (current.distance != dist[u]) {
                continue;
            }

            for (Edge edge : graph[u]) {
                int v = edge.destination;
                int newDistance = dist[u] + edge.time;

                if (newDistance < dist[v]) {
                    dist[v] = newDistance;
                    parent[v] = u;
                    pq.offer(new Node(v, newDistance));
                }
            }
        }

        System.out.println("\nShortest travel times from intersection " + source);

        for (int i = 0; i < V; i++) {
            if (dist[i] == Integer.MAX_VALUE) {
                System.out.println(i + " : Unreachable");
            } else {
                System.out.println(i + " : " + dist[i] + " minutes");
            }
        }

        // Find the closest reachable hospital
        int nearestHospital = -1;
        int minTime = Integer.MAX_VALUE;

        for (int i = 0; i < V; i++) {
            if (hospitals[i] && dist[i] < minTime) {
                minTime = dist[i];
                nearestHospital = i;
            }
        }

        if (nearestHospital == -1) {
            System.out.println("\nNo reachable hospital found.");
            return;
        }

        System.out.println("\nNearest hospital: " + nearestHospital);
        System.out.println("Minimum travel time: " + minTime + " minutes");

        // Reconstruct the path
        ArrayList<Integer> path = new ArrayList<>();

        for (int v = nearestHospital; v != -1; v = parent[v]) {
            path.add(v);
        }

        Collections.reverse(path);

        System.out.println("Optimal route: " + path);
        printRoute(path);
    }

    // Text-based visual representation of the route
    void printRoute(ArrayList<Integer> path) {
        System.out.println("\nNavigation:");

        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));

            if (i < path.size() - 1) {
                System.out.print(" --> ");
            }
        }

        System.out.println();
    }

    public static void main(String[] args) {
        SmartTrafficManagement city =
                new SmartTrafficManagement(6);

        // Roads: intersection, intersection, travel time
        city.addRoad(0, 1, 4);
        city.addRoad(0, 2, 2);
        city.addRoad(1, 2, 1);
        city.addRoad(1, 3, 5);
        city.addRoad(2, 3, 8);
        city.addRoad(2, 4, 10);
        city.addRoad(3, 4, 2);
        city.addRoad(3, 5, 6);
        city.addRoad(4, 5, 3);

        int source = 0;

        // Hospitals are located at intersections 4 and 5
        boolean[] hospitals = new boolean[6];
        hospitals[4] = true;
        hospitals[5] = true;

        System.out.println("=== Initial Traffic Conditions ===");
        city.dijkstra(source, hospitals);

        // Traffic congestion changes the travel time
        System.out.println("\n=== After Traffic Update ===");
        city.updateRoad(0, 2, 12);

        // Recalculate using the updated road weights
        city.dijkstra(source, hospitals);
    }
}
