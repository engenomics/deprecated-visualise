package org.engenomics.visualise;

import java.awt.*;

public class ColorIndex {
    private Color color;
    private long index;

    public ColorIndex(Color color, long index) {

        this.color = color;
        this.index = index;
    }

    @Override
    public String toString() {
        return "ColorIndex{" +
                "color=" + color +
                ", index=" + index +
                '}';
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
