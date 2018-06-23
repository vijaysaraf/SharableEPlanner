package com.twosri.dev.util;

import org.springframework.stereotype.Service;

import com.twosri.dev.bean.CustomException;

@Service
public class ExceptionFactory {

	public CustomException createException(ErrorCode errorCode, String messgae, Exception actualException) {
		return new CustomException(System.currentTimeMillis(), errorCode, messgae, actualException);
	}
}
