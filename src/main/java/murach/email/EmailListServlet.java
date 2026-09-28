package murach.email;

import java.io.IOException;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import murach.business.User;
import murach.data.UserDAO;
import murach.data.UserDB;
import murach.util.MailUtilGmail;

public class EmailListServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDB();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/index.jsp";

        String action = request.getParameter("action");
        if (action == null) {
            action = "join";
        }

        String message = "";

        if (action.equals("join")) {
            url = "/index.jsp";
        } else if (action.equals("add")) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            User user = new User(firstName, lastName, email);

            if (userDAO.emailExists(user.getEmail())) {
                message = "This email address already exists.<br>"
                        + "Please enter another email address.";
                url = "/index.jsp";
            } else {
                int result = userDAO.insert(user);

                if (result > 0) {
                    message = "";
                    // send email to user
                    String to = email;
                    String from = "auquockhanh02112006@gmail.com";
                    String subject = "Welcome to our email list";
                    String body = "Dear " + firstName + ",\n\n"
                            + "Thanks for joining our email list. "
                            + "We'll make sure to send "
                            + "you announcements about new products "
                            + "and promotions.\n"
                            + "Have a great day and thanks again!\n\n"
                            + "Kelly Slivkoff\n"
                            + "Mike Murach & Associates";
                    boolean isBodyHTML = false;
                    try {
                        MailUtilGmail.sendMail(to, from, subject, body,
                                isBodyHTML);
                    } catch (MessagingException e) {
                        String errorMessage
                                = "ERROR: Unable to send email. "
                                + "Check Tomcat logs for details.<br>"
                                + "NOTE: You may need to configure your system "
                                + "as described in chapter 14.<br>"
                                + "ERROR MESSAGE: " + e.getMessage();
                        request.setAttribute("errorMessage", errorMessage);
                        this.log(
                                "Unable to send email. \n"
                                        + "Here is the email you tried to send: \n"
                                        + "=====================================\n"
                                        + "TO: " + email + "\n"
                                        + "FROM: " + from + "\n"
                                        + "SUBJECT: " + subject + "\n\n"
                                        + body + "\n\n");
                        }
                        url = "/thanks.jsp";
                } else {
                    message = "Unable to save your information. Please try again.";
                    url = "/index.jsp";
                }
            }

            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }

            getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}
