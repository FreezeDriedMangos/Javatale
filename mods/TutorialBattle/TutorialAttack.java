import javax.swing.*;

public class TutorialAttack extends Attack{
    public TutorialAttack(int attack, BoxLabel box, JLayeredPane panel, Soul soul){
        super(attack, box, panel);
        
        soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);  
        
        if(attack == 1){
            box.defaultAttackSize(true);
        } else if (attack >= 2 && attack != 5 && attack != 6 && attack != 7 && attack != 8){
            soul.setColor(soul.RED);
            box.defaultAttackSize(true);
            
            SetVelocityPM pm = new SetVelocityPM(panel);
            Projectile template = new Projectile("imgs/tutorial/lightning.png", 0,0, new Vector(1.5, 0), 1);
            pm.spawnProjectile(new Projectile(template, box.getX() - 30, soul.getY()));
            projectileManagers.add(pm);
        } else if(attack == 5){
            box.defaultAttackSize(true);
            soul.setColor(soul.BLUE);
            
            SetVelocityPM pm = new SetVelocityPM(panel);
            Projectile template = new Projectile("imgs/tutorial/lightning.png", 0,0, new Vector(1.5, 0), 1);
            pm.spawnProjectile(new Projectile(template, box.getX() - 60, box.getY() + box.getHeight() - template.getHeight()));
            projectileManagers.add(pm);
        } else if(attack == 6){
            box.defaultAttackSize(true);
            soul.setColor(soul.GREEN);
            
            projectileManagers.add(new GreenSoulPM(panel, "imgs/tutorial/lightning.png", new int[]{4,4,4,1,2}, 20, 1, 1, soul));
        } else if(attack == 7){
            box.defaultPurpleSize(true);
            soul.setColor(soul.PURPLE);
            
            SetVelocityPM pm = new SetVelocityPM(panel);
            Projectile template = new Projectile("imgs/tutorial/lightning.png", 0,0, new Vector(1.5, 0), 1);
            pm.spawnProjectile(new Projectile(template, box.getX() - 30, soul.getY()));
            projectileManagers.add(pm);
        } else if(attack == 8){
            box.defaultAttackSize(true);
            soul.setColor(soul.YELLOW);
            
            SetVelocityPM pm = new SetVelocityPM(panel);
            Projectile template = new Projectile("imgs/tutorial/lightning.png", 0,0, new Vector(-1.5, Math.PI * 3/2), 1);
            pm.spawnProjectile(new Projectile(template, soul.getX(), box.getY() - 30));
            projectileManagers.add(pm);
        }
    }
    
    public void tickUpdate(double delta, Soul soul){}
}
