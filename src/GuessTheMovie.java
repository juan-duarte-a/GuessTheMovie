import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GuessTheMovie {
    public static void main(String[] args) {
        String movie;
        String[] movieNames = loadMovieNames();

        movie = getRandomMovieName(movieNames);

        if (game(movie)) {
            System.out.printf("¡Has adivinado '%s' correctamente!%n", movie);
        } else {
            System.out.println("Game over!");
        }
    }

    public static String[] loadMovieNames() {
        File file = new File("peliculas.txt");
        String[] movies;
        String movie;

        int count = 0;
        int countMovies = 0;

        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        assert sc != null;
        while (sc.hasNextLine()) {
            count++;
            movie = sc.nextLine();

            if (!movie.trim().isEmpty())
                countMovies++;
        }

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        movies = new String[countMovies];
        countMovies = 0;

        for (int i = 0; i < count; i++) {
            movie = sc.nextLine().trim();

            if (!movie.isEmpty()) {
                movies[countMovies] = movie.toLowerCase();
                countMovies++;
            }
        }

        return movies;
    }

    public static String getRandomMovieName(String[] movies) {
        Random random = new Random();
        return movies[random.nextInt(movies.length)];
    }

    public static boolean game(String movie) {
        char[] movieName = movie.toCharArray();
        char[] movieNameGuess = new char[movie.length()];
        char[] guessed = new char[10];
        Scanner sc = new Scanner(System.in);
        String guess;
        boolean correct;
        boolean repeated;
        int points = 10;

        for (int i = 0; i < movie.length(); i++) {
            if (movieName[i] == ' ')
                movieNameGuess[i] = ' ';
        }

        updateGuess(movieName, " ", movieNameGuess);

        while (points > 0) {
            System.out.printf("%nAdivina la letra o la película:%n");

            do {
                System.out.print("-> ");
                guess = sc.nextLine().toLowerCase();
            } while (guess.length() < 1);

            correct = updateGuess(movieName, guess, movieNameGuess);

            if (Arrays.equals(movieName, movieNameGuess))
                return true;
            else if (!correct) {
                if (guess.length() > 1) {
                    System.out.println("¡Equivocado!");
                    return false;
                }

                repeated = false;
                for (char c: guessed) {
                    if (c == guess.toCharArray()[0]) {
                        System.out.println("Letra previamente intentada.");
                        repeated = true;
                        break;
                    }
                }

                if (!repeated) {
                    guessed[10 - points] = guess.toCharArray()[0];
                    points--;
                }
            }

            if (10 - points == 1)
                System.out.print("Has intentado " + (10 - points) + " letra equivocada: ");
            else
                System.out.print("Has intentado " + (10 - points) + " letras equivocadas: ");

            if (points < 10) {
                for (int i = 0; i < 10 - points; i++) {
                    if (guessed[i] != Character.MIN_VALUE) {
                        System.out.print(guessed[i]);
                        if (i != 10 - points - 1)
                            System.out.print(", ");
                    }
                }
            }

            System.out.println();
        }

        return false;
    }

    public static boolean updateGuess(char[] movieName, String guess, char[] movieGuess) {
        boolean correct = false;
        char guessChar;

        if (guess.length() > 1) {
            if (Arrays.equals(guess.toCharArray(), movieName)) {
                System.arraycopy(movieName, 0, movieGuess, 0, movieName.length);
                System.out.println("¡Muy bien!");
                correct = true;
            }
        } else {
            guessChar = guess.toCharArray()[0];
            for (int i = 0; i < movieName.length; i++) {
                if (movieName[i] == guessChar) {
                    movieGuess[i] = guessChar;
                    correct = true;
                }
            }
        }

        if (!Arrays.equals(movieName, movieGuess) && guess.length() == 1) {
            System.out.print("\nEstás adivinando: ");

            for (char c: movieGuess)
                if (c != Character.MIN_VALUE)
                    System.out.print(c);
                else
                    System.out.print('_');
            System.out.println();
        }

        return correct;
    }
}