package ru.netology.graphics.image;

public class ColorSchema implements TextColorSchema{

    private char[] schema = { '#', '$', '@', '%', '*', '+', '-','.',' '};
    @Override
    public char convert(int color) {
        return schema[color/(255/8)];
    }
}
