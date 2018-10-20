import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Random;

public class Might implements Automata{
    private double[][] cells;
    private double[][] str;
    private int width;
    private int height;
    private final Random r = new Random();

    public Might(int w, int h) {
        width = w;
        height = h;
        cells = new double[w][h];
        str = new double[w][h];
    }

    public Timeline get(GraphicsContext gc) {

        for(int x = 0; x < width; x++) for(int y = 0; y < height; y++) {
            cells[x][y] = r.nextDouble();
            str[x][y] = 0;
        }
        return new Timeline(new KeyFrame(Duration.millis(25), event -> {
            double[][] nextHue = cells.clone();
            for(int x = 0; x < width; x++) for(int y = 0; y < height; y++) {

                // Choose a random neighbour to interact with at x+xD, y+yD
                int rand = r.nextInt(4);
                int xD = 0;
                int yD = 0;
                if(rand==0) xD = 1;
                else if(rand==1) yD = 1;
                else if(rand==2) xD = -1;
                else if(rand==3) yD = -1;

                // Calculate hue differences
                double diff = (1 + getHue(x,y) - getHue(x+xD,y+yD)) % 1;
                double minDiff = (diff<0.5)?diff:1-diff;
                double magDiff = (diff<0.5)?diff:diff-1;
                double strDiff = getStr(x,y) - getStr(x+xD,y+yD);

                if(minDiff > 0.2 && r.nextDouble() < 0.2) {
                    // If the hues are different enough, chance to fight

                    if(r.nextDouble() > 0.5 + strDiff*2) {
                        // Current cell has been invaded

                        // Set hue to same as invading cell.
                        nextHue[x][y] = getHue(x+xD,y+yD);

                        // Add colour divergence if necessary.
                        nextHue[x][y] = (1 + getHue(x,y) + 0*strDiff) % 1;

                        // Modify strength of invader and invaded
                        setStr(x-xD, y-yD, (getStr(x, y) + getStr(x-xD, y-yD))/2);
                        modStr(x-xD, y-yD, -0.3);
                        setStr(x, y, getStr(x+xD, y+yD));
                        modStr(x, y, 0.3);
                    }
                } else if(r.nextDouble() > minDiff*2 + 0.4 - (0.5*strDiff)) {
                    // If the hues are similar enough, chance to merge.

                    // Merge hues
                    nextHue[x][y] = (getHue(x,y) - magDiff/4) % 1;

                    // Merge and decay strength
                    setStr(x,y,getStr(x,y)- 0.25*strDiff);
                    modStr(x,y,-0.005);

                }
            }
            for(int x = 0; x < width; x++) for(int y = 0; y < height; y++) {
                setHue(x, y, nextHue[x][y]);
                gc.setFill(Color.hsb(getHue(x,y)*360,1,(-getStr(x,y)/2+1.5)/2));
                gc.fillRect(x*8, y*8, 8, 8);
            }
        }));

    }

    private double getHue(int x, int y) {
        return cells[(width+x)%width][(height+y)%height];
    }

    private void setHue(int x, int y, double val) {
        cells[(width+x)%width][(height+y)%height] = val;
    }

    private double getStr(int x, int y) {
        return str[(width+x)%width][(height+y)%height];
    }

    private void modStr(int x, int y, double mult) {
        if(mult > 0) str[(width+x)%width][(height+y)%height]  += (0.5 - str[(width+x)%width][(height+y)%height]) * mult;
        else         str[(width+x)%width][(height+y)%height]  += (0.5 + str[(width+x)%width][(height+y)%height]) * mult;
    }
    private void setStr(int x, int y, double val) {
        str[(width+x)%width][(height+y)%height] = val;
    }
}
