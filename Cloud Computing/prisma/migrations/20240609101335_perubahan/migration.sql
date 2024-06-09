/*
  Warnings:

  - You are about to drop the column `userId` on the `jadwalcemilan` table. All the data in the column will be lost.
  - You are about to drop the column `userId` on the `jadwalmain` table. All the data in the column will be lost.
  - You are about to drop the column `userId` on the `jadwalmakan` table. All the data in the column will be lost.
  - You are about to drop the column `userId` on the `jadwaltemu` table. All the data in the column will be lost.
  - You are about to drop the column `userId` on the `jadwalvitamin` table. All the data in the column will be lost.
  - Added the required column `peliharaanId` to the `JadwalCemilan` table without a default value. This is not possible if the table is not empty.
  - Added the required column `peliharaanId` to the `JadwalMain` table without a default value. This is not possible if the table is not empty.
  - Added the required column `peliharaanId` to the `JadwalMakan` table without a default value. This is not possible if the table is not empty.
  - Added the required column `peliharaanId` to the `JadwalTemu` table without a default value. This is not possible if the table is not empty.
  - Added the required column `peliharaanId` to the `JadwalVitamin` table without a default value. This is not possible if the table is not empty.

*/
-- DropForeignKey
ALTER TABLE `jadwalcemilan` DROP FOREIGN KEY `JadwalCemilan_userId_fkey`;

-- DropForeignKey
ALTER TABLE `jadwalmain` DROP FOREIGN KEY `JadwalMain_userId_fkey`;

-- DropForeignKey
ALTER TABLE `jadwalmakan` DROP FOREIGN KEY `JadwalMakan_userId_fkey`;

-- DropForeignKey
ALTER TABLE `jadwaltemu` DROP FOREIGN KEY `JadwalTemu_userId_fkey`;

-- DropForeignKey
ALTER TABLE `jadwalvitamin` DROP FOREIGN KEY `JadwalVitamin_userId_fkey`;

-- AlterTable
ALTER TABLE `jadwalcemilan` DROP COLUMN `userId`,
    ADD COLUMN `peliharaanId` INTEGER NOT NULL;

-- AlterTable
ALTER TABLE `jadwalmain` DROP COLUMN `userId`,
    ADD COLUMN `peliharaanId` INTEGER NOT NULL;

-- AlterTable
ALTER TABLE `jadwalmakan` DROP COLUMN `userId`,
    ADD COLUMN `peliharaanId` INTEGER NOT NULL;

-- AlterTable
ALTER TABLE `jadwaltemu` DROP COLUMN `userId`,
    ADD COLUMN `peliharaanId` INTEGER NOT NULL;

-- AlterTable
ALTER TABLE `jadwalvitamin` DROP COLUMN `userId`,
    ADD COLUMN `peliharaanId` INTEGER NOT NULL;

-- AddForeignKey
ALTER TABLE `JadwalMakan` ADD CONSTRAINT `JadwalMakan_peliharaanId_fkey` FOREIGN KEY (`peliharaanId`) REFERENCES `Peliharaan`(`peliharaanId`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `JadwalVitamin` ADD CONSTRAINT `JadwalVitamin_peliharaanId_fkey` FOREIGN KEY (`peliharaanId`) REFERENCES `Peliharaan`(`peliharaanId`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `JadwalMain` ADD CONSTRAINT `JadwalMain_peliharaanId_fkey` FOREIGN KEY (`peliharaanId`) REFERENCES `Peliharaan`(`peliharaanId`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `JadwalCemilan` ADD CONSTRAINT `JadwalCemilan_peliharaanId_fkey` FOREIGN KEY (`peliharaanId`) REFERENCES `Peliharaan`(`peliharaanId`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `JadwalTemu` ADD CONSTRAINT `JadwalTemu_peliharaanId_fkey` FOREIGN KEY (`peliharaanId`) REFERENCES `Peliharaan`(`peliharaanId`) ON DELETE RESTRICT ON UPDATE CASCADE;
