const { validationResult } = require("express-validator");
const {
  get,
  getById,
  create,
  updateById,
  patchById,
  deleteById,
} = require("../models/jadwalAktifitas.model");

// Controller function to get all jadwal makanan
const getAllJadwalAktifitas = async (req, res) => {
  try {
    const jadwalAktifitas = await get();
    res.json(jadwalAktifitas);
  } catch (error) {
    res.status(500).json({ error: "Internal Server Error" });
  }
};

// Controller function to get a jadwal makanan by ID
const getJadwalAktifitasById = async (req, res) => {
  const { jadwalId } = req.params;
  try {
    const jadwalAktifitas = await getById(parseInt(jadwalId));
    if (!jadwalAktifitas) {
      return res.status(404).json({ error: "Jadwal Aktifitasan not found" });
    }
    res.json(jadwalAktifitas);
  } catch (error) {
    res.status(500).json({ error: "Internal Server Error" });
  }
};

// Controller function to create a new jadwal makanan
const createJadwalAktifitas = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  const jadwalAktifitasData = {
    ...req.body,
    peliharaanId: parseInt(req.body.peliharaanId)
  };

  try {
    const createdJadwalAktifitas = await create(jadwalAktifitasData);
    res.status(201).json(createdJadwalAktifitas);
  } catch (error) {
    res.status(500).json({ error: "Internal Server Error" });
  }
};

// Controller function to update a jadwal makanan by ID
const updateJadwalAktifitasById = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  const { jadwalId } = req.params;
  const jadwalAktifitasData = {
    ...req.body,
    peliharaanId: parseInt(req.body.peliharaanId)
  };

  try {
    const updatedJadwalAktifitas = await updateById(
      parseInt(jadwalId),
      jadwalAktifitasData
    );
    if (!updatedJadwalAktifitas) {
      return res.status(404).json({ error: "Jadwal Aktifitasan not found" });
    }
    res.json(updatedJadwalAktifitas);
  } catch (error) {
    res.status(500).json({ error: "Internal Server Error" });
  }
};

const patchJadwalAktifitasById = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  const { jadwalId } = req.params;
  const jadwalAktifitasData = {
    ...req.body,
    // peliharaanId: parseInt(req.body.peliharaanId)
  };

  try {
    const updatedJadwalAktifitas = await patchById(
      parseInt(jadwalId),
      jadwalAktifitasData
    );
    if (!updatedJadwalAktifitas) {
      return res.status(404).json({ error: "Jadwal Aktifitasan not found" });
    }
    res.json(updatedJadwalAktifitas);
  } catch (error) {
    res.status(500).json({ error: "Internal Server Error" });
  }
};

// Controller function to delete a jadwal makanan by ID
const deleteJadwalAktifitasById = async (req, res) => {
  const { jadwalId } = req.params;
  try {
    await deleteById(parseInt(jadwalId));
    res.status(204).send(); // No content in response
  } catch (error) {
    res.status(500).json({ error: "Internal Server Error" });
  }
};

module.exports = {
  getAllJadwalAktifitas,
  getJadwalAktifitasById,
  createJadwalAktifitas,
  updateJadwalAktifitasById,
  patchJadwalAktifitasById,
  deleteJadwalAktifitasById,
};
