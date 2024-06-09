-- CreateTable
CREATE TABLE `JadwalMakan` (
    `jadwalId` VARCHAR(191) NOT NULL,
    `jadwalMakan` VARCHAR(191) NOT NULL,
    `waktu` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `userId` VARCHAR(191) NOT NULL,

    PRIMARY KEY (`jadwalId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `User` (
    `userUID` VARCHAR(191) NOT NULL,
    `identifier` VARCHAR(191) NOT NULL,

    UNIQUE INDEX `User_identifier_key`(`identifier`),
    PRIMARY KEY (`userUID`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `JadwalMakan` ADD CONSTRAINT `JadwalMakan_userId_fkey` FOREIGN KEY (`userId`) REFERENCES `User`(`userUID`) ON DELETE RESTRICT ON UPDATE CASCADE;
