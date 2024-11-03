const Joi = require('joi');

const couponSchema = Joi.object({
  type: Joi.string().valid('cart-wise', 'product-wise', 'bxgy').required(),
  details: Joi.when('type', {
    switch: [
      {
        is: 'cart-wise',
        then: Joi.object({
          threshold: Joi.number().min(0).required(),
          discount: Joi.number().min(0).max(100).required()
        })
      },
      {
        is: 'product-wise',
        then: Joi.object({
          product_id: Joi.number().required(),
          discount: Joi.number().min(0).max(100).required()
        })
      },
      {
        is: 'bxgy',
        then: Joi.object({
          buy_products: Joi.array().items(
            Joi.object({
              product_id: Joi.number().required(),
              quantity: Joi.number().min(1).required()
            })
          ).required(),
          get_products: Joi.array().items(
            Joi.object({
              product_id: Joi.number().required(),
              quantity: Joi.number().min(1).required()
            })
          ).required(),
          repetition_limit: Joi.number().min(1).required()
        })
      }
    ]
  }).required()
});

const validateCoupon = (req, res, next) => {
  const { error } = couponSchema.validate(req.body);
  if (error) {
    return res.status(400).json({ error: error.details[0].message });
  }
  next();
};

module.exports = { validateCoupon };