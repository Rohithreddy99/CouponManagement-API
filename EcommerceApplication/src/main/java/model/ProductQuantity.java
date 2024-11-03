package model;



import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ProductQuantity {
	
	 @NotNull
	    private Long productId;
	 

		@Nonnull
	    @Min(1)
	    private Integer quantity;
	    
	    public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


}
