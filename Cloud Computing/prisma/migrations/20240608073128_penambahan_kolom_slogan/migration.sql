-- CreateTable
CREATE TABLE `Peliharaan` (
    `peliharaanId` INTEGER NOT NULL AUTO_INCREMENT,
    `nama` VARCHAR(191) NOT NULL,
    `umur` INTEGER NOT NULL,
    `jenisKelamin` ENUM('JANTAN', 'BETINA') NOT NULL,
    `slogan` VARCHAR(191) NOT NULL,
    `userId` VARCHAR(191) NOT NULL,

    PRIMARY KEY (`peliharaanId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `Peliharaan` ADD CONSTRAINT `Peliharaan_userId_fkey` FOREIGN KEY (`userId`) REFERENCES `User`(`userUID`) ON DELETE RESTRICT ON UPDATE CASCADE;
