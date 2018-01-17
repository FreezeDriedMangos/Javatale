import java.util.ArrayList;
import javax.swing.JLayeredPane;
import java.util.Random;

import javax.swing.JLabel;

public class AnnoyingDogAttack extends Attack{
    static final int FRISBEE_WALL_1 = 0;             static final int FRISBEE_WALL_2 = 1;
    static final int AIMED_FRISBEES_HORIZONTAL = 2;  static final int AIMED_FRISBEES_0 = 3;
    static final int BARK_0 = 4;                     static final int BARK_1 = 5;
    
    static final int BLUE = 10;                      static final int GREEN = 11;    
    static final int PURPLE = 12;                    static final int YELLOW = 13;
    static final int ALL_COLORS = 14;                static final int ALL_COLORS_HARD = 15;
    
    private static final Projectile FRISBEE = new Projectile("imgs/dog/frisbee_big.png", 0, 0, new Vector(3, 0), 11);
    private static final Projectile DOG = new Projectile("imgs/dog/dogProjectile.png", 0, 0, new Vector(2.5, 0), 7);
    private static final Projectile SOLID_BOX = new Projectile("imgs/dog/solidBox.png", 0, 0, new Vector(2.5, Math.PI /2), 7);
    private static final Projectile BREAKABLE_BOX = new Projectile("imgs/dog/breakableBox.png", 0, 0, new Vector(2.5, Math.PI /2), 7);
    private static final Projectile BONE_1 = new Projectile("imgs/dog/bones/bone1.png", 0, 0, new Vector(5, 0), 7);
    
    int frisbeeSpeed = 3;
    int frisbeeDamage = 11;
    
