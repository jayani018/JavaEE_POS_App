package lk.ijse.pos.dto;

/**
 * @author : Jayani_Arunika  8/27/2023 - 4:50 PM
 * @since : v0.01.0
 **/

public class purchaseOrder {
    private String orderId;
    private String orderDate;
    private String customerId;
    private String itemCode;
    private String numItems;
    private String total;

    public purchaseOrder() {
    }

    public purchaseOrder(String orderId, String orderDate, String customerId, String itemCode, String numItems, String total) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.itemCode = itemCode;
        this.numItems = numItems;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getNumItems() {
        return numItems;
    }

    public void setNumItems(String numItems) {
        this.numItems = numItems;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
