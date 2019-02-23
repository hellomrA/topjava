package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface MealService {

    Meal add(Meal meal, int userId);

    void update(Meal meal, int userId);

    void delete(int id, int userId) throws NotFoundException;

    Meal get(int id, int userId) throws NotFoundException;

    List<Meal> getAll(int userId);

    List<Meal> getByDate(LocalDate startDate, LocalDate endDate, int userId);
}