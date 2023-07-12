package com.newdmsp.demo;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Replace;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Matcher类能对Pattern对象字符串进行匹配
 * @author Administrator
 *
 */
public class REF{
    public static void main(String[] args) {
//        Pattern p=Pattern.compile("Indian");
//        Matcher m=p.matcher("One little Indian,two little Indian,three little Indian");
//        StringBuffer sb=new StringBuffer();
//        boolean result = m.find();
//        int i = 0 ;
//        while(result){//如果匹配成功就替换
//            m.appendReplacement(sb, "Chinese"+i);
//            result=m.find();//继续下一步匹配
//            i = i+1;
//        }
//		m.appendTail(sb);
        String newContext = Replace("abc\\(.*?\\)","One little abc(1),two little abc(123),three little abc(1)","Chinese(fr)");
        System.out.println(newContext);
    }

    /**
     * eg: "one zeros, zeros" 替换成 “one One1, One2"
     * @param compile: 需要替换的字符串(zeros)
     * @param context: 初始内容(one zeros, zeros)
     * @param replace: 替换的字符串(One)
     * @return
     */
    public static String Replace(String compile, String context, String replace) {
        Pattern p=Pattern.compile(compile);
        Matcher m=p.matcher(context);
        StringBuffer sb=new StringBuffer();
        boolean result = m.find();
        int i = 0 ;
        while(result){//如果匹配成功就替换
            m.appendReplacement(sb, replace+i);
            result=m.find();//继续下一步匹配
            i = i+1;
        }
        if (i==0){
            return context;
        }else {
            return sb.toString();

        }
//		m.appendTail(sb);
//        System.out.println(sb.toString());
    }
}
