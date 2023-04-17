package com.app.controller;

import com.app.DAO.MovieDao;
import com.app.DAO.RatingDao;
import com.app.model.Movie;
import com.app.utility.ConsoleColors;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class MovieController {

    static DecimalFormat df = new DecimalFormat("0.00");

    static MovieDao movieDao = new MovieDao();
    static RatingDao ratingDao = new RatingDao();
    Movie activeMovie;
    public static void unratedMovies(Scanner input, List<Movie> movies) {
        String choice;

        if (movies.isEmpty()) {
            System.out.println("No movies here!\n");
            System.out.println("Press any key to go back");
            if(input.nextLine() != null) {
                Menu.loggedInMenu(input);
            }
        }

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+==============================================================+");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-2s | %-20s | %-15s | %-15s |\n", "#", "Movie", "Avg. Rating", "# of Ratings");
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+==============================================================+");
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.printf("| %-2s | %-20s | %-15s | %-15s |\n",
                    (i + 1), movie.getMovieName().trim(), df.format(movie.getAverageRating()), movie.getNumOfRatings());
        }
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+==============================================================+");
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "If you would like to rate a movie, please enter the movie number or type exit.");

        choice = input.nextLine();

        if (choice.equalsIgnoreCase("exit") && AccountManipulation.activeAccount.equals("guest")) {
            Menu.startMenu(input);
        } else if (choice.equalsIgnoreCase("exit") && !AccountManipulation.activeAccount.equals("guest")) {
            Menu.loggedInMenu(input);
        }

        try {
            Movie movie = movies.get(Integer.parseInt(choice) - 1);
            RatingController.rateMovie(input, movie, "unratedMovie");
        } catch(Exception e) {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a number corresponding to a movie!!");
            ratedMovies(input, movies);
        }
    }

    public static void ratedMovies(Scanner input, List<Movie> movies) {
        String activeEmail = AccountManipulation.activeAccount;
        String choice;

        if (movies.isEmpty()) {
            System.out.println("No movies here!\n");
            System.out.println("Press any key to go back");
            if(input.nextLine() != null) {
                Menu.loggedInMenu(input);
            }
        }

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+=================================================================================+");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-2s | %-20s | %-15s | %-15s | %-15s |\n", "#", "Movie", "Your Rating","Avg. Rating", "# of Ratings");
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+=================================================================================+");
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.printf("| %-2s | %-20s | %-15s | %-15s | %-15s |\n",
                    (i + 1), movie.getMovieName().trim(), movieDao.getUserRating(activeEmail, movie.getMovieName()),df.format(movie.getAverageRating()), movie.getNumOfRatings());
        }
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+=================================================================================+");
        System.out.println();

        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "If you would like to edit or delete your rating enter the corresponding number, please enter the movie number or type exit.");

        choice = input.nextLine();

        if (choice.equalsIgnoreCase("exit")) {
            Menu.loggedInMenu(input);
        }

        try {
            Movie movie = movies.get(Integer.parseInt(choice) - 1);
            RatingController.changeRating(input, movie);
        } catch(Exception e) {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a number corresponding to a movie!!");
            ratedMovies(input, movies);
        }

    }

    public static void addMovie(Scanner input) {
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Please enter the name of the movie you would like to add");

        if (movieDao.addMovie(input.nextLine())) {
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Movie added successfully");
            Menu.loggedInMenu(input);
        } else {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Something went very wrong");
        }
    }

    public static void editMovie(Scanner input) {
        List<Movie> movies = movieDao.getAllMovies();
        String choice;

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+==============================================================+");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-2s | %-55s |\n", "#", "Movie");
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+==============================================================+");
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.printf("| %-2s | %-55s |\n",
                    (i + 1), movie.getMovieName().trim());
        }
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+==============================================================+");
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "If you would like to edit a movie, please enter the movie number or type exit.");

        choice = input.nextLine();

        if (choice.equalsIgnoreCase("exit")) {
            Menu.loggedInMenu(input);
        }

        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Please enter the new name");

        String newName = input.nextLine();

        try {
            Movie movie = movies.get(Integer.parseInt(choice) - 1);
            if (movieDao.editMovie(newName, movie.getMovieName())) {
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Movie updated successfully!");
                Menu.loggedInMenu(input);
            } else {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Something went horribly wrong");
            }
        } catch(Exception e) {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a number corresponding to a movie!!");
            ratedMovies(input, movies);
        }
    }
}
