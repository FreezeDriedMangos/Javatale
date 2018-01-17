import java.util.ArrayList;
import javax.swing.JLayeredPane;

public class PairedProjectilePM extends ProjectileManager{
    ArrayList<Projectile> hidden = new ArrayList<Projectile>();
    
    public PairedProjectilePM(ArrayList<Projectile> p, ArrayList<Projectile>h, JLayeredPane pan, int l){
        super(p, pan, l);
        hidden = h;
    }
    
    public void pathing(double delta, Soul soul){
        for(Projectile p : projectiles){
            p.update(delta);
        }
        
        for(Projectile p : hidden){
            p.update(delta);
        }
    }
    
    public void switchIndex(int index){
        Projectile temp = projectiles.get(index);
        temp.setVisible(false);
        projectiles.set(index, hidden.get(index));
        hidden.set(index, temp);
        hidden.get(index).setVisible(true);
    }
}