package com.se.softengineer.utils;

import com.se.softengineer.entity.IndexSymNode;
import lombok.Data;

import java.util.List;

/**
 * 返回结果
 **/
public class Result {

    private int code;   //编码
    private String message;    //成功、失败
    private Long total;    //总记录条数
    private Object data;   //数据
    private List<String> allSym; //存一个用户的所有体系表名
    private List<IndexSymNode> allNodes;//一个用户一个指标体系表的所有信息

    public Result(int code, String message, Long total, Object data,List<String> allSym,List<IndexSymNode> allNodes) {
        this.code = code;
        this.message = message;
        this.total = total;
        this.data = data;
        this.allSym=allSym;
        this.allNodes=allNodes;

    }

    public static Result fail(){
        return new Result(Code.WORK_ERR,"失败", 0L,null,null,null);
    }

    public static Result success(){
        return new Result(Code.WORK_OK,"成功!!",0L,null,null,null);
    }

    public static Result success(Object data){
        return new Result(Code.WORK_OK,"成功", 0L,data,null,null);
    }

    public static Result success(String message,Object data){
        return new Result(Code.WORK_OK,message, 0L,data,null,null);
    }

    public static Result success(Object data,Long total){
        return new Result(Code.WORK_OK,"成功",total,data,null,null);
    }
    public static Result success( List<String> data){
        return new Result(Code.WORK_OK,"成功",null,null,data,null);
    }

    public List<String> getAllSym() {
        return allSym;
    }

    public void setAllSym(List<String> allSym) {
        this.allSym = allSym;
    }

    public List<IndexSymNode> getAllNodes() {
        return allNodes;
    }

    public void setAllNodes(List<IndexSymNode> allNodes) {
        this.allNodes = allNodes;
    }
    public static Result trans(List<IndexSymNode> data){
        return new Result(Code.WORK_OK,"成功",null,null,null,data);
    }

    public Result() {
    }

    public Result(int code, String message) {
        this.code = Code.WORK_OK;
        this.message = message;
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
}
