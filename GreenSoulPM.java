import javax.swing.JLayeredPane;
import java.util.ArrayList;

public class GreenSoulPM extends ProjectileManager{
    public static final int LEFT = 1; //these correspond to the side of the soul this projectile will hit (the opposite of the direction it travels)
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    public static final int TOP = 4;
    public static final int NONE = 0;
    Projectile template;
    int distanceBetween;
    String filePath = "";
    String closeFilePath = "";
    
    Projectile TOP_CLOSE, BOTTOM_CLOSE, LEFT_CLOSE;

    //template should be facing to the left (the equivalent of a right projectile)
    public GreenSoulPM(JLayeredPane pan, String filePath, int[] directions, int dist, int damage, double sp, Soul soul){
        super(new ArrayList<Projectile>(), pan, Main.PROJECTILE_LAYER_VISIBLE); 
        
        this.filePath = filePath;
        closeFilePath = filePath.substring(0, filePath.length() - 4) + "Red.png";
        template = new Projectile(filePath, 0, 0, new Vector(sp, Math.PI), damage);
        distanceBetween = dist;
        
        double speed = sp;
        int currentDistance = (int)(83 * speed);
        int centerX = (soul.getX() + (soul.getWidth() / 2))  -  (template.getWidth() / 2); //the x value that centers a projectile on the soul
        int centerY = (soul.getY() + (soul.getHeight() / 2)) - (template.getHeight() / 2); //the y value that centers a projectile on the soul
        
        for (int i : directions){
            if(i == 1){ //coming from the left
                int soulBoundry = soul.getX();
                
                Projectile p = new Projectile(template, soulBoundry - currentDistance - template.getWidth(), centerY);
                p.rotate(Math.PI);
                p.velocity.setAB(speed, 0);
                
                spawnProjectile(p);
                
                //THIS ONE IS BROKEN
            } else if (i == 2){ //coming from the right
                int soulBoundry = soul.getX() + soul.getWidth();
                
                Projectile p = new Projectile(template, soulBoundry + currentDistance, centerY);
                p.velocity.setAB(-speed, 0);
                
                spawnProjectile(p);
            } else if (i == 3){ //coming from the bottom
                int soulBoundry = soul.getY() + soul.getHeight();
                
                Projectile p = new Projectile(template, centerX, soulBoundry + currentDistance);
                p.rotate(Math.PI / 2);
                p.velocity.setAB(0, -speed);
                
                spawnProjectile(p);
            } else if (i == 4){ //coming from the top
                int soulBoundry = soul.getY();
                
                Projectile p = new Projectile(template, centerX, soulBoundry - currentDistance - template.getHeight());
                p.rotate(Math.PI * 3/2);
                p.velocity.setAB(0, speed);
                
                spawnProjectile(p);
                
                //THIS ONE IS BROKEN, TOO
            } 
            
            //LATER, MAKE NEGATIVES IN THE DIRECTION ARRAY YELLOW ARROWS

            currentDistance += distanceBetween + template.getWidth();
        }
        
        //set up the templates for the close projectiles
        TOP_CLOSE = new Projectile(closeFilePath, 0, 0);
        TOP_CLOSE.rotate(Math.PI * 3/2);
        BOTTOM_CLOSE = new Projectile(closeFilePath, 0, 0);
        BOTTOM_CLOSE.rotate(Math.PI / 2);
        LEFT_CLOSE = new Projectile(closeFilePath, 0, 0);
        LEFT_CLOSE.rotate(Math.PI);
    }
    
    public void pathing(double delta, Soul soul){
        for(Projectile p : projectiles){ 
            p.update(delta);
        }
        
        if(projectiles.size() > 0){
            Projectile p = projectiles.get(0);
            
            if(p.velocity.a() > 0){
                p.setImage(LEFT_CLOSE.image);
                //p.rotate(Math.PI);
            } else if(p.velocity.a() < 0){
                p.setImage("mods/" + Main.battleLoaded + "/" + closeFilePath);
            } else if(p.velocity.b() > 0){
                p.setImage(TOP_CLOSE.image);
                //p.rotate(Math.PI * 3/2);
            } else if(p.velocity.b() < 0){
                p.setImage(BOTTOM_CLOSE.image);
                //p.rotate(Math.PI / 2);
            }
        }
    } 

