# ECommerceApplication


A RESTful API for managing different types of discount coupons in an e-commerce platform.

## Implemented Cases

### 1. Cart-wise Coupons
- Percentage discount on total cart value above threshold
- Example: 10% off on cart total > $100

### 2. Product-wise Coupons
- Percentage discount on specific products
- Example: 20% off on Product A

### 3. Buy X Get Y (BXGY) Coupons
- Buy specific quantities of products to get other products free
- Supports multiple product combinations
- Configurable repetition limit
- Example: Buy 2 of [X,Y,Z], get 1 of [A,B,C] free

## Unimplemented Cases (Future Enhancements)

1. Time-based Constraints
   - Expiration dates
   - Valid time windows
   - Seasonal discounts

2. User-specific Constraints
   - First-time user discounts
   - Loyalty tier-based discounts
   - Usage limits per user

3. Complex Combinations
   - Stackable coupons
   - Priority-based coupon application
   - Maximum discount limits

4. Category-based Discounts
   - Discounts on product categories
   - Category combination deals

5. Dynamic Pricing
   - Time-sensitive pricing
   - Inventory-based pricing
   - Bulk purchase discounts

## Limitations

1. In-memory Database
   - Data persistence not implemented
   - Restarts clear all coupons

2. Concurrency
   - No race condition handling
   - No distributed system support

3. Performance
   - O(n) lookup for coupon validation
   - No caching implemented

4. Validation
   - Basic input validation only
   - No complex business rule validation

## Assumptions

1. Product Data
   - Product IDs are unique integers
   - Prices are positive numbers
   - Quantities are positive integers

2. Discount Application
   - Only one coupon per cart
   - Discounts calculated on original prices
   - Free items added after discount calculation

3. Business Rules
   - Discounts cannot exceed 100%
   - Cart total calculated before tax
   - All prices in same currency

## API Endpoints

### Create Coupon
```http
POST /api/coupons
```

### Get All Coupons
```http
GET /api/coupons
```

### Get Coupon by ID
```http
GET /api/coupons/:id
```

### Update Coupon
```http
PUT /api/coupons/:id
```

### Delete Coupon
```http
DELETE /api/coupons/:id
```

### Get Applicable Coupons
```http
POST /api/applicable-coupons
```

### Apply Coupon
```http
POST /api/apply-coupon/:id
```

## Setup and Running

1. Install dependencies:
```bash
npm install
```

2. Start the server:
```bash
npm start
```

3. Development mode:
```bash
npm run dev
```

4. Run tests:
```bash
npm test
```