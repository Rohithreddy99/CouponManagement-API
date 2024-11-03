package service;



import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CouponServiceTest {
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        couponService = new CouponService();
    }

    @Test
    void testCartWiseCoupon() {
        // Create cart-wise coupon
        Coupon coupon = new Coupon();
        coupon.setType(CouponType.CART_WISE);
        
        CouponDetails details = new CouponDetails();
        details.setThreshold(100.0);
        details.setDiscount(10.0);
        coupon.setDetails(details);

        coupon = couponService.createCoupon(coupon);

        // Create cart
        Cart cart = new Cart();
        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setQuantity(2);
        item.setPrice(60.0);
        cart.setItems(List.of(item));

        // Apply coupon
        ApplyCouponResponse result = couponService.applyCoupon(coupon.getId(), cart);

        assertEquals(12.0, result.getTotalDiscount()); // 10% of 120
        assertEquals(108.0, result.getFinalPrice());
    }

    @Test
    void testProductWiseCoupon() {
        // Create product-wise coupon
        Coupon coupon = new Coupon();
        coupon.setType(CouponType.PRODUCT_WISE);
        
        CouponDetails details = new CouponDetails();
        details.setProductId(1L);
        details.setDiscount(20.0);
        coupon.setDetails(details);

        coupon = couponService.createCoupon(coupon);

        // Create cart
        Cart cart = new Cart();
        CartItem item1 = new CartItem();
        item1.setProductId(1L);
        item1.setQuantity(1);
        item1.setPrice(100.0);

        CartItem item2 = new CartItem();
        item2.setProductId(2L);
        item2.setQuantity(1);
        item2.setPrice(50.0);

        cart.setItems(List.of(item1, item2));

        // Apply coupon
        ApplyCouponResponse result = couponService.applyCoupon(coupon.getId(), cart);

        assertEquals(20.0, result.getTotalDiscount()); // 20% of 100
        assertEquals(130.0, result.getFinalPrice());
    }
}
