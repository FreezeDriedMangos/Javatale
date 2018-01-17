import javax.swing.JLayeredPane;
import java.util.ArrayList;

public class SpiralPM extends ProjectileManager{
    Projectile template;
    double angleBetween;
    int frequency;
    double lastAngle = 0;

    double rootX = 0;
    double rootY = 0;

    public SpiralPM(JLayeredPane pan, int l, Projectile p, double x, double y, double theta, int f){
        super(new ArrayList<Projectile>(), pan, l);
        template = p;
        angleBetween = theta;
        frequency = f;

        rootX = x;
        rootY = y;
    }

    public void pathing(double delta, Soul soul){
        if(age % frequency == 0){
            lastAngle += angleBetween;

            Vector v = new Vector(template.velocity.r(), lastAngle);
            Projectile p = new Projectile(template, rootX, rootY);
            p.velocity = v;

            spawnProjectile(p);
        }

        for (Projectile p : projectiles){
            p.update(delta);
        }
    }
}
