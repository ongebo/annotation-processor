package com.example.processor;

@Factory(type = Meal.class, id = "Tiramisu")
public class Tiramisu implements Meal {
    public float getPrice() {
        return 4.5f;
    }
}
