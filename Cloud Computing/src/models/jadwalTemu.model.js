const prisma = require("../configs/prismaClient");

// Function to retrieve all jadwal makanan from the database
const get = async () => {
  try {
    return await prisma.jadwalTemu.findMany();
  } catch (error) {
    console.log(error);
  }
};

// Function to retrieve a specific jadwal makanan by ID from the database
const getById = async (jadwalId) => {
  try {
    return await prisma.jadwalTemu.findUnique({
      where: {
        jadwalId,
      },
    });
  } catch (error) {
    console.log(error);
  }
};

// Function to create a new jadwal makanan in the database
const create = async (jadwalTemu) => {
  try {
    return await prisma.jadwalTemu.create({
      data: jadwalTemu,
    });
  } catch (error) {
    console.log(error);
  }
};

// Function to update an existing jadwal makanan by ID in the database
const updateById = async (jadwalId, jadwalTemu) => {
  try {
    return await prisma.jadwalTemu.update({
      where: {
        jadwalId,
      },
      data: jadwalTemu,
    });
  } catch (error) {
    console.log(error);
  }
};

const patchById = async (jadwalId, jadwalTemu) => {
    try {
      return await prisma.jadwalTemu.update({
        where: {
          jadwalId,
        },
        data: jadwalTemu,
      });
    } catch (error) {
      console.log(error);
    }
  };

// Function to delete a jadwal makanan by ID from the database
const deleteById = async (jadwalId) => {
  try {
    await await prisma.jadwalTemu.delete({
      where: {
        jadwalId,
      },
    });
  } catch (error) {
    console.log(error);
  }
};

module.exports = {
  get,
  getById,
  create,
  updateById,
  patchById,
  deleteById,
};
