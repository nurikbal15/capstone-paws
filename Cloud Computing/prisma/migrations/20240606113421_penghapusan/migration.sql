/*
  Warnings:

  - You are about to drop the column `identifier` on the `user` table. All the data in the column will be lost.
  - You are about to drop the column `name` on the `user` table. All the data in the column will be lost.

*/
-- DropIndex
DROP INDEX `User_identifier_key` ON `user`;

-- AlterTable
ALTER TABLE `user` DROP COLUMN `identifier`,
    DROP COLUMN `name`;
