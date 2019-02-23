package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            controller = appCtx.getBean(MealRestController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        MealTo meal = new MealTo(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")), false);

        log.info(id.isEmpty() ? "Create {}" : "Update {}", meal);
        if (id.isEmpty()) {
            controller.add(meal);
        } else {
            controller.update(meal, Integer.valueOf(id));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "filter":
                String sdStr = request.getParameter("startDate");
                LocalDate startDate = StringUtils.isEmpty(sdStr) ? null: LocalDate.parse(sdStr);
                String edStr = request.getParameter("endDate");
                LocalDate endDate = StringUtils.isEmpty(sdStr) ? null: LocalDate.parse(edStr);
                String stStr = request.getParameter("startTime");
                LocalTime startTime = StringUtils.isEmpty(stStr) ? null: LocalTime.parse(stStr);
                String etStr = request.getParameter("startTime");
                LocalTime endTime = StringUtils.isEmpty(stStr) ? null: LocalTime.parse(etStr);
                log.info("get filtered");
                request.setAttribute("meals",
                        controller.getBetweenDateAndTime(startDate, endDate, startTime, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final MealTo meal = "create".equals(action) ?
                        new MealTo(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, false) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        controller.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
