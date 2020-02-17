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
import java.sql.ResultSet;
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
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Teran Weerasinghe
 */
@WebServlet(name = "SaveServlet", urlPatterns = {"/SaveServlet"})
public class SaveServlet extends HttpServlet {

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

        Session session = HibernateUtil.getSessionFactory().openSession();
        PrintWriter writer = response.getWriter();

        try {
            String userID = request.getParameter("userID");
            String vehID = request.getParameter("vehID");
            double amount = Double.parseDouble(request.getParameter("amount"));

            double cash = 50;
            double newValue = 0;
            String sql = null;
            ResultSet rs = null;
            String vehicleNo = null;
            int paymentCount = 0;
            Vehicle veh;
            Payment payment;
            JSONObject jSONObject = new JSONObject();
            String hql = null;

            Criteria criteria = session.createCriteria(Payment.class);
            criteria.add(Restrictions.eq("idvehicle", vehID));
            criteria.add(Restrictions.eq("status", 1));
            criteria.add(Restrictions.gt("balance", 0));

            criteria.setProjection(Projections.count("idpayment"));

            paymentCount = (int) criteria.uniqueResult();

            criteria = session.createCriteria(Vehicle.class);
            criteria.add(Restrictions.eq("idvehicle", vehID));

            veh = (Vehicle) criteria.uniqueResult();

            vehicleNo = veh.getVehNo();

            if (paymentCount == 0) {
                jSONObject.put("status", "1");
                writer.println(jSONObject);
            } else if (paymentCount == 1) {

                criteria = session.createCriteria(Payment.class);
                criteria.add(Restrictions.eq("idvehicle", vehID));
                criteria.add(Restrictions.eq("status", 1));
                criteria.add(Restrictions.gt("balance", 0));;

                payment = (Payment) criteria.uniqueResult();

                double balance = payment.getBalance();
                int paymentID = payment.getIdpayment();
                String date = payment.getDate();

                String savedTime = Helper.getTimeOrDate("time");
                String savedDate = Helper.getTimeOrDate("date");
                Query q = null;

                if (balance == cash) { //balance == 50

                    if (amount >= cash) {

                        newValue = amount - cash;

                        hql = "UPDATE payment SET user_iduser= :pUserID, payble=1, balance='0', paymentDate= :pPaymentDate, paymentTime= :pPaymentTime   WHERE idpayment= :pPaymentID ";

                        q = session.createQuery(hql);
                        q.setParameter("pUserID", userID);
                        q.setParameter("pPaymentDate", savedDate);
                        q.setParameter("pPaymentTime", savedTime);
                        q.setParameter("pPaymentID", paymentID);

                        q.executeUpdate();

                        jSONObject.put("status", "2");

                    } else if (amount < cash) {

                        newValue = cash - amount;

                        hql = "UPDATE payment SET user_iduser='" + userID + "', payble=1, balance='" + newValue + "', paymentDate='" + savedDate + "', paymentTime='" + savedTime + "'   WHERE idpayment='" + paymentID + "' ";

                        q = session.createQuery(hql);
                        q.executeUpdate();

                        jSONObject.put("status", "3");

                    }

                } else if (balance < cash) {

                    if (amount >= balance) {
                        newValue = amount - balance;

                        hql = "UPDATE payment SET user_iduser='" + userID + "', payble=1, balance='" + newValue + "', paymentDate='" + savedDate + "', paymentTime='" + savedTime + "'   WHERE idpayment='" + paymentID + "' ";
                        q = session.createQuery(hql);
                        q.executeUpdate();

                        jSONObject.put("status", "4");

                    } else if (balance > amount) {
                        newValue = balance - amount;

                        hql = "UPDATE payment SET user_iduser='" + userID + "', payble=1, balance='" + newValue + "', paymentDate='" + savedDate + "', paymentTime='" + savedTime + "'   WHERE idpayment='" + paymentID + "' ";
                        q = session.createQuery(hql);
                        q.executeUpdate();

                        jSONObject.put("status", "5");

                    }

                }

                jSONObject.put("date", savedDate);
                jSONObject.put("time", savedTime);
                jSONObject.put("cash", "50");
                jSONObject.put("remainAndDays", getCountAndRemain(session, vehID));
                jSONObject.put("vehicleNo", vehicleNo);
                writer.println(jSONObject);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
            writer.close();
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

    private String getCountAndRemain(Session session, String vehID) {

        double balance = 0.0;
        int days = 0;
        try {

            Criteria criteria = session.createCriteria(Payment.class);
            criteria.add(Restrictions.eq("idvehicle", vehID));
            criteria.add(Restrictions.eq("status", 1));
            criteria.add(Restrictions.gt("balance", 0));

            List<Payment> paymentList = criteria.list();

            for (Payment payment : paymentList) {
                ++days;
                balance += payment.getBalance();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return balance + "-" + days;

    }

    
}
