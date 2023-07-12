package com.luban.xbongbong.api.helper.exception;

import com.luban.xbongbong.api.model.XbbResponse;

/**
 * 销帮帮接口异常
 * @author hp
 */
public class XbbException extends RuntimeException {

	private static final long serialVersionUID = 3794250441365105227L;

	private int errorCode;
	private String errMsg;

	public XbbException(int errorCode, String errMsg) {
		super("error code: " + errorCode + ", error message: " + errMsg);
		this.errorCode = errorCode;
		this.errMsg = errMsg;
	}

	public XbbException(XbbResponse<?> response) {
		super("error code: " + response.getCode() + ", error message: " + response.getMsg());
		this.errorCode = response.getCode();
		this.errMsg = response.getMsg();
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
