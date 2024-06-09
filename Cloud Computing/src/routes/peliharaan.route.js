const express = require("express");
const { body } = require("express-validator");
const router = express.Router();

// Require the authentificateToken middleware
const { authentificateToken } = require("../middlewares/auth");

// Import your controller or service functions for managing data
const {
  getAllPeliharaan,
  getPeliharaanById,
  createPeliharaan,
  updatePeliharaanById,
  deletePeliharaanById,
  patchPeliharaanById,
} = require("../controllers/peliharaan.controller");

// Validation rules for create and update routes
const createOrUpdateRules = [
  body("nama")
    .trim()
    .isLength({ min: 1 })
    .withMessage("Nama field is required"),
  // body("userId")
  //   .trim()
  //   .isLength({ min: 1 })
  //   .withMessage("User ID field is required"),
  //   body("waktu")
  //     .trim()
  //     .isLength({ min: 1 })
  //     .withMessage("Waktu field is required")
  //     .isISO8601()
  //     .withMessage("Waktu must be a valid ISO 8601 date and time"),
  body("umur")
  .trim()
  .isLength({ min: 1 })
  .withMessage("Umur field is required"),
  body("jenisKelamin")
    .isIn(["JANTAN", "BETINA"])
    .withMessage("JenisKelamin must be either JANTAN or BETINA"),
];

// Routes that require authentication and input validation
router.post("/", authentificateToken, createOrUpdateRules, createPeliharaan);
router.put(
  "/:peliharaanId",
  authentificateToken,
  createOrUpdateRules,
  updatePeliharaanById
);
router.patch(
  "/:peliharaanId",
  authentificateToken,
  //   createOrUpdateRules,
  patchPeliharaanById
);

// Routes that require authentication (GET all, GET by ID, DELETE)
router.get("/", authentificateToken, getAllPeliharaan);
router.get("/:peliharaanId", authentificateToken, getPeliharaanById);
router.delete("/:peliharaanId", authentificateToken, deletePeliharaanById);

module.exports = router;
