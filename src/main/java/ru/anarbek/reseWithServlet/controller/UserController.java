package ru.anarbek.reseWithServlet.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.anarbek.reseWithServlet.dao.UserDao;
import ru.anarbek.reseWithServlet.dto.ErrorDto;
import ru.anarbek.reseWithServlet.exception.UserNotFoundException;
import ru.anarbek.reseWithServlet.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UserController extends HttpServlet {

    private static final Long serialVersionUID = 1L;

    private UserDao userDao;

    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userDao = new UserDao();
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String parameterId = req.getParameter("id");

        if (parameterId != null) {
            int id = Integer.parseInt(parameterId);
            try {
                User user = this.userDao.getById(id);
                String json = this.gson.toJson(user);
                out.print(json);
            } catch (UserNotFoundException e) {
                String json = this.gson.toJson(new ErrorDto(e.getMessage(), e.getCode()));
                out.print(json);
            }
        } else {
            List<User> users = this.userDao.getAll();
            String json = this.gson.toJson(users);

            out.print(json);
        }

        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        BufferedReader reader = req.getReader();
        User user = this.gson.fromJson(reader, User.class);

        if (user.getEmail() == null) {
            String json = this.gson.toJson(new ErrorDto("Email is required", 200));
            resp.setStatus(500);
            out.print(json);
            out.flush();
            return;
        }

        try {
            this.userDao.getByEmail(user.getEmail());
        } catch (UserNotFoundException exception) {
            this.userDao.add(user);
            return;
        }

        String json = this.gson.toJson(new ErrorDto("User with this email is exist", 101));
        resp.setStatus(500);
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        BufferedReader reader = req.getReader();
        User user = this.gson.fromJson(reader, User.class);

        try {
            this.userDao.getById(user.getId());
        } catch (UserNotFoundException exception) {
            String json = this.gson.toJson(new ErrorDto("User with this id not found", 102));
            resp.setStatus(404);
            out.print(json);
            out.flush();
            return;
        }
        this.userDao.update(user);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String parameterId = req.getParameter("id");
        if (parameterId != null) {
            int id = Integer.parseInt(parameterId);
            try {
                this.userDao.getById(id);
            } catch (UserNotFoundException exception) {
                String json = this.gson.toJson(new ErrorDto("User with this id not found", 102));
                resp.setStatus(404);
                out.print(json);
                out.flush();
                return;
            }
            this.userDao.delete(id);
        }
    }
}
