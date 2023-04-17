package com.app.controller;

import com.app.DAO.AccountDao;
import com.app.DAO.MovieDao;
import com.app.model.Movie;
import com.app.utility.ConsoleColors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Menu {

    static MovieDao movieDao = new MovieDao();
    static AccountDao accountDao = new AccountDao();
    public static void startMenu(Scanner input) {
        String choice;

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+==============================================+\n" +
                                                              "|   1.  REGISTER                               |\n" +
                                                              "|   2.  LOGIN                                  |\n" +
                                                              "|   3.  VIEW MOVIES                            |\n" +
                                                              "|   4.  EXIT                                   |\n" +
                                                              "+==============================================+\n");

        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Please make a selection");

        choice = input.nextLine();

        switch (choice) {
            case "1" -> AccountManipulation.register(input);
            case "2" -> AccountManipulation.login(input);
            case "3" -> {
                AccountManipulation.activeAccount = "guest";
                MovieController.unratedMovies(input, movieDao.getAllMovies());
            }
            case "4" -> {
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "\nCome back soon!");
                input.close();
                System.exit(0);
            }
            default -> {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a valid input");
                startMenu(input);
            }
        }
    }

    public static void loggedInMenu(Scanner input) {
        String choice;
        String activeAccount = AccountManipulation.activeAccount;

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "+==============================================+");
        System.out.print("|  Welcome, ");
        System.out.format("%-35s", activeAccount);
        System.out.println("|");
        if (accountDao.isAdmin(activeAccount)) {
            System.out.println(
                    """
                            |   1.  VIEW UNRATED MOVIES                    |
                            |   2.  VIEW PREVIOUSLY RATED MOVIES           |
                            |   3.  LOGOUT                                 |
                            |                                              |
                            |   ADMIN OPTIONS                              |
                            |   4.  ADD MOVIE                              |
                            |   5.  UPDATE MOVIE                           |
                            +==============================================+
                            """);
        } else {
            System.out.println(
                    """
                            |   1.  VIEW UNRATED MOVIES                    |
                            |   2.  VIEW PREVIOUSLY RATED MOVIES           |
                            |   3.  LOGOUT                                 |
                            +==============================================+
                            """);
        }

        choice = input.nextLine();

        switch (choice) {
            case "1" -> MovieController.unratedMovies(input, movieDao.getUnratedMovies(AccountManipulation.activeAccount));
            case "2" -> MovieController.ratedMovies(input, movieDao.getRatedMovies(AccountManipulation.activeAccount));
            case "3" -> startMenu(input);
            case "4" -> {
                if (accountDao.isAdmin(activeAccount)){
                    MovieController.addMovie(input);
                } else {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a valid input");
                    loggedInMenu(input);
                }
            }
            case "5" -> {
                if (accountDao.isAdmin(activeAccount)){
                    MovieController.editMovie(input);
                } else {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a valid input");
                    loggedInMenu(input);
                }
            }
            default -> {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a valid input");
                loggedInMenu(input);
            }
        }

    }
}
