package com.example.springbootmybatis.common;

import org.apache.commons.lang.StringUtils;

public class StringHandle {






    public static String broadNumber(String broadNumber){

        if (StringUtils.isNotEmpty(broadNumber)&&!broadNumber.substring(0,1).equals('0')){
            StringBuffer stringBuffer = new StringBuffer(broadNumber);
            stringBuffer.insert(0,'0');
            return stringBuffer.toString();
        }
        return null;
    }

    public static String insertString(String s,char c,int index){
        if (StringUtils.isNotEmpty(s)&&s.length()>=index){
            StringBuffer stringBuffer = new StringBuffer(s);
            stringBuffer.insert(index,c);
            return stringBuffer.toString();
        }
        return s;
    }
}
