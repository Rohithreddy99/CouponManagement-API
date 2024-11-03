const CouponService = require('../src/services/couponService');

describe('CouponService', () => {
  let service;

  beforeEach(() => {
    service = new CouponService();
  });

  describe('Cart-wise Coupons', () => {
    test('should apply discount when cart total exceeds threshold', async () => {
      const coupon = {
        id: '1',
        type: 'cart-wise',
        details: {
          threshold: 100,
          discount: 10
        }
      };

      const cart = {
        items: [
          { product_id: 1, quantity: 2, price: 60 }
        ]
      };

      await service.createCoupon(coupon);
      const result = await service.applyCoupon('1', cart);
      
      expect(result.total_discount).toBe(12); // 10% of 120
      expect(result.final_price).toBe(108);
    });
  });

  describe('Product-wise Coupons', () => {
    test('should apply discount to specific product', async () => {
      const coupon = {
        id: '2',
        type: 'product-wise',
        details: {
          product_id: 1,
          discount: 20
        }
      };

      const cart = {
        items: [
          { product_id: 1, quantity: 1, price: 100 },
          { product_id: 2, quantity: 1, price: 50 }
        ]
      };

      await service.createCoupon(coupon);
      const result = await service.applyCoupon('2', cart);
      
      expect(result.total_discount).toBe(20); // 20% of 100
      expect(result.final_price).toBe(130);
    });
  });

  describe('BXGY Coupons', () => {
    test('should apply buy X get Y discount correctly', async () => {
      const coupon = {
        id: '3',
        type: 'bxgy',
        details: {
          buy_products: [
            { product_id: 1, quantity: 2 }
          ],
          get_products: [
            { product_id: 2, quantity: 1 }
          ],
          repetition_limit: 1
        }
      };

      const cart = {
        items: [
          { product_id: 1, quantity: 2, price: 50 },
          { product_id: 2, quantity: 1, price: 30 }
        ]
      };

      await service.createCoupon(coupon);
      const result = await service.applyCoupon('3', cart);
      
      expect(result.total_discount).toBe(30); // Free product worth 30
      expect(result.final_price).toBe(100);
    });
  });
});