    public void setSpeed(int index, double newSpeed, Soul soul){
        Projectile p = projectiles.get(index);
        int distanceToSoul = 0;

        if(p.velocity.theta() == 0){
            distanceToSoul = soul.getX() - (p.getWidth() + p.getX());
        } else if(p.velocity.theta() == Math.PI / 2){
            distanceToSoul = soul.getY() + soul.getHeight() - p.getY();
        } else if(p.velocity.theta() == Math.PI){
            distanceToSoul = soul.getX() + soul.getWidth() - p.getX();
        } else if(p.velocity.theta() == Math.PI * 3/2){
            distanceToSoul = soul.getY() - (p.getHeight() + p.getY());
        }

        double time = distanceToSoul / p.velocity.r();
        double newDistance = newSpeed * time;
        double deltaDistance = newDistance - distanceToSoul;

        if(p.velocity.theta() == 0){
            p.moveLocation(deltaDistance, 0);
        } else if(p.velocity.theta() == Math.PI / 2){
            p.moveLocation(0, deltaDistance);
        } else if(p.velocity.theta() == Math.PI){
            p.moveLocation(deltaDistance, 0);
        } else if(p.velocity.theta() == Math.PI * 3/2){
            p.moveLocation(0, deltaDistance);
        }

        p.velocity.setR(newSpeed);

    }

    public boolean isColliding(Soul soul){
        //check distance from the edge of the soul's collision box, if it's <= 29 and the sheild is facing the right direction, it hits the sheild.
        //if it's <= 0, it hits the soul
        int soulX = soul.getX();
        int soulY = soul.getY();
        ArrayList remove = new ArrayList();
        boolean collided = false;
        
        for(Projectile p : projectiles){
            int distanceToSoul = 100;
            int projectileLocation = 0;
            int soulBoundry = 0;
            
            if(p.velocity.a() > 0){ //coming from the left
                projectileLocation = p.getX() + p.getWidth();
                soulBoundry = soulX + 4;
                distanceToSoul = soulBoundry - projectileLocation;
            } else if (p.velocity.a() < 0){ //coming from the right
                projectileLocation = p.getX();
                soulBoundry = soulX + 12;
                distanceToSoul = projectileLocation - soulBoundry;
            } else if (p.velocity.b() > 0){ //coming from the top
                projectileLocation = p.getY() + p.getHeight();
                soulBoundry = soulY + 4;
                distanceToSoul = soulBoundry - projectileLocation;
            }  else if (p.velocity.b() < 0){ //coming from the bottom
                projectileLocation = p.getY();
                soulBoundry = soulY + 12;
                distanceToSoul = projectileLocation - soulBoundry;
            }
            
            boolean hitShield = false;
            if(0 < distanceToSoul && distanceToSoul <= 29){ //possible sheild collision=
                int shieldDirection = soul.shieldDirection;
                
                if(p.velocity.a() > 0 && shieldDirection == 4) //coming from the left
                    hitShield = true;
                else if (p.velocity.a() < 0 && shieldDirection == 2) //coming from the right
                    hitShield = true;
                else if (p.velocity.b() > 0 && shieldDirection == 1) //coming from the top
                    hitShield = true;
                else if (p.velocity.b() < 0 && shieldDirection == 3) //coming from the bottom
                    hitShield = true;
                
                if (hitShield){
                    remove.add(p);
                    p.setVisible(false);
                    SoundPlayer.play("sounds/ding.wav");
                    soul.shield.setImage("imgs/soul/shieldHit" + soul.shieldDirection + ".png");
                    
                    final Soul s = soul;
                    Thread t = new Thread(){
                        public void run(){
                            try{
                                Thread.sleep(100);
                                s.shield.setImage("imgs/soul/shield" + s.shieldDirection + ".png");
                            } catch(Exception e){}
                        }
                    };
                    
                    t.start();
                }
            }
            
            if (!hitShield && distanceToSoul <= 0){
                //hit soul
                p.setVisible(false);
                remove.add(p);
                
                if(soul.invincibilityTimer <= 0){
                    soul.hp -= p.damage;
                    SoundPlayer.play("sounds/hit.wav");
                    collided = true;
                    
                    if(p.activatesInvincibility){
                        soul.invincibilityTimer = Soul.MAX_INVINCIBILITY_FRAMES;
                        soul.setInvicibilityImage(true);
                    }
                }
            }
        }
        
        for(Object o : remove)
            projectiles.remove(o);
            
        return collided;
    }
}
