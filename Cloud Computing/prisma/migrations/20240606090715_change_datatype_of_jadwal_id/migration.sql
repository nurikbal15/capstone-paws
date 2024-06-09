/*
  Warnings:

  - The primary key for the `jadwalmakan` table will be changed. If it partially fails, the table could be left without primary key constraint.
  - You are about to alter the column `jadwalId` on the `jadwalmakan` table. The data in that column could be lost. The data in that column will be cast from `VarChar(191)` to `Int`.

*/
-- AlterTable
ALTER TABLE `jadwalmakan` DROP PRIMARY KEY,
    MODIFY `jadwalId` INTEGER NOT NULL AUTO_INCREMENT,
    ADD PRIMARY KEY (`jadwalId`);
