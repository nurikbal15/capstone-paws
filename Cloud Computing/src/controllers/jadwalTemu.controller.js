const { validationResult } = require('express-validator');
const {
  get,
  getById,
  create,
  updateById,
  patchById,
  deleteById,
} = require('../models/jadwalTemu.model');

// Controller function to get all jadwal makanan
const getAllJadwalTemu = async (req, res) => {
  try {
    const jadwalTemu = await get();
    res.json(jadwalTemu);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to get a jadwal makanan by ID
const getJadwalTemuById = async (req, res) => {
  const { jadwalId } = req.params;
  try {
    const jadwalTemu = await getById(parseInt(jadwalId));
    if (!jadwalTemu) {
      return res.status(404).json({ error: 'Jadwal Temuan not found' });
    }
    res.json(jadwalTemu);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to create a new jadwal makanan
const createJadwalTemu = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }
  
  const jadwalTemuData = {
    ...req.body,
    peliharaanId: parseInt(req.body.peliharaanId)
  };


  try {
    const createdJadwalTemu = await create(jadwalTemuData);
    res.status(201).json(createdJadwalTemu);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to update a jadwal makanan by ID
const updateJadwalTemuById = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  const { jadwalId } = req.params;
  const jadwalTemuData = {
    ...req.body,
    peliharaanId: parseInt(req.body.peliharaanId)
  };


  try {
    const updatedJadwalTemu = await updateById(parseInt(jadwalId), jadwalTemuData);
    if (!updatedJadwalTemu) {
      return res.status(404).json({ error: 'Jadwal Temuan not found' });
    }
    res.json(updatedJadwalTemu);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

const patchJadwalTemuById = async (req, res) => {
  // Validate inputs using Express Validator
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  const { jadwalId } = req.params;
  const jadwalTemuData = {
    ...req.body,
    // peliharaanId: parseInt(req.body.peliharaanId)
  };


  try {
    const updatedJadwalTemu = await patchById(parseInt(jadwalId), jadwalTemuData);
    if (!updatedJadwalTemu) {
      return res.status(404).json({ error: 'Jadwal Temuan not found' });
    }
    res.json(updatedJadwalTemu);
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

// Controller function to delete a jadwal makanan by ID
const deleteJadwalTemuById = async (req, res) => {
  const { jadwalId } = req.params;
  try {
    await deleteById(parseInt(jadwalId));
    res.status(204).send(); // No content in response
  } catch (error) {
    res.status(500).json({ error: 'Internal Server Error' });
  }
};

module.exports = {
  getAllJadwalTemu,
  getJadwalTemuById,
  createJadwalTemu,
  updateJadwalTemuById,
  patchJadwalTemuById,
  deleteJadwalTemuById,
};
