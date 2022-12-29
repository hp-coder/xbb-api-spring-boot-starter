package com.luban.xbongbong.api.helper.exception;

/**
 * 销帮帮接口异常
 */
public class XbbException extends Exception {

	private static final long serialVersionUID = 3794250441365105227L;

	private int errorCode;
	private String errMsg;
	
	public XbbException(int errorCode, String errMsg) {
		super("error code: " + errorCode + ", error message: " + errMsg);
		this.errorCode = errorCode;
		this.errMsg = errMsg;
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
