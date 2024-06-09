/*
  Warnings:

  - You are about to drop the `jadwalcemilan` table. If the table is not empty, all the data it contains will be lost.

*/
-- DropForeignKey
ALTER TABLE `jadwalcemilan` DROP FOREIGN KEY `JadwalCemilan_peliharaanId_fkey`;

-- DropTable
DROP TABLE `jadwalcemilan`;
