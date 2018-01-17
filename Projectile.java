import javax.swing.*;

public class Projectile extends CollisionLabel{
    public Vector velocity = new Vector();
    public boolean removeOnCollision = true;
    public boolean activatesInvincibility = true;
    public boolean destroyable = true;
    
    public int damage = 2;
    public boolean orange = false;
    public boolean blue = false;
    public boolean white = true;
    
    public int age = 0;
    
    public Projectile(){ super(); }
    
    public Projectile(Projectile p, double x, double y){
        super(p, (int)x, (int)y);
        //this.setLocation(x, y);
        
        velocity = new Vector(p.velocity.r(), p.velocity.theta());
        removeOnCollision = p.removeOnCollision;
        activatesInvincibility = p.activatesInvincibility;
        
        orange = p.orange;
        blue = p.blue;
        white = p.white;
        damage = p.damage;
    }
    
    public Projectile(Projectile p, int x, int y){
        this(p, (double)x, (double)y);
    }
    
    public Projectile(String filePath, int x, int y){
        super("mods/" + Main.battleLoaded + "/" + filePath, x, y); //adjust the filepath so that the root directory is the main project folder, not the mod folder
    }
    
    public Projectile(String filePath, int x, int y, Vector v){
        super("mods/" + Main.battleLoaded + "/" + filePath, x, y); //adjust the filepath so that the root directory is the main project folder, not the mod folder
        velocity = v;
    }
    
    public Projectile(String filePath, int x, int y, Vector v, int d){
        super("mods/" + Main.battleLoaded + "/" + filePath, x, y); //adjust the filepath so that the root directory is the main project folder, not the mod folder
        velocity = v;
        damage = d;
    }
    
    public Projectile(String filePath, int x, int y, Vector v, boolean b){ //this is to be used only for pellets (yellow soul). the boolean is used only to differentiate this constructor 
        super(filePath, x, y);
        velocity = v;
    }
    
    public void update(double delta){
        super.moveLocation((velocity.a() * delta), (velocity.b() * delta));
        age++;
    }
    
    public void changeColor(int color){
        if(color == 0){
            blue = true;
            orange = false;
            white = false;
        } else if (color == 1){
            blue = false;
            orange = true;
            white = false;
        } else {
            blue = false;
            orange = false;
            white = true;
        }
    }
    
    public boolean isColliding(Soul soul){ //returns true if there's any overlap between this and the parameter
        if(white){
            return super.isColliding(soul);
        } else if(orange && (soul.velocity.a() == 0 && soul.velocity.b() == 0)){
            return super.isColliding(soul);
        } else if(blue && (soul.velocity.a() != 0 && soul.velocity.b() != 0)){
            return super.isColliding(soul);
        }
        
        return super.isColliding(soul);
    }
    
    public void setNotCollidable(){
        setVisible(false);
        damage = 0;
        activatesInvincibility = false;
    }
}