/*
  Warnings:

  - You are about to drop the column `vitamin` on the `jadwalvitamin` table. All the data in the column will be lost.
  - You are about to drop the `jadwalmain` table. If the table is not empty, all the data it contains will be lost.
  - Added the required column `jenisVitamin` to the `JadwalVitamin` table without a default value. This is not possible if the table is not empty.

*/
-- DropForeignKey
ALTER TABLE `jadwalmain` DROP FOREIGN KEY `JadwalMain_peliharaanId_fkey`;

-- AlterTable
ALTER TABLE `jadwalvitamin` DROP COLUMN `vitamin`,
    ADD COLUMN `jenisVitamin` VARCHAR(191) NOT NULL;

-- DropTable
DROP TABLE `jadwalmain`;

-- CreateTable
CREATE TABLE `JadwalAktifitas` (
    `jadwalId` INTEGER NOT NULL AUTO_INCREMENT,
    `jadwalAktifitas` VARCHAR(191) NOT NULL,
    `waktu` DATETIME(3) NOT NULL,
    `peliharaanId` INTEGER NOT NULL,

    PRIMARY KEY (`jadwalId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `JadwalAktifitas` ADD CONSTRAINT `JadwalAktifitas_peliharaanId_fkey` FOREIGN KEY (`peliharaanId`) REFERENCES `Peliharaan`(`peliharaanId`) ON DELETE RESTRICT ON UPDATE CASCADE;
