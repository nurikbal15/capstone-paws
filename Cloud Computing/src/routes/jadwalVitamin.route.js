const express = require("express");
const { body } = require("express-validator");
const router = express.Router();

// Require the authentificateToken middleware
const { authentificateToken } = require("../middlewares/auth");

// Import your controller or service functions for managing data
const {
  getAllJadwalVitamin,
  getJadwalVitaminById,
  createJadwalVitamin,
  updateJadwalVitaminById,
  deleteJadwalVitaminById,
  patchJadwalVitaminById,
} = require("../controllers/jadwalVitamin.controller");

// Validation rules for create and update routes
const createOrUpdateRules = [
  body("jenisVitamin")
    .trim()
    .isLength({ min: 1 })
    .withMessage("Jenis Vitamin field is required"),
  // body("userId")
  //   .trim()
  //   .isLength({ min: 1 })
  //   .withMessage("User ID field is required"),

  body("peliharaanId")
  .trim()
  .isLength({ min: 1 })
  .withMessage("peliharaanId field is required"),

  body("waktu")
    .trim()
    .isLength({ min: 1 })
    .withMessage("Waktu field is required")
    .isISO8601()
    .withMessage("Waktu must be a valid ISO 8601 date and time"),
];

// Routes that require authentication and input validation
router.post("/", authentificateToken, createOrUpdateRules, createJadwalVitamin);
router.put(
  "/:jadwalId",
  authentificateToken,
  createOrUpdateRules,
  updateJadwalVitaminById
);
router.patch(
  "/:jadwalId",
  authentificateToken,
//   createOrUpdateRules,
  patchJadwalVitaminById
);

// Routes that require authentication (GET all, GET by ID, DELETE)
router.get("/", authentificateToken, getAllJadwalVitamin);
router.get("/:jadwalId", authentificateToken, getJadwalVitaminById);
router.delete("/:jadwalId", authentificateToken, deleteJadwalVitaminById);

module.exports = router;
