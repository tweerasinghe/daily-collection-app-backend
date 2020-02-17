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
import com.tuktuk.pojo.Vehicle;
import com.tuktuk.pojo.Payment;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Teran Weerasinghe
 */
@WebServlet(name = "VehicleSearchServlet", urlPatterns = {"/VehicleSearchServlet"})
public class VehicleSearchServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        String vehicle_no = request.getParameter("vehNo");

        JSONObject data = new JSONObject();
        PrintWriter writer = response.getWriter();
        Session session = HibernateUtil.getSessionFactory().openSession();
        int vehicleID = 0;

        try {

            Criteria criteria = session.createCriteria(Vehicle.class);
            criteria.add(Restrictions.eq("vehNo", vehicle_no));
            criteria.add(Restrictions.eq("status", 1));

            Vehicle vehicle = (Vehicle) criteria.uniqueResult();

            vehicleID = vehicle.getIdvehicle();

            if (vehicleID != 0) {

                criteria = session.createCriteria(Payment.class);
                criteria.setProjection(Projections.count("idpayment"));
                criteria.add(Restrictions.eq("vehicle_idvehicle", vehicleID));
                criteria.add(Restrictions.eq("status", 1));
                criteria.add(Restrictions.gt("balance", 0));

                int paymentCount = (int) criteria.uniqueResult();

                if (paymentCount == 0) {
                    data.put("status", 1);
                } else if (paymentCount > 0) {

                    criteria = session.createCriteria(Payment.class);
                    criteria.setProjection(Projections.sum("balance"));
                    criteria.add(Restrictions.eq("vehicle_idvehicle", vehicleID));
                    criteria.add(Restrictions.eq("status", 1));
                    criteria.add(Restrictions.gt("balance", 0));

                    double balance = (double) criteria.uniqueResult();

                    data.put("idvehicle", vehicle.getIdvehicle());
                    data.put("QRID", vehicle.getQrid());
                    data.put("vehNo", vehicle.getVehNo());
                    data.put("ownerName", vehicle.getOwnerName());
                    data.put("mobile1", vehicle.getMobile1());
                    data.put("mobile2", vehicle.getMobile2());
                    data.put("address", vehicle.getAddress());
                    data.put("balance", balance + "");
                }

            } else {
                data.put("status", 0);
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
