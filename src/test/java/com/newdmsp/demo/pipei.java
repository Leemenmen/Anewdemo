package com.newdmsp.demo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pipei {
    public static String Replace(String compile, String context, String picName, String end) {
        Pattern p=Pattern.compile(compile);
        Matcher m=p.matcher(context);
        StringBuffer sb=new StringBuffer();
        boolean result = m.find();
        int i = 0 ;
        while(result){//如果匹配成功就替换
            m.appendReplacement(sb, picName+"_"+i+end);
            result=m.find();//继续下一步匹配
            i = i+1;
        }
        m.appendTail(sb);
        return sb.toString();
    }
    public static void main(String[] args) {
        // 正则表达式匹配 context中所有 savefig(任意长度字符)，并替换
        String context = "weather.to_csv(\"weather.csv\",index = False)333\n123weather.to_csv('weather.csv',index = False)show()44";
        System.out.println(context);
        String newContext = Replace("\\.to_csv\\(.+['|\"]",context, ".to_csv('test_file",".csv'");
        String newContext1 = Replace("show\\(.*?\\)",context, "savefig('test_png",".png')");
        System.out.println(newContext);
        System.out.println(newContext1);


    }
}
