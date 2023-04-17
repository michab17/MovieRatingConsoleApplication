package com.app;

import java.util.Scanner;
import com.app.controller.Menu;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Menu.startMenu(input);
    }

}
