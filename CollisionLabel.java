//By Clay

import java.awt.*;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import java.io.File;
import javax.imageio.ImageIO;

import java.util.ArrayList;

class CollisionLabel extends JLabel{
    //public Polygon polygon = new Polygon(); //the collision box (can be any shape made using vertexes)
    //public ArrayList<Point> verticies = new ArrayList<Point>();
    public Area area;
    private int lastX = 0;
    private int lastY = 0;
    private double truncatedX = 0;
    private double truncatedY = 0;
    JLabel collision = new JLabel(); //this variable is for testing only
    
    BufferedImage image; 
    
    public CollisionLabel(){ super(); }
    
    public CollisionLabel(CollisionLabel l){
        super(new ImageIcon(l.image));
        this.area = new Area(l.area);
        this.image = copyImage(l.image);//(BufferedImage)l.image.clone();
        super.setSize(l.getWidth(), l.getHeight());
    }
    
    public CollisionLabel(CollisionLabel l, int x, int y){
        this(l);
        
        super.setLocation(x, y);
        moveCollisionBox(x - l.getX(), y - l.getY());
        lastX = x;
        lastY = y;
    }
    
    public CollisionLabel(String filePath, int x, int y){
        super(new ImageIcon(filePath));
        super.setLocation(x, y);
        lastX = x;
        lastY = y;
        
        try{
            image = ImageIO.read(new File(filePath));
        } catch(Exception e){
            System.out.println("CollisionLabel ERROR: Image filepath error: " + filePath + "\nvia: " + e.getMessage());
        }
        
        if(image == null){
            System.out.println("CollisionLabel ERROR: Image initialization error");
        }
        
        int xMax = image.getWidth();
        int yMax = image.getHeight();
        super.setSize(xMax, yMax);
        
        //area = getArea_FastHack(image);
        AffineTransform translate = new AffineTransform();
        translate.setToTranslation(x, y);
        area = getArea_FastHack(image).createTransformedArea(translate);
        
        this.image = image;
    }
    
    //Code modified from: http://stackoverflow.com/questions/7052422/image-graphic-into-a-shape
    public static Area getArea_FastHack(BufferedImage image) {
        //Assumes any alpha value greater than zero as Shape Color
        if(image==null) return null;

        Area area = new Area();
        Rectangle r;
        int y1,y2;

        for (int x=0; x<image.getWidth(); x++) {
            y1=99;
            y2=-1;
            for (int y=0; y<image.getHeight(); y++) {
                if (image.getRGB(x,y)!=0) {
                    if(y1==99) {y1=y;y2=y;}
                    if(y>(y2+1)) {
                        r = new Rectangle(x,y1,1,y2-y1);
                        area.add(new Area(r)); 
                        y1=y;y2=y;
                    }
                    y2=y;
                }               
            }
            if((y2-y1)>=0) {
                r = new Rectangle(x,y1,1,y2-y1);
                area.add(new Area(r)); 
            }
        }

        return area;
    }
    
    public boolean isColliding(CollisionLabel cl){ //returns true if there's any overlap between this and the parameter
        Area areaTest = (Area)area.clone();
        areaTest.intersect(cl.area);
        return !areaTest.isEmpty();
    }
    
    public void setLocation(double x, double y){ //moves the collisionlabel to the specified coordinates
        moveLocation(x - (double)lastX, y - (double)lastY);
    }
    
    public void moveLocation(double deltaX, double deltaY){ //moves the collisionlabel by the specified distance
        truncatedX += deltaX - (int)deltaX;
        truncatedY += deltaY - (int)deltaY;
        if(truncatedX >= 1){
            deltaX++; truncatedX--;
        } else if(truncatedX <= -1){
            deltaX--; truncatedX++;
        }
        if(truncatedY >= 1){
            deltaY++; truncatedY--;
        } else if(truncatedY <= -1){
            deltaY--; truncatedY++;
        }
        
        super.setLocation(lastX + (int)deltaX, lastY + (int)deltaY);
        moveCollisionBox((int)deltaX, (int)deltaY);
        
        lastX = lastX + (int)deltaX;
        lastY = lastY + (int)deltaY;
    }
    
    public void setImage(String filePath){
        super.setIcon(new ImageIcon(filePath));
    }
    
    public void setImage(BufferedImage i){
        super.setIcon(new ImageIcon(i));
    }
    
    public void moveCollisionBox(double deltaX, double deltaY){
        area = area.createTransformedArea(AffineTransform.getTranslateInstance(deltaX, deltaY));
    }
    
    public void rotate(double theta){
        AffineTransform rotate = AffineTransform.getRotateInstance(theta, getWidth()/2, getHeight()/2);
        AffineTransformOp rotator = new AffineTransformOp(rotate, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        
        BufferedImage rotated = CollisionLabel.copyImage(image);
        rotator.filter(image, rotated);
        image = rotated;
        
        //AffineTransformOp.applyTransform(image, rotate, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        
        area = area.createTransformedArea(rotate);
        
        super.setIcon(new ImageIcon(image));
    }
    
    public int getX(){ return super.getX(); }
    public int getY(){ return super.getY(); }
    public int getWidth(){ return super.getWidth(); }
    public int getHeight(){ return super.getHeight(); }
    
    //code copied from http://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
    static BufferedImage copyImage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}









