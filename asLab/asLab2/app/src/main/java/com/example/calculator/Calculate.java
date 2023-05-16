package com.example.calculator;

import static java.util.Map.*;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;


//  Model
public class Calculate {
    public static final int MAXSIZE = 1000;

    // 将优先级次序存储到二维数组
    private static char[][] precede = {
            {'>','>','<','<','<','>','>',},
            {'>','>','<','<','<','>','>',},
            {'>','>','>','>','<','>','>',},
            {'>','>','>','>','<','>','>',},
            {'<','<','<','<','<','=','x',},
            {'>','>','>','>','x','>','>',},
            {'<','<','<','<','<','x','='}
    };

    // 判断字符是操作数还是运算符
    private static boolean In(char ch) {
        switch (ch) {
            case'+':
            case'-':
            case'*':
            case'/':
            case'(':
            case')':
            case'=':
                return true;
            default:
                return false;
        }
    }

    /*
     * 比较优先级
     * top 栈顶运算符
     * outside 栈外运算符
     * */
    private static Character Precede(char top, char outside) {
        int a = 0,b = 0;
        switch (top) {
            case'+':
                a = 0;
                break;
            case'-':
                a = 1;
                break;
            case'*':
                a = 2;
                break;
            case'/':
                a = 3;
                break;
            case'(':
                a = 4;
                break;
            case')':
                a = 5;
                break;
            case'#':
                a = 6;
                break;
            default: break;
        }
        switch(outside){
            case'+':
                b = 0;
                break;
            case'-':
                b = 1;
                break;
            case'*':
                b = 2;
                break;
            case'/':
                b = 3;
                break;
            case'(':
                b = 4;
                break;
            case')':
                b = 5;
                break;
            case'=':
                b = 6;
                break;
            default: break;
        }
        return precede[a][b];
    }

    /*
     * 运算求值函数
     * */
    private static double operate(double a, char theta, double b) {
        switch (theta) {
            case'+':
                return a + b;
            case'-':
                return a - b;
            case'*':
                return a * b;
            case'/':
                return a / b;
        }
        return 0.0;
    }

    /*
     * 提取数字
     * */
    private static double atof(char[] z) {
        String val = "";
        for (int i = 0; i < z.length; i++) {
            if(z[i] != 'a') {
                val += z[i];
            } else break;
        }
        double ans = Double.parseDouble(val);
        System.out.println("ans: " + ans);
        return ans;
    }

    /*
     * 表达式求值函数
     * val 表达式
     * return 结果
     * Double 类型
     */
    public static Double EvaluateExpression(String val) {
        char[] str = new char[MAXSIZE];
        for (int i = 0; i < val.length(); i++) {
            str[i] = val.charAt(i);
        }

        // 定义用于存放运算符的OPTR栈类型
        Stack<Character> OPTR = new Stack<>();
        // 定义用于存放操作数的OPND栈类型
        Stack<Double> OPND = new Stack<>();

        OPTR.push('#');

        char ch;			// 当前字符
        int i = 0;			// 用于扫描字符串下标
        char[] z = new char[100];	// 用于暂时存放数字的字符串
        int j = 0;			// 数字和小数点构成的字符串下标

        while(true){
            ch = str[i];
            if(ch == '\0') break;	// 字符串未结束，循环继续

            if(In(ch)){	// 运算符
                switch(Precede(OPTR.peek(),ch))
                {	// 比较运算符栈顶元素和ch的优先级
                    case'>':
                        char theta;		// 运算符
                        double a,b,c;	// 运算数a,运算数b,运算结果c
                        theta = OPTR.pop();
                        b = OPND.pop();
                        a = OPND.pop();
                        c = operate(a,theta,b);
                        OPND.push(c);
                        break;
                    case'<':
                        OPTR.push(ch);
                        i++;
                        ch = str[i];
                        break;
                    case'=':
                        OPTR.pop();
                        i++;
                        ch = str[i];
                        break;
                    default:
                        i++;
                        ch = str[i];
                        break;
                }
            } else{
                while((ch >= '0' && ch <= '9') || ch == '.'){ // 操作数和小数点,先存进数组再转换为浮点型存入栈
                    z[j] = ch;
                    j++;
                    i++;
                    ch = str[i];
                }
                z[j] = 'a';	 // 在数组存放数字后加一字母，用于atof函数转化
                double d = atof(z);
                Arrays.fill(z, '\0'); j = 0;	// 把数组置空,下标置为0，等待下一个运算数进入
                OPND.push(d);
            }
        }
        if (OPND.peek() == null) {
            return 0.0;
        } else return OPND.peek();
    }


}
