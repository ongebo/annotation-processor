package com.example.processor;

@Factory(type = Meal.class, id = "Margherita")
public class MargheritaPizza implements Meal {
    public float getPrice() {
        return 6.0f;
    }
}
