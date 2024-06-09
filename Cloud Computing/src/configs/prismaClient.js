const { PrismaClient } = require('@prisma/client');

let prisma;

if (!prisma) {
  prisma = new PrismaClient();
  prisma.$connect();
}

module.exports = prisma;
