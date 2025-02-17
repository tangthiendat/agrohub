DELIMITER $$

CREATE PROCEDURE get_next_auto_increment (
    IN tbl_name VARCHAR(64),
    OUT next_id INT
)
BEGIN
    SET @query = CONCAT('SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ''', tbl_name, '''');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;
