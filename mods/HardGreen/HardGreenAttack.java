import javax.swing.*;

public class HardGreenAttack extends Attack{
    public HardGreenAttack(int attack, BoxLabel box, JLayeredPane panel, Soul soul){
        super(attack, box, panel);
        
        soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, (box.getY() + box.getHeight() / 2 - soul.getHeight() / 2) - 30);  
        box.defaultAttackSize(true);
        soul.setColor(soul.GREEN);
        
        if(attack % 2 == 0){
            int damage = 1;
            double speed = 2.5;
            int[] directions = new int[60];
            for(int i = 0; i < 60; i++){
                directions[i] = (int)((Math.random() * 3) + 1.5);
            }
            
            GreenSoulPM pm = new GreenSoulPM(panel, "imgs/arrow.png", directions, 10, damage, speed, soul);
            projectileManagers.add(pm);
        } else {
            int damage = 1;
            double speed = 1.2;
            int[] directions = new int[40];
            for(int i = 0; i < 40; i++){
                directions[i] = (int)((Math.random() * 3) + 1.5);
            }
            
            GreenSoulPM pm = new GreenSoulPM(panel, "imgs/arrow.png", directions, -10, damage, speed, soul);
            projectileManagers.add(pm);
        }
    }
    
    public void tickUpdate(double delta, Soul soul){}
}
