SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `Prognos_NewWorldModel` DEFAULT CHARACTER SET utf8 ;
USE `Prognos_NewWorldModel` ;

-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Organization`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Organization` (
  `OrganizationID` CHAR(45) NOT NULL ,
  `isTerroristOrganization` CHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`OrganizationID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Person`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Person` (
  `PersonID` CHAR(45) NOT NULL ,
  `hasEconomicStanding` CHAR(45) NULL DEFAULT NULL ,
  `hasNationality` CHAR(45) NULL DEFAULT NULL ,
  `hasOccupation` CHAR(45) NULL DEFAULT NULL ,
  `hasClusterPartition` CHAR(45) NULL DEFAULT NULL ,
  `hasEducationLevel` CHAR(45) NULL DEFAULT NULL ,
  `hasOIForOEFInfluence` CHAR(45) NULL DEFAULT NULL ,
  `knowsPersonImprisionedInOIForOEF` CHAR(45) NULL DEFAULT NULL ,
  `knowsPersonKilledInOIForOEF` CHAR(45) NULL DEFAULT NULL ,
  `hasInfluencePartition` CHAR(45) NULL DEFAULT NULL ,
  `hasFamilyStatus` CHAR(45) NULL DEFAULT NULL ,
  `hasKinshipToTerrorist` CHAR(45) NULL DEFAULT NULL ,
  `hasFriendshipWithTerrorist` CHAR(45) NULL DEFAULT NULL ,
  `hasTerroristBeliefs` CHAR(45) NULL DEFAULT NULL ,
  `communicatesWithTerrorist` CHAR(45) NULL DEFAULT NULL ,
  `usesWeblog` CHAR(45) NULL DEFAULT NULL ,
  `usesEmail` CHAR(45) NULL DEFAULT NULL ,
  `usesCellular` CHAR(45) NULL DEFAULT NULL ,
  `usesChatroom` CHAR(45) NULL DEFAULT NULL ,
  `isTerroristPerson` CHAR(45) NULL ,
  PRIMARY KEY (`PersonID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Person_Org`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Person_Org` (
  `PersonID` CHAR(45) NOT NULL ,
  `OrganizationID` CHAR(45) NOT NULL ,
  `isMemberOfOrganization` CHAR(45) NULL ,
  PRIMARY KEY (`PersonID`, `OrganizationID`) ,
  INDEX `fk_person_org_person1_idx` (`PersonID` ASC) ,
  INDEX `fk_person_org_organization1_idx` (`OrganizationID` ASC) ,
  CONSTRAINT `fk_person_org_person1`
    FOREIGN KEY (`PersonID` )
    REFERENCES `Prognos_NewWorldModel`.`Person` (`PersonID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_org_organization1`
    FOREIGN KEY (`OrganizationID` )
    REFERENCES `Prognos_NewWorldModel`.`Organization` (`OrganizationID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Ship`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Ship` (
  `ShipID` CHAR(45) NOT NULL ,
  `hasErraticBehavior` CHAR(45) NULL DEFAULT NULL ,
  `hasEquipmentFailure` CHAR(45) NULL DEFAULT NULL ,
  `isCrewVisible` CHAR(45) NULL DEFAULT NULL ,
  `hasTerroristPlan` CHAR(45) NULL DEFAULT NULL ,
  `hasTerroristCrew` CHAR(45) NULL DEFAULT NULL ,
  `hasWeaponVisible` CHAR(45) NULL DEFAULT NULL ,
  `propellerTurnCount` CHAR(45) NULL DEFAULT NULL ,
  `shipRCSchange` CHAR(45) NULL DEFAULT NULL ,
  `hasAggressiveBehavior` CHAR(45) NULL DEFAULT NULL ,
  `turnRate` CHAR(45) NULL DEFAULT NULL ,
  `speedChange` CHAR(45) NULL DEFAULT NULL ,
  `cavitation` CHAR(45) NULL DEFAULT NULL ,
  `isJettisoningCargo` CHAR(45) NULL DEFAULT NULL ,
  `hasExchangeIllicitCargoPlan` CHAR(45) NULL DEFAULT NULL ,
  `hasExchangeIllicitCargoPartition` CHAR(45) NULL DEFAULT NULL ,
  `hasBombPortPlan` CHAR(45) NULL DEFAULT NULL ,
  `isShipOfInterest` CHAR(45) NULL DEFAULT NULL ,
  `hasNormalChangeInDestination` CHAR(45) NULL DEFAULT NULL ,
  `hasUnusualRoute` CHAR(45) NULL DEFAULT NULL ,
  `hasTypeOfShip` CHAR(45) NULL DEFAULT NULL ,
  `isHijacked` CHAR(45) NULL DEFAULT NULL ,
  `hasEvasiveBehavior` CHAR(45) NULL DEFAULT NULL ,
  `isElectronicsWorking` CHAR(45) NULL DEFAULT NULL ,
  `hasResponsiveRadio` CHAR(45) NULL DEFAULT NULL ,
  `hasResponsiveAIS` CHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`ShipID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Ship_Person`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Ship_Person` (
  `ShipID` CHAR(45) NOT NULL ,
  `PersonID` CHAR(45) NOT NULL ,
  `hasCrewMember` CHAR(45) NULL ,
  PRIMARY KEY (`ShipID`, `PersonID`) ,
  INDEX `fk_ship_person_ship1_idx` (`ShipID` ASC) ,
  INDEX `fk_ship_person_person1_idx` (`PersonID` ASC) ,
  CONSTRAINT `fk_ship_person_ship1`
    FOREIGN KEY (`ShipID` )
    REFERENCES `Prognos_NewWorldModel`.`Ship` (`ShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ship_person_person1`
    FOREIGN KEY (`PersonID` )
    REFERENCES `Prognos_NewWorldModel`.`Person` (`PersonID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Ship_Report`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Ship_Report` (
  `ReportedShipID` CHAR(45) NOT NULL ,
  `hasErraticBehaviorRPT` CHAR(45) NULL ,
  `hasResponsiveAISRPT` CHAR(45) NULL ,
  `hasEquipmentFailureRPT` CHAR(45) NULL DEFAULT NULL ,
  `isCrewVisibleRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasTerroristPlanRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasTerroristCrewRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasWeaponVisibleRPT` CHAR(45) NULL DEFAULT NULL ,
  `propellerTurnCountRPT` CHAR(45) NULL DEFAULT NULL ,
  `shipRCSchangeRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasAggressiveBehaviorRPT` CHAR(45) NULL DEFAULT NULL ,
  `turnRateRPT` CHAR(45) NULL DEFAULT NULL ,
  `speedChangeRPT` CHAR(45) NULL DEFAULT NULL ,
  `cavitationRPT` CHAR(45) NULL DEFAULT NULL ,
  `isJettisoningCargoRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasExchangeIllicitCargoPlanRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasExchangeIllicitCargoPartitionRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasBombPortPlanRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasNormalChangeInDestinationRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasUnusualRouteRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasTypeOfShipRPT` CHAR(45) NULL DEFAULT NULL ,
  `isHijackedRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasEvasiveBehaviorRPT` CHAR(45) NULL DEFAULT NULL ,
  `isElectronicsWorkingRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasResponsiveRadioRPT` CHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`ReportedShipID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Person_Report`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Person_Report` (
  `ReportedPersonID` CHAR(45) NOT NULL ,
  `hasEconomicStandingRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasNationalityRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasOccupationRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasClusterPartitionRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasEducationLevelRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasOIForOEFInfluenceRPT` CHAR(45) NULL DEFAULT NULL ,
  `knowsPersonImprisionedInOIForOEFRPT` CHAR(45) NULL DEFAULT NULL ,
  `knowsPersonKilledInOIForOEFRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasInfluencePartition` CHAR(45) NULL DEFAULT NULL ,
  `hasFamilyStatusRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasKinshipToTerroristRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasFriendshipWithTerroristRPT` CHAR(45) NULL DEFAULT NULL ,
  `hasTerroristBeliefsRPT` CHAR(45) NULL DEFAULT NULL ,
  `communicatesWithTerroristRPT` CHAR(45) NULL DEFAULT NULL ,
  `usesWeblogRPT` CHAR(45) NULL DEFAULT NULL ,
  `usesEmail` CHAR(45) NULL DEFAULT NULL ,
  `usesCellularRPT` CHAR(45) NULL DEFAULT NULL ,
  `usesChatroomRPT` CHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`ReportedPersonID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Ship_Person_Report`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Ship_Person_Report` (
  `ReportedShipID` CHAR(45) NOT NULL ,
  `ReportedPersonID` CHAR(45) NOT NULL ,
  `hasCrewMemberRPT` CHAR(45) NULL ,
  PRIMARY KEY (`ReportedShipID`, `ReportedPersonID`) ,
  INDEX `fk_ship_person_Report_ship_Report1_idx` (`ReportedShipID` ASC) ,
  INDEX `fk_ship_person_Report_person_Report1_idx` (`ReportedPersonID` ASC) ,
  CONSTRAINT `fk_ship_person_Report_ship_Report1`
    FOREIGN KEY (`ReportedShipID` )
    REFERENCES `Prognos_NewWorldModel`.`Ship_Report` (`ReportedShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ship_person_Report_person_Report1`
    FOREIGN KEY (`ReportedPersonID` )
    REFERENCES `Prognos_NewWorldModel`.`Person_Report` (`ReportedPersonID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Organization_Report`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Organization_Report` (
  `ReportedOrganizationID` CHAR(45) NOT NULL ,
  `isTerroristOrganizationRPT` CHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`ReportedOrganizationID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Person_Org_Report`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Person_Org_Report` (
  `ReportedPersonID` CHAR(45) NOT NULL ,
  `ReportedOrganizationID` CHAR(45) NOT NULL ,
  `isMemberOfOrganizationRPT` CHAR(45) NULL ,
  PRIMARY KEY (`ReportedPersonID`, `ReportedOrganizationID`) ,
  INDEX `fk_person_org_Report_person_Report1_idx` (`ReportedPersonID` ASC) ,
  INDEX `fk_person_org_Report_organization_Report1_idx` (`ReportedOrganizationID` ASC) ,
  CONSTRAINT `fk_person_org_Report_person_Report1`
    FOREIGN KEY (`ReportedPersonID` )
    REFERENCES `Prognos_NewWorldModel`.`Person_Report` (`ReportedPersonID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_org_Report_organization_Report1`
    FOREIGN KEY (`ReportedOrganizationID` )
    REFERENCES `Prognos_NewWorldModel`.`Organization_Report` (`ReportedOrganizationID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`ShipSensor`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`ShipSensor` (
  `SensorID` CHAR(45) NOT NULL ,
  PRIMARY KEY (`SensorID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`ShipSensorOf`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`ShipSensorOf` (
  `sr` CHAR(45) NOT NULL ,
  `tr` CHAR(45) NOT NULL ,
  PRIMARY KEY (`sr`, `tr`) ,
  INDEX `fk_ShipSensorOf_Ship1_idx` (`tr` ASC) ,
  CONSTRAINT `fk_SensorOf_Sensor1`
    FOREIGN KEY (`sr` )
    REFERENCES `Prognos_NewWorldModel`.`ShipSensor` (`SensorID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ShipSensorOf_Ship1`
    FOREIGN KEY (`tr` )
    REFERENCES `Prognos_NewWorldModel`.`Ship` (`ShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`ShipSensorProperty`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`ShipSensorProperty` (
  `sr` CHAR(45) NOT NULL ,
  `tr` CHAR(45) NOT NULL ,
  `sensorPerformance` CHAR(45) NULL ,
  PRIMARY KEY (`sr`, `tr`) ,
  INDEX `fk_ShipSensorProperty_Ship1_idx` (`tr` ASC) ,
  CONSTRAINT `fk_SensorProperty_Sensor1`
    FOREIGN KEY (`sr` )
    REFERENCES `Prognos_NewWorldModel`.`ShipSensor` (`SensorID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ShipSensorProperty_Ship1`
    FOREIGN KEY (`tr` )
    REFERENCES `Prognos_NewWorldModel`.`Ship` (`ShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`ActualShip`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`ActualShip` (
  `rt` CHAR(45) NOT NULL ,
  `ActualShip` CHAR(45) NOT NULL ,
  INDEX `fk_ActualShip_Ship_Report1_idx` (`rt` ASC) ,
  INDEX `fk_ActualShip_Ship1_idx` (`ActualShip` ASC) ,
  PRIMARY KEY (`rt`) ,
  CONSTRAINT `fk_ActualShip_Ship_Report1`
    FOREIGN KEY (`rt` )
    REFERENCES `Prognos_NewWorldModel`.`Ship_Report` (`ReportedShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ActualShip_Ship1`
    FOREIGN KEY (`ActualShip` )
    REFERENCES `Prognos_NewWorldModel`.`Ship` (`ShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Ship_Ship`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Ship_Ship` (
  `ShipID1` CHAR(45) NOT NULL ,
  `ShipID2` CHAR(45) NOT NULL ,
  `isWithinRadarRange` CHAR(45) NULL ,
  `areMeeting` VARCHAR(45) NULL ,
  PRIMARY KEY (`ShipID1`, `ShipID2`) ,
  INDEX `fk_ship_ship_ship1_idx` (`ShipID1` ASC) ,
  INDEX `fk_ship_ship_ship2_idx` (`ShipID2` ASC) ,
  CONSTRAINT `fk_ship_ship_ship1`
    FOREIGN KEY (`ShipID1` )
    REFERENCES `Prognos_NewWorldModel`.`Ship` (`ShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ship_ship_ship2`
    FOREIGN KEY (`ShipID2` )
    REFERENCES `Prognos_NewWorldModel`.`Ship` (`ShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Ship_Ship_Report`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Ship_Ship_Report` (
  `ReportedShipID1` CHAR(45) NOT NULL ,
  `ReportedShipID2` CHAR(45) NOT NULL ,
  `isWithinRadarRangeRPT` CHAR(45) NULL ,
  `areMeetingRPT` CHAR(45) NULL ,
  PRIMARY KEY (`ReportedShipID1`, `ReportedShipID2`) ,
  INDEX `fk_ship_ship_Report_ship_Report1_idx` (`ReportedShipID1` ASC) ,
  INDEX `fk_ship_ship_Report_ship_Report2_idx` (`ReportedShipID2` ASC) ,
  CONSTRAINT `fk_ship_ship_Report_ship_Report1`
    FOREIGN KEY (`ReportedShipID1` )
    REFERENCES `Prognos_NewWorldModel`.`Ship_Report` (`ReportedShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ship_ship_Report_ship_Report2`
    FOREIGN KEY (`ReportedShipID2` )
    REFERENCES `Prognos_NewWorldModel`.`Ship_Report` (`ReportedShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Field`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Field` (
  `FieldID` CHAR(45) NOT NULL ,
  `potentialTerroristAttacks` CHAR(45) NULL ,
  PRIMARY KEY (`FieldID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`Location`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`Location` (
  `ShipID` CHAR(45) NOT NULL ,
  `FieldID` CHAR(45) NOT NULL ,
  PRIMARY KEY (`ShipID`) ,
  INDEX `fk_location_ship1_idx` (`ShipID` ASC) ,
  INDEX `fk_location_field1_idx` (`FieldID` ASC) ,
  CONSTRAINT `fk_location_ship1`
    FOREIGN KEY (`ShipID` )
    REFERENCES `Prognos_NewWorldModel`.`Ship` (`ShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_location_field1`
    FOREIGN KEY (`FieldID` )
    REFERENCES `Prognos_NewWorldModel`.`Field` (`FieldID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`ActualPerson`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`ActualPerson` (
  `rt` CHAR(45) NOT NULL ,
  `ActualPerson` CHAR(45) NOT NULL ,
  PRIMARY KEY (`rt`) ,
  INDEX `fk_ActualPerson_Person1_idx` (`ActualPerson` ASC) ,
  CONSTRAINT `fk_ActualPerson_Person_Report1`
    FOREIGN KEY (`rt` )
    REFERENCES `Prognos_NewWorldModel`.`Person_Report` (`ReportedPersonID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ActualPerson_Person1`
    FOREIGN KEY (`ActualPerson` )
    REFERENCES `Prognos_NewWorldModel`.`Person` (`PersonID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`ActualOrganization`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`ActualOrganization` (
  `rt` CHAR(45) NOT NULL ,
  `ActualOrganization` CHAR(45) NOT NULL ,
  PRIMARY KEY (`rt`) ,
  INDEX `fk_ActualOrganization_Organization1_idx` (`ActualOrganization` ASC) ,
  CONSTRAINT `fk_ActualOrganization_Organization_Report1`
    FOREIGN KEY (`rt` )
    REFERENCES `Prognos_NewWorldModel`.`Organization_Report` (`ReportedOrganizationID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ActualOrganization_Organization1`
    FOREIGN KEY (`ActualOrganization` )
    REFERENCES `Prognos_NewWorldModel`.`Organization` (`OrganizationID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`PersonSensor`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`PersonSensor` (
  `SensorID` CHAR(45) NOT NULL ,
  PRIMARY KEY (`SensorID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`PersonSensorOf`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`PersonSensorOf` (
  `sr` CHAR(45) NOT NULL ,
  `tr` CHAR(45) NOT NULL ,
  PRIMARY KEY (`sr`, `tr`) ,
  INDEX `fk_ShipSensorOf_Ship1_idx` (`tr` ASC) ,
  CONSTRAINT `fk_SensorOf_Sensor10`
    FOREIGN KEY (`sr` )
    REFERENCES `Prognos_NewWorldModel`.`PersonSensor` (`SensorID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ShipSensorOf_Ship10`
    FOREIGN KEY (`tr` )
    REFERENCES `Prognos_NewWorldModel`.`Ship` (`ShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`PersonSensorProperty`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`PersonSensorProperty` (
  `sr` CHAR(45) NOT NULL ,
  `tr` CHAR(45) NOT NULL ,
  `sensorPerformance` CHAR(45) NULL ,
  PRIMARY KEY (`sr`, `tr`) ,
  INDEX `fk_ShipSensorProperty_Ship1_idx` (`tr` ASC) ,
  CONSTRAINT `fk_SensorProperty_Sensor10`
    FOREIGN KEY (`sr` )
    REFERENCES `Prognos_NewWorldModel`.`PersonSensor` (`SensorID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ShipSensorProperty_Ship10`
    FOREIGN KEY (`tr` )
    REFERENCES `Prognos_NewWorldModel`.`Ship` (`ShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`OrganizationSensor`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`OrganizationSensor` (
  `SensorID` CHAR(45) NOT NULL ,
  PRIMARY KEY (`SensorID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`OrganizationSensorOf`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`OrganizationSensorOf` (
  `sr` CHAR(45) NOT NULL ,
  `tr` CHAR(45) NOT NULL ,
  PRIMARY KEY (`sr`, `tr`) ,
  INDEX `fk_ShipSensorOf_Ship1_idx` (`tr` ASC) ,
  CONSTRAINT `fk_SensorOf_Sensor11`
    FOREIGN KEY (`sr` )
    REFERENCES `Prognos_NewWorldModel`.`OrganizationSensor` (`SensorID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ShipSensorOf_Ship11`
    FOREIGN KEY (`tr` )
    REFERENCES `Prognos_NewWorldModel`.`Ship` (`ShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Prognos_NewWorldModel`.`OrganizationSensorProperty`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Prognos_NewWorldModel`.`OrganizationSensorProperty` (
  `sr` CHAR(45) NOT NULL ,
  `tr` CHAR(45) NOT NULL ,
  `sensorPerformance` CHAR(45) NULL ,
  PRIMARY KEY (`sr`, `tr`) ,
  INDEX `fk_ShipSensorProperty_Ship1_idx` (`tr` ASC) ,
  CONSTRAINT `fk_SensorProperty_Sensor11`
    FOREIGN KEY (`sr` )
    REFERENCES `Prognos_NewWorldModel`.`OrganizationSensor` (`SensorID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ShipSensorProperty_Ship11`
    FOREIGN KEY (`tr` )
    REFERENCES `Prognos_NewWorldModel`.`Ship` (`ShipID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `Prognos_NewWorldModel` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
