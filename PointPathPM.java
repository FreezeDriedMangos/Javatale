import javax.swing.JLayeredPane;
import java.util.ArrayList;
import java.awt.Point;

public class PointPathPM extends ProjectileManager{
    ArrayList<Point> points;
    ArrayList indexes;

    public PointPathPM (ArrayList<Projectile> proj, JLayeredPane pan, int l, ArrayList<Point> p){
        super(proj, pan, l);

        for(int i = 0; i < proj.size(); i++){
            indexes.add(-1);
        }
    }

    public void spawnProjectile(Projectile p){
        super.spawnProjectile(p);
        indexes.add(-1);

        Point nextPoint = points.get(0);

        double speed = p.velocity.r();
        p.velocity.setAB(nextPoint.getX(), nextPoint.getY());
        p.velocity.setR(speed);
    }

    public void pathing(double delta, Soul soul){
        for(int i = 0; i < projectiles.size(); i++){
            Projectile p = projectiles.get(i);
            int index = (Integer)(indexes.get(i));

            if(index < points.size() - 1){
                Point nextPoint = points.get(index + 1);

                double speed = p.velocity.r();
                p.velocity.setAB(nextPoint.getX(), nextPoint.getY());
                p.velocity.setR(speed);

                p.update(delta);
            } else {
                projectiles.remove(i);
                indexes.remove(i);
            }
        }
    }
}