    public AnnoyingDogAttack(int attack, BoxLabel box, JLayeredPane panel, Soul soul){
        super(attack, box, panel);
        SOLID_BOX.removeOnCollision = false;
        
        if(attack == FRISBEE_WALL_1){
            SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
            int centerX = box.getX() + box.getWidth()/2;
            int distanceToCenter = 400;
            int y = box.getY(); 
            
            for(int i = 0; i < 4; i++){ //number of collumns
                y = box.getY();
                for(int j = 0; j < 18; j++){ //number of frisbees in each collumn
                    if((j/2 % 4) % 2 == 0){
                        FRISBEE.velocity.setA(3);
                        pm.spawnProjectile(new Projectile(FRISBEE, centerX - distanceToCenter - FRISBEE.getWidth(), y));
                    } else {
                        FRISBEE.velocity.setA(-3);
                        pm.spawnProjectile(new Projectile(FRISBEE, centerX + distanceToCenter, y));
                    }
                    y += FRISBEE.getHeight();
                }
                distanceToCenter += 200; //distance between collumns
            }
            
            projectileManagers.add(pm);
        } else if(attack == FRISBEE_WALL_2){ //THIS ONE IS KINDA VERY BROKEN
            System.out.println("AnnoyingDogAttack, constructor: ERROR: This attack has been deprecated");
        } else if (attack == AIMED_FRISBEES_HORIZONTAL){
            projectileManagers.add(new SetVelocityPM(new ArrayList<Projectile>(), panel, Main.PROJECTILE_LAYER_HIDDEN));
        } else if (attack == AIMED_FRISBEES_0){
            projectileManagers.add(new SetVelocityPM(new ArrayList<Projectile>(), panel, Main.PROJECTILE_LAYER_HIDDEN));
        } else if (attack == BARK_0){
            projectileManagers.add(new TargetedPM(panel, Main.PROJECTILE_LAYER_VISIBLE, soul));
        } else if (attack == BARK_1){
            projectileManagers.add(new TargetedPM(panel, Main.PROJECTILE_LAYER_VISIBLE, soul));
            projectileManagers.get(0).spawnProjectile(new Projectile("imgs/dog/bark.png", 320 - 33, 100, new Vector(3.5, 0), 11));
        } else if (attack == GREEN){
            Projectile template = new Projectile("imgs/dog/ball.png", 0, 0, new Vector(3, Math.PI), 6);
            GreenSoulPM pm = new GreenSoulPM(panel, "imgs/dog/ball.png", new int[]{1,1,1,4,2,4,2,3,3,3}, 32, 6, 3, soul);
            projectileManagers.add(pm);
        } else if (attack == PURPLE){
            SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
            
            //spawn the middle row of projectiles
            int y = box.getY() + 60; //soul.stringMinY + soul.DISTANCE_BETWEEN_STRINGS - DOG.getHeight()/2; //this is the y value needed to center the projectile on the middle string
            for(int i = 0; i < 52; i++){
                if(i % 5 != 0 && i % 5 != 1){ //we want to leave a gap every fourth projectile
                    pm.spawnProjectile(new Projectile(DOG, box.getX() - (DOG.getWidth()) * i, y));
                }
            }
            
            //spawn the top row of projectiles
            y -= soul.DISTANCE_BETWEEN_STRINGS; //move the y value up one string
            for(int i = 0; i < 52; i++){
                if(i % 10 == 3){ //we want to leave a gap every fourth projectile
                    pm.spawnProjectile(new Projectile(DOG, box.getX() - (DOG.getWidth()) * i, y));
                }
            }
            
            //spawn the bottom row of projectiles
            y += soul.DISTANCE_BETWEEN_STRINGS * 2; //move the y value down two string
            DOG.velocity.setR(-2.5); //make DOG go the opposite direction
            for(int i = 0; i < 52; i++){
                if(i % 10 == 6){ //we want to leave a gap every fourth projectile
                    pm.spawnProjectile(new Projectile(DOG, box.getX() + box.getWidth() + (DOG.getWidth()) * i, y));
                }
            }
            DOG.velocity.setR(2.5); //put DOG_SLOW back to normal
            
            projectileManagers.add(pm);
        } else if (attack == YELLOW){
            SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
            int y = 0;
            int projectileWidth = BREAKABLE_BOX.getWidth();
            
            for(int i = 0; i < 4; i++){
                int collumn = (int)((Math.random() * 3) + 0.5);
                
                if(collumn == 0){ //the collumn of breakable blocks was chosen to be the first one
                    for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                        pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 0, y)); //spawn the breakable block
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 1, y)); //spawn a solid block
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 2, y)); //spawn a solid block
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 3, y)); //spawn a solid block
                        
                        y -= projectileWidth;
                    }
                } else if (collumn == 1){ //the collumn of breakable blocks was chosen to be the second one
                    for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 0, y)); //spawn a solid block
                        pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 1, y)); //spawn the breakable block
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 2, y)); //spawn a solid block
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 3, y)); //spawn a solid block
                        
                        y -= projectileWidth;
                    }
                } else if (collumn == 2){ //the collumn of breakable blocks was chosen to be the third one
                    for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 0, y)); //spawn a solid block
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 1, y)); //spawn a solid block
                        pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 2, y)); //spawn the breakable block
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 3, y)); //spawn a solid block
                        
                        y -= projectileWidth;
                    }
                } else if (collumn == 3){ //the collumn of breakable blocks was chosen to be the last one
                    for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 0, y)); //spawn a solid block
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 1, y)); //spawn a solid block
                        pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 2, y)); //spawn a solid block
                        pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 3, y)); //spawn the breakable block
                        
                        y -= projectileWidth;
                    }
                }
                
                y -= projectileWidth * 10;
            }
            
            projectileManagers.add(pm);
        } else if (attack == BLUE){
            SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
            int centerX = box.getX() + box.getWidth()/2;
            int distanceToCenter = 400;
            int y = box.getY() + 5; 
            
            for(int i = 0; i < 8; i++){ //number of collumns
                if(i % 2 == 0){
                    pm.spawnProjectile(new Projectile(BONE_1, centerX - distanceToCenter - BONE_1.getWidth(), y));
                } else {
                    BONE_1.velocity.setA(-3);
                    pm.spawnProjectile(new Projectile(BONE_1, centerX + distanceToCenter, y));
                    BONE_1.velocity.setA(3);
                    
                    distanceToCenter += 300;
                }
            }
            
            projectileManagers.add(pm);
        } else if (attack == ALL_COLORS){
            //set up the first segment of this attack (red)
            SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
            int centerX = box.getX() + box.getWidth()/2;
            int distanceToCenter = 400;
            int y = box.getY(); 
            
            for(int i = 0; i < 4; i++){ //number of collumns
                y = box.getY();
                for(int j = 0; j < 18; j++){ //number of frisbees in each collumn
                    if((j/2 % 4) % 2 == 0){
                        FRISBEE.velocity.setA(3);
                        pm.spawnProjectile(new Projectile(FRISBEE, centerX - distanceToCenter - FRISBEE.getWidth(), y));
                    } else {
                        FRISBEE.velocity.setA(-3);
                        pm.spawnProjectile(new Projectile(FRISBEE, centerX + distanceToCenter, y));
                    }
                    y += FRISBEE.getHeight();
                }
                distanceToCenter += 200; //distance between collumns
            }
            
            projectileManagers.add(pm);
        } else if (attack == ALL_COLORS_HARD){
            //set up the first segment of this attack (red)
            SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
            int centerX = box.getX() + box.getWidth()/2;
            int distanceToCenter = 400;
            int y = box.getY(); 
            
            for(int i = 0; i < 4; i++){ //number of collumns
                y = box.getY();
                for(int j = 0; j < 18; j++){ //number of frisbees in each collumn
                    if((j/2 % 4) % 2 == 0){
                        FRISBEE.velocity.setA(3);
                        pm.spawnProjectile(new Projectile(FRISBEE, centerX - distanceToCenter - FRISBEE.getWidth(), y));
                    } else {
                        FRISBEE.velocity.setA(-3);
                        pm.spawnProjectile(new Projectile(FRISBEE, centerX + distanceToCenter, y));
                    }
                    y += FRISBEE.getHeight();
                }
                distanceToCenter += 200; //distance between collumns
            }
            
            projectileManagers.add(pm);
        }
    }
    
    public void tickUpdate(double delta, Soul soul){
        if(attack == AIMED_FRISBEES_HORIZONTAL){
            if(age % 20 == 0){
                projectileManagers.get(0).spawnProjectile(new Projectile("imgs/dog/frisbee_big.png", box.getX() - 64, soul.getY(), new Vector(frisbeeSpeed, 0), frisbeeDamage));
            }
        } else if(attack == AIMED_FRISBEES_0){
            if(age % 80 == 0){
                projectileManagers.get(0).spawnProjectile(new Projectile("imgs/dog/frisbee_big.png", box.getX() - 128, soul.getY(), new Vector(frisbeeSpeed, 0), 1));
            }
        } else if (attack == BARK_0){
            //formula:
            // (age - timeEventsInAPair) % timeBetweenPairs == 0 || (age) % timeBetweenPairs == 0
            
            if((age - 20) % 120 == 0 || age % 120 == 0){
                projectileManagers.get(0).spawnProjectile(new Projectile("imgs/dog/bark.png", 320 - 33, 100, new Vector(3.5, 0), 2));
                SoundPlayer.play("sounds/dog/dogBark.wav");
            }
        } else if (attack == BARK_1){
            if(age == 20){
                projectileManagers.get(0).spawnProjectile(new Projectile("imgs/dog/bark.png", monster.getX(), monster.getY(), new Vector(3.5, 0), 11));
                SoundPlayer.play("sounds/dog/dogBark.wav");
            } else if (age == 40 || age == 60){
                monster.setLocation(430, 200);
                projectileManagers.get(0).spawnProjectile(new Projectile("imgs/dog/bark.png", monster.getX(), monster.getY(), new Vector(3.5, 0), 11));
                SoundPlayer.play("sounds/dog/dogBark.wav");
            } else if (age == 80 || age == 100){
                monster.setLocation(75, 200);
                projectileManagers.get(0).spawnProjectile(new Projectile("imgs/dog/bark.png", monster.getX(), monster.getY(), new Vector(3.5, 0), 11));
                SoundPlayer.play("sounds/dog/dogBark.wav");
            } else if (age == 120 || age == 140){
                monster.setLocation(430, 250);
                projectileManagers.get(0).spawnProjectile(new Projectile("imgs/dog/bark.png", monster.getX(), monster.getY(), new Vector(3.5, 0), 11));
                SoundPlayer.play("sounds/dog/dogBark.wav");
            } else if(age == 160 || age == 180){
                monster.setLocation(320 - 33, 100);
                projectileManagers.get(0).spawnProjectile(new Projectile("imgs/dog/bark.png", monster.getX(), monster.getY(), new Vector(3.5, 0), 11));
                SoundPlayer.play("sounds/dog/dogBark.wav");
            } else if(age == 200 || age == 240 || age == 280 || age == 300){
                projectileManagers.get(0).spawnProjectile(new Projectile("imgs/dog/bark.png", monster.getX(), monster.getY(), new Vector(3.5, 0), 11));
                SoundPlayer.play("sounds/dog/dogBark.wav");
            }
        } else if (attack == ALL_COLORS){
            if(age == 370){
                //make the projectiles from the last part invisible and non-damaging
                projectileManagers.get(0).setNotCollidable();
                projectileManagers.remove(0);
                
                //change the box size and soul color (this is very unusual ... but may actually be a better way of doing things...)
                soul.setColor(soul.BLUE);
                box.changeSize(186, 140, true);
                
                //set up the next portion of the attack (blue)
                SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
                int centerX = box.getX() + box.getWidth()/2;
                int distanceToCenter = 400;
                int y = box.getY() + 5; 
            
                for(int i = 0; i < 14; i++){ //number of collumns
                    if(i % 2 == 0){
                        pm.spawnProjectile(new Projectile(BONE_1, centerX - distanceToCenter - BONE_1.getWidth(), y));
                    } else {
                        BONE_1.velocity.setA(-5);
                        pm.spawnProjectile(new Projectile(BONE_1, centerX + distanceToCenter, y));
                        BONE_1.velocity.setA(5);
                        
                        distanceToCenter += 300;
                    }
                }
            
                projectileManagers.add(pm);
            } else if (age == 370 * 2){
                //make the projectiles from the last part invisible and non-damaging
                projectileManagers.get(0).setNotCollidable();
                projectileManagers.remove(0);
                
                //change the box size and soul color (this is very unusual ... but may actually be a better way of doing things...)
                box.defaultAttackSize(true);
                soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
                soul.setColor(soul.GREEN);
                
                //Just in case of residual gravity
                soul.velocity = new Vector();
                
                //set up the next portion of the attack (green)
                GreenSoulPM pm = new GreenSoulPM(panel, "imgs/dog/ball.png", new int[]{1,1,1,4,2,4,2,3,3,3,4,3,4,2,1,3}, 32, 6, 3, soul);
                projectileManagers.add(pm);
            } else if (age == 370 * 3){
                    //make the projectiles from the last part invisible and non-damaging
                    projectileManagers.get(0).setNotCollidable();
                    projectileManagers.remove(0);
                
                    //make the shield from the last soul color invisible
                    soul.shield.setVisible(false);
                    
                    //reset the soul's velocity
                    soul.velocity = new Vector();
                    if(Input.getRight()){
                        soul.velocity.add(new Vector(2, 0));
                    }
                    if(Input.getLeft()){
                        soul.velocity.add(new Vector(-2, 0));
                    }
                    
                    //change the box size(this is very unusual ... but may actually be a better way of doing things...)
                    box.defaultPurpleSize(false);
                    
                    //set up the soul's strings
                    soul.strings.setSize(200, 100);
                    soul.strings.setLocation(box.getX() + 20, box.getY() + 20);
                    soul.strings.setVisible(true);
                    soul.stringMinY = soul.strings.getY() + 10; //+ soul.DISTANCE_BETWEEN_STRINGS;
                    
                    //set the soul location and color
                    soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
                    soul.setColor(soul.PURPLE);
                    
                    //set up the next portion of the attack (purple)
                    SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
            
                    //spawn the middle row of projectiles
                    int y = box.getY() + 60; //soul.stringMinY + soul.DISTANCE_BETWEEN_STRINGS - DOG.getHeight()/2; //this is the y value needed to center the projectile on the middle string
                    for(int i = 0; i < 52; i++){
                        if(i % 5 != 0 && i % 5 != 1){ //we want to leave a gap every fourth projectile
                            pm.spawnProjectile(new Projectile(DOG, box.getX() - (DOG.getWidth()) * i, y));
                        }
                    }
            
                    //spawn the top row of projectiles
                    y -= soul.DISTANCE_BETWEEN_STRINGS; //move the y value up one string
                    for(int i = 0; i < 52; i++){
                        if(i % 10 == 3){ //we want to leave a gap every fourth projectile
                            pm.spawnProjectile(new Projectile(DOG, box.getX() - (DOG.getWidth()) * i, y));
                        }
                    }
                    
                    //spawn the bottom row of projectiles
                    y += soul.DISTANCE_BETWEEN_STRINGS * 2; //move the y value down two string
                    for(int i = 0; i < 52; i++){
                        if(i % 10 == 8){ //we want to leave a gap every fourth projectile
                            pm.spawnProjectile(new Projectile(DOG, box.getX() - (DOG.getWidth()) * i, y));
                        }
                    }
                    
                    projectileManagers.add(pm);
            }else if (age == 370 * 4){
                //make the strings invisible since the soul's no longer purple
                soul.strings.setVisible(false);
                
                //reset the soul's velocity
                soul.velocity = new Vector();
                if(Input.getRight()){
                    soul.velocity.add(new Vector(2, 0));
                }
                if(Input.getLeft()){
                    soul.velocity.add(new Vector(-2, 0));
                }
                if(Input.getUp()){
                    soul.velocity.add(new Vector(2, Math.PI * 3/2));
                }
                if(Input.getDown()){
                    soul.velocity.add(new Vector(-2, Math.PI * 3/2));
                }
                
                //make the projectiles from the last part invisible and non-damaging
                projectileManagers.get(0).setNotCollidable();
                projectileManagers.remove(0);
                
                //change the box size and soul color (this is very unusual ... but may actually be a better way of doing things...)
                box.changeSize(80, 200, false);
                soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
                soul.setColor(soul.YELLOW);
                        
                //set up the next portion of the attack (yellow)
                SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
                int y = 0;
                int projectileWidth = BREAKABLE_BOX.getWidth();
            
                for(int i = 0; i < 8; i++){
                    int collumn = (int)((Math.random() * 3) + 0.5);
                
                    if(collumn == 0){ //the collumn of breakable blocks was chosen to be the first one
                        //for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                            pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 0, y)); //spawn the breakable block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 1, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 2, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 3, y)); //spawn a solid block
                        
                            y -= projectileWidth;
                        //}   
                    } else if (collumn == 1){ //the collumn of breakable blocks was chosen to be the second one
                       // for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 0, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 1, y)); //spawn the breakable block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 2, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 3, y)); //spawn a solid block
                        
                            y -= projectileWidth;
                        //}
                    } else if (collumn == 2){ //the collumn of breakable blocks was chosen to be the third one
                        //for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 0, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 1, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 2, y)); //spawn the breakable block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 3, y)); //spawn a solid block
                            
                            y -= projectileWidth;
                        //}
                    } else if (collumn == 3){ //the collumn of breakable blocks was chosen to be the last one
                        //for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 0, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 1, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 2, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 3, y)); //spawn the breakable block
                        
                            y -= projectileWidth;
                        //}
                    }
                
                    y -= projectileWidth * 6;
                }
            
                projectileManagers.add(pm);
            } else if (age == 370 * 5){
                //make the projectiles from the last part invisible and non-damaging
                projectileManagers.get(0).setNotCollidable();
                projectileManagers.remove(0);
                
                //remove all pellets
                for(Projectile p : soul.pellets)
                    p.setVisible(false);
                
                //change the box size and soul color (this is very unusual ... but may actually be a better way of doing things...)
                soul.setColor(soul.RED);
                box.defaultAttackSize(true);
                
                //set up the next portion of the attack (red)
                SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
                int centerX = box.getX() + box.getWidth()/2;
                int distanceToCenter = 400;
                int y = box.getY(); 
                int random = 0;
                
                for(int i = 0; i < 4; i++){ //number of pairs of collumns
                    y = box.getY();
                    random = (int)(Math.random() + 0.5); //randomize wheather or not to offset one column
                    
                    for(int j = 0; j < 18; j++){ //number of frisbees in each collumn
                        if((j/2 % 4) % 2 == random){
                            FRISBEE.velocity.setA(3);
                            pm.spawnProjectile(new Projectile(FRISBEE, centerX - distanceToCenter - FRISBEE.getWidth(), y));
                        } else {
                            FRISBEE.velocity.setA(-3);
                            pm.spawnProjectile(new Projectile(FRISBEE, centerX + distanceToCenter, y));
                        }
                        y += FRISBEE.getHeight();
                    }
                    distanceToCenter += 200; //distance between collumns
                }
            
                projectileManagers.add(pm);
            }
        }  else if (attack == ALL_COLORS_HARD){
            if(age == 370){
                //make the projectiles from the last part invisible and non-damaging
                projectileManagers.get(0).setNotCollidable();
                projectileManagers.remove(0);
                
                //change the box size and soul color (this is very unusual ... but may actually be a better way of doing things...)
                soul.setColor(soul.BLUE);
                box.changeSize(186 * 2, 140, true);
                
                //set up the next portion of the attack (blue)
                SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
                int centerX = box.getX() + box.getWidth()/2;
                int distanceToCenter = 400;
                int speed = 3;
                int y = box.getY() + 5; 
                int x = 0;
                String filepath = "bone1.png";
                int boneHeight = (int)(Math.random()*18 + 0.5) + 1; //picks a random bone height between 1-19, inclusive
                int lastBoneHeight = 0;
            
                for(int i = 0; i < 8; i++){ //number of collumns
                    if(i % 2 == 0){
                        boneHeight = (int)(Math.random()*9 + 0.5) + 1; //picks a random bone height between 1-19, inclusive
                        x = centerX - distanceToCenter - BONE_1.getWidth();
                        filepath = "imgs/dog/bones/bone" + boneHeight + ".png";
                        pm.spawnProjectile(new Projectile(filepath, x, y, new Vector(speed, 0), 7));
                        
                        //calculate distance to center
                        boneHeight *= 4; //calculate the actual height of the gap
                        boneHeight += 10; //calculate the actual height of the gap
                        
                        double inverseSpeed = 1 / Battle.SOUL_SPEED;
                        int DECELERATION_DISTANCE = 6; //the vertical distance the soul travels from start of deceleration to zenith of jump
                        int DECELERATION_TIME = 27; //the time the soul takes to decelerate from SOUL_SPEED to -SOUL_SPEED
                        
                        int time1 = (int)(inverseSpeed * (boneHeight - DECELERATION_DISTANCE)) * 2 + DECELERATION_TIME; //calculate the time needed to jump through the first gap and land
                        distanceToCenter += speed * time1 - 20;
                    } else {
                        boneHeight = (int)(Math.random()*9 + 0.5) + 1; //picks a random bone height between 1-19, inclusive
                        x = centerX + distanceToCenter;
                        filepath = "imgs/dog/bones/bone" + boneHeight + ".png";
                        pm.spawnProjectile(new Projectile(filepath, x, y, new Vector(-speed, 0), 7));
                        
                        //calculate distance to center
                        lastBoneHeight *= 4; //calculate the actual height of the gap
                        boneHeight *= 4; //calculate the actual height of the gap
                        lastBoneHeight += 10; //calculate the actual height of the gap
                        boneHeight += 10; //calculate the actual height of the gap
                        
                        double inverseSpeed = 1 / Battle.SOUL_SPEED;
                        int DECELERATION_DISTANCE = 6; //the vertical distance the soul travels from start of deceleration to zenith of jump
                        int DECELERATION_TIME = 27; //the time the soul takes to decelerate from SOUL_SPEED to -SOUL_SPEED
                        
                        int time1 = (int)(inverseSpeed * (boneHeight - DECELERATION_DISTANCE)) * 2 + DECELERATION_TIME; //calculate the time needed to jump through the first gap and land
                        int time2 = (int)(inverseSpeed * (boneHeight - DECELERATION_DISTANCE)) * 2 + DECELERATION_TIME; //calculate the time needed to jump through the second gap and land
                        distanceToCenter += speed * time2;
                    }
                    lastBoneHeight = boneHeight;
                }
            
                projectileManagers.add(pm);
            } else if (age == 370 * 2){
                //make the projectiles from the last part invisible and non-damaging
                projectileManagers.get(0).setNotCollidable();
                projectileManagers.remove(0);
                
                //change the box size and soul color (this is very unusual ... but may actually be a better way of doing things...)
                box.defaultAttackSize(true);
                soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
                soul.setColor(soul.GREEN);
                
                //Just in case of residual gravity
                soul.velocity = new Vector();
                
                //set up the next portion of the attack (green)
                GreenSoulPM pm = new GreenSoulPM(panel, "imgs/dog/ball.png", new int[]{1,1,1,4,2,4,2,3,3,3,4,3,4,2,1,3}, 32, 6, 3, soul);
                projectileManagers.add(pm);
            } else if (age == 370 * 3){
                    //make the projectiles from the last part invisible and non-damaging
                    projectileManagers.get(0).setNotCollidable();
                    projectileManagers.remove(0);
                
                    //make the shield from the last soul color invisible
                    soul.shield.setVisible(false);
                    
                    //reset the soul's velocity
                    soul.velocity = new Vector();
                    if(Input.getRight()){
                        soul.velocity.add(new Vector(2, 0));
                    }
                    if(Input.getLeft()){
                        soul.velocity.add(new Vector(-2, 0));
                    }
                    
                    //change the box size(this is very unusual ... but may actually be a better way of doing things...)
                    box.defaultPurpleSize(false);
                    
                    //set up the soul's strings
                    soul.strings.setSize(200, 100);
                    soul.strings.setLocation(box.getX() + 20, box.getY() + 20);
                    soul.strings.setVisible(true);
                    soul.stringMinY = soul.strings.getY() + 10; //+ soul.DISTANCE_BETWEEN_STRINGS;
                    
                    //set the soul location and color
                    soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
                    soul.setColor(soul.PURPLE);
                    
                    //set up the next portion of the attack (purple)
                    SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
            
                    //spawn the middle row of projectiles
                    int y = box.getY() + 60; //soul.stringMinY + soul.DISTANCE_BETWEEN_STRINGS - DOG.getHeight()/2; //this is the y value needed to center the projectile on the middle string
                    for(int i = 0; i < 52; i++){
                        if(i % 5 != 0 && i % 5 != 1){ //we want to leave a gap every fourth projectile
                            pm.spawnProjectile(new Projectile(DOG, box.getX() - (DOG.getWidth()) * i, y));
                        }
                    }
            
                    //spawn the top row of projectiles
                    y -= soul.DISTANCE_BETWEEN_STRINGS; //move the y value up one string
                    for(int i = 0; i < 52; i++){
                        if(i % 10 == 3){ //we want to leave a gap every fourth projectile
                            pm.spawnProjectile(new Projectile(DOG, box.getX() - (DOG.getWidth()) * i, y));
                        }
                    }
                    
                    //spawn the bottom row of projectiles
                    y += soul.DISTANCE_BETWEEN_STRINGS * 2; //move the y value down two string
                    DOG.velocity.setR(-2.5); //make DOG go the opposite direction
                    for(int i = 0; i < 52; i++){
                        if(i % 10 == 6){ //we want to leave a gap every fourth projectile
                            pm.spawnProjectile(new Projectile(DOG, box.getX() + box.getWidth() + (DOG.getWidth()) * i, y));
                        }
                    }
                    DOG.velocity.setR(2.5); //put DOG back to normal
                    
                    projectileManagers.add(pm);
            }else if (age == 370 * 4){
                //make the strings invisible since the soul's no longer purple
                soul.strings.setVisible(false);
                
                //reset the soul's velocity
                soul.velocity = new Vector();
                if(Input.getRight()){
                    soul.velocity.add(new Vector(2, 0));
                }
                if(Input.getLeft()){
                    soul.velocity.add(new Vector(-2, 0));
                }
                if(Input.getUp()){
                    soul.velocity.add(new Vector(2, Math.PI * 3/2));
                }
                if(Input.getDown()){
                    soul.velocity.add(new Vector(-2, Math.PI * 3/2));
                }
                
                //make the projectiles from the last part invisible and non-damaging
                projectileManagers.get(0).setNotCollidable();
                projectileManagers.remove(0);
                
                //change the box size and soul color (this is very unusual ... but may actually be a better way of doing things...)
                box.changeSize(80, 200, false);
                soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
                soul.setColor(soul.YELLOW);
                        
                //set up the next portion of the attack (yellow)
                SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
                int y = 0;
                int projectileWidth = BREAKABLE_BOX.getWidth();
            
                for(int i = 0; i < 8; i++){
                    int collumn = (int)((Math.random() * 3) + 0.5);
                
                    if(collumn == 0){ //the collumn of breakable blocks was chosen to be the first one
                        //for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                            pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 0, y)); //spawn the breakable block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 1, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 2, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 3, y)); //spawn a solid block
                        
                            y -= projectileWidth;
                        //}   
                    } else if (collumn == 1){ //the collumn of breakable blocks was chosen to be the second one
                       // for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 0, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 1, y)); //spawn the breakable block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 2, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 3, y)); //spawn a solid block
                        
                            y -= projectileWidth;
                        //}
                    } else if (collumn == 2){ //the collumn of breakable blocks was chosen to be the third one
                        //for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 0, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 1, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 2, y)); //spawn the breakable block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 3, y)); //spawn a solid block
                            
                            y -= projectileWidth;
                        //}
                    } else if (collumn == 3){ //the collumn of breakable blocks was chosen to be the last one
                        //for(int j = 0; j < 4; j++){ //spawn four rows of blocks
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 0, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 1, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(SOLID_BOX, box.getX() + projectileWidth * 2, y)); //spawn a solid block
                            pm.spawnProjectile(new Projectile(BREAKABLE_BOX, box.getX() + projectileWidth * 3, y)); //spawn the breakable block
                        
                            y -= projectileWidth;
                        //}
                    }
                
                    y -= projectileWidth * 6;
                }
            
                projectileManagers.add(pm);
            } else if (age == 370 * 5){
                //make the projectiles from the last part invisible and non-damaging
                projectileManagers.get(0).setNotCollidable();
                projectileManagers.remove(0);
                
                //remove all pellets
                for(Projectile p : soul.pellets)
                    p.setVisible(false);
                
                //change the box size and soul color (this is very unusual ... but may actually be a better way of doing things...)
                soul.setColor(soul.RED);
                box.defaultAttackSize(true);
                
                //set up the next portion of the attack (red)
                SetVelocityPM pm = new SetVelocityPM(panel, Main.PROJECTILE_LAYER_VISIBLE);
                int centerX = box.getX() + box.getWidth()/2;
                int distanceToCenter = 400;
                int y = box.getY(); 
                int random = 0;
                
                for(int i = 0; i < 4; i++){ //number of pairs of collumns
                    y = box.getY();
                    random = (int)(Math.random() + 0.5); //randomize wheather or not to offset one column
                    
                    for(int j = 0; j < 18; j++){ //number of frisbees in each collumn
                        if((j/2 % 4) % 2 == random){
                            FRISBEE.velocity.setA(3);
                            pm.spawnProjectile(new Projectile(FRISBEE, centerX - distanceToCenter - FRISBEE.getWidth(), y));
                        } else {
                            FRISBEE.velocity.setA(-3);
                            pm.spawnProjectile(new Projectile(FRISBEE, centerX + distanceToCenter, y));
                        }
                        y += FRISBEE.getHeight();
                    }
                    distanceToCenter += 200; //distance between collumns
                }
            
                projectileManagers.add(pm);
            }
        }
    }
}
