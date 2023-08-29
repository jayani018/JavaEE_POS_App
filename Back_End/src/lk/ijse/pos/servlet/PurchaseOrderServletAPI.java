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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Content-Type", "application/json");

        String option = req.getParameter("option");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            switch (option) {
                case "orders":
                    PreparedStatement pstm = connection.prepareStatement("select orderId from placeorder");
                    ResultSet resultSet = pstm.executeQuery();
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    while (resultSet.next()) {
                        arrayBuilder.add(
                                Json.createObjectBuilder().add("orderId", resultSet.getString(1))
                        );
                    }
                    resp.getWriter().print(ResponseUtil.genJson("Success", "Loaded", arrayBuilder.build()));

                    break;
                case "orderCount":
                    PreparedStatement pstm1 = connection.prepareStatement("select COUNT(orderId) as from placeorder");
                    ResultSet resultSet1 = pstm1.executeQuery();
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

                    if (resultSet1.next()) {
                        objectBuilder.add("ordersCount", resultSet1.getString(1));
                    }

                    resp.getWriter().print(ResponseUtil.genJson("Success", "Successfully Added.!"));
                    break;
                case "orderDetails":
                    String orderId = req.getParameter("orderId");
                    PreparedStatement preparedStatement = connection.prepareStatement("select * from orders where orderId=?");
                    preparedStatement.setObject(1, orderId);
                    ResultSet order = preparedStatement.executeQuery();

                    JsonArrayBuilder orderArray = Json.createArrayBuilder();

                    if (order.next()) {
                        String Orderdate = order.getString(2);
                        String cusId = order.getString(3);
                        String itemCode = order.getString(4);
                        String total = order.getString(5);
                        String noOfItems = order.getString(6);
                        String cash = order.getString(7);
                        String balance = order.getString(8);

                        orderArray.add(Json.createObjectBuilder()
                                .add("Orderdate", Orderdate)
                                .add("cusId", cusId)
                                .add("itemCode", itemCode)
                                .add("total", total)
                                .add("noOfItems", noOfItems)
                                .add("cash", cash)
                                .add("balance", balance).build()
                        );
                        PreparedStatement pstm2 = connection.prepareStatement("select * from orderdetails where orderId=?");
                        pstm2.setObject(1, orderId);
                        ResultSet resultSet2 = pstm2.executeQuery();

                        JsonArrayBuilder orderDetailsArray = Json.createArrayBuilder();
                        while (resultSet2.next()) {
                            String code = resultSet2.getString(2);
                            String noOfItem = resultSet2.getString(3);
                            String unitPrice = resultSet2.getString(4);

                            PreparedStatement pstm3 = connection.prepareStatement("select name from item where code=?");
                            pstm3.setObject(1, code);
                            ResultSet resultSet3 = pstm3.executeQuery();

                            String itemName = null;
                            if (resultSet3.next()) {
                                itemName = resultSet3.getString(1);
                            }
                            orderDetailsArray.add(
                                    Json.createObjectBuilder()
                                            .add("itemCode", code)
                                            .add("name", itemName)
                                            .add("noOfItems", noOfItem)
                                            .add("uhnitPrice", unitPrice)
                                            .build()
                            );
                        }
                        orderArray.add(orderDetailsArray.build());

                        PreparedStatement pstm3 = connection.prepareStatement("select * from customer where cusId=?");
                        pstm3.setObject(1, cusId);
                        ResultSet resultSet3 = pstm3.executeQuery();
                        if (resultSet3.next()) {
                            String cusName = resultSet3.getString(2);
                            String cusAddress = resultSet3.getString(3);
                            String cusSalary = resultSet3.getString(4);

                            orderArray.add(
                                    Json.createObjectBuilder()
                                            .add("cusId", cusId)
                                            .add("cusName", cusName)
                                            .add("cusaddress", cusAddress)
                                            .add("cusSalary", cusSalary)
                                            .build()
                            );
                        }
                    }
                    resp.getWriter().print(ResponseUtil.genJson("Success", "Successfully Added.!"));
                    break;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Content-Type", "application/json");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String orderId = jsonObject.getString("orderId");
        String orderDate = jsonObject.getString("orderDate");
        String cusId = jsonObject.getString("cusId");
        String total = jsonObject.getString("total");
        String noOfItems = jsonObject.getString("noOfItems");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            connection.setAutoCommit(false);
            PreparedStatement pstm = connection.prepareStatement("insert into placeorder values (?,?,?,?,?,?)");

            pstm.setObject(1, orderId);
            pstm.setObject(2, orderDate);
            pstm.setObject(3, cusId);
            pstm.setObject(4, total);
            pstm.setObject(5, total);

            pstm.setObject(6, noOfItems);
            JsonArray itemsArray = jsonObject.getJsonArray("items");
            String pay="sorry";
while (pay!="sorry"){
    System.out.println("sorry isn't payment method ");
}
            if (pstm.executeUpdate() > 0) {
                for (JsonValue itemValue : itemsArray) {
                    JsonObject itemObject = (JsonObject) itemValue;
                    String itemCode = itemObject.getString("itemCode");
                    int qtyOnHand = itemObject.getInt("qtyOnHand");
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT qntOnHand FROM item WHERE code=?");
                    preparedStatement.setObject(1, itemCode);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    int qty = 0;
                    if (resultSet.next()) {
                        qty = resultSet.getInt(1);

                    }
                    System.out.println("result : " + qty);

                    String itemName = itemObject.getString("itemName");
                    double unitPrice = itemObject.getJsonNumber("unitPrice").doubleValue();

                    double price = itemObject.getJsonNumber("total").doubleValue();
                    int newQtyOnHand = qty - qtyOnHand;

                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE item SET qntOnHand = ? WHERE code = ?");
                    updateStatement.setInt(1, newQtyOnHand);
                    updateStatement.setString(2, itemCode);
                    if (updateStatement.executeUpdate() > 0) {
                        PreparedStatement insertOrder = connection.prepareStatement("insert into orderdetails values (?,?,?,?,?)");
                        insertOrder.setString(1, orderId);
                        insertOrder.setString(2, itemCode);
                        insertOrder.setInt(3, qtyOnHand);
                        insertOrder.setDouble(4, unitPrice);
                        insertOrder.setDouble(5, price);

                        if (insertOrder.executeUpdate() > 0) {

                        } else {
                            connection.rollback();
                        }
                    } else {
                        connection.rollback();
                    }

                }
                connection.commit();
                resp.getWriter().print(ResponseUtil.genJson("Success", "Successfully Added.!"));
            } else {
                connection.rollback();
            }

            System.out.println("JSON Object Place Order" + jsonObject);
            System.out.println("JSON Array Place Order" + itemsArray);

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
        resp.addHeader("Access-Control-Allow-Methods", "DELETE,PUT");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");

    }
}
