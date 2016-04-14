package org.engenomics.visualise;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        new Main().run();
    }

    public void run() throws IOException, InterruptedException {
        List<ColorIndex> colors = Utils.getColors();

        List<List<ColorIndex>> lines = Utils.getLines(colors);

        int limit = (int) (Math.floor((double) colors.size() / (double) 70) + 1);

        ImageInfo info = new ImageInfo(70, limit, 8, true);
        PngWriter pngw = new PngWriter(new File("g.png"), info, true);
        for (int i = 0; i < limit; i++) {

            int[] rgbs = new int[70];

            for (int j = 0; j < 70; j++) {
                if (j < lines.get(i).size()) {
                    rgbs[j] = (lines.get(i).get(j).getColor()).getRGB();

                } else {
                    rgbs[j] = new Color(0, 0, 0, 0).getRGB(); //Transparent
                }
            }

            ImageLineInt line = new ImageLineInt(info);
            for (int k = 0; k < rgbs.length; k++) {
                ImageLineHelper.setPixelRGBA8(line, k, rgbs[k]);
            }

//            System.out.println("Writing row " + i + "...");
            pngw.writeRow(line, i);
//            System.out.println("Finished writing row " + i + "!");
        }
        pngw.end();

    }
}
