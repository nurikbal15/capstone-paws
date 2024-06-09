const express = require("express");
const { body } = require("express-validator");
const router = express.Router();

// Require the authentificateToken middleware
const { authentificateToken } = require("../middlewares/auth");

// Import your controller or service functions for managing data
const {
  getAllJadwalTemu,
  getJadwalTemuById,
  createJadwalTemu,
  updateJadwalTemuById,
  deleteJadwalTemuById,
  patchJadwalTemuById,
} = require("../controllers/jadwalTemu.controller");

// Validation rules for create and update routes
const createOrUpdateRules = [
  body("jadwalTemu")
    .trim()
    .isLength({ min: 1 })
    .withMessage("Jadwal Temu field is required"),
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
router.post("/", authentificateToken, createOrUpdateRules, createJadwalTemu);
router.put(
  "/:jadwalId",
  authentificateToken,
  createOrUpdateRules,
  updateJadwalTemuById
);
router.patch(
  "/:jadwalId",
  authentificateToken,
//   createOrUpdateRules,
  patchJadwalTemuById
);

// Routes that require authentication (GET all, GET by ID, DELETE)
router.get("/", authentificateToken, getAllJadwalTemu);
router.get("/:jadwalId", authentificateToken, getJadwalTemuById);
router.delete("/:jadwalId", authentificateToken, deleteJadwalTemuById);

module.exports = router;
