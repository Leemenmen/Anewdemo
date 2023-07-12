package com.newdmsp.demo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleDraw {
    private final String RECT_SHAPE="rect";
    private final String OVAL_SHAPE="oval";

    private Frame frame=new Frame("绘图");

    Button btnRect=new Button("绘制矩形");
    Button btnOval=new Button("绘制椭圆");
    private String shape="";
    private class MyCanvas extends Canvas{
        @Override
        public void paint(Graphics g) {
            if(shape.equals(RECT_SHAPE)){
                g.setColor(Color.BLACK);
                g.drawRect(100,100,150,100);
            }else if (shape.equals(OVAL_SHAPE)){
                g.setColor(Color.RED);
                g.drawOval(100,100,150,100);
            }
        }
    }

    MyCanvas drawArea =new MyCanvas();

    public void init(){
        btnOval.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shape=OVAL_SHAPE;
                drawArea.repaint();
            }
        });
        btnRect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shape=RECT_SHAPE;
                drawArea.repaint();
            }
        });
        Panel panel=new Panel();
        panel.add(btnRect);
        panel.add(btnOval);
        frame.add(panel,BorderLayout.SOUTH);
        drawArea.setPreferredSize(new Dimension(300,500));
        frame.add(drawArea);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleDraw().init();
    }

}
