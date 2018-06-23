package com.twosri.dev.bean;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.twosri.dev.util.ErrorCode;

import lombok.Data;

@Data
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = -841656692618798267L;
	long exceptionId;
	ErrorCode errorCode;
	String displayMessage;
	Exception actualException;

	public CustomException() {
		super();
	}

	public CustomException(long exceptionId, ErrorCode errorCode, String displayMessage, Exception actualException) {
		super();
		this.exceptionId = exceptionId;
		this.errorCode = errorCode;
		this.displayMessage = displayMessage;
		this.actualException = actualException;

	}

	public String getStackTrace(Exception e) {
		if (e != null) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			return stringWriter.toString();
		} else
			return "None";
	}

	@Override
	public String toString() {
		return "CustomException \n[\nexceptionId=" + exceptionId + ",\nerrorCode=" + errorCode + ",\ndisplayMessage="
				+ displayMessage + ",\nactualException=" + getStackTrace(actualException) + "\n]\n";
	}

}
