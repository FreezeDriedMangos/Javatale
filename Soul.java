import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Soul extends CollisionLabel{
    public static final double SPEED = Battle.SOUL_SPEED;
    
    public static final int RED = 0;      public static final int BLUE = 1;
    public static final int GREEN = 2;    public static final int PURPLE = 3;
    public static final int YELLOW = 4;   public static final int WHITE = 5;
    public static final int CYAN = 6;     public static final int ORANGE = 7;
    public static final int MAX_INVINCIBILITY_FRAMES = 70;

    public Vector velocity = new Vector();

    public int invincibilityTimer = 0;
    public boolean[] color = {true, false, false, false, false, false, false, false};
    public int colorIndex = 0;
    public int hp = 20;
    public int maxHP = 20;
    public int lv = 1;
    public int attack = 10;
    public String name;
    
    //Soul color variables
    //the below is used for blue soul
    public Vector gravity = new Vector(-0.15, Math.PI * 3 / 2);
    
    //the below are used for green soul
    public int shieldDirection = 1;
    private int shieldOffsetX = 23;
    private int shieldOffsetY = 24;
    public CollisionLabel shield = new CollisionLabel("imgs/soul/shieldHitBox.png", 23, 38);
    
    //the below are used for purple soul
    public int string = 0;
    public int stringCount = 3;
    public int stringMinY = 110 + 250; //distance from top of box + boxY
    static final int DISTANCE_BETWEEN_STRINGS = 40;
    public JLabel strings = new JLabel(new ImageIcon("imgs/soul/strings.png"));
    
    //the below are used for blue soul
    public boolean canJump = false; 
    public boolean jumping = false;
    public int jumpTimer = 0;
    public static final int MAX_JUMP_TIME = 45;
    
    //used for yellow soul
    ArrayList<Projectile> pellets = new ArrayList<Projectile>();
    static final Projectile PELLET = new Projectile("imgs/soul/pellet.png", 0, 0, new Vector(2 * 1.5, Math.PI * 3 / 2), true);
    public int pelletTimer = 0;
    public int PELLET_TIMER_MAX = 15;
    
    public Soul(int x, int y){
        super("imgs/soul/red.png", x, y);

        int namePick = (int)(Math.random() * 5);
        if(namePick == 0){
            name = "frisk";
        } else if(namePick == 1){
            name = "chara";
        } else if(namePick == 2){
            name = "papyru";
        } else if(namePick == 3){
            name = "sans";
        } else if(namePick == 4){
            name = "???";
        } else if(namePick == 5){
            name = "[redacted[";
        }
        
        Rectangle rect = new Rectangle(x + 4, y + 4, 8, 8);
        area = new Area(rect);
        
        //set up shield variables
        shield.setVisible(false);
        /*
        SHIELD_UP.setIcon(new ImageIcon("imgs/soul/shield1.png"));
        
        SHIELD_RIGHT.rotate(Math.PI * 3/2);
        SHIELD_RIGHT.setIcon(new ImageIcon("imgs/soul/shield2.png"));
        
        SHIELD_DOWN.rotate(Math.PI);
        SHIELD_DOWN.setIcon(new ImageIcon("imgs/soul/shield3.png"));
        
        SHIELD_LEFT.rotate(Math.PI / 2);
        SHIELD_LEFT.setIcon(new ImageIcon("imgs/soul/shield4.png"));*/
    }

    public void setColor(int c){
        colorIndex = c;

        for(int i = 0; i < 8; i++){
            color[i] = false;
        }

        color[c] = true;
        switch(c){
            case 0:
            super.setImage("imgs/soul/red.png");
            break;
            case 1:
            super.setImage("imgs/soul/blue.png");
                jumpTimer = MAX_JUMP_TIME;
                jumping = true;
                canJump = false;
            break;
            case 2:
            super.setImage("imgs/soul/green.png");
            break;
            case 3:
            super.setImage("imgs/soul/purple.png");
            break;
            case 4:
            super.setImage("imgs/soul/yellow.png");
            break;
            case 5:
            super.setImage("imgs/soul/white.png");
            break;
            case 6:
            super.setImage("imgs/soul/cyan.png");
            break;
            case 7:
            super.setImage("imgs/soul/orange.png");
            break;
        }
        
        if(this.color[GREEN]){
            shield.setLocation(getX() - shieldOffsetX, getY() - shieldOffsetY);
            setShield(1);
            shield.setVisible(true);
        } else {
            shield.setVisible(false);
        }
    }

    public void updateLocation(double delta, BoxLabel box){
        //here is where collision with the box be checked for and resolved
        double newX = super.getX() + (velocity.a() * delta);
        double newY = super.getY() + (velocity.b() * delta);
        double width = super.getWidth();
        double height = super.getHeight();

        double boxMinX = box.getX() + box.borderWidth;
        double boxMaxX = box.getX() + box.getWidth() - box.borderWidth;
        double boxMinY = box.getY() + box.borderWidth;
        double boxMaxY = box.getY() + box.getHeight() - box.borderWidth;

        if(newX < boxMinX){
            newX = boxMinX;
        } else if (newX + width > boxMaxX){
            newX = boxMaxX - width;
        }

        if(newY < boxMinY){
            newY = boxMinY;
        } else if (newY + height > boxMaxY){
            newY = boxMaxY - height;
        }
        
        //purple soul logic
        if(color[PURPLE]){
            newY = (stringMinY + string * DISTANCE_BETWEEN_STRINGS) - getWidth() / 2;
            
            int stringMinX = strings.getX() - getWidth() / 2;
            int stringMaxX = strings.getX() + strings.getWidth() - getWidth() / 2;
            if(newX < stringMinX){
                newX = stringMinX;
            } else if (newX > stringMaxX){
                newX = stringMaxX;
            }
        }
        
        super.setLocation(newX, newY);
        
        //Misc soul color stuff
        if(color[BLUE]){ //Things related to jumping
            if(!jumping && velocity.b() >= -SPEED){
                velocity.add(gravity);
            } else if (jumping){
                jumpTimer++;
            }
            
            if(jumpTimer > MAX_JUMP_TIME){
                jumping = false;
            }
            
            if(canJump && velocity.b() > 0){
                velocity.setB(0);
            }
            
            if(newY + height == boxMaxY){
                canJump = true;
            }
        }
    }
    
    public void setInvicibilityImage(boolean invincible){
        String color = "red";
        if(!invincible){
            switch(colorIndex){
                case 0:
                super.setImage("imgs/soul/red.png");
                break;
                case 1:
                super.setImage("imgs/soul/blue.png");
                break;
                case 2:
                super.setImage("imgs/soul/green.png");
                break;
                case 3:
                super.setImage("imgs/soul/purple.png");
                break;
                case 4:
                super.setImage("imgs/soul/yellow.png");
                break;
                case 5:
                super.setImage("imgs/soul/white.png");
                break;
                case 6:
                super.setImage("imgs/soul/cyan.png");
                break;
                case 7:
                super.setImage("imgs/soul/orange.png");
                break;
            }
        } else {
           switch(colorIndex){
                case 0:
                super.setImage("imgs/soul/redInvincible.gif");
                break;
                case 1:
                super.setImage("imgs/soul/blueInvincible.gif");
                break;
                case 2:
                super.setImage("imgs/soul/greenInvincible.gif");
                break;
                case 3:
                super.setImage("imgs/soul/purpleInvincible.gif");
                break;
                case 4:
                super.setImage("imgs/soul/yellowInvincible.gif");
                break;
                case 5:
                super.setImage("imgs/soul/whiteInvincible.gif");
                break;
                case 6:
                super.setImage("imgs/soul/cyanInvincible.gif");
                break;
                case 7:
                super.setImage("imgs/soul/orangeInvincible.gif");
                break;
            } 
        }
    }
    
    public void updatePellets(double delta){
        if(color[YELLOW]){ 
            if(pelletTimer > 0){
                pelletTimer--;
            }
            
            ArrayList<Projectile> remove = new ArrayList<Projectile>();
            
            for(Projectile p : pellets){
                p.update(delta);
                if(p.getY() < -p.getHeight()){
                    remove.add(p);
                }
            }
            
            for(Projectile p : remove){
                p.setVisible(false);
                pellets.remove(p);
            }
        }
    }
    
    public void setShield(int direction){
        shieldDirection = direction;
        shield.setImage("imgs/soul/shield" + direction + ".png");
        
        /*
        if(direction == 1){
            shield.area = new Area(SHIELD_UP.area);
        } else if(direction == 2){
            shield.area = new Area(SHIELD_RIGHT.area);
        } else if(direction == 3){
            shield.area = new Area(SHIELD_DOWN.area);
        } else if(direction == 4){
            shield.area = new Area(SHIELD_LEFT.area);
        }
        
        shield.moveCollisionBox(shield.getX(), shield.getY());*/
    }
}


