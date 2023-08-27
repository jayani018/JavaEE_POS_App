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
 * @author : Jayani_Arunika  8/26/2023 - 8:55 PM
 * @since : v0.01.0
 **/

@WebServlet(urlPatterns = "/pages/item")
public class ItemServletAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.addHeader("Access-Control-Allow-Origin","*");
            resp.addHeader("Content-Type","application/json");

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from item");
            ResultSet rst = pstm.executeQuery();

            JsonArrayBuilder allItems = Json.createArrayBuilder();
            while (rst.next()) {
                String code = rst.getString(1);
                String name = rst.getString(2);
                String unitPrice = rst.getString(3);
                String qtyOnHand = rst.getString(4);

                JsonObjectBuilder itemObject = Json.createObjectBuilder();
                itemObject.add("code", code);
                itemObject.add("name", name);
                itemObject.add("unitPrice", unitPrice);
                itemObject.add("qtyOnHand",qtyOnHand);
                allItems.add(itemObject.build());

            }
            resp.getWriter().print(ResponseUtil.genJson("Success","Loaded",allItems.build()));
        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Content-Type", "application/json");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String code = jsonObject.getString("code");
        String name = jsonObject.getString("name");
        String unitPrice=jsonObject.getString("unitPrice");
        String qtyOnHand=jsonObject.getString("quantity");

//        double unitPrice = Double.parseDouble(jsonObject.getString("unitPrice"));
//        int qtyOnHand = Integer.parseInt(jsonObject.getString("qtyOnHand"));

        System.out.println("json : "+jsonObject);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("insert into item values(?,?,?,?)");
            pstm.setObject(1, code);
            pstm.setObject(2, name);
            pstm.setObject(3, unitPrice);
            pstm.setObject(4, qtyOnHand);

            if (pstm.executeUpdate() > 0) {
                resp.getWriter().print(ResponseUtil.genJson("Success", "Successfully Added.!"));
            }
        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Content-Type", "application/json");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String code = jsonObject.getString("code");
        String name = jsonObject.getString("name");
        String unitPrice = jsonObject.getString("unitPrice");
        String qtyOnHand = jsonObject.getString("qtyOnHand");
        System.out.println(jsonObject);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("update item set name=?,unitPrice=?,qntOnHand=? where code=?");
            pstm.setObject(4,code);
            pstm.setObject(1,name);
            pstm.setObject(2,unitPrice);
            pstm.setObject(3,qtyOnHand);

            if (pstm.executeUpdate()>0){
                resp.getWriter().print(ResponseUtil.genJson("Success","Customer Updated..!"));
            }else {
                resp.getWriter().print(ResponseUtil.genJson("Failed","Customer Updated Failed..!"));
            }
        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Content-Type", "application/json");

        String code = req.getParameter("code");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("delete from item where code=?");
            pstm.setObject(1,code);

            if (pstm.executeUpdate()>0){
                resp.getWriter().print(ResponseUtil.genJson("Success", "Customer Deleted..!"));
            }else {
                resp.getWriter().print(ResponseUtil.genJson("Failed", "Customer Delete Failed..!"));
            }
        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "PUT, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "content-type");
    }
}
