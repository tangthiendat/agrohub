package com.ttdat.core.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_INPUT("AGH-101", "Invalid input", ErrorType.VALIDATION),

    UNAUTHORIZED("AGH-201", "Unauthorized", ErrorType.AUTHENTICATION),
    ACCOUNT_DISABLED("AGH-202", "Account is disabled", ErrorType.AUTHENTICATION),
    INVALID_CREDENTIALS("AGH-203", "Invalid credentials", ErrorType.AUTHENTICATION),
    TOKEN_NOT_VALID("AGH-204", "Token not valid", ErrorType.AUTHENTICATION),
    ACCESS_DENIED("AGH-205", "You do not have permission to perform this action", ErrorType.AUTHENTICATION),

    EMAIL_NOT_FOUND("AGH-301", "User email not found", ErrorType.RESOURCE),
    USER_NOT_FOUND("AGH-302", "User not found", ErrorType.RESOURCE),
    ROLE_NOT_FOUND("AGH-303", "Role not found", ErrorType.RESOURCE),
    PERMISSION_NOT_FOUND("AGH-304", "Permission not found", ErrorType.RESOURCE),
    EMAIL_ALREADY_EXISTS("AGH-305", "User with this email already exists", ErrorType.RESOURCE),
    PERMISSION_IN_USE("AGH-306", "Permission is in use", ErrorType.RESOURCE),
    UNIT_NOT_FOUND("AGH-307", "Unit not found", ErrorType.RESOURCE),
    UNIT_ALREADY_EXISTS("AGH-308", "Unit with this name already exists", ErrorType.RESOURCE),
    CATEGORY_NOT_FOUND("AGH-309", "Category not found", ErrorType.RESOURCE),
    CATEGORY_ALREADY_EXISTS("AGH-310", "Category with this name already exists", ErrorType.RESOURCE),
    PRODUCT_NOT_FOUND("AGH-311", "Product not found", ErrorType.RESOURCE),
    PRODUCT_ALREADY_EXISTS("AGH-312", "Product with this name already exists", ErrorType.RESOURCE),
    SUPPLIER_NOT_FOUND("AGH-313", "Supplier not found", ErrorType.RESOURCE),
    SUPPLIER_ALREADY_EXISTS("AGH-314", "Supplier with this name already exists", ErrorType.RESOURCE),
    WAREHOUSE_NOT_FOUND("AGH-315", "Warehouse not found", ErrorType.RESOURCE),
    WAREHOUSE_ALREADY_EXISTS("AGH-316", "Warehouse with this name already exists", ErrorType.RESOURCE),
    PURCHASE_ORDER_NOT_FOUND("AGH-317", "Purchase order not found", ErrorType.RESOURCE),
    SUPPLIER_RATING_NOT_FOUND("AGH-318", "Supplier rating not found", ErrorType.RESOURCE),
    PRODUCT_LOCATION_NOT_FOUND("AGH-319", "Product location not found", ErrorType.RESOURCE),
    PRODUCT_LOCATION_ALREADY_EXISTS("AGH-320", "Product location with this name already exists", ErrorType.RESOURCE),
    PRODUCT_BATCH_NOT_FOUND("AGH-321", "Product batch not found", ErrorType.RESOURCE),
    PRODUCT_UNIT_NOT_FOUND("AGH-322", "Product unit not found", ErrorType.RESOURCE),
    DEBT_ACCOUNT_NOT_FOUND("AGH-323", "Debt account not found", ErrorType.RESOURCE),
    CUSTOMER_NOT_FOUND("AGH-324", "Customer not found", ErrorType.RESOURCE),
    PRODUCT_BATCH_LOCATION_NOT_FOUND("AGH-325", "Product batch location not found", ErrorType.RESOURCE),

    LOCATION_OUT_OF_STOCK("AGH-325", "Location out of stock", ErrorType.BUSINESS),
    BATCH_OUT_OF_STOCK("AGH-326", "Batch out of stock", ErrorType.BUSINESS),
    PRODUCT_OUT_OF_STOCK("AGH-327", "Product out of stock", ErrorType.BUSINESS),

    INTERNAL_SERVER_ERROR("AGH-501", "Internal server error", ErrorType.SYSTEM),;

    private final String code;
    private final String message;
    private final ErrorType errorType;
}
