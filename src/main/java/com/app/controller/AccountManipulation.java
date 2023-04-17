package com.app.controller;

import com.app.DAO.AccountDao;
import com.app.utility.ConsoleColors;

import java.util.Scanner;

public class AccountManipulation {
    static AccountDao accountDao = new AccountDao();
    static String activeAccount;
    public static void register(Scanner input) {
        String choice;
        String email;
        String password;
        boolean isAdmin = false;

        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Welcome to the Account Creation Page!");
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Please enter your email:");

        choice = input.nextLine();

        if (choice.contains("admin")) {
            isAdmin = true;
            choice = choice.replace(" admin", "");
        }

        if (choice.matches(".*@.*\\..*")) {
            email = choice;
            System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Please enter your desired password:");
            password = input.nextLine();
            System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Please confirm your password:");
            if (password.equals(input.nextLine())) {
                if (accountDao.createAccount(email, password, isAdmin)) {
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Account creation successful, please log in");
                    Menu.startMenu(input);
                } else {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Account with that email already exists");
                    register(input);
                }
            } else {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Passwords did not match, please try again.");
                register(input);
            }
        } else {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Please enter a valid email:");
            register(input);
        }

    }

    public static void login(Scanner input) {
        String email;
        String password;

        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Welcome to the Login Page!");
        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Please enter your email:\n");

        email = input.nextLine();

        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + "Please enter your password:\n");

        password = input.nextLine();

        if (accountDao.checkCredentials(email, password)) {
            activeAccount = email;
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Log in successful!\n");
             Menu.loggedInMenu(input);
        } else {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Log in unsuccessful, please try again\n");
            login(input);
        }
    }
}
