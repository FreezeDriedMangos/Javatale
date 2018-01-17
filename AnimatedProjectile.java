//WHATEVER USED TO BE HERE, FORGET IT. MAKE IT SO THAT IT UPDATES THE COLLISION BOX FOR EVERY FRAME. RECIVES A FILE
//PATH TO A FOLDER THAT CONTAINS ALL THE FRAMES, NAMED 1 - n. n BEING THE NUMBER OF FRAMES. DO THE UPDATING  AND 
//SLEEPING IN A THREAD SO THAT TIMING IS MAINTAINED. 
//IF IT HAS BEEN ROTATED, MAKE SURE TO RETAIN THAT ROTATION
// n CAN BE FOUND USING THE LINE: (new File(filePath)).listFiles().length
// ALSO INCLUDE A SPRITESHEET OPTION

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class AnimatedProjectile extends Projectile{
    File folder;
    double rotation = 0;
    
    public AnimatedProjectile(String filePath, int x, int y){ //assumes the filepath is a folder containing the frames labeled as 1-maxFrames 
        super(filePath + "1.png", x, y);
        folder = new File(filePath);
    }
    
    public AnimatedProjectile(AnimatedProjectile template, int x, int y){ //assumes the filepath is a folder containing the frames labeled as 1-maxFrames 
        super(template, x, y);
        folder = template.folder;
    }
    
    public AnimatedProjectile(String filePath, int x, int y, Vector v, int damage){ //assumes the filepath is a folder containing the frames labeled as 1-maxFrames 
        super(filePath + "1.png", x, y, v, damage);
        folder = new File(filePath);
    }
    
    public void animate(){
        animate(10);
    }
    
    public void animate(int fps){
        if(fps > 1000)
            fps = 1000;
            
        final int secondsPerFrame = (int)((1.0 / (double)fps) * 1000);
        final File[] frames = folder.listFiles();
        
        Thread t = new Thread(){
            public void run(){
                for(int i = 0; i < frames.length; i++){
                    try {
                        BufferedImage frame = ImageIO.read(frames[i]);
                        setImage(frame);
                        area = getArea_FastHack(frame); 
                        moveCollisionBox(getX(), getY()); 
                    
                        rotate(rotation);
                        Thread.sleep(secondsPerFrame);
                    } catch (Exception e){}
                }
            }
        };
        
        t.start();
    }
    
    public void rotate(double theta){
        super.rotate(theta);
        
        rotation += theta;
    }
}

/*
public class AnimatedProjectile extends Projectile{
    private int frameWidth = 0;
    private int frameHeight = 0;
    private int currentFrameX = 0;
    
    private BufferedImage spriteSheet;
    
    public AnimatedProjectile(String filePath, int frameW, int frameH, int x, int y){
        super();
        
        setLocation(x, y);
        
        frameWidth = frameW;
        frameHeight = frameH;
        
        try{
            spriteSheet = ImageIO.read(new File(filePath));
            image = spriteSheet.getSubimage(0, 0, frameWidth, frameHeight); 
            setImage(image);
        } catch(Exception e){
            System.out.println("CollisionLabel ERROR: Image filepath error\nvia: " + e.getMessage());
        }
        
        area = getArea_FastHack(image);
        moveCollisionBox(x, y);
    }
    
    public void run(){
        Thread t = new Thread(){
            public void run(){
                for (int h = 0; h < spriteSheet.getHeight(); h += frameHeight){
                    for (int w = 0; w < spriteSheet.getWidth(); w += frameWidth){
                        image = spriteSheet.getSubimage(w, h, frameWidth, frameHeight);
                        setImage(image);
                        area = getArea_FastHack(image); 
                        moveCollisionBox(getX(), getY()); System.out.println(area.getBounds().getLocation());
                        try {
                            Thread.sleep(100);
                        } catch (Exception e){}
                    }
                }
            }
        };
        t.start();
    }
} */


