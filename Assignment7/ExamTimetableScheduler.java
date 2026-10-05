
import java.util.*;

public class ExamTimetableScheduler {

    public static Map<String, Set<String>> buildConflictGraph(
            Map<String, List<String>> enrollments) {

        Map<String, Set<String>> graph = new HashMap<>();

        for (List<String> courses : enrollments.values()) {
            for (String course : courses) {
                graph.putIfAbsent(course, new HashSet<>());
            }
        }

        for (List<String> courses : enrollments.values()) {

            Set<String> uniqueCourses = new HashSet<>(courses);
            List<String> courseList = new ArrayList<>(uniqueCourses);

            for (int i = 0; i < courseList.size(); i++) {
                for (int j = i + 1; j < courseList.size(); j++) {

                    String course1 = courseList.get(i);
                    String course2 = courseList.get(j);

                    graph.get(course1).add(course2);
                    graph.get(course2).add(course1);
                }
            }
        }

        return graph;
    }

    public static Map<String, Integer> greedyColoring(
            Map<String, Set<String>> graph,
            List<String> order) {

        Map<String, Integer> color = new HashMap<>();

        for (String course : order) {

            Set<Integer> usedColors = new HashSet<>();

            for (String neighbor : graph.get(course)) {
                if (color.containsKey(neighbor)) {
                    usedColors.add(color.get(neighbor));
                }
            }

            int c = 0;

            while (usedColors.contains(c)) {
                c++;
            }

            color.put(course, c);
        }

        return color;
    }

    public static Map<String, Integer> welshPowell(
            Map<String, Set<String>> graph) {

        List<String> order = new ArrayList<>(graph.keySet());

        order.sort((a, b) ->
                Integer.compare(
                        graph.get(b).size(),
                        graph.get(a).size()
                )
        );

        return greedyColoring(graph, order);
    }

    public static Map<String, Integer> dsatur(
            Map<String, Set<String>> graph) {

        Map<String, Integer> color = new HashMap<>();
        Set<String> uncolored = new HashSet<>(graph.keySet());

        while (!uncolored.isEmpty()) {

            String selected = null;
            int bestSaturation = -1;
            int bestDegree = -1;

            for (String course : uncolored) {

                Set<Integer> neighborColors = new HashSet<>();

                for (String neighbor : graph.get(course)) {
                    if (color.containsKey(neighbor)) {
                        neighborColors.add(color.get(neighbor));
                    }
                }

                int saturation = neighborColors.size();
                int degree = graph.get(course).size();

                if (saturation > bestSaturation ||
                        (saturation == bestSaturation &&
                                degree > bestDegree)) {

                    selected = course;
                    bestSaturation = saturation;
                    bestDegree = degree;
                }
            }

            Set<Integer> forbiddenColors = new HashSet<>();

            for (String neighbor : graph.get(selected)) {
                if (color.containsKey(neighbor)) {
                    forbiddenColors.add(color.get(neighbor));
                }
            }

            int selectedColor = 0;

            while (forbiddenColors.contains(selectedColor)) {
                selectedColor++;
            }

            color.put(selected, selectedColor);
            uncolored.remove(selected);
        }

        return color;
    }

    public static Map<Integer, List<String>> createSlots(
            Map<String, Integer> coloring) {

        Map<Integer, List<String>> slots = new TreeMap<>();

        for (Map.Entry<String, Integer> entry : coloring.entrySet()) {

            String course = entry.getKey();
            int slot = entry.getValue();

            slots.computeIfAbsent(
                    slot,
                    k -> new ArrayList<>()
            ).add(course);
        }

        return slots;
    }

    public static boolean validateColoring(
            Map<String, Set<String>> graph,
            Map<String, Integer> coloring) {

        for (String course : graph.keySet()) {

            for (String neighbor : graph.get(course)) {

                if (coloring.get(course)
                        .equals(coloring.get(neighbor))) {

                    return false;
                }
            }
        }

        return true;
    }

    public static Map<String, String> allocateRooms(
            List<String> courses,
            Map<String, Integer> enrollmentCount,
            Map<String, Integer> rooms) {

        courses.sort((a, b) ->
                Integer.compare(
                        enrollmentCount.get(b),
                        enrollmentCount.get(a)
                )
        );

        List<String> roomList = new ArrayList<>(rooms.keySet());

        roomList.sort((a, b) ->
                Integer.compare(
                        rooms.get(b),
                        rooms.get(a)
                )
        );

        Map<String, String> allocation = new HashMap<>();
        Set<String> usedRooms = new HashSet<>();

        for (String course : courses) {

            int students = enrollmentCount.get(course);
            String selectedRoom = null;

            for (String room : roomList) {

                if (!usedRooms.contains(room)
                        && rooms.get(room) >= students) {

                    selectedRoom = room;
                    break;
                }
            }

            if (selectedRoom == null) {
                return null;
            }

            allocation.put(course, selectedRoom);
            usedRooms.add(selectedRoom);
        }

        return allocation;
    }

