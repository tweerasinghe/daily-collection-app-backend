/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuktuk.servlet;

import com.tuktuk.config.Helper;
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Teran Weerasinghe
 */
@WebServlet(name = "DataSynchronizedServlet", urlPatterns = {"/DataSynchronizedServlet"})
public class DataSynchronizedServlet extends HttpServlet {

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

        String newDate = Helper.getTimeOrDate("time");
        String newTime = Helper.getTimeOrDate("date");

        PrintWriter writer = response.getWriter();
        Session session = HibernateUtil.getSessionFactory().openSession();

        int vehicleCount = 0;
        int paymentCount = 0;

        String hql = null; 

        try {

            Criteria criteria = session.createCriteria(Vehicle.class);
            criteria.add(Restrictions.eq("status", 1));
            criteria.setProjection(Projections.count("idvehicle"));

            vehicleCount = (int) criteria.uniqueResult();

            criteria = session.createCriteria(Payment.class);
            criteria.add(Restrictions.eq("date", newDate));
            criteria.add(Restrictions.eq("status", 1));
            criteria.setProjection(Projections.count("idpayment"));

            paymentCount = (int) criteria.uniqueResult();

            if (paymentCount != 0) {
                JSONObject jSONObject = new JSONObject();
                if (vehicleCount == paymentCount) {

                    jSONObject.put("status", "success");

                } else if (vehicleCount != paymentCount) {
                    remove(session, newDate);

                    String status = initialize(session, vehicleCount, writer, newDate, newTime);

                    if (status.equalsIgnoreCase("Success")) {
                        jSONObject.put("status", "success");
                    } else {
                        jSONObject.put("status", "failed");
                    }
                    writer.println(jSONObject);
                }

                writer.println(jSONObject);

            }

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

    private String initialize(Session session, int vehicleCount, PrintWriter writer, String newDate, String newTime) {
        String status = null;
        try {

            int iterateCount = 0;

            Criteria criteria = session.createCriteria(Vehicle.class);
            criteria.add(Restrictions.eq("status", 1));

            List<Vehicle> vehList = criteria.list();
            for (Vehicle vehicle : vehList) {
                ++iterateCount;

                Payment payment = new Payment();
                payment.setDate(newDate);
                payment.setTime(newTime);
                payment.setCash(0.0);
                payment.setStatus(1);
                payment.setVehicle(vehicle);
                payment.setUser((User) session.load(User.class, 1));
                payment.setPayble(0);
                payment.setBalance(50.0);
                payment.setPaymentDate(newDate);
                payment.setPaymentTime(newTime);

                session.save(payment);

            }

            if (vehicleCount == iterateCount) {
                status = "Success";
            } else {
                status = "Failed";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    private void remove(Session session, String newDate) {
        try {

            String hql = "delete from payment where date= :pDate ";

            session.createQuery(hql).setParameter("pDate", newDate).executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
