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
import org.json.JSONArray;
import org.json.JSONObject;
import com.tuktuk.pojo.*;
import java.util.List;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Teran Weerasinghe
 */
@WebServlet(name = "HistoryServlet", urlPatterns = {"/HistoryServlet"})
public class HistoryServlet extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {

            final int userID = Integer.parseInt(request.getParameter("userid"));
            final String date = request.getParameter("date").replaceAll(" ", "");

            JSONArray array = new JSONArray();
            List<Payment> paymentList = null;
            double balance = 0.0;

            Criteria criteria = session.createCriteria(Payment.class);
            criteria.add(Restrictions.eq("date", date));
            criteria.add(Restrictions.eq("user_iduser", userID));
            criteria.add(Restrictions.eq("status", 1));

            for (Payment payment : paymentList) {
                int ticketID = payment.getIdpayment();
                int vehID = payment.getVehicle().getIdvehicle();
                String paymentDate = payment.getDate();
                String paymentTime = payment.getTime();

                criteria = session.createCriteria(Payment.class);
                criteria.setProjection(Projections.sum("balance"));
                criteria.add(Restrictions.eq("vehicle_idvehicle", vehID));
                criteria.add(Restrictions.eq("status", 1));

                balance = (double) criteria.uniqueResult();

                JSONObject data = new JSONObject();
                data.put("ticketID", ticketID + "");
                data.put("date", paymentDate);
                data.put("time", paymentTime);
                data.put("balance", balance);

                data.put("vehNo", payment.getVehicle().getVehNo());

                array.put(data);

            }

            out.println(array);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            out.close();
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
