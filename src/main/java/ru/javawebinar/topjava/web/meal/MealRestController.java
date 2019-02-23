package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll meals");
        return MealsUtil.getWithExcess(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public MealTo get(int id) {
        log.info("get meal {}", id);
        return MealsUtil.createWithExcess(service.get(id, SecurityUtil.authUserId()), false);
    }

    public MealTo add(MealTo mealTo) {
        log.info("add meal{}", mealTo);
        Meal meal = MealsUtil.createFromTo(mealTo);
        ValidationUtil.checkNew(meal);
        return MealsUtil.createWithExcess(service.add(meal, SecurityUtil.authUserId()), false);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(MealTo mealTo, int id) {
        log.info("update {} with id={}", mealTo, id);
        Meal meal = MealsUtil.createFromTo(mealTo);
        ValidationUtil.assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

    public List<MealTo> getBetweenDateAndTime(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("get between date {} and {}", startDate, endDate);
        return MealsUtil.getFilteredWithExcess(service.getByDate(startDate, endDate, SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay(),
                startTime,
                endTime);
    }


}