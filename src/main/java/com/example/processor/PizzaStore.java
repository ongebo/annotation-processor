package com.example.processor;

import java.util.Scanner;

public class PizzaStore {
    public static void main(String[] args) {
        PizzaStore pizzaStore = new PizzaStore();
        Scanner scanner = new Scanner(System.in);
        System.out.print("What do you want to eat? ");
        String string = scanner.nextLine();
        System.out.println("Bill: $" + pizzaStore.order(string).getPrice());
    }

    private Meal order(String mealName) {
        if (mealName == null) {
            throw new IllegalArgumentException("Name of the meal is null!");
        } else if ("Margherita".equals(mealName)) {
            return new MargheritaPizza();
        } else if ("Calzone".equals(mealName)) {
            return new CalzonePizza();
        } else if ("Tiramisu".equals(mealName)) {
            return new Tiramisu();
        } else {
            throw new IllegalArgumentException("Unknown meal '" + mealName + "'");
        }
    }
}
