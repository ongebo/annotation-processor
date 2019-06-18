package com.example.pizzastore;

import com.example.processor.Factory;

@Factory(type = Meal.class, id = "Calzone")
public class CalzonePizza implements Meal {
    public float getPrice() {
        return 8.5f;
    }
}
