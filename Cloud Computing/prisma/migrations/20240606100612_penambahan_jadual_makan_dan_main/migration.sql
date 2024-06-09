-- CreateTable
CREATE TABLE `JadwalVitamin` (
    `jadwalId` INTEGER NOT NULL AUTO_INCREMENT,
    `vitamin` VARCHAR(191) NOT NULL,
    `waktu` DATETIME(3) NOT NULL,
    `userId` VARCHAR(191) NOT NULL,

    PRIMARY KEY (`jadwalId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `JadwalMain` (
    `jadwalId` INTEGER NOT NULL AUTO_INCREMENT,
    `aktivitasMainBulu` VARCHAR(191) NOT NULL,
    `waktu` DATETIME(3) NOT NULL,
    `userId` VARCHAR(191) NOT NULL,

    PRIMARY KEY (`jadwalId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `JadwalVitamin` ADD CONSTRAINT `JadwalVitamin_userId_fkey` FOREIGN KEY (`userId`) REFERENCES `User`(`userUID`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `JadwalMain` ADD CONSTRAINT `JadwalMain_userId_fkey` FOREIGN KEY (`userId`) REFERENCES `User`(`userUID`) ON DELETE RESTRICT ON UPDATE CASCADE;
