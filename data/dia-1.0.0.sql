SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `dia` ;
CREATE SCHEMA IF NOT EXISTS `dia` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `dia` ;

-- -----------------------------------------------------
-- Table `dia`.`garden`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dia`.`garden` ;

CREATE TABLE IF NOT EXISTS `dia`.`garden` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `garden_name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dia`.`device`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dia`.`device` ;

CREATE TABLE IF NOT EXISTS `dia`.`device` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `device_name` VARCHAR(45) NOT NULL,
  `pin` VARCHAR(45) NOT NULL,
  `device_mask` VARCHAR(45) NOT NULL,
  `garden_id` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `device_name_UNIQUE` (`device_name` ASC),
  INDEX `fk_device_garden1_idx` (`garden_id` ASC),
  CONSTRAINT `fk_device_garden1`
    FOREIGN KEY (`garden_id`)
    REFERENCES `dia`.`garden` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dia`.`device_access`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dia`.`device_access` ;

CREATE TABLE IF NOT EXISTS `dia`.`device_access` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `device_id` INT NOT NULL,
  `user_name` VARCHAR(45) NULL,
  `user_mask` VARCHAR(45) NOT NULL,
  `is_default` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_mask_UNIQUE` (`user_mask` ASC),
  CONSTRAINT `fk_device_access_device`
    FOREIGN KEY (`device_id`)
    REFERENCES `dia`.`device` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dia`.`end_point`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dia`.`end_point` ;

CREATE TABLE IF NOT EXISTS `dia`.`end_point` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `device_id` INT NOT NULL,
  `operation_mode` INT NOT NULL DEFAULT 0,
  `operation_type` INT NOT NULL DEFAULT 0,
  `schedule` VARCHAR(1000) NULL,
  `current_status` INT NOT NULL DEFAULT 0,
  `sensor_data` VARCHAR(45) NOT NULL DEFAULT 'T:0;M:0',
  PRIMARY KEY (`id`),
  INDEX `fk_end_point_device1_idx` (`device_id` ASC),
  CONSTRAINT `fk_end_point_device1`
    FOREIGN KEY (`device_id`)
    REFERENCES `dia`.`device` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `dia`.`device`
-- -----------------------------------------------------
START TRANSACTION;
USE `dia`;
INSERT INTO `dia`.`device` (`id`, `device_name`, `pin`, `device_mask`, `garden_id`) VALUES (1, 'initial-required', 'a;lksdfkjwoei#@%$!lskdfjcnjdalejlvj87327#', '99999999999', NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `dia`.`end_point`
-- -----------------------------------------------------
START TRANSACTION;
USE `dia`;
INSERT INTO `dia`.`end_point` (`id`, `device_id`, `operation_mode`, `operation_type`, `schedule`, `current_status`, `sensor_data`) VALUES (1, 1, 0, 0, NULL, NULL, NULL);

COMMIT;

