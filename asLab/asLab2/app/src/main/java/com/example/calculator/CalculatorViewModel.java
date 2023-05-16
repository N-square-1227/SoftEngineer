package com.example.calculator;

import static android.app.ProgressDialog.show;
import static android.widget.Toast.LENGTH_LONG;

import android.app.Application;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalculatorViewModel extends AndroidViewModel {
    private MutableLiveData<String> input;
    private MutableLiveData<String> result;
    public Calculate calcu=new Calculate();
    public char now;
    public boolean flag;//表达式是否有效

    public CalculatorViewModel(){
        super(new Application());

        this.input=new MutableLiveData<>();
        this.result=new MutableLiveData<>();
        result.setValue("结果是： ");
        input.setValue("");
        now=' ';
        flag=true;

    }

    public MutableLiveData<String> getInput() {
        return input;
    }

    public MutableLiveData<String> getResult() {
        return result;
    }

    public void setInput(MutableLiveData<String> input) {
        this.input = input;
    }

    public void setResult(MutableLiveData<String> result) {
        this.result = result;
    }

    //获取输入的表达式
    public void getInput(char s){
        //和前面的拼接
        String ss= String.valueOf(s);
        String nownow=String.valueOf(now);
        String c="+-*/";
        //连续输入两个符号
        if(c.contains(ss)&&c.contains(nownow)) {
            flag=false;
        }
        input.setValue(input.getValue()+s);
        now=s;
    }
    //C 的功能 清除两个文本框
    public void clearBoth(){
        input.setValue("");
        result.setValue("");
    }
    //CE 仅清除 结果 文本框
    public void clearResult(){
        result.setValue("");
    }
    //= 的功能 计算结果 调用calculate类
    public void calculate(TextView t){
        //OkHttpClient okHttpClient = NetworkHelper.getOkHttpClient(getApplication());
        String s=input.getValue();
        System.out.println("表达式："+s);
        String resu = null;
        System.out.println(s.contains("/0")+":"+flag);
        if(s.contains("/0")|| !flag)
        {
            t.setText("表达式输入有误");
            //Toast.makeText(, "默认的Toast", Toast.LENGTH_SHORT).show();
        }else{
            resu= String.valueOf(calcu.EvaluateExpression(s+"="));
            result.setValue(resu);
            t.setText(result.getValue());
        }

    }
}
