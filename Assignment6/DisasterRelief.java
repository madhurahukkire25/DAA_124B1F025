import java.util.*;

public class DisasterRelief {

    static class Item {
        String name;
        int weight;
        int utility;
        int priority;

        Item(String name, int weight, int utility, int priority) {
            this.name = name;
            this.weight = weight;
            this.utility = utility;
            this.priority = priority;
        }

        int totalValue() {
            return utility + priority;
        }
    }

    static class Result {
        int totalWeight;
        int totalUtility;
        List<Item> selectedItems;

        Result() {
            totalWeight = 0;
            totalUtility = 0;
            selectedItems = new ArrayList<>();
        }
    }

    public static Result bruteForce(Item[] items, int capacity) {
        int n = items.length;
        Result best = new Result();

        for (int mask = 0; mask < (1 << n); mask++) {
            int weight = 0;
            int utility = 0;
            List<Item> selected = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    weight += items[i].weight;
                    utility += items[i].totalValue();
                    selected.add(items[i]);
                }
            }

            if (weight <= capacity && utility > best.totalUtility) {
                best.totalWeight = weight;
                best.totalUtility = utility;
                best.selectedItems = selected;
            }
        }

        return best;
    }

    public static Result dynamicProgramming(Item[] items, int capacity) {
        int n = items.length;

        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {

                if (items[i - 1].weight <= w) {
                    dp[i][w] = Math.max(
                            dp[i - 1][w],
                            items[i - 1].totalValue()
                                    + dp[i - 1][w - items[i - 1].weight]
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        Result result = new Result();

        int w = capacity;

        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                Item item = items[i - 1];

                result.selectedItems.add(item);
                result.totalWeight += item.weight;
                result.totalUtility += item.totalValue();

                w -= item.weight;
            }
        }

        Collections.reverse(result.selectedItems);

        return result;
    }

    public static Result greedy(Item[] items, int capacity) {

        Item[] sortedItems = items.clone();

        Arrays.sort(sortedItems, (a, b) -> {
            double ratioA =
                    (double) a.totalValue() / a.weight;

            double ratioB =
                    (double) b.totalValue() / b.weight;

            return Double.compare(ratioB, ratioA);
        });

        Result result = new Result();

        for (Item item : sortedItems) {

            if (result.totalWeight + item.weight <= capacity) {

                result.selectedItems.add(item);
                result.totalWeight += item.weight;
                result.totalUtility += item.totalValue();
            }
        }

        return result;
    }

    public static void printResult(
            String algorithm,
            Result result) {

        System.out.println("\n" + algorithm);
        System.out.println("-------------------------");

        System.out.println(
                "Total Weight: " +
                result.totalWeight + " kg"
        );

        System.out.println(
                "Total Utility: " +
                result.totalUtility
        );

        System.out.println("Selected Items:");

        for (Item item : result.selectedItems) {
            System.out.println(
                    item.name +
                    " | Weight: " +
                    item.weight +
                    " kg | Utility: " +
                    item.utility +
                    " | Priority: " +
                    item.priority
            );
        }
    }

    static class Truck {
        String name;
        int capacity;

        Truck(String name, int capacity) {
            this.name = name;
            this.capacity = capacity;
        }
    }

    public static void multipleTrucks(
            Item[] items,
            Truck[] trucks) {

        List<Item> remaining =
                new ArrayList<>(Arrays.asList(items));

        remaining.sort((a, b) -> {
            double ratioA =
                    (double) a.totalValue() / a.weight;

            double ratioB =
                    (double) b.totalValue() / b.weight;

            return Double.compare(ratioB, ratioA);
        });

        System.out.println("\nMULTIPLE TRUCK ALLOCATION");
        System.out.println("=========================");

        for (Truck truck : trucks) {

            int remainingCapacity = truck.capacity;

            List<Item> selected = new ArrayList<>();

            Iterator<Item> iterator =
                    remaining.iterator();

            while (iterator.hasNext()) {

                Item item = iterator.next();

                if (item.weight <= remainingCapacity) {

                    selected.add(item);
                    remainingCapacity -= item.weight;

                    iterator.remove();
                }
            }

            System.out.println(
                    "\n" + truck.name +
                    " Capacity: " +
                    truck.capacity + " kg"
            );

            System.out.println(
                    "Used Capacity: " +
                    (truck.capacity - remainingCapacity) +
                    " kg"
            );

            System.out.println("Items:");

            for (Item item : selected) {
                System.out.println(
                        item.name +
                        " (" +
                        item.weight +
                        " kg)"
                );
            }
        }

        if (!remaining.isEmpty()) {

            System.out.println(
                    "\nItems not allocated:"
            );

            for (Item item : remaining) {
                System.out.println(item.name);
            }
        }
    }

    public static void main(String[] args) {

        Item[] items = {
                new Item("Medicine", 2, 10, 10),
                new Item("Food", 3, 15, 8),
                new Item("Water", 4, 20, 9),
                new Item("Blankets", 5, 18, 4),
                new Item("Medical Kit", 2, 12, 10),
                new Item("Clothes", 3, 8, 2)
        };

        int capacity = 10;

        System.out.println(
                "DISASTER RELIEF RESOURCE ALLOCATION"
        );

        System.out.println(
                "Truck Capacity: " +
                capacity +
                " kg"
        );

        Result bruteForce =
                bruteForce(items, capacity);

        Result dynamicProgramming =
                dynamicProgramming(items, capacity);

        Result greedy =
                greedy(items, capacity);

        printResult(
                "Brute Force",
                bruteForce
        );

        printResult(
                "Dynamic Programming",
                dynamicProgramming
        );

        printResult(
                "Greedy",
                greedy
        );

        Truck[] trucks = {
                new Truck("Truck 1", 10),
                new Truck("Truck 2", 8),
                new Truck("Truck 3", 7)
        };

        multipleTrucks(items, trucks);
    }
}

