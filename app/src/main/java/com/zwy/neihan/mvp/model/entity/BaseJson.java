package com.zwy.neihan.mvp.model.entity;


import com.zwy.neihan.mvp.model.api.Api;

import java.io.Serializable;

/**
 * 如果你服务器返回的数据固定为这种方式(字段名可根据服务器更改)
 * 替换范型即可重用BaseJson
 * Created by jess on 26/09/2016 15:19
 * Contact with jess.yan.effort@gmail.com
 */

public class BaseJson<T> implements Serializable{
    private T data;
    private String message;
    private String msg;
    private int delay;
    @Override
    public String toString() {
        return "BaseJson{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }


    public T getData() {
        return data;
    }

    public String getCode() {
        return message;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * 请求是否成功
     * @return
     */
    public boolean isSuccess() {
        if (message.equals(Api.RequestSuccess)) {
            return true;
        } else {
            return false;
        }
    }
}
