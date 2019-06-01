package gameObjects;

import Animations.explosions.small_explosion;
import menus.GamePanel;
import resources.ImageLoader;
import resources.Location;
import resources.RectLoader;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class SampleBird implements Drawable {

    public static int counter = 0;
    public int id ;
    double x , y;
    ArrayList<Rectangle> rectangles = new ArrayList<>();
    ArrayList<Location> bandageLocations = new ArrayList<>();
    int bandageWidth , bandageHeight;
    HpBar hp = new HpBar();
    double heart = 100;
    boolean visible = true;
    public long shakeTime;
    public boolean died = false;
    public void loadBoxes(){
        rectangles = RectLoader.loadRectangles("src/GameObjects/sampleBox2.txt");
        for (Rectangle rect : rectangles){
            rect.xmin /= 2;
            rect.ymin /= 2;
            rect.xmax /= 2;
            rect.ymax /= 2;
        }

    }
    public SampleBird(){

        this(0,0);

    }
    public SampleBird(int x , int y){

        counter ++;
        id = counter;
        this.x = x;
        this.y = y;

        loadBoxes();
        shakeStartTime = -500000;
        bandageWidth =  ImageLoader.getImage("bandage").getWidth(null);
        bandageHeight =  ImageLoader.getImage("bandage").getHeight(null);
    }

    public double shakeX , shakeY;
    boolean mark_explosion = false;
    @Override
    public void draw(Graphics g) {

        if (died && !mark_explosion){
            mark_explosion = true;
            GamePanel.getInstance().add_explosion((int)this.x,(int)this.y);

        }
        if (heart < 0) return ;
        hp.x = (int)this.x + 80;
        hp.y = (int)this.y - 120;



        if (died) {
            long diff = System.currentTimeMillis() - shakeStartTime;
            float percentage = (float) 1.0001 - (float) diff / (shakeTime);
            float alpha = Math.min((float) 1, percentage);
            alpha = Math.max((float)0.01,alpha);
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(ac);

        }

        long currentTime = System.currentTimeMillis();
        if (visible && currentTime - shakeStartTime > shakeTime) {
            g.drawImage(ImageLoader.getImage("chicken"), (int) x + 30, (int) y, null);
            for (Location bandageLocation : bandageLocations){
                g.drawImage(ImageLoader.getImage("bandage"),(int)bandageLocation.x - bandageWidth/2,(int)bandageLocation.y - bandageHeight/ 2,null);
            }
            hp.draw(g);
        }
        if (currentTime - shakeStartTime <= shakeTime){
            if (GamePanel.paused){
                shakeX = shakeY = 0;
            }
            else {
                shakeX = Math.random() * 8;
                shakeY = Math.random() * 8;
            }
            g.drawImage(ImageLoader.getImage("chicken"), (int) x + 30 + (int)shakeX, (int) y + (int)shakeY, null);
            for (Location bandageLocation : bandageLocations){
                g.drawImage(ImageLoader.getImage("bandage"),(int)bandageLocation.x - bandageWidth/2,(int)bandageLocation.y - bandageHeight/ 2,null);
            }
            hp.draw(g);
        }
        heart = Math.max(heart,0);


        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(ac);


    }

    public void update(boolean paused){

        if (paused) return ;
        x+= 0.5;
        for (Location bandageLocation : bandageLocations){
            bandageLocation.x += 0.5;
        }
    }

    public boolean hit(Rectangle b){
        if (SpaceShip.showResume) return false;
        for (Rectangle rectangle : this.rectangles)
        {

            if (new Rectangle(rectangle.xmin + (int)x + 30 , rectangle.ymin + (int)y , rectangle.xmax + (int)x + 30  , rectangle.ymax + (int)y).hit(b)) return true;
        }

        return false;
    }


    public void die() {

        visible = false;
        shake(true,300);

    }

    public void reduceHeart(int value) {
        this.heart -= value;
        hp.percentage -= 0.25;
        if (this.heart <= 0){
            this.die();
        }else{
//            this.shake();
        }
    }
    public long shakeStartTime;

    public void shake(boolean died , long shakeTime){
        shakeStartTime = System.currentTimeMillis();
        this.died = died;
        if (died){
            hp.percentage = 0;
        }
        this.shakeTime = shakeTime;
    }
    public void shake(){
        shake(true);
    }
    public void shake(boolean died) {

        shake(died,2000);

    }

    public void addBandate(int x, int y) {
        bandageLocations.add(new Location(x-ImageLoader.getImage("bandage").getWidth(null)/2,y-ImageLoader.getImage("bandage").getHeight(null)/2 ));
    }
}
