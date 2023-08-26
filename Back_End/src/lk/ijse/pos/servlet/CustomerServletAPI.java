package lk.ijse.pos.servlet;

import com.mysql.jdbc.Driver;
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
 * @author : Jayani_Arunika  8/24/2023 - 2:27 PM
 * @since : v0.01.0
 **/

@WebServlet(urlPatterns = "/pages/customer")
public class CustomerServletAPI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.addHeader("Access-Control-Allow-Origin","*");
            resp.addHeader("Content-Type","application/json");

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from customer");
            ResultSet rst = pstm.executeQuery();

            JsonArrayBuilder allCustomers = Json.createArrayBuilder();
            while (rst.next()) {
                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
                String salary = rst.getString(4);



                JsonObjectBuilder customerObject = Json.createObjectBuilder();
                customerObject.add("cusId", id);
                customerObject.add("cusName", name);
                customerObject.add("cusAddress", address);
                customerObject.add("cusSalary",salary);
                allCustomers.add(customerObject.build());

                System.out.println(customerObject.add("cusId", id));
            }



            resp.getWriter().print(ResponseUtil.genJson("Success","Loaded",allCustomers.build()));
        } catch (ClassNotFoundException e ) {
                resp.setStatus(500);
                resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusId = req.getParameter("cusId");
        String cusName = req.getParameter("cusName");
        String cusAddress = req.getParameter("cusAddress");
        double cusSalary = Double.parseDouble(req.getParameter("cusSalary"));

        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Content-Type", "application/json");

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
                PreparedStatement pstm = connection.prepareStatement("insert into customer values (?,?,?,?)");

                pstm.setObject(1,cusId);
                pstm.setObject(2,cusName);
                pstm.setObject(3,cusAddress);
                pstm.setObject(4,cusSalary);

                System.out.println("cusId,cusName,cusAddress,cusSalary");

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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Content-Type", "application/json");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String cusId = jsonObject.getString("cusId");
        String cusName = jsonObject.getString("cusName");
        String cusAddress = jsonObject.getString("cusAddress");
        String cusSalary = jsonObject.getString("cusSalary");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("update customer set name=?,address=?,salary=? where id=?");
            pstm.setObject(4,cusId);
            pstm.setObject(1,cusName);
            pstm.setObject(2,cusAddress);
            pstm.setObject(3,cusSalary);

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

        String cusId = req.getParameter("cusId");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("delete from customer where id=?");
            pstm.setObject(1,cusId);

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
