-- CreateTable
CREATE TABLE `JadwalCemilan` (
    `jadwalId` INTEGER NOT NULL AUTO_INCREMENT,
    `jadwalCemilan` VARCHAR(191) NOT NULL,
    `waktu` DATETIME(3) NOT NULL,
    `userId` VARCHAR(191) NOT NULL,

    PRIMARY KEY (`jadwalId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `JadwalCemilan` ADD CONSTRAINT `JadwalCemilan_userId_fkey` FOREIGN KEY (`userId`) REFERENCES `User`(`userUID`) ON DELETE RESTRICT ON UPDATE CASCADE;
