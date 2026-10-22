import java.util.*;

class Pair {
    String itemName;
    int item;
    int wt;
    int profit;
    int prio;
    boolean divisible;

    Pair(int item, String itemName, int wt, int profit,
         int prio, boolean divisible) {
        this.item = item;
        this.itemName = itemName;
        this.wt = wt;
        this.profit = profit;
        this.prio = prio;
        this.divisible = divisible;
    }
}

class  FrationalKnapsack {

    public static void main(String[] args) {

        PriorityQueue<Pair> pq = new PriorityQueue<>((a, b) -> {
            if (a.prio != b.prio) {
                return Integer.compare(a.prio, b.prio);
            }

            double p1 = (double) a.profit / a.wt;
            double p2 = (double) b.profit / b.wt;

            return Double.compare(p2, p1);
        });

        // item, name, weight, value, priority, divisible
        pq.add(new Pair(0, "Medicine", 10, 100, 1, false));
        pq.add(new Pair(1, "Infant Milk", 20, 60, 3, true));
        pq.add(new Pair(2, "Rice", 30, 90, 2, true));
        pq.add(new Pair(3, "Blankets", 15, 40, 3, false));
        pq.add(new Pair(4, "Biscuits", 5, 50, 1, false));

        int capacity = 50;
        double totalProfit = 0;

        System.out.println("=== Flood Relief Supply Allocation ===");
        System.out.println("Total carrying capacity: " + capacity + " kg\n");

        while (!pq.isEmpty() && capacity > 0) {

            Pair curr = pq.poll();

            if (curr.wt <= capacity) {

                System.out.println("Item: " + curr.itemName);
                System.out.println("Status: Taken completely");
                System.out.println("Quantity taken: " + curr.wt + " kg");
                System.out.println("Total quantity: " + curr.wt + " kg");
                System.out.println("Fraction taken: 1/1 (100.00%)");
                System.out.printf("Relief value: %.2f%n%n",
                        (double) curr.profit);

                capacity -= curr.wt;
                totalProfit += curr.profit;

            } else {

                if (!curr.divisible) {
                    System.out.println("Item: " + curr.itemName);
                    System.out.println("Status: Skipped (non-divisible)");
                    System.out.println("Quantity taken: 0 kg\n");
                    continue;
                }

                int quantityTaken = capacity;

                double fraction =
                        (double) quantityTaken / curr.wt;

                double percentage = fraction * 100;
                double earnedProfit = curr.profit * fraction;

                System.out.println("Item: " + curr.itemName);
                System.out.println("Status: Taken fractionally");
                System.out.println("Quantity taken: "
                        + quantityTaken + " kg");
                System.out.println("Total quantity: "
                        + curr.wt + " kg");

                System.out.println("Fraction taken: "
                        + quantityTaken + "/" + curr.wt);

                System.out.printf("Percentage taken: %.2f%%%n",
                        percentage);

                System.out.printf("Relief value: %.2f%n%n",
                        earnedProfit);

                totalProfit += earnedProfit;
                capacity = 0;
            }
        }

        System.out.println("=== Final Summary ===");
        System.out.printf("Total relief value: %.2f%n", totalProfit);
        System.out.println("Remaining capacity: " + capacity + " kg");
    }
}
