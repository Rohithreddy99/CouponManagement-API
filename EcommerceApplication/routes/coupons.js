const express = require('express');
const router = express.Router();
const CouponController = require('../controllers/couponController');
const { validateCoupon } = require('../middleware/validation');

const couponController = new CouponController();

router.post('/coupons', validateCoupon, (req, res) => couponController.createCoupon(req, res));
router.get('/coupons', (req, res) => couponController.getAllCoupons(req, res));
router.get('/coupons/:id', (req, res) => couponController.getCouponById(req, res));
router.put('/coupons/:id', validateCoupon, (req, res) => couponController.updateCoupon(req, res));
router.delete('/coupons/:id', (req, res) => couponController.deleteCoupon(req, res));
router.post('/applicable-coupons', (req, res) => couponController.getApplicableCoupons(req, res));
router.post('/apply-coupon/:id', (req, res) => couponController.applyCoupon(req, res));

module.exports = router;