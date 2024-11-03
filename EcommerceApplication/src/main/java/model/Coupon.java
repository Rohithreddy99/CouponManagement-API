package model;

import java.time.LocalDate;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;

public class Coupon {
private String id;
    
    @Nonnull
    private CouponType type;
    
    @Valid
    @Nonnull
    private CouponDetails details;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CouponType getType() {
		return type;
	}

	public void setType(CouponType type) {
		this.type = type;
	}

	public CouponDetails getDetails() {
		return details;
	}

	public void setDetails(CouponDetails details) {
		this.details = details;
	}
	
}
