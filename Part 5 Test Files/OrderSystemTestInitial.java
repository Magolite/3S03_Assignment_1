package com.example.shop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderSystemTestInitial {

    //OrderItem

    @Test
    void orderItem_validConstruction_returnsTotalPrice() {
        OrderItem item = new OrderItem("Book", 3, 10.0);
        assertEquals(30.0, item.getTotalPrice());
    }

    @Test
    void orderItem_zeroQuantity_throwsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new OrderItem("Book", 0, 10.0));
    }

    @Test
    void orderItem_getQuantity_returnsCorrectValue() {
        OrderItem item = new OrderItem("Hat", 5, 20.0);
        assertEquals(5, item.getQuantity());
    }

    //Order

    @Test
    void order_initialStatus_isCreated() {
        Order order = new Order();
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    void order_addItem_whenCreated_succeeds() {
        Order order = new Order();
        order.addItem(new OrderItem("Pen", 1, 2.0));
        assertEquals(1, order.getItems().size());
    }

    //PricingService

    @Test
    void pricing_calculateSubtotal_multipleItems() {
        Order order = new Order();
        order.addItem(new OrderItem("A", 2, 5.0));
        order.addItem(new OrderItem("B", 1, 10.0));
        PricingService ps = new PricingService();
        assertEquals(20.0, ps.calculateSubtotal(order));
    }

    @Test
    void pricing_calculateTax_positiveSubtotal() {
        PricingService ps = new PricingService();
        assertEquals(20.0, ps.calculateTax(100.0));
    }

    //DiscountService

    @Test
    void discount_student10_applies10PercentOff() {
        DiscountService ds = new DiscountService();
        assertEquals(90.0, ds.applyDiscount(100.0, "STUDENT10"));
    }

    @Test
    void discount_blackFriday_applies30PercentOff() {
        DiscountService ds = new DiscountService();
        assertEquals(70.0, ds.applyDiscount(100.0, "BLACKFRIDAY"));
    }

    //PaymentValidator

    @Test
    void payment_card_returnsTrue() {
        PaymentValidator pv = new PaymentValidator();
        assertTrue(pv.isPaymentMethodValid("card"));
    }

    @Test
    void payment_paypal_returnsTrue() {
        PaymentValidator pv = new PaymentValidator();
        assertTrue(pv.isPaymentMethodValid("paypal"));
    }

    //OrderService

    @Test
    void orderService_validPaymentNoDiscount_returnsCorrectTotal() {
        OrderService os = new OrderService();
        Order order = new Order();
        order.addItem(new OrderItem("Item", 1, 100.0));
        double result = os.processOrder(order, "", "card");
        assertEquals(120.0, result);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void orderService_withStudent10Discount_returnsCorrectTotal() {
        OrderService os = new OrderService();
        Order order = new Order();
        order.addItem(new OrderItem("Textbook", 1, 100.0));
        double result = os.processOrder(order, "STUDENT10", "paypal");
        assertEquals(108.0, result);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }
}