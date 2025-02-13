DELIMITER //

CREATE TRIGGER update_last_pup_valid_to
    AFTER INSERT ON product_unit_prices
    FOR EACH ROW
BEGIN
    UPDATE product_unit_prices
    SET valid_to = NEW.valid_from
    WHERE product_unit_id = NEW.product_unit_id
      AND valid_to IS NULL
      AND product_unit_price_id <> NEW.product_unit_price_id
        ORDER BY valid_from DESC
    LIMIT 1;
END;
//

DELIMITER ;