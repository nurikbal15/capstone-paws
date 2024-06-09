const prisma = require("../configs/prismaClient");

// Function to retrieve all jadwal makanan from the database
const get = async () => {
  try {
    return await prisma.peliharaan.findMany();
  } catch (error) {
    console.log(error);
  }
};

// Function to retrieve a specific jadwal makanan by ID from the database
const getById = async (peliharaanId) => {
  try {
    return await prisma.peliharaan.findUnique({
      where: {
        peliharaanId,
      },
    });
  } catch (error) {
    console.log(error);
  }
};

// Function to create a new jadwal makanan in the database
const create = async (peliharaan) => {
  try {
    return await prisma.peliharaan.create({
      data: peliharaan,
    });
  } catch (error) {
    console.log(error);
  }
};

// Function to update an existing jadwal makanan by ID in the database
const updateById = async (peliharaanId, peliharaan) => {
  try {
    return await prisma.peliharaan.update({
      where: {
        peliharaanId,
      },
      data: peliharaan,
    });
  } catch (error) {
    console.log(error);
  }
};

const patchById = async (peliharaanId, peliharaan) => {
    try {
      return await prisma.peliharaan.update({
        where: {
          peliharaanId,
        },
        data: peliharaan,
      });
    } catch (error) {
      console.log(error);
    }
  };

// Function to delete a jadwal makanan by ID from the database
const deleteById = async (peliharaanId) => {
  try {
    await await prisma.peliharaan.delete({
      where: {
        peliharaanId,
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
