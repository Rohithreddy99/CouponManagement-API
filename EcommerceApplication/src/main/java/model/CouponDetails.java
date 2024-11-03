package model;

import java.util.List;
import jakarta.validation.constraints.Min;

public class CouponDetails {
	
	 // For cart-wise coupons
    private Double threshold;
    private Double discount;

    // For product-wise coupons
    private Long productId;

    // For BXGY coupons
    private List<ProductQuantity> buyProducts;
    private List<ProductQuantity> getProducts;
    
    @Min(1)
    private Integer repetitionLimit;
    
    
    public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public List<ProductQuantity> getBuyProducts() {
		return buyProducts;
	}

	public void setBuyProducts(List<ProductQuantity> buyProducts) {
		this.buyProducts = buyProducts;
	}

	public List<ProductQuantity> getGetProducts() {
		return getProducts;
	}

	public void setGetProducts(List<ProductQuantity> getProducts) {
		this.getProducts = getProducts;
	}

	public Integer getRepetitionLimit() {
		return repetitionLimit;
	}

	public void setRepetitionLimit(Integer repetitionLimit) {
		this.repetitionLimit = repetitionLimit;
	}

	
}

