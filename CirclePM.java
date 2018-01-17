import javax.swing.JLayeredPane;
import java.util.ArrayList;

public class CirclePM extends ProjectileManager{
    private double rotationalSpeed = 0;
    private Projectile template;
    private double contractSpeed = 2;
    private double startingDistance = 100;
    private int projectileCount = 10;
    public double rootX = 0;
    public double rootY = 0;

    public CirclePM(JLayeredPane pan, int l, Projectile proj, double x, double y, double rot, double dist, int count){
        super(new ArrayList<Projectile>(), pan, l);

        template = proj;
        contractSpeed = proj.velocity.r();
        rotationalSpeed = rot;
        startingDistance = dist;
        projectileCount = count;

        rootX = x;
        rootY = y;
    }

    public void spawnCircle(){
        double angleBetween = (2 * Math.PI) / projectileCount;
        for(int i = 0; i < projectileCount; i++){
            Vector v = new Vector(startingDistance, i * angleBetween);
            Projectile projectile = new Projectile(template, v.a() + rootX, v.b() + rootY);
            v.setR(v.r() * -1);
            projectile.velocity = v;
            spawnProjectile(projectile);
        }
    }

    public void pathing(double delta, Soul soul){
        for(Projectile p : projectiles){
            Vector v = new Vector(p.velocity.r(), p.velocity.theta());
            v.setR(v.r() * -1);
            //v now represents p’s coordinates relative to (rootX, rootY)

            v.setTheta(v.theta() + rotationalSpeed);
            p.setLocation(v.a() + rootX, v.b() + rootY);

            v.setR(v.r() * -1);
            p.velocity = v;

            p.update(delta);
        }
    }
}
