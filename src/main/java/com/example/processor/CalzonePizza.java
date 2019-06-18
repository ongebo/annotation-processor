package com.example.processor;

@Factory(type = Meal.class, id = "Calzone")
public class CalzonePizza implements Meal {
    public float getPrice() {
        return 8.5f;
    }
}
