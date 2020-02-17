/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuktuk.servlet;

import com.tuktuk.config.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.json.JSONObject;
import com.tuktuk.pojo.*;
import java.util.List;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Teran Weerasinghe
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");

        Session session = HibernateUtil.getSessionFactory().openSession();
        JSONObject data = new JSONObject();
        PrintWriter writer = response.getWriter();

        String companyName = null;
        String companyAddress = null;
        String companyTelephone = null;
        User user = null;
        List<Printinfo> ticketInfo = null;
        try {

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("username", username));
            criteria.add(Restrictions.eq("password", password));
            criteria.add(Restrictions.eq("status", 1));

            user = (User) criteria.uniqueResult();

            if (user != null) {

                data.put("id", user.getIduser());
                data.put("name", user.getUsername());

                criteria = session.createCriteria(User.class);
                ticketInfo = criteria.list();

                for (Printinfo p : ticketInfo) {
                    switch (p.getId()) {
                        case 1:
                            companyName = p.getDescription();
                            break;
                        case 2:
                            companyAddress = p.getDescription();
                            break;
                        case 3:
                            companyTelephone = p.getDescription();
                            break;
                    }
                }

                data.put("companyName", companyName);
                data.put("companyAddress", companyAddress);
                data.put("companyTelephone", companyTelephone);

            }

            writer.println(data);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            writer.close();
            session.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
