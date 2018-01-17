import javax.swing.*;

public class HardGreenBattle extends Battle{
    public HardGreenBattle (JFrame frame){
        super(frame);
    }
    
    public void setUpMonster(){
        monsterFilePath = "";
        monsterHitFilePath = "";
        monster.setSize(226, 214);
        monster.setLocation(320 - 113, 30);
        
        monsterDefense = 1;
        monsterAttack = 10;
        monsterHealth = 70;
        monsterMaxHealth = monsterHealth;
        monsterName = "The Computer";
        
        checkText = "* This is the tutorial.";
        actOption1 = "Talk";
    }
    
    public void defineSoulStats(){
        soul.lv = 1;
        soul.hp = 20;
        soul.maxHP = 20;
        soul.attack = 2;
    }
    
    public void setUpItems(){
        items.add(new Item("Pie", 5, "You just healed!"));
    }
    
    public String selectTrack(){
        return "sounds/dontForgetToDelete/spearOfJustice.wav";
    }
    
    public void setText(){
        box.setText("* Get ready...");
    }
    
    public void setDialogue(){}
    
    public void setUpAttack(){
        attackTimer = ATTACK_TIMER_MAX * 2;
        attack = new HardGreenAttack(turn, box, soulPanel, soul);
    }
    
    public void actOption1Selected(){}
    public void actOption2Selected(){}
    public void actOption3Selected(){}
    public void fleeSelected(){}
    public void spareSelected(){}
    
    public void introAnimation(){}
    public void endAnimation(){}
}