    public static void printTimetable(
            Map<Integer, List<String>> slots,
            Map<String, Integer> enrollmentCount,
            Map<String, Integer> rooms) {

        System.out.println("\n========== EXAM TIMETABLE ==========");

        for (Map.Entry<Integer, List<String>> entry : slots.entrySet()) {

            int slot = entry.getKey();
            List<String> courses = entry.getValue();

            System.out.println("\nTime Slot " + (slot + 1));

            Map<String, String> allocation =
                    allocateRooms(
                            courses,
                            enrollmentCount,
                            rooms
                    );

            if (allocation == null) {

                System.out.println("ERROR: Not enough rooms!");

                for (String course : courses) {
                    System.out.println(
                            course +
                            " (" +
                            enrollmentCount.get(course) +
                            " students)"
                    );
                }

            } else {

                for (String course : courses) {

                    System.out.println(
                            course +
                            " | Students: " +
                            enrollmentCount.get(course) +
                            " | Room: " +
                            allocation.get(course)
                    );
                }
            }
        }
    }

    public static void printColoring(
            String algorithm,
            Map<String, Integer> coloring,
            Map<String, Set<String>> graph) {

        int numberOfColors =
                Collections.max(coloring.values()) + 1;

        boolean valid =
                validateColoring(graph, coloring);

        System.out.println(
                "\n" + algorithm +
                " -> " +
                numberOfColors +
                " time slots"
        );

        System.out.println("Valid: " + valid);

        Map<Integer, List<String>> slots =
                createSlots(coloring);

        for (Map.Entry<Integer, List<String>> entry
                : slots.entrySet()) {

            System.out.println(
                    "Slot " +
                    (entry.getKey() + 1) +
                    ": " +
                    entry.getValue()
            );
        }
    }

    public static void main(String[] args) {

        Map<String, List<String>> enrollments =
                new HashMap<>();

        enrollments.put(
                "S1",
                Arrays.asList(
                        "CS101",
                        "MATH201",
                        "PHY101"
                )
        );

        enrollments.put(
                "S2",
                Arrays.asList(
                        "CS101",
                        "ENG101"
                )
        );

        enrollments.put(
                "S3",
                Arrays.asList(
                        "MATH201",
                        "PHY101",
                        "ENG101"
                )
        );

        enrollments.put(
                "S4",
                Arrays.asList(
                        "CS101",
                        "MATH201"
                )
        );

        enrollments.put(
                "S5",
                Arrays.asList(
                        "CS201",
                        "PHY101"
                )
        );

        Map<String, Set<String>> graph =
                buildConflictGraph(enrollments);

        System.out.println("========== CONFLICT GRAPH ==========");

        for (String course : graph.keySet()) {
            System.out.println(
                    course + " -> " + graph.get(course)
            );
        }

        List<String> normalOrder =
                new ArrayList<>(graph.keySet());

        Map<String, Integer> greedy =
                greedyColoring(graph, normalOrder);

        printColoring(
                "Greedy",
                greedy,
                graph
        );

        Map<String, Integer> welshPowell =
                welshPowell(graph);

        printColoring(
                "Welsh-Powell",
                welshPowell,
                graph
        );

        Map<String, Integer> dsatur =
                dsatur(graph);

        printColoring(
                "DSATUR",
                dsatur,
                graph
        );

        Map<String, Integer> enrollmentCount =
                new HashMap<>();

        for (List<String> courses : enrollments.values()) {

            for (String course : courses) {

                enrollmentCount.put(
                        course,
                        enrollmentCount.getOrDefault(
                                course,
                                0
                        ) + 1
                );
            }
        }

        Map<String, Integer> rooms =
                new HashMap<>();

        rooms.put("Room-A", 60);
        rooms.put("Room-B", 50);
        rooms.put("Room-C", 40);

        Map<Integer, List<String>> finalSlots =
                createSlots(dsatur);

        printTimetable(
                finalSlots,
                enrollmentCount,
                rooms
        );
    }
}

