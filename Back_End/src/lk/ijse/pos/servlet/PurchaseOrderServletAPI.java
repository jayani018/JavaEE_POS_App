package lk.ijse.pos.servlet;

import lk.ijse.pos.servlet.util.ResponseUtil;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author : Jayani_Arunika  8/27/2023 - 12:48 PM
 * @since : v0.01.0
 **/

@WebServlet(urlPatterns = "/pages/placeOrder")
public class PurchaseOrderServletAPI extends HttpServlet {
   // @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try {
//            resp.addHeader("Access-Control-Allow-Origin","*");
//            resp.addHeader("Content-Type","application/json");
//
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
//            PreparedStatement pstm = connection.prepareStatement("select * from placeorder");
//            ResultSet rst = pstm.executeQuery();
//
//            JsonArrayBuilder allOrders = Json.createArrayBuilder();
//            while (rst.next()) {
//                String orderId = rst.getString(1);
//                String orderDate = rst.getString(2);
//                String cusId = rst.getString(3);
//                String itemCode = rst.getString(4);
//                String total = rst.getString(5);
//                String noOfItems = rst.getString(6);
//
//
//
//                JsonObjectBuilder OrderObject = Json.createObjectBuilder();
//                OrderObject.add("orderId", orderId);
//                OrderObject.add("orderDate", orderDate);
//                OrderObject.add("cusId", cusId);
//                OrderObject.add("itemCode",itemCode);
//                OrderObject.add("total",total);
//                OrderObject.add("noOfItems",noOfItems);
//                allOrders.add(OrderObject.build());
//
//                //System.out.println(customerObject.add("cusId", id));
//            }
//
//            resp.getWriter().print(ResponseUtil.genJson("Success","Loaded",allOrders.build()));
//        } catch (ClassNotFoundException e ) {
//            resp.setStatus(500);
//            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
//        } catch (SQLException e) {
//            resp.setStatus(500);
//            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
//        }
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Content-Type", "application/json");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String orderId = jsonObject.getString("orderId");
        String orderDate = jsonObject.getString("orderDate");
        String cusId = jsonObject.getString("cusId");
        String itemCode = jsonObject.getString("itemCode");
        String total = jsonObject.getString("total");
        String noOfItems = jsonObject.getString("noOfItems");


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("insert into placeorder values (?,?,?,?,?,?)");

            pstm.setObject(1,orderId);
            pstm.setObject(2,orderDate);
            pstm.setObject(3,cusId);
            pstm.setObject(4,itemCode);
            pstm.setObject(5,total);
            pstm.setObject(6,noOfItems);


            if (pstm.executeUpdate() > 0){
                resp.getWriter().print(ResponseUtil.genJson("Success","Successfully Added.!"));
            }
        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));

        }catch (SQLException e){
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
        }

    }
}
