import javax.swing.JLayeredPane;
import java.util.ArrayList;

public class GravityPM extends ProjectileManager{
    public Vector gravity;
    public boolean collidesWithBox = false;
    public BoxLabel box;

    public GravityPM(ArrayList<Projectile> p, JLayeredPane pan, int l){
        super(p, pan, l);
        gravity = new Vector(- 0.2, Math.PI / 2);
    }

    public GravityPM(ArrayList<Projectile> p, JLayeredPane pan, int l, boolean collides, BoxLabel b){
        super(p, pan, l);
        collidesWithBox = collides;
        box = b;
    }

    public void pathing(double delta, Soul soul){
        for (Projectile p : projectiles){
            p.velocity.add(gravity);
            p.update(delta);

            if(collidesWithBox){
                if(/*is outside of box on sides*/true){
                    double deltaTheta = 2 * (-p.velocity.theta());
                    p.velocity.setTheta(p.velocity.theta() + deltaTheta);
                } else if (/*is outside of box on top or bottom*/false){
                    double deltaTheta = 2 * ((Math.PI/2) - p.velocity.theta());
                    p.velocity.setTheta(p.velocity.theta() + deltaTheta);
                }
            }
        }
    }
}
