package controller;



import model.*;
import service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CouponController {
    private final CouponService couponService = new CouponService();

    @PostMapping("/coupons")
    public ResponseEntity<Coupon> createCoupon(@Valid @RequestBody Coupon coupon) {
        return new ResponseEntity<>(couponService.createCoupon(coupon), HttpStatus.CREATED);
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/coupons/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable String id) {
        return couponService.getCouponById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/coupons/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable String id, @Valid @RequestBody Coupon coupon) {
        return couponService.updateCoupon(id, coupon)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable String id) {
        if (couponService.deleteCoupon(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/applicable-coupons")
    public ResponseEntity<Map<String, List<ApplicableCoupon>>> getApplicableCoupons(@RequestBody Cart cart) {
        List<ApplicableCoupon> coupons = couponService.getApplicableCoupons(cart);
        return ResponseEntity.ok(Map.of("applicable_coupons", coupons));
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<Map<String, ApplyCouponResponse>> applyCoupon(@PathVariable String id, @RequestBody Cart cart) {
    	ApplyCouponResponse updatedCart = couponService.applyCoupon(id, cart);
        return ResponseEntity.ok(Map.of("updated_cart", updatedCart));
    }
}
