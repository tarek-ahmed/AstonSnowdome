package controller;

import model.LessonSelection;
import model.LessonTimetable;
import model.Users;
import model.Lesson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet to handle, process and respond to requests from the web server with endpoint /do/*
 * @author Tarek Foyz Ahmed
 */
public class Controller extends HttpServlet {

   private Users users;
   private LessonTimetable availableLessons;
   private RequestDispatcher requestDispatcher;

    public void init() {
         users = new Users();
         availableLessons = new LessonTimetable();
         this.getServletContext().setAttribute("lessonTimetable", availableLessons);
    }
    
    public void destroy() {
        
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        // /login end-point
        if(path.equals("/login")){
            String usernameInput = request.getParameter("username");
            String passwordInput = request.getParameter("password");
            
            int userID = users.isValid(usernameInput, passwordInput);
            
            if(userID != -1){
                HttpSession session = request.getSession();
                session.setAttribute("user", userID);
                session.setAttribute("userName", usernameInput);
                LessonSelection lessonSelection = new LessonSelection(userID);
                session.setAttribute("lessonSelection", lessonSelection);
                requestDispatcher = this.getServletContext().getRequestDispatcher("/lessonTimetableView.jspx");
            }else{
                requestDispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
            }
            requestDispatcher.forward(request, response);
        }
        // /checkName end-point
        else if(path.equals("/checkName")){
            String usernameToCheck = request.getParameter("newUsername");
            
            response.setContentType("application/xml; charset=\"UTF-8\"");
            PrintWriter servletOut = response.getWriter();
            
            if (users.isValidUsername(usernameToCheck)){
                servletOut.println(
                    "<?xml version='1.0' encoding='UTF-8'?>\n" +
                    "<isValid>true</isValid>"
                );
            } else{
                servletOut.println(
                    "<?xml version='1.0' encoding='UTF-8'?>\n" +
                    "<isValid>false</isValid>"
                );
            };
        }
        // /addUser end-point
        else if(path.equals("/addUser")){
                String newUsernameInput = request.getParameter("newUsername");
                String newPasswordInput = request.getParameter("newPassword");
                
                users.addUser(newUsernameInput, newPasswordInput);
                
                requestDispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
                requestDispatcher.forward(request, response);
        }
        else{
            HttpSession session = request.getSession(false);
            
            if(session == null || (session.getAttribute("lessonSelection") == null)){
                requestDispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
            } else{
                // /viewLessons end-point
                if(path.equals("/viewLessons")){
                    requestDispatcher = this.getServletContext().getRequestDispatcher("/lessonTimetableView.jspx");
                } 
                // /viewSelectedLessons end-point
                else if(path.equals("/viewSelectedLessons")){
                    requestDispatcher = this.getServletContext().getRequestDispatcher("/selectedLessonsView.jspx");
                } 
                // /addLesson end-point
                else if(path.equals("/addLesson")){
                    String lessonId = request.getParameter("lessonId");
                    
                    LessonSelection lessonSelection = (LessonSelection) session.getAttribute("lessonSelection");
                    
                    Lesson newLesson = availableLessons.getLesson(lessonId);
                    lessonSelection.addLesson(newLesson);
                    requestDispatcher = this.getServletContext().getRequestDispatcher("/do/viewSelectedLessons");
                }
                // /removeLesson end-point
                else if(path.equals("/removeLesson")){
                    LessonSelection lessonSelection = (LessonSelection) session.getAttribute("lessonSelection");
                    String lessonId = request.getParameter("lessonId");
                    lessonSelection.removeLesson(lessonId);
                    requestDispatcher = this.getServletContext().getRequestDispatcher("/do/viewSelectedLessons");
                }                
                // /finaliseBooking end-point
                else if(path.equals("/finaliseBooking")){
                    LessonSelection lessonSelection = (LessonSelection) session.getAttribute("lessonSelection");
                    lessonSelection.updateBooking();
                    requestDispatcher = this.getServletContext().getRequestDispatcher("/do/viewSelectedLessons");
                }
                // /login end-point
                else if(path.equals("/logout")){
                    session.invalidate();
                    requestDispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");     
                } 
                else{
                    requestDispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
                }
            }
            requestDispatcher.forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
