package com.example.pizzastore;

import com.example.processor.Factory;

@Factory(type = Meal.class, id = "Margherita")
public class MargheritaPizza implements Meal {
    public float getPrice() {
        return 6.0f;
    }
}
