package com.twosri.dev.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomMessage {

	// GENERIC
	public static final String NO_CUSTOMER = "No details found for [0]";
	public static final String GENERIC_ERROR = "Internal server error occurred! Please contact administrator";

	// USERS
	public static final String USER_ALREADY_EXIST = "User [0] already exist";

	public static final String USERID_IS_NOT_VALID = "Please enter valid UserId";
	public static final String PASSWORD_IS_NOT_VALID = "Please enter valid Password";
	public static final String USER_ADDED_SUCCESS = "User [0] created successfully";
	public static final String USER_EDITED_SUCCESS = "User [0] updated successfully";
	public static final String USER_DELETED_SUCCESS = "User [0] deleted successfully";

	// CUSTOMER
	public static final String CUSTOMER_ALREADY_EXIST = "Customer [0] already exist";
	public static final String CUSTOMER_NAME_IS_NOT_VALID = "Please enter valid Name";
	public static final String CUSTOMER_ADDED_SUCCESS = "Customer [0] created successfully";
	public static final String CUSTOMER_EDITED_SUCCESS = "Customer [0] updated successfully";
	public static final String CUSTOMER_DELETED_SUCCESS = "Customer [0] deleted successfully";

	// PHASE
	public static final String PHASE_NAME_ALREADY_EXIST = "Phase [0] already exist";
	public static final String PHASE_NAME_IS_NOT_VALID = "Please enter valid Name";
	public static final String PHASE_MAN_POWER_NOT_VALID = "Please enter valid Man Power";
	public static final String PHASE_SEQUENCE_NOT_VALID = "Please enter valid Sequence";
	public static final String PHASE_ADDED_SUCCESS = "Phase [0] created successfully";
	public static final String PHASE_EDITED_SUCCESS = "Phase [0] updated successfully";
	public static final String PHASE_DELETED_SUCCESS = "Phase [0] deleted successfully";

	// REFERENCE
	public static final String REFERENCE_ALREADY_EXIST = "Reference [0] already exist";
	public static final String REFERENCE_NAME_IS_NOT_VALID = "Please enter valid Name";
	public static final String REFERENCE_VALUE_NOT_VALID = "Please enter valid Value";
	public static final String REFERENCE_ADDED_SUCCESS = "Reference [0] created successfully";
	public static final String REFERENCE_EDITED_SUCCESS = "Reference [0] updated successfully";
	public static final String REFERENCE_DELETED_SUCCESS = "Reference [0] deleted successfully";

	// CALCULATION
	public static final String CALCULATION_ENTRY_ALREADY_EXIST = "Entry for product type [0] for phase [1] already exist";
	public static final String CALCULATION_PRODUCT_IS_NOT_VALID = "Please enter valid product type";
	public static final String CALCULATION_PHASE_IS_NOT_VALID = "Please enter valid phase";
	public static final String CALCULATION_MAN_HRS_IS_NOT_VALID = "Please enter valid Man Hours";
	public static final String CALCULATION_ENTRY_ADDED_SUCCESS = "Entry for Phase [0] and ProductType [1] created successfully";
	public static final String CALCULATION_ENTRY_EDITED_SUCCESS = "Entry for Phase [0] and ProductType [1] updated successfully";
	public static final String CALCULATION_ENTRY_DELETED_SUCCESS = "Entry deleted successfully";

	//ORDER
	public static final String ORDER_CUSTOMER_IS_NOT_VALID = "Please enter valid customer name";
	public static final String ORDER_NUMBER_IS_NOT_VALID = "Please enter valid purchase order number";
	public static final String ORDER_JOB_NO_IS_NOT_VALID = "Please enter valid job number";
	public static final String ORDER_WIDTH_IS_NOT_VALID = "Please enter valid width";
	public static final String ORDER_DEPTH_IS_NOT_VALID = "Please enter valid depth";
	public static final String ORDER_HEIGHT_IS_NOT_VALID = "Please enter valid height";
	public static final String ORDER_QUANTITY_IS_NOT_VALID = "Please enter valid quantity";
	public static final String ORDER_PRODUCT_IS_NOT_VALID = "Please enter valid product type";
	public static final String ORDER_PRICE_IS_NOT_VALID = "Please enter valid price";
	public static final String ORDER_ENTRY_ADDED_SUCCESS = "Order number [0] created successfully";
	public static final String ORDER_ENTRY_EDITED_SUCCESS = "Order number [0] updated successfully";
	public static final String ORDER_ENTRY_DELETED_SUCCESS = "Order number [0] deleted successfully";
	
	//EVENT
	public static final String EVENT_ADDED_SUCCESS = "Event number [0] created successfully";
	public static final String EVENT_EDITED_SUCCESS = "Event number [0] updated successfully";
	public static final String EVENT_DELETED_SUCCESS = "Event number [0] deleted successfully";

	public static String getMessage(String message, final String[] param) {
		if (param != null) {
			for (int i = 0; i < param.length; i++) {
				message = message.replace("[" + i + "]", param[i]);
			}
		}
		log.debug("message {}", message);
		return message;
	}

}
