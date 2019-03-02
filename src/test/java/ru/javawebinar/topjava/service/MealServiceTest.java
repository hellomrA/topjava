package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by Антон on 02.03.2019.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() throws Exception {
        Meal meal = service.get(MEAL1_ID, USER_ID);
        assertMatch(meal, MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, USER_ID);
    }

    @Test
    public void delete() throws Exception {
        service.delete(ADMIN_MEAL1_ID, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEAL2);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        assertMatch(service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 0, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 0, 0), USER_ID), MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEALS);
    }

    @Test
    public void update() throws Exception {
        Meal updated = new Meal(MEAL2);
        updated.setDescription("Updated");
        updated.setCalories(2000);
        service.update(updated, USER_ID);
        assertMatch(updated, service.get(MEAL2_ID, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        Meal updated = new Meal(MEAL2);
        updated.setDescription("Updated");
        updated.setCalories(2000);
        service.update(updated, ADMIN_ID);
    }


    @Test
    public void create() throws Exception {
        Meal created = new Meal(LocalDateTime.now(), "Created", 1500);
        service.create(created, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), created, ADMIN_MEAL2, ADMIN_MEAL1);
    }

}