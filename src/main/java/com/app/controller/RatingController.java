package com.app.controller;

import com.app.DAO.MovieDao;
import com.app.DAO.RatingDao;
import com.app.model.Movie;
import com.app.utility.ConsoleColors;

import java.util.Scanner;

public class RatingController {

    static MovieDao movieDao = new MovieDao();
    static RatingDao ratingDao = new RatingDao();
    public static void changeRating(Scanner input, Movie movie) {
        String choice;

        String activeAccount = AccountManipulation.activeAccount;
        System.out.println();

        // add choice to delete or update
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+==============================================+\n" +
                                                              "|   1.  CHANGE RATING                          |\n" +
                                                              "|   2.  DELETE                                 |\n" +
                                                              "|   3.  EXIT                                   |\n" +
                                                              "+==============================================+\n");

        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Your current rating for " + movie.getMovieName() + " is " + movieDao.getUserRating(activeAccount, movie.getMovieName()));

        choice = input.nextLine();

        switch (choice) {
            case "1" -> rateMovie(input, movie, "ratedMovie");
            case "2" -> deleteRating(input, movie);
            case "3" -> Menu.loggedInMenu(input);
            default -> {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a valid input");
                changeRating(input, movie);
            }
        }

    }

    private static void deleteRating(Scanner input, Movie movie) {

        if (ratingDao.deleteRating(movie.getMovieId(), AccountManipulation.activeAccount)) {
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Rating successfully deleted");
            MovieController.ratedMovies(input, movieDao.getRatedMovies(AccountManipulation.activeAccount));
        } else {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Oh no! something went very wrong!");
        }
    }

    public static void rateMovie(Scanner input, Movie movie, String methodName) {
        String activeAccount = AccountManipulation.activeAccount;
        String choice;
        String options = "012345";
        boolean check = false;

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+==============================================================+");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-60s |\n", "Movie: " + movie.getMovieName());
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-60s |\n", " ");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-60s |\n", "Rating:");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-60s |\n", "0. Really Bad");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-60s |\n", "1. Bad");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-60s |\n", "2. Not Good");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-60s |\n", "3. Okay");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-60s |\n", "4. Good");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-60s |\n", "5. Great");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-60s |\n", " ");
        System.out.printf(ConsoleColors.PURPLE_BOLD_BRIGHT + "| %-60s |\n", "6. Exit");
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+==============================================================+");
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Please enter your rating:");

        try {

            choice = input.nextLine();

            if (options.contains(choice)) {
                if (methodName.equals("ratedMovie")) {
                    check = ratingDao.changeRating(Double.valueOf(choice), movie.getMovieId(), activeAccount);
                }
                if (methodName.equals("unratedMovie")) {
                    check = ratingDao.rateMovie(Double.valueOf(choice), movie.getMovieId(), activeAccount);
                }
                if (check) {
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Rating Successful!");

                    if (activeAccount.equals("guest")) {
                        Menu.startMenu(input);
                    }

                } else {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Oh no! Something went wrong!");
                    System.exit(1);
                }

                System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Would you like to:");
                System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "1. Edit or delete another rating");
                System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "2. Go back to the home page");

                choice = input.nextLine();

                if (choice.equals("1")) {
                    if (methodName.equals("ratedMovie")) {
                        MovieController.ratedMovies(input, movieDao.getRatedMovies(activeAccount));
                    }
                    if (methodName.equals("unratedMovie")) {
                        MovieController.unratedMovies(input, movieDao.getUnratedMovies(activeAccount));
                    }
                } else if (choice.equals("2")) {
                    if (activeAccount.equals("guest")) {
                        Menu.startMenu(input);
                    } else {
                        Menu.loggedInMenu(input);
                    }
                } else {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a correct value!!");
                }

                switch(choice) {
                    case "1" -> MovieController.ratedMovies(input, movieDao.getRatedMovies(activeAccount));
                    case "2" -> Menu.loggedInMenu(input);
                }
            } else if (choice.equals("6")) {
                if (activeAccount.equals("guest")) {
                    Menu.startMenu(input);
                } else {
                    Menu.loggedInMenu(input);
                }
            } else {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a number corresponding to a movie!!");
                rateMovie(input, movie, methodName);
            }


        } catch(Exception e) {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a number corresponding to a movie!!");
            rateMovie(input, movie, methodName);
        }
    }
}
