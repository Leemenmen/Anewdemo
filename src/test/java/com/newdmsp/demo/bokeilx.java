package com.newdmsp.demo;


import java.awt.Container;
import javax.swing.*;

public class bokeilx extends JFrame{
    public bokeilx() {
        setBounds(390, 180, 800 , 500); //绝对定位窗体大小位置
        Container c=getContentPane(); //容器
        c.setLayout(null); //窗体不使用任何布局管理器
        JLabel m=new JLabel();
        Icon icon=new ImageIcon("ceshi.jpg");		//获取图片
        JLabel la=new JLabel();  //标签
        la.setIcon(icon);			//将图片放在标签中
        la.setHorizontalAlignment(SwingConstants.LEFT); //设置图片在标签的最左边
        la.setBounds(10, 10,300, 150);		//设置标签位置大小
        la.setOpaque(true);
        c.add(la);
        setVisible(true); //设置窗体可见
        setDefaultCloseOperation(EXIT_ON_CLOSE); //设置窗体退出样式
    }


    public static void main(String[] args) {
        new bokeilx();

    }

}


