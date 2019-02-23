package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMeals = repository.getOrDefault(userId, new HashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeals.put(meal.getId(), meal);
            repository.putIfAbsent(userId, userMeals);
            return meal;
        }
        // treat case: update, but absent in storage
        return userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null && userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null) {
            return userMeals.get(id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> userMeals = repository.getOrDefault(userId, new HashMap<>());
        return userMeals.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getByDate(LocalDate startDate, LocalDate endDate, int userId) {
        return getAll(userId).stream().filter(meal ->
                        DateTimeUtil.isDateBetween(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

