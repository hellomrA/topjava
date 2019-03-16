package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
//    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int deleteByIdAndUserId(int id, int userId);

    @Transactional
    @Modifying
    Meal save(Meal meal);

    Optional<Meal> findByIdAndUserId(Integer id, Integer userId);

    List<Meal> getAllByUserId(int userId, Sort sort);

    List<Meal> getByUserIdAndDateTimeBetween(int userId, LocalDateTime startDate, LocalDateTime endDate, Sort sort);
}
