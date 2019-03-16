package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

/**
 * Created by Антон on 10.03.2019.
 */
@ActiveProfiles({Profiles.POSTGRES_DB, Profiles.JDBC})
public class MealJdbcServiceTest extends MealServiceTest{
}
