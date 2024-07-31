package org.univers.ems.pojo.response;

import lombok.Data;

@Data
public class ResponseMsg {
    private int code;
    private String message;
    private Object data;

    public ResponseMsg(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ResponseMsg(int code, String msg, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ResponseMsg() {

    }

    public static ResponseMsg ok(Object data) {
        return new ResponseMsg(0, "success", data);
    }

    public static ResponseMsg ok(String message, Object data) {
        return new ResponseMsg(0, message, data);
    }

    public static ResponseMsg error(String message) {
        return new ResponseMsg(500, message);
    }

    public static ResponseMsg error(String message, Object data) {
        return new ResponseMsg(500, message, data);
    }
}
