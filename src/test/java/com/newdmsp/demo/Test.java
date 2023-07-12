package com.newdmsp.demo;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class Test {
    public static void main(String[] args) throws Exception{
        exportSimpleWord();
    }

    public static void exportSimpleWord() throws Exception {
        CustomXWPFDocument document = new CustomXWPFDocument();
        String path = "D://测试";
        FileOutputStream out = new FileOutputStream(new File(path + ".doc"));

        // 添加标题
        XWPFParagraph titleParagraph = document.createParagraph();
        // 设置段落居中
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText("titleParagraph.setAlignment(ParagraphAlignment.CENTER);\n" +
                "        XWPFRun titleRun = titleParagraph.createRun(); XWPFParagraph firstParagraph = document.createParagraph();\n" +
                "        XWPFRun firstRun = firstParagraph.createRun();\n" +
                "        firstRun.setText(\"具体操作方式：\");\n" +
                "        firstRun.setFontFamily(\"仿宋\");\n" +
                "        firstRun.setFontSize(11);");
//        titleRun.setFontSize(20);
//        titleRun.setFontFamily("宋体");
//        titleRun.setBold(true);

//        XWPFParagraph firstParagraph = document.createParagraph();
//        XWPFRun firstRun = firstParagraph.createRun();
//        firstRun.setText("具体操作方式：");
//        firstRun.setFontFamily("仿宋");
//        firstRun.setFontSize(11);
//        //换行
//        firstParagraph.setWordWrap(true);
//
//        XWPFParagraph twoParagraph = document.createParagraph();
//        twoParagraph.setIndentationFirstLine(500);
//        XWPFRun twoRun = twoParagraph.createRun();
//        twoRun.setFontFamily("仿宋");
//        twoRun.setFontSize(11);
//        twoRun.setText("继承POI操作Word中类XWPFDocument。");

        FileInputStream in = new FileInputStream("F:\\project\\Anewdemo\\stuCode\\stu1\\2022_06_30_210329_fig_1.png");
        byte[] ba = new byte[in.available()];
        in.read(ba);
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(ba);
        XWPFParagraph picture = document.createParagraph();
        //添加图片
        document.addPictureData(byteInputStream, CustomXWPFDocument.PICTURE_TYPE_PNG);
        //图片大小、位置
        document.createPicture(document.getAllPictures().size() - 1, 100, 100, picture);

        document.write(out);
        out.close();
    }
}
