/*
  Warnings:

  - You are about to drop the column `slogan` on the `peliharaan` table. All the data in the column will be lost.

*/
-- AlterTable
ALTER TABLE `peliharaan` DROP COLUMN `slogan`;

-- CreateTable
CREATE TABLE `JadwalTemu` (
    `jadwalId` INTEGER NOT NULL AUTO_INCREMENT,
    `jadwalTemu` VARCHAR(191) NOT NULL,
    `waktu` DATETIME(3) NOT NULL,
    `userId` VARCHAR(191) NOT NULL,

    PRIMARY KEY (`jadwalId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `JadwalTemu` ADD CONSTRAINT `JadwalTemu_userId_fkey` FOREIGN KEY (`userId`) REFERENCES `User`(`userUID`) ON DELETE RESTRICT ON UPDATE CASCADE;
