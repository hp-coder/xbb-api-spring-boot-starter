package com.hp.xbongbong.api.helper.exception;

import com.google.common.base.Preconditions;
import com.hp.xbongbong.api.ratelimiter.XbbApiRequest;
import com.hp.xbongbong.api.helper.XbbUrl;
import com.hp.xbongbong.api.model.XbbResponse;
import lombok.Getter;

import java.util.Objects;

/**
 * 销帮帮接口异常
 *
 * @author hp
 */
@Getter
public class XbbException extends RuntimeException {

    private static final long serialVersionUID = 3794250441365105227L;

    private final int errorCode;
    private final String errMsg;
    private XbbUrl url;

    public XbbException(String errMsg) {
        super("error_code=-1||error_message=" + errMsg);
        this.errorCode = -1;
        this.errMsg = errMsg;
    }

    public XbbException(String errMsg, Throwable throwable) {
        super("error_code=-1||error_message=" + errMsg, throwable);
        this.errorCode = -1;
        this.errMsg = errMsg;
    }

    public XbbException(int errorCode, String errMsg) {
        super("error_code=" + errorCode + "||error_message=" + errMsg);
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public XbbException(XbbResponse<?> response) {
        super(response.getLogInfo());
        this.errorCode = response.getCode();
        this.errMsg = response.getMsg();
    }

    public XbbException(XbbApiRequest request, XbbResponse<?> response) {
        super(request.getLogInfo() + "||" + response.getLogInfo());
        Preconditions.checkArgument(Objects.nonNull(request.getUrl()));
        this.url = request.getUrl();
        this.errorCode = response.getCode();
        this.errMsg = response.getMsg();
    }
}
