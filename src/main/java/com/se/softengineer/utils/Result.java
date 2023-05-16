package com.se.softengineer.utils;

import lombok.Data;

/**
 * 返回结果
 **/
public class Result {

    private int code;   //编码
    private String message;    //成功、失败
    private Long total;    //总记录条数
    private Object data;   //数据

    public Result() {
    }

    public Result(int code, String message) {
        this.code = Code.WORK_OK;
        this.message = message;
    }

    public Result(int code, String message, Long total, Object data) {
        this.code = code;
        this.message = message;
        this.total = total;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Result fail(){
        return new Result(Code.WORK_ERR,"失败", 0L,null);
    }

    public static Result success(){
        return new Result(Code.WORK_OK,"成功!!",0L,null);
    }

    public static Result success(Object data){
        return new Result(Code.WORK_OK,"成功", 0L,data);
    }

    public static Result success(String message,Object data){
        return new Result(Code.WORK_OK,message, 0L,data);
    }

    public static Result success(Object data,Long total){
        return new Result(Code.WORK_OK,"成功",total,data);
    }


}
