package com.example.shop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderSystemTest {

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
    void orderItem_negativeQuantity_throwsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new OrderItem("Book", -1, 10.0));
    }

    @Test
    void orderItem_negativeUnitPrice_throwsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new OrderItem("Book", 1, -5.0));
    }

    @Test
    void orderItem_zeroPriceIsAllowed() {
        OrderItem item = new OrderItem("Freebie", 2, 0.0);
        assertEquals(0.0, item.getTotalPrice());
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

    @Test
    void order_addItem_afterPaid_throwsException() {
        Order order = new Order();
        order.setStatus(OrderStatus.PAID);
        assertThrows(IllegalStateException.class,
            () -> order.addItem(new OrderItem("Pen", 1, 2.0)));
    }

    @Test
    void order_addItem_afterCancelled_throwsException() {
        Order order = new Order();
        order.setStatus(OrderStatus.CANCELLED);
        assertThrows(IllegalStateException.class,
            () -> order.addItem(new OrderItem("Pen", 1, 2.0)));
    }

    @Test
    void order_setStatus_updatesStatus() {
        Order order = new Order();
        order.setStatus(OrderStatus.PAID);
        assertEquals(OrderStatus.PAID, order.getStatus());
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
    void pricing_calculateSubtotal_emptyOrder_returnsZero() {
        Order order = new Order();
        PricingService ps = new PricingService();
        assertEquals(0.0, ps.calculateSubtotal(order));
    }

    @Test
    void pricing_calculateTax_positiveSubtotal() {
        PricingService ps = new PricingService();
        assertEquals(20.0, ps.calculateTax(100.0));
    }

    @Test
    void pricing_calculateTax_zeroSubtotal_returnsZero() {
        PricingService ps = new PricingService();
        assertEquals(0.0, ps.calculateTax(0.0));
    }

    @Test
    void pricing_calculateTax_negativeSubtotal_throwsException() {
        PricingService ps = new PricingService();
        assertThrows(IllegalArgumentException.class,
            () -> ps.calculateTax(-1.0));
    }

    //DiscountService

    @Test
    void discount_nullCode_returnsOriginalSubtotal() {
        DiscountService ds = new DiscountService();
        assertEquals(100.0, ds.applyDiscount(100.0, null));
    }

    @Test
    void discount_blankCode_returnsOriginalSubtotal() {
        DiscountService ds = new DiscountService();
        assertEquals(100.0, ds.applyDiscount(100.0, "   "));
    }

    @Test
    void discount_student10_applies10PercentOff() {
        DiscountService ds = new DiscountService();
        assertEquals(90.0, ds.applyDiscount(100.0, "STUDENT10"));
    }

    @Test
    void discount_student10_caseInsensitive() {
        DiscountService ds = new DiscountService();
        assertEquals(90.0, ds.applyDiscount(100.0, "student10"));
    }

    @Test
    void discount_blackFriday_applies30PercentOff() {
        DiscountService ds = new DiscountService();
        assertEquals(70.0, ds.applyDiscount(100.0, "BLACKFRIDAY"));
    }

    @Test
    void discount_invalidCode_throwsException() {
        DiscountService ds = new DiscountService();
        assertThrows(IllegalArgumentException.class,
            () -> ds.applyDiscount(100.0, "INVALID"));
    }

    @Test
    void discount_unknownCode_returnsOriginalSubtotal() {
        DiscountService ds = new DiscountService();
        assertEquals(100.0, ds.applyDiscount(100.0, "RANDOMCODE"));
    }

    //PaymentValidator

    @Test
    void payment_nullMethod_returnsFalse() {
        PaymentValidator pv = new PaymentValidator();
        assertFalse(pv.isPaymentMethodValid(null));
    }

    @Test
    void payment_card_returnsTrue() {
        PaymentValidator pv = new PaymentValidator();
        assertTrue(pv.isPaymentMethodValid("card"));
    }

    @Test
    void payment_cardUpperCase_returnsTrue() {
        PaymentValidator pv = new PaymentValidator();
        assertTrue(pv.isPaymentMethodValid("CARD"));
    }

    @Test
    void payment_paypal_returnsTrue() {
        PaymentValidator pv = new PaymentValidator();
        assertTrue(pv.isPaymentMethodValid("paypal"));
    }

    @Test
    void payment_crypto_returnsFalse() {
        PaymentValidator pv = new PaymentValidator();
        assertFalse(pv.isPaymentMethodValid("crypto"));
    }

    @Test
    void payment_unknownMethod_throwsException() {
        PaymentValidator pv = new PaymentValidator();
        assertThrows(UnsupportedOperationException.class,
            () -> pv.isPaymentMethodValid("bitcoin"));
    }

    //OrderService

    @Test
    void orderService_invalidPayment_cancelOrderAndReturnsZero() {
        OrderService os = new OrderService();
        Order order = new Order();
        order.addItem(new OrderItem("Widget", 1, 50.0));
        double result = os.processOrder(order, null, "crypto");
        assertEquals(0.0, result);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void orderService_nullPayment_cancelOrderAndReturnsZero() {
        OrderService os = new OrderService();
        Order order = new Order();
        order.addItem(new OrderItem("Widget", 1, 50.0));
        double result = os.processOrder(order, null, null);
        assertEquals(0.0, result);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void orderService_validPaymentNoDiscount_returnsCorrectTotal() {
        OrderService os = new OrderService();
        Order order = new Order();
        order.addItem(new OrderItem("Item", 1, 100.0));
        // subtotal=100, no discount, tax=20 → total=120
        double result = os.processOrder(order, "", "card");
        assertEquals(120.0, result);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void orderService_withStudent10Discount_returnsCorrectTotal() {
        OrderService os = new OrderService();
        Order order = new Order();
        order.addItem(new OrderItem("Textbook", 1, 100.0));
        // subtotal=100, 10% off=90, tax=18 → total=108
        double result = os.processOrder(order, "STUDENT10", "paypal");
        assertEquals(108.0, result);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void orderService_withBlackFridayDiscount_returnsCorrectTotal() {
        OrderService os = new OrderService();
        Order order = new Order();
        order.addItem(new OrderItem("TV", 1, 100.0));
        // subtotal=100, 30% off=70, tax=14 → total=84
        double result = os.processOrder(order, "BLACKFRIDAY", "card");
        assertEquals(84.0, result);
    }

    @Test
    void orderService_emptyOrderValidPayment_returnsZero() {
        OrderService os = new OrderService();
        Order order = new Order();
        double result = os.processOrder(order, null, "card");
        assertEquals(0.0, result);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }
}