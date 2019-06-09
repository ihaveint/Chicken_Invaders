package menus;

import Animations.Resume.Resume;
import Animations.explosions.small_explosion;
import Levels.Level;
import Levels.LevelManager;
import Listener.GameKeyListener;
import Listener.GameMouseListener;
import Listener.GameMouseMotionListener;
import gameObjects.*;
import resources.ImageLoader;
import resources.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends FullSizePanel {
    public boolean running = false;
    public static boolean paused = false;
//    public static ArrayList<shoot> shoots = new ArrayList<shoot>();
    public static ArrayList<RedShoot> main_shoots = new ArrayList<>();
    public static ArrayList<Missile> missiles = new ArrayList<>();
//    public static Location mouseLocation = new Location(700,700);
    public int spaceShipWidth , spaceShipHeight;
    public int shootWidth , shootHeight;
    public static Resume resumeAnimation = new Resume();
    public SpaceShip spaceShip = new SpaceShip();
    private static ArrayList<SampleBird> sampleBirds = new ArrayList<>();
    private static ArrayList<small_explosion> small_explosions = new ArrayList<>();
    public static ArrayList<Bird> final_birds = new ArrayList<>();

    public static GamePanel ourInstance = new GamePanel();

    public static GamePanel getInstance() {
        return ourInstance;
    }

    public static void reset() {
        for (SampleBird sampleBird : sampleBirds){
            sampleBird.hp.increasing = false;
            sampleBird.hp.decreasing = false;
            sampleBird.hp.drawPercentage = 0;
        }
    }

    public void add_explosion(int x , int y){
        small_explosions.add(new small_explosion(x,y));

    }
//    RandomBird test;
    public GamePanel(){
        super();

        final_birds.add(new MorgheEntehari());
        final_birds.add(new MorgheEntehari());
        final_birds.add(new MorghePile());
        sampleBirds.add(new SampleBird(0,100));

        sampleBirds.add(new SampleBird(600,300));

        currentLevel.sampleBirds = sampleBirds;
        currentLevel.final_birds = final_birds;
        currentLevel.powerUps.add(new RedPowerUp());
        Level level2 = new Level();
        level2.final_birds.add(new MorghePile());
        level2.final_birds.add(new MorghePile());
        level2.final_birds.get(0).birdLocation = new Location(600,600);
        currentLevel.nextLevel = level2;
        spaceShipWidth = ImageLoader.getImage("SpaceShip").getWidth(null);
        spaceShipHeight = ImageLoader.getImage("SpaceShip").getHeight(null);
        shootWidth = ImageLoader.getImage("RedBullet").getWidth(null);
        shootHeight = ImageLoader.getImage("RedBullet").getHeight(null);
//        this.addMouseMotionListener(GameMouseMotionListener.getInstance());
//        this.addKeyListener(GameKeyListener.getInstance());
//        this.addMouseListener(GameMouseListener.getInstance());
    }

    public static void pause() {
        paused = true;
    }

    public static void switchState() {
        if (paused) {
            paused = false;
            resumeAnimation.currentPercentage = 0;
            SpaceShip.showResume = true;
        }
        else paused = true;
    }

    public static synchronized void removeBird(int id) {
        for (int ptr = 0 ; ptr < sampleBirds.size() ; ptr ++){
            if (sampleBirds.get(ptr).id == id){
                sampleBirds.remove(ptr);
                break;
            }
        }

    }

    public static void drawHeat(int i , Graphics g) {
        g.drawImage(ImageLoader.getImage("heat" +i),0,0,400,400,null);
    }


    public static boolean delayHeat = false;
    long startDelay = -5000;
    public Level currentLevel = new Level();

//    RedPowerUp testPowerUp = new RedPowerUp();
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        MovingBackGround.getInstance().draw(g,paused);
        g.drawImage(ImageLoader.getImage("upper_left"),0,0,400,400,null);

        spaceShip.currentHeat = Math.min(spaceShip.currentHeat,100);
        if (spaceShip.currentHeat == 100){
            delayHeat = true;
            startDelay = System.currentTimeMillis();
        }
        if (spaceShip.currentHeat < 0) {
            spaceShip.currentHeat = 0;
        }
        if (delayHeat && System.currentTimeMillis() - startDelay > 5000){
            delayHeat = false;

        }

        drawHeat((int) (spaceShip.currentHeat / 100 * 16), g);





        for (small_explosion small_explosion : small_explosions){
            small_explosion.draw(g);
        }
        ArrayList<UpgradbleShoot> currentShoots = new ArrayList<>();
        for (UpgradbleShoot shoot : main_shoots){
            currentShoots.add(shoot);
        }
        for (UpgradbleShoot tir : currentShoots){
            tir.checkBarkhord();
        }
        for (UpgradbleShoot tir : currentShoots){

            tir.draw(g);

        }


//        testPowerUp.draw(g);
//        testPowerUp.update();

        int missileWidth , missileHeight;
        missileWidth = ImageLoader.getImage("missile").getWidth(null);
        missileHeight = ImageLoader.getImage("missile").getHeight(null);

        for (Missile missile : missiles){
            if (missile.visible)
                g.drawImage(ImageLoader.getImage("missile"),missile.x-missileWidth/2,missile.y - missileHeight/2 , null);
        }


        LevelManager.getInstance().update();
        currentLevel = LevelManager.getInstance().currentLevel;
        currentLevel.draw(g);

        spaceShip.draw(g);

        if (SpaceShip.showResume) {
            resumeAnimation.draw(g);
            resumeAnimation.update();
        }
        for (UpgradbleShoot tir : currentShoots){
            tir.update(paused||spaceShip.showResume);
        }

        for (Missile missile : missiles){
            missile.update(paused||spaceShip.showResume);
        }

        spaceShip.update(paused);

        if (paused){
            PauseFrame.getInstance().draw(g);
            running = false;
        }


        SpaceShip.currentHeat -= 0.7;



    }
}
