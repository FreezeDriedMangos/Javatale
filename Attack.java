import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.awt.geom.Area;

public abstract class Attack{
    ArrayList<ProjectileManager> projectileManagers = new ArrayList<ProjectileManager>();
    int age = 0;
    int attack;
    JLayeredPane panel;
    BoxLabel box;
    
    JLabel monster;
    
    public boolean variation = false;
    
    public Attack(int attack, BoxLabel b, JLayeredPane p){
        this.attack = attack;
        box = b;
        panel = p;
    }
    
    public void update(double delta, Soul soul){
        tickUpdate(delta, soul);
        
        for(ProjectileManager pm : projectileManagers){
            pm.update(delta, soul);
        }
        
        age++;
    }
    
    public boolean isColliding(Soul soul){
        boolean result = false;
        
        for(ProjectileManager pm : projectileManagers){
            if(pm.isColliding(soul)){
                result = true;
                break;
            }
        }
        
        if(soul.color[soul.YELLOW]){
            ArrayList<Projectile> removePellet = new ArrayList<Projectile>();
            
            for(ProjectileManager pm : projectileManagers){
                ArrayList remove = new ArrayList();
                
                for(Projectile p : pm.projectiles){
                    for(Projectile pellet : soul.pellets){
                        if(p.isColliding(pellet)){
                            if(pm.pelletCollision(p)){
                                p.setVisible(false);
                                remove.add(p);
                            }
                            
                            pellet.setVisible(false);
                            //removePellet.add(pellet);
                            pellet.area = new Area();
                            pellet.velocity = new Vector();
                        }
                    }
                }
                
                for(Object p : remove){
                    pm.projectiles.remove(p);
                }
            }
            //for(Projectile p : removePellet){
            //    soul.pellets.remove(p);
            //}
        }
        
        
        return result;
    }
    
    public void setInvisible(){
        for(ProjectileManager pm : projectileManagers){
            pm.setInvisible();
        }
    }
    
    public abstract void tickUpdate(double delta, Soul soul);
}