
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Test {
    public static void main(String[] args) throws Exception {
        double[][] data = new double[100][100];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = Math.random()*1000;
            }
        }
        HeatMap hm = new HeatMap(data, false, Gradient.createGradient(Color.yellow, Color.red, 100));
        hm.setCoordinateBounds(0, data.length, 0, 1000);
        hm.setDrawXTicks(true);
        hm.setDrawYTicks(true);
        hm.setDrawXAxisTitle(true);
        hm.setDrawYAxisTitle(true);
        hm.setDrawTitle(true);
        hm.setTitle("My Heat Map");
        hm.setXAxisTitle("The x-axis");
        hm.setYAxisTitle("The y-axis");
        hm.setDrawLegend(true);
        hm.setBounds(0,0,600,400);
        BufferedImage img = new BufferedImage(600, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        hm.paintComponent(g2);
        g2.dispose();

        ImageIO.write(img, "png", new File("heat.png"));
    }
}
