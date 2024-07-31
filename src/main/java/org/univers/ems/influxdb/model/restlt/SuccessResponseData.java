package org.univers.ems.influxdb.model.restlt;



public class SuccessResponseData extends ResponseData {
    public SuccessResponseData() {
        super(true, DEFAULT_SUCCESS_CODE, "请求成功", "请求成功", (Object)null);
    }

    public SuccessResponseData(Object object) {
        super(true, DEFAULT_SUCCESS_CODE, "请求成功", "请求成功", object);
    }

    public SuccessResponseData(Integer code, String message, Object object) {
        super(true, code, message, message, object);
    }

    public SuccessResponseData(Integer code, String message, String localizedMsg, Object object) {
        super(true, code, message, localizedMsg, object);
    }

    public SuccessResponseData(Integer code, String message) {
        super(true, code, message);
    }
}

