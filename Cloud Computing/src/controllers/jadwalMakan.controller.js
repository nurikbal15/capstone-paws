const { validationResult } = require('express-validator');
const {
  get,
  getById,
  create,
  updateById,
  patchById,
  deleteById,
} = require('../models/jadwalMakan.model');

// Controller function to get all jadwal makanan
const getAllJadwalMakan = async (req, res) => {
  try {
    const jadwalMakan = await get();
    res.json(jadwalMakan);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to get a jadwal makanan by ID
const getJadwalMakanById = async (req, res) => {
  const { jadwalId } = req.params;
  try {
    const jadwalMakan = await getById(parseInt(jadwalId));
    if (!jadwalMakan) {
      return res.status(404).json({ error: 'Jadwal Makanan not found' });
    }
    res.json(jadwalMakan);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to create a new jadwal makanan
const createJadwalMakan = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }
  
  const jadwalMakanData = {
    ...req.body,
    peliharaanId: parseInt(req.body.peliharaanId)
  };

  try {
    const createdJadwalMakan = await create(jadwalMakanData);
    res.status(201).json(createdJadwalMakan);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to update a jadwal makanan by ID
const updateJadwalMakanById = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  const { jadwalId } = req.params;
  const jadwalMakanData = {
    ...req.body,
    peliharaanId: parseInt(req.body.peliharaanId)
  };


  try {
    const updatedJadwalMakan = await updateById(parseInt(jadwalId), jadwalMakanData);
    if (!updatedJadwalMakan) {
      return res.status(404).json({ error: 'Jadwal Makanan not found' });
    }
    res.json(updatedJadwalMakan);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

const patchJadwalMakanById = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  const { jadwalId } = req.params;
  const jadwalMakanData = {
    ...req.body,
    // peliharaanId: parseInt(req.body.peliharaanId)
  };


  try {
    const updatedJadwalMakan = await patchById(parseInt(jadwalId), jadwalMakanData);
    if (!updatedJadwalMakan) {
      return res.status(404).json({ error: 'Jadwal Makanan not found' });
    }
    res.json(updatedJadwalMakan);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to delete a jadwal makanan by ID
const deleteJadwalMakanById = async (req, res) => {
  const { jadwalId } = req.params;
  try {
    await deleteById(parseInt(jadwalId));
    res.status(204).send(); // No content in response
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

module.exports = {
  getAllJadwalMakan,
  getJadwalMakanById,
  createJadwalMakan,
  updateJadwalMakanById,
  patchJadwalMakanById,
  deleteJadwalMakanById,
};
