package by.training.finaltask.controller.commands;

import by.training.finaltask.bean.MenuItem;
import by.training.finaltask.bean.Result;
import by.training.finaltask.bean.entities.Role;
import by.training.finaltask.bean.entities.Student;
import by.training.finaltask.bean.entities.User;
import by.training.finaltask.bean.page.Page;
import by.training.finaltask.controller.Command;
import by.training.finaltask.service.ServiceFactory;
import by.training.finaltask.service.excpetion.ServiceException;
import by.training.finaltask.service.impl.StudentServiceImpl;
import by.training.finaltask.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Login implements Command {

    private static final Logger controllerLog = LogManager.getLogger("ControllerLog");

    private static Map<Role, List<MenuItem>> menu = new ConcurrentHashMap<>();

    static {
        menu.put(Role.STUDENT, new ArrayList<>(Arrays.asList(
                new MenuItem("/mark/list.html", "marks"),
                new MenuItem("/document/stud/list.html", "document"),
                new MenuItem("/student/find.html", "profile"),
                new MenuItem("/logout.html", "logout")
        )));

        menu.put(Role.ADMINISTRATOR, new ArrayList<>(Arrays.asList(
                new MenuItem("/student/list.html", "students"),
                new MenuItem("/user/list.html", "userList"),
                new MenuItem("/dean/list.html", "deanList"),
                new MenuItem("/logout.html", "logout")
        )));

        menu.put(Role.DEAN, new ArrayList<>(Arrays.asList(
                new MenuItem("/student/list.html", "students"),
                new MenuItem("/document/dean/list.html", "document"),
                new MenuItem("/logout.html", "logout")
        )));
    }

    @Override
    public Result execute(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String login = request.getParameter("login");
        String pass = request.getParameter("password");

        if (pass != null && login != null) {
            try {
                UserServiceImpl userService = ServiceFactory.getInstance().getUserService();
                User user = userService.login(login, pass);
                if (user != null) {
                    HttpSession session = request.getSession(false);
                    session.setAttribute("authorizedUser", user);
                    session.setAttribute("menu", menu.get(user.getRole()));
                    request.setAttribute("userRole", user.getRole().getValue());
                    controllerLog.info("we authorise user");
                    return new Result(Page.PROFILE_HTML, true);
                } else {
                    controllerLog.info("can not authorise user");
                    request.getSession(false).setAttribute("incorrectData", "user with this login and pass" +
                            " do not exist");
                    return new Result(Page.LOGIN_FORM, true);
                }
            } catch (ServiceException e) {
                controllerLog.error(e.getMessage(), e);
                request.getSession(false).setAttribute("error", e.getMessage());
                return new Result(Page.ERROR, false);
            }
        } else {
            return new Result(Page.LOGIN_FORM, true);
        }
    }
}
