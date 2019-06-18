package com.example.pizzastore;

public class MealFactory {
    public Meal create(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null!");
        } else if ("Margherita".equals(id)) {
            return new MargheritaPizza();
        } else if ("Calzone".equals(id)) {
            return new CalzonePizza();
        } else if ("Tiramisu".equals(id)) {
            return new Tiramisu();
        } else {
            throw new IllegalArgumentException("Unknown id '" + id + "'");
        }
    }
}
