package com.example.pizzastore;

import com.example.processor.Factory;

@Factory(type = Meal.class, id = "Tiramisu")
public class Tiramisu implements Meal {
    public float getPrice() {
        return 4.5f;
    }
}
