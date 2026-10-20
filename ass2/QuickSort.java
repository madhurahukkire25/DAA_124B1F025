
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class QuickSort {

    static class Movie {
        String title;
        int year;
        double rating;
        int votes;

        Movie(String title, int year, double rating, int votes) {
            this.title = title;
            this.year = year;
            this.rating = rating;
            this.votes = votes;
        }

        public String toString() {
            return title + " | Year: " + year
                    + " | IMDb Rating: " + rating
                    + " | Votes: " + votes;
        }
    }

    public static void main(String[] args) {

        ArrayList<Movie> movies = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File("imdb_top_1000.csv"));

            if (sc.hasNextLine()) {
                sc.nextLine(); // Skip header
            }

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                String[] data = line.split(
                    ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1
                );

                if (data.length < 16) {
                    continue;
                }

                try {
                    String title = data[1].trim().replace("\"", "");
                    int year = Integer.parseInt(
                        data[2].trim().replace("\"", ""));
                    double rating = Double.parseDouble(
                        data[6].trim().replace("\"", ""));
                    int votes = Integer.parseInt(
                        data[14].trim().replace("\"", "")
                            .replace(",", ""));

                    movies.add(new Movie(title, year, rating, votes));

                } catch (NumberFormatException e) {
                    // Skip rows with invalid numeric values
                }
            }

            sc.close();

        } catch (FileNotFoundException e) {
            System.out.println("Dataset file not found!");
            System.out.println("Place imdb_top_1000.csv in the project folder.");
            return;
        }

        if (movies.isEmpty()) {
            System.out.println("No valid movie records found.");
            return;
        }

        Scanner input = new Scanner(System.in);

        System.out.println("===== StreamFlix Movie Recommendation System =====");
        System.out.println("1. Sort by IMDb Rating (Lowest First)");
        System.out.println("2. Sort by Release Year (Oldest First)");
        System.out.println("3. Sort by Popularity (Fewest Votes First)");
        System.out.print("Enter your choice: ");

        if (!input.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            input.close();
            return;
        }

        int choice = input.nextInt();

        if (choice < 1 || choice > 3) {
            System.out.println("Invalid choice!");
            input.close();
            return;
        }

        long start = System.nanoTime();

        solve(movies, 0, movies.size() - 1, choice);

        long end = System.nanoTime();

        System.out.println("\nSorted Movies (Top 20):");

        int limit = Math.min(20, movies.size());

        for (int i = 0; i < limit; i++) {
            System.out.println((i + 1) + ". " + movies.get(i));
        }

        System.out.println("\nTotal valid movies: " + movies.size());
        System.out.printf("Sorting time: %.3f ms%n",
                (end - start) / 1_000_000.0);

        input.close();
    }

    static void solve(ArrayList<Movie> movies,
                      int low, int high, int choice) {

        if (low >= high) {
            return;
        }

        int pivotIdx = partitionIdx(movies, low, high, choice);

        solve(movies, low, pivotIdx - 1, choice);
        solve(movies, pivotIdx + 1, high, choice);
    }

    // Your original partition approach: j <= high
    static int partitionIdx(ArrayList<Movie> movies,
                            int low, int high, int choice) {

        int i = low - 1;
        int j = low;
        Movie pivot = movies.get(high);

        while (j <= high) {

            boolean condition = false;

            if (choice == 1) {
                condition = movies.get(j).rating >= pivot.rating;
            } else if (choice == 2) {
                condition = movies.get(j).year >= pivot.year;
            } else if (choice == 3) {
                condition = movies.get(j).votes >= pivot.votes;
            }

            if (condition) {
                i++;
                swap(movies, i, j);
            }

            j++;
        }

        return i;
    }

    static void swap(ArrayList<Movie> movies, int i, int j) {
        Movie temp = movies.get(i);
        movies.set(i, movies.get(j));
        movies.set(j, temp);
    }
}
