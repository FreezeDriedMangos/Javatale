import java.awt.*;
import javax.swing.*;

public class BoxLabel extends DialogueLabel{
    int borderWidth;
    private int movedX = 0;
    private int movedY = 0;
    Battle battle;

    public BoxLabel(){
        super();
        //super.setBackgroundColor(new Color(0, 0, 0, 0));
        super.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 5), BorderFactory.createEmptyBorder(13, 13, 13, 13)));
        super.setLocation(33, 250);
        super.setSize(576, 140);
        super.setFont(Main.MAIN_FONT);

        borderWidth = 5;
    }

    public BoxLabel(Battle b){
        this();
        battle = b;
    }

    public void retainCollisionLabel(CollisionLabel label){
        int labelX = label.getX();
        int thisX = this.getX() + borderWidth;
        int labelY = label.getY();
        int thisY = this.getY() + borderWidth;

        int thisWidth = this.getWidth() - 2*borderWidth;
        int thisHeight = this.getHeight() - 2*borderWidth;

        if(labelX < thisX){
            label.setLocation(thisX, labelY);
        } else if (labelX + label.getWidth() > thisX + thisWidth){
            label.setLocation((thisX + thisWidth) - label.getWidth(), labelY);
        }

        labelX = label.getX();

        if(labelY < thisY){
            label.setLocation(labelX, thisY);
        } else if (labelY + label.getHeight() > thisY + thisHeight){
            label.setLocation(labelX, (thisY + thisHeight) - label.getHeight());
        }
    }

    public void changeSize(int w, int h, boolean sleep){
        if(sleep)
            changeSize(w, h, 5);
        else 
            changeSize(w, h, 0);
    }

    public void changeSize(int w, int h, int sleep){
        moveLocation(-movedX, -movedY);
        
        if(w % 2 == 1){
            w += 1; 
            System.out.println("BoxLabel, changeSize: WARNING: Please use even values for width and height.");
        }if(h % 2 == 1){
            h += 1;
            System.out.println("BoxLabel, changeSize: WARNING: Please use even values for width and height.");
        }
 
        if(w > getWidth()){
            int oldX = getX();
            for (int i = getWidth() - w; i <= 0; i += 2){
                setSize(i + w, getHeight());
                oldX--;
                setLocation(oldX, getY());
                battle.sleep(sleep);
            } 
        } else {
            int oldX = getX();
            for (int i = getWidth() - w; i >= 0; i -= 2){
                setSize(i + w, getHeight());
                oldX++;
                setLocation(oldX, getY());
                battle.sleep(sleep);
            } 
        }
        
        if(h > getHeight()){
            int oldY = getY();
            for (int i = getHeight() - h; i <= 0; i++){
                setSize(getWidth(), i + h);
                oldY--;
                setLocation(getX(), oldY);
                battle.sleep(sleep);
            }
        } else {
            int oldY = getY();
            for (int i = getHeight() - h; i >= 0; i--){
                setSize(getWidth(), i + h);
                oldY++;
                setLocation(getX(), oldY);
                battle.sleep(sleep);
            }
        }
    }
    
    public void moveLocation(int deltaX, int deltaY){
        movedX += deltaX;
        movedY += deltaY;
        
        int oldX = getX();
        int oldY = getY();
        
        if(deltaX > 0)
            for(int i = 1; i <= deltaX; i++)
                setLocation(oldX + i, oldY);
        else
            for(int i = -1; i >= deltaX; i--)
                setLocation(oldX + i, oldY);
        
        int newX = getX();
        
        if(deltaY > 0)
            for(int i = 1; i <= deltaY; i++)
                setLocation(newX, oldY + i);
        else
            for(int i = -1; i >= deltaY; i--)
                setLocation(newX, oldY + i);
    }

    public void defaultAttackSize(boolean sleep){
        changeSize(186, 190, sleep);
    }

    public void dialogueBoxSize(boolean sleep){
        changeSize(576, 140, sleep);
    }

    public void defaultPurpleSize(boolean sleep){
        changeSize(246, 140, sleep);
    }

    public int getX(){ return super.getX(); }

    public int getY(){ return super.getY(); }

    public int getWidth(){ return super.getWidth(); }

    public int getHeight(){ return super.getHeight(); }
}



