package com.userinfotemplate.demo.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public final class ImageUtil {
    private static final char[] chars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final int size=4;
    private static final int lines=5;
    private static final int width=100;
    private static final int height=40;
    private static final int font_size=40;
    public static Object[] createImg(){
        StringBuffer s=new StringBuffer();
        BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics=image.getGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, width, height);
        Random random=new Random();
        for(int i=0;i<size;i++){
            int n=random.nextInt(chars.length);
            graphics.setColor(getColor());
            //graphics.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            graphics.setFont(new Font(null, Font.BOLD+Font.ITALIC,font_size ));
            graphics.drawString(chars[n]+"", i*width/size, height);
            s.append(chars[n]);
        }
        for(int i=0;i<lines;i++){
            graphics.setColor(getColor());
            graphics.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));

        }
        return new Object[]{s.toString(),image};
    }
    private static Color getColor() {
        Random random=new Random();
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color;
    }

}
