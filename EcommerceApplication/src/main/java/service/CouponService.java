package service;

import model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CouponService {
    private final Map<String, Coupon> coupons = new ConcurrentHashMap<>();

    public Coupon createCoupon(Coupon coupon) {
        coupon.setId(UUID.randomUUID().toString());
        coupons.put(coupon.getId(), coupon);
        return coupon;
    }

    public List<Coupon> getAllCoupons() {
        return new ArrayList<>(coupons.values());
    }

    public Optional<Coupon> getCouponById(String id) {
        return Optional.ofNullable(coupons.get(id));
    }

    public Optional<Coupon> updateCoupon(String id, Coupon coupon) {
        if (!coupons.containsKey(id)) {
            return Optional.empty();
        }
        coupon.setId(id);
        coupons.put(id, coupon);
        return Optional.of(coupon);
    }

    public boolean deleteCoupon(String id) {
        return coupons.remove(id) != null;
    }

    public List<ApplicableCoupon> getApplicableCoupons(Cart cart) {
        List<ApplicableCoupon> applicableCoupons = new ArrayList<>();
        
        for (Coupon coupon : coupons.values()) {
            double discount = calculateDiscount(coupon, cart);
            if (discount > 0) {
                ApplicableCoupon applicableCoupon = new ApplicableCoupon();
                applicableCoupon.setCouponId(coupon.getId());
                applicableCoupon.setType(coupon.getType());
                applicableCoupon.setDiscount(discount);
                applicableCoupons.add(applicableCoupon);
            }
        }
        
        return applicableCoupons;
    }

    public ApplyCouponResponse applyCoupon(String id, Cart cart) {
        Coupon coupon = coupons.get(id);
        if (coupon == null) {
            throw new IllegalArgumentException("Coupon not found");
        }

        double discount = calculateDiscount(coupon, cart);
        double totalPrice = calculateTotalPrice(cart);

        ApplyCouponResponse updatedCart = new ApplyCouponResponse();
        updatedCart.setItems(cart.getItems());
        updatedCart.setTotalPrice(totalPrice);
        updatedCart.setTotalDiscount(discount);
        updatedCart.setFinalPrice(totalPrice - discount);

        return updatedCart;
    }

    private double calculateDiscount(Coupon coupon, Cart cart) {
        return switch (coupon.getType()) {
            case CART_WISE -> calculateCartWiseDiscount(coupon, cart);
            case PRODUCT_WISE -> calculateProductWiseDiscount(coupon, cart);
            case BXGY -> calculateBxGyDiscount(coupon, cart);
        };
    }

    private double calculateCartWiseDiscount(Coupon coupon, Cart cart) {
        double cartTotal = calculateTotalPrice(cart);
        if (cartTotal >= coupon.getDetails().getThreshold()) {
            return (cartTotal * coupon.getDetails().getDiscount()) / 100;
        }
        return 0;
    }

    private double calculateProductWiseDiscount(Coupon coupon, Cart cart) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(coupon.getDetails().getProductId()))
                .mapToDouble(item -> (item.getPrice() * item.getQuantity() * coupon.getDetails().getDiscount()) / 100)
                .sum();
    }

    private double calculateBxGyDiscount(Coupon coupon, Cart cart) {
        Map<Long, Integer> cartQuantities = new HashMap<>();
        Map<Long, Double> cartPrices = new HashMap<>();
        
        cart.getItems().forEach(item -> {
            cartQuantities.put(item.getProductId(), item.getQuantity());
            cartPrices.put(item.getProductId(), item.getPrice());
        });

        // Calculate how many times the BXGY condition can be applied
        int possibleApplications = coupon.getDetails().getBuyProducts().stream()
                .mapToInt(required -> {
                    int available = cartQuantities.getOrDefault(required.getProductId(), 0);
                    return available / required.getQuantity();
                })
                .min()
                .orElse(0);

        if (possibleApplications == 0) {
            return 0;
        }

        // Apply repetition limit
        int actualApplications = Math.min(possibleApplications, coupon.getDetails().getRepetitionLimit());

        // Calculate total discount
        return coupon.getDetails().getGetProducts().stream()
                .mapToDouble(free -> {
                    Double price = cartPrices.get(free.getProductId());
                    return price != null ? price * free.getQuantity() * actualApplications : 0;
                })
                .sum();
    }

    private double calculateTotalPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
