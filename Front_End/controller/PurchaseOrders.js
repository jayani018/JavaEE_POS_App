//Customer
var selectElement = $("#customerId1");

function updateSelector() {
    selectElement.empty();
    $.each(jsonObject, function (index, customer) {
        var option = $("<option>")
            .val(customer.id)
            .text(customer.id);
        selectElement.append(option);
    });
}

updateSelector();
$(document).ready(function () {
    $("#customerId1").change(function () {
        var selectedId = $(this).val();
        var selectedCustomer = jsonObject.find(function (customer) {
            return customer.id === selectedId;
        });

        if (selectedCustomer) {
            $("#customerName").val(selectedCustomer.name);
            $("#customerAddress").val(selectedCustomer.address);
            $("#customerSalary").val(selectedCustomer.salary);
        } else {
            $("#customerName").val("");
            $("#customerAddress").val("");
            $("#customerSalary").val("");
        }
    });
});

//Item
var selectElementItem = $("#itemCode");

function updateSelectorItem() {
    selectElementItem.empty();
    $.each(jsonObject, function (index, item) {
        var option = $("<option>")
            .val(item.code)
            .text(item.code);
        selectElementItem.append(option);
    });
}

updateSelectorItem();
$(document).ready(function () {
    $("#itemCode").change(function () {
        var selectedCode = $(this).val();
        var selectedItem = jsonObject.find(function (item) {
            return item.code === selectedCode;
        });

        if (selectedItem) {
            $("#itemName").val(selectedItem.name);
            $("#unitPrice").val(selectedItem.unitPrice);
            $("#qtyOnHand").val(selectedItem.qtyOnHand);
        } else {
            $("#itemName").val("");
            $("#unitPrice").val("");
            $("#qtyOnHand").val("");
        }
    });
});

//clear btn event
$("#cancel").click(function () {
    clearFileds();
});

function clearFileds() {
    $("#numItems,#total,#cash,#balance").val("");
    $("#numItems,#total,#cash,#balance").css("");
    $("#numItems").focus();
}

$("#addToCart").click(function () {
    var tbody = $("#placeOrderTBody");
    var orderId = $("#orderId").val();
    var orderDate = $("#orderDate").val();
    var customerId = $("#customerId").val();
    var itemCode = $("#itemCode").val();
    var numItems = parseInt($("#numItems").val());
    var total = parseFloat($("#total").val());

    var total = unitPrice * qtyOnHand;

    var row = $("<tr>");
    row.append($("<td>").text(itemCode));
    row.append($("<td>").text(itemName));
    row.append($("<td>").text("$" + unitPrice.toFixed(2)));
    row.append($("<td>").text(qtyOnHand));
    row.append($("<td>").text("$" + total.toFixed(2)));
    row.append($("<td>").html('<i class="bi bi-trash del"></i>'));

    tbody.append(row);

    // Clear form fields after adding to cart


    $("#itemCode").val("");
    $("#itemName").val("");
    $("#unitPrice").val("");
    $("#qtyOnHand").val("");
    $("#orderId").focus();


    var totalSum = 0;
    var qtyOnHandCount = 0;
    tbody.find("tr").each(function () {
        var totalText = $(this).find("td:eq(4)").text();
        var totalValue = parseFloat(totalText.replace("$", ""));
        var qtyOnHandText = $(this).find("td:eq(3)").text();
        var qtyOnHandValue = parseInt(qtyOnHandText);
        if (!isNaN(totalValue)) {
            totalSum += totalValue;

        }
        if (!isNaN(qtyOnHandValue)) {
            qtyOnHandCount += qtyOnHandValue;
        }

    });
    $("#total").val(totalSum);
    var rowCount = tbody.find("tr").length;
    $("#numItems").val(qtyOnHandCount);

    console.log(totalSum);
});

$("#cash").keypress(function (event) {

    var keyCode = event.which;


    if (keyCode < 48 || keyCode > 57) {

        event.preventDefault();
        var bal = $("#cash").val() - $("#total").val();
        $("#balance").val(bal)
    }
});

//Order Details
$("#placeOrder").click(function () {

    $("#placeOrderTBody").empty();
    //get item code from cart details table
    $("#placeOrderTBody tr").each(function () {
        var itemCode = $(this).find("td:first-child").text();
        itemCodes.push(itemCode);
    });

    var orderID = $("#orderId").val();
    var customerID = $("#customerId1").val();
    var total = $("#total").val();
    var bal = $("#balance").val();
    var cash = $("#cash").val();

    var newRow = $("<tr>");
    newRow.append("<td>" + orderID + "</td>");
    newRow.append("<td>" + customerID + "</td>");

    var itemCodesList = "";
    $.each(itemCodes, function (index, itemCode) {
        itemCodesList += itemCode + ", ";
    });
    itemCodesList = itemCodesList.slice(0, -2); // Remove the trailing comma and space

    newRow.append("<td>" + itemCodesList + "</td>");
    newRow.append("<td>" + cash + "</td>");
    newRow.append("<td>" + bal + "</td>");
    newRow.append("<td>" + total + "</td>");
    newRow.append('<td><i class="bi bi-trash del"></i></td>');

    $("#orderTBody").append(newRow);
    itemCodes.length = 0;
    $("#orderId").val("");
    $("#orderDate").val("");
    $("#customerId1").val("");
    $("#customerName").val("");
    $("#customerAddress").val("");
    $("#customerSalary").val("");
    incrementOrderID();
})
