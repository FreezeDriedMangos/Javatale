import javax.swing.JLayeredPane;
import java.util.ArrayList;
import javax.swing.JLayeredPane;
import java.util.ArrayList;

public class TrackingPM extends ProjectileManager{
    double maxRotation;
    //Soul soul;

    public TrackingPM(JLayeredPane pan, int l, double maxRot){
        super(new ArrayList<Projectile>(), pan, l);
        maxRotation = maxRot;
    }

    /*
    public TrackingPM(JLayeredPane pan, int l, double maxRot, Soul s){
    super(new ArrayList<Projectile>(), pan, l);
    maxRotation = maxRot;
    soul = s;
    }

    public void spawnProjectile(Projectile p){
    panel.add(p, layer);

    Velocity v = new Velocity();
    v.setAB(soul.getX() - p.getX(), soul.getY() - p.getY());
    double rotation = v.theta();

    if(rotation > maxRotation){
    rotation = maxRoation;
    } else if (rotation < -maxRotation){
    rotation = -maxRotation;
    }

    p.velocity.setTheta(rotation);
    projectiles.add(p);
    }
     */

    public void pathing(double delta, Soul soul){
        for(Projectile p : projectiles){
            Vector v = new Vector();
            v.setAB(-soul.getX() + p.getX(), -soul.getY() + p.getY());
            double rotation = v.theta();

            if(rotation > maxRotation){
                rotation = maxRotation;
            } else if (rotation < -maxRotation){
                rotation = -maxRotation;
            }

            p.velocity.setTheta(rotation);
            p.update(delta);
        }
    }
}
