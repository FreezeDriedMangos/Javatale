import java.util.ArrayList;
import javax.swing.JLayeredPane;

public class WavePM extends ProjectileManager{
    public boolean isVertical;
    public double horizontalOffset, verticalOffset, stretch, amplitude;

    public WavePM(ArrayList<Projectile> p, JLayeredPane pan, int l, double horizOff, double vertOff, double amp, double st, boolean vertical){
        super(p, pan, l);
        horizontalOffset = horizOff;
        verticalOffset = vertOff;
        amplitude = amp;
        stretch = st;
        isVertical = vertical;
    }
    
    public void pathing(double delta, Soul soul){
        for(Projectile p : projectiles){
            if (!isVertical){
                p.moveLocation(p.velocity.a(), 0);
                
                double y = verticalOffset + amplitude * Math.sin(stretch * p.getX() - horizontalOffset);
                p.setLocation(p.getX(), y);
            } else {
                p.moveLocation(0, p.velocity.b());
                
                double x = horizontalOffset + amplitude * Math.sin(stretch * p.getY() - verticalOffset);
                p.setLocation(p.getY(), x);
            }
        }
    }
}
