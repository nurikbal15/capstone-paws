const { validationResult } = require('express-validator');
const {
  get,
  getById,
  create,
  updateById,
  patchById,
  deleteById,
} = require('../models/jadwalVitamin.model');

// Controller function to get all jadwal makanan
const getAllJadwalVitamin = async (req, res) => {
  try {
    const jadwalVitamin = await get();
    res.json(jadwalVitamin);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to get a jadwal makanan by ID
const getJadwalVitaminById = async (req, res) => {
  const { jadwalId } = req.params;
  try {
    const jadwalVitamin = await getById(parseInt(jadwalId));
    if (!jadwalVitamin) {
      return res.status(404).json({ error: 'Jadwal Vitaminan not found' });
    }
    res.json(jadwalVitamin);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to create a new jadwal makanan
const createJadwalVitamin = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }
  
  const jadwalVitaminData = {
    ...req.body,
    peliharaanId: parseInt(req.body.peliharaanId)
  };

  try {
    const createdJadwalVitamin = await create(jadwalVitaminData);
    res.status(201).json(createdJadwalVitamin);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to update a jadwal makanan by ID
const updateJadwalVitaminById = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  const { jadwalId } = req.params;
  const jadwalVitaminData = {
    ...req.body,
    peliharaanId: parseInt(req.body.peliharaanId)
  };


  try {
    const updatedJadwalVitamin = await updateById(parseInt(jadwalId), jadwalVitaminData);
    if (!updatedJadwalVitamin) {
      return res.status(404).json({ error: 'Jadwal Vitaminan not found' });
    }
    res.json(updatedJadwalVitamin);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

const patchJadwalVitaminById = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  const { jadwalId } = req.params;
  const jadwalVitaminData = {
    ...req.body,
    // peliharaanId: parseInt(req.body.peliharaanId)
  };


  try {
    const updatedJadwalVitamin = await patchById(parseInt(jadwalId), jadwalVitaminData);
    if (!updatedJadwalVitamin) {
      return res.status(404).json({ error: 'Jadwal Vitaminan not found' });
    }
    res.json(updatedJadwalVitamin);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to delete a jadwal makanan by ID
const deleteJadwalVitaminById = async (req, res) => {
  const { jadwalId } = req.params;
  try {
    await deleteById(parseInt(jadwalId));
    res.status(204).send(); // No content in response
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

module.exports = {
  getAllJadwalVitamin,
  getJadwalVitaminById,
  createJadwalVitamin,
  updateJadwalVitaminById,
  patchJadwalVitaminById,
  deleteJadwalVitaminById,
};
