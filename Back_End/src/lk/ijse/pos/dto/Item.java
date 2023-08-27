package lk.ijse.pos.dto;

/**
 * @author : Jayani_Arunika  8/26/2023 - 11:39 PM
 * @since : v0.01.0
 **/

public class Item {
    private String code;
    private String name;
    private double unitPrice;
    private int qtyOnHand;

    public Item() {
    }

    public Item(String code, String name, double unitPrice, int qtyOnHand) {
        this.code = code;
        this.name = name;
        this.unitPrice = unitPrice;
        this.qtyOnHand = qtyOnHand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }
}
