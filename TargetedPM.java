import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.util.ArrayList;

public class TargetedPM extends ProjectileManager{
    Soul soul;
    
    public TargetedPM(JPanel pan, Soul s){
        super(new ArrayList<Projectile>(), pan);
        soul = s;
    }
    
    public TargetedPM(JLayeredPane pan, int l, Soul s){
        super(new ArrayList<Projectile>(), pan, l);
        soul = s;
    }

    public void spawnProjectile(Projectile p){
        Vector v = new Vector();
        v.setAB(soul.getX() - p.getX(), soul.getY() - p.getY());
        double rotation = v.theta();

        p.velocity.setTheta(rotation);
        
        super.spawnProjectile(p);
    }
    
    public void pathing(double delta, Soul soul){
        for(Projectile p : projectiles){
            p.update(delta);
        }
    }
}
