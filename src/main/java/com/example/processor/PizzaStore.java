package com.example.processor;

import java.util.Scanner;

public class PizzaStore {
    private MealFactory mealFactory;

    public PizzaStore() {
        this.mealFactory = new MealFactory();
    }

    public static void main(String[] args) {
        PizzaStore pizzaStore = new PizzaStore();
        Scanner scanner = new Scanner(System.in);
        System.out.print("What do you want to eat? ");
        String string = scanner.nextLine();
        System.out.println("Bill: $" + pizzaStore.order(string).getPrice());
    }

    private Meal order(String mealName) {
        return mealFactory.create(mealName);
    }
}
