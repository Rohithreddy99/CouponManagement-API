class CouponService {
  constructor() {
    this.coupons = new Map();
  }

  async createCoupon(coupon) {
    if (this.coupons.has(coupon.id)) {
      throw new Error('Coupon already exists');
    }
    this.coupons.set(coupon.id, coupon);
    return coupon;
  }

  async getAllCoupons() {
    return Array.from(this.coupons.values());
  }

  async getCouponById(id) {
    return this.coupons.get(id);
  }

  async updateCoupon(id, data) {
    if (!this.coupons.has(id)) {
      return null;
    }
    const updated = { ...this.coupons.get(id), ...data };
    this.coupons.set(id, updated);
    return updated;
  }

  async deleteCoupon(id) {
    return this.coupons.delete(id);
  }

  async getApplicableCoupons(cart) {
    const applicableCoupons = [];
    
    for (const [id, coupon] of this.coupons) {
      const discount = this.calculateDiscount(coupon, cart);
      if (discount > 0) {
        applicableCoupons.push({
          coupon_id: id,
          type: coupon.type,
          discount
        });
      }
    }
    
    return applicableCoupons;
  }

  async applyCoupon(id, cart) {
    const coupon = this.coupons.get(id);
    if (!coupon) {
      throw new Error('Coupon not found');
    }

    const updatedCart = this.applyCouponToCart(coupon, { ...cart });
    return updatedCart;
  }

  calculateDiscount(coupon, cart) {
    switch (coupon.type) {
      case 'cart-wise':
        return this.calculateCartWiseDiscount(coupon, cart);
      case 'product-wise':
        return this.calculateProductWiseDiscount(coupon, cart);
      case 'bxgy':
        return this.calculateBxGyDiscount(coupon, cart);
      default:
        return 0;
    }
  }

  calculateCartWiseDiscount(coupon, cart) {
    const cartTotal = cart.items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    if (cartTotal >= coupon.details.threshold) {
      return (cartTotal * coupon.details.discount) / 100;
    }
    return 0;
  }

  calculateProductWiseDiscount(coupon, cart) {
    const item = cart.items.find(item => item.product_id === coupon.details.product_id);
    if (item) {
      return (item.price * item.quantity * coupon.details.discount) / 100;
    }
    return 0;
  }

  calculateBxGyDiscount(coupon, cart) {
    const { buy_products, get_products, repetition_limit } = coupon.details;
    
    // Calculate how many times the BXGY condition can be applied
    const buyProductCounts = new Map();
    cart.items.forEach(item => {
      buyProductCounts.set(item.product_id, (buyProductCounts.get(item.product_id) || 0) + item.quantity);
    });

    // Check if we have enough "buy" products
    const possibleApplications = Math.min(
      ...buy_products.map(prod => 
        Math.floor((buyProductCounts.get(prod.product_id) || 0) / prod.quantity)
      )
    );

    if (possibleApplications === 0) return 0;

    // Calculate total discount based on free products
    const actualApplications = Math.min(possibleApplications, repetition_limit);
    const totalDiscount = get_products.reduce((sum, prod) => {
      const item = cart.items.find(item => item.product_id === prod.product_id);
      return sum + (item ? item.price * prod.quantity * actualApplications : 0);
    }, 0);

    return totalDiscount;
  }

  applyCouponToCart(coupon, cart) {
    const discount = this.calculateDiscount(coupon, cart);
    const totalPrice = cart.items.reduce((sum, item) => sum + (item.price * item.quantity), 0);

    // Apply discounts based on coupon type
    cart.items = cart.items.map(item => ({
      ...item,
      total_discount: 0 // Individual item discounts would be calculated based on coupon type
    }));

    return {
      items: cart.items,
      total_price: totalPrice,
      total_discount: discount,
      final_price: totalPrice - discount
    };
  }
}

module.exports = CouponService;