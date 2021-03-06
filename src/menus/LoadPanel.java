package menus;

import gameObjects.HpBar;
import resources.ImageLoader;

import java.awt.*;

public class LoadPanel extends FullSizePanel{
    public HpBar hp = new HpBar((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-ImageLoader.getImage("HP2").getWidth(null)/2,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-ImageLoader.getImage("HP2").getHeight(null)/2);
    private static LoadPanel ourInstance = new LoadPanel();

    public static LoadPanel getInstance() {
        return ourInstance;
    }

    public LoadPanel() {
        super();
        hp.percentage = 0;
        hp.increasing = true;
        hp.decreasing = false;
        hp.type = "load_bar";

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        hp.draw(g);
        hp.percentage += 0.0015;
        hp.percentage = Math.min(1,hp.percentage);

    }
}
