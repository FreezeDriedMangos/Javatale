import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.util.ArrayList;

public class SetVelocityPM extends ProjectileManager{
    public SetVelocityPM(ArrayList<Projectile> p, JPanel pan){
        super(p, pan);
    }
    
    public SetVelocityPM(ArrayList<Projectile> p, JLayeredPane pan, int l){
        super(p, pan, l);
    }
    
    public SetVelocityPM(JLayeredPane pan, int l){
        super(new ArrayList<Projectile>(), pan, l);
    }
    
    public SetVelocityPM(JLayeredPane pan){
        super(new ArrayList<Projectile>(), pan, Main.PROJECTILE_LAYER_VISIBLE);
    }
    
    public void pathing(double delta, Soul soul){
        for(Projectile p : projectiles){
            p.update(delta);
        }
    }
}