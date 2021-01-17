package com.ahsailabs.beritakita;

/**
 * Created by ahmad s on 17/01/21.
 */
public class Test {
    public static void main(String[] args){
        System.out.println("Hello AppMaker");
        int x = 100 + 50;
        String greeting = "Hello";

        boolean isJavaFun = true;
        boolean isFishTasty = false;
        System.out.println(isJavaFun);     // Outputs true
        System.out.println(isFishTasty);   // Outputs false

        int time = 22;
        if (time < 10) {
            System.out.println("Good morning.");
        } else if (time < 20) {
            System.out.println("Good day.");
        } else {
            System.out.println("Good evening.");
        }
        // Outputs "Good evening."

        int time2 = 20;
        String result = (time2 < 18) ? "Good day." : "Good evening.";
        System.out.println(result);

        int day = 4;
        switch (day) {
            case 6:
                System.out.println("Today is Saturday");
                break;
            case 7:
                System.out.println("Today is Sunday");
                break;
            default:
                System.out.println("Looking forward to the Weekend");
        }
        // Outputs "Looking forward to the Weekend"

        int i = 0;
        while (i < 5) {
            System.out.println(i);
            i++;
        }

        int j = 0;
        do {
            System.out.println(j);
            j++;
        }
        while (j < 5);

        for (int k = 0; k < 5; k++) {
            System.out.println(k);
        }

        for (int l = 0; l < 10; l++) {
            if (l == 4) {
                break;
            }
            System.out.println(l);
        }

        for (int m = 0; m < 10; m++) {
            if (m == 4) {
                continue;
            }
            System.out.println(m);
        }

        String[] cars1;
        String[] cars = {"Volvo", "BMW", "Ford", "Mazda"};
        int[] myNum = {10, 20, 30, 40};

        //String[] cars = {"Volvo", "BMW", "Ford", "Mazda"};
        System.out.println(cars[0]);
        // Outputs Volvo

        //String[] cars = {"Volvo", "BMW", "Ford", "Mazda"};
        cars[0] = "Opel";
        System.out.println(cars[0]);
        // Now outputs Opel instead of Volvo

        //String[] cars = {"Volvo", "BMW", "Ford", "Mazda"};
        System.out.println(cars.length);
        // Outputs 4

        //String[] cars = {"Volvo", "BMW", "Ford", "Mazda"};
        for (int n = 0; n < cars.length; n++) {
            System.out.println(cars[n]);
        }

        //String[] cars = {"Volvo", "BMW", "Ford", "Mazda"};
        for (String car : cars) {
            System.out.println(car);
        }

        int[][] myNumbers = { {1, 2, 3, 4}, {5, 6, 7} };
        int chosenNumber = myNumbers[1][2];
        System.out.println(chosenNumber); // Outputs 7
    }
}
