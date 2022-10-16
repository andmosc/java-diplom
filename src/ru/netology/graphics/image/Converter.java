package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;


public class Converter implements TextGraphicsConverter {

    TextColorSchema schema = new ColorSchema();
    private int maxWidth;
    private int maxHeight;
    private double maxRatio = 0;

    @Override
    public String convert(String url) throws BadImageSizeException, IOException {

        BufferedImage img = ImageIO.read(new URL(url));

        if (this.maxRatio != 0) {
            double imgRatio = (double) img.getWidth() / img.getHeight();
            if (imgRatio > this.maxRatio) {
                throw new BadImageSizeException(imgRatio, this.maxRatio);
            }
        }

        int newWidth = img.getWidth();

        int newHeight = img.getHeight();

        if (newWidth > this.maxWidth) {
            double ratio = (double) newWidth / this.maxWidth;
            newWidth = (int) (newWidth / ratio);
            newHeight = (int) (newHeight / ratio);

        }
        if (newHeight > this.maxHeight) {
            double ratio = (double) newHeight / this.maxHeight;
            newWidth = (int) (newWidth / ratio);
            newHeight = (int) (newHeight / ratio);
        }

        Image scaleImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaleImage, 0, 0, null);
        //ImageIO.write( bwImg, "png", new File("out.png"));
        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder str = new StringBuilder();
        String string;
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                str.append(c);
                str.append(c);
            }
            str.append("\n");
        }
        return str.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
