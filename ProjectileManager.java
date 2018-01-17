
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.util.ArrayList;

public abstract class ProjectileManager{
    ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    JLayeredPane layeredPanel;
    JPanel panel;
    int layer = -1;
    int age = 0;
    int attack = 0;
    
    public ProjectileManager(ArrayList<Projectile> p, JPanel pan){
        panel = pan;
        projectiles = p;
        
        for(Projectile proj : projectiles){
            panel.add(proj, null, layer);
            proj.moveCollisionBox(pan.getX(), pan.getY());
        }
    }
    
    public ProjectileManager(ArrayList<Projectile> p, JLayeredPane pan, int l){
        layeredPanel = pan;
        projectiles = p;
        layer = l;
        
        for(Projectile proj : projectiles){
            layeredPanel.add(proj, null, layer);
        }
    }
    
    public final void update(double delta, Soul soul){
        pathing(delta, soul);
        age++;
    }
    
    public abstract void pathing(double delta, Soul soul);
    
    public void spawnProjectile(Projectile p){
        projectiles.add(p);
        if(layer != -1){
            layeredPanel.add(p, null, layer);
        } else {
            panel.add(p);
            p.moveCollisionBox(panel.getX(), panel.getY());
        }
    }
    
    public boolean isColliding(Soul soul){/*
        if(soul.color[soul.GREEN]){
            ArrayList remove = new ArrayList();
            for(Projectile p : projectiles){
                if(p.isColliding(soul.shield)){
                    remove.add(p);
                    p.setVisible(false);
                    SoundPlayer.play("sounds/ding.wav", false);
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
            
            for(Object o : remove)
                projectiles.remove(o);
        } */
        
        boolean collided = false;
        ArrayList remove = new ArrayList();
        
        for (Projectile p : projectiles){
            if(soul.isColliding(p)){
                if(soul.invincibilityTimer <= 0){
                    if(p.white || (p.orange && soul.velocity.r() == 0) || (p.blue && soul.velocity.r() != 0)){
                        soul.hp -= p.damage;
                        SoundPlayer.play("sounds/hit.wav");
                    
                        if(p.activatesInvincibility){
                            soul.invincibilityTimer = Soul.MAX_INVINCIBILITY_FRAMES;
                            soul.setInvicibilityImage(true);
                        }
                    
                        if(p.removeOnCollision){
                            p.setVisible(false);
                            remove.add(p);
                        }
                    }
                }
                collided = true;
            }
        }
        
        for(Object o : remove){
            projectiles.remove(o);
        }
        return collided;
    }
    
    public void setInvisible(){
        for (Projectile p : projectiles)
            p.setVisible(false);
    }
    
    public boolean pelletCollision(Projectile p){
        if(p.removeOnCollision){
            p.setVisible(false);
            return true;
        } else {
            return false;
        }
    }
    
    public void setNotCollidable(){
        for (Projectile p : projectiles){
            p.setNotCollidable();
        }
    }
}