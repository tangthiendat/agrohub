CREATE TRIGGER insert_admin_permission
    AFTER INSERT ON permissions
    FOR EACH ROW
BEGIN
    INSERT INTO permission_role (role_id, permission_id)
    VALUES (1, NEW.permission_id);
END;