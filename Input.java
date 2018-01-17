public class Input{
    private static boolean right = false;
    private static boolean rightRisingEdge = false;
    private static boolean rightFallingEdge = false;
    private static boolean left = false;
    private static boolean leftRisingEdge = false;
    private static boolean leftFallingEdge = false;
    private static boolean up = false;
    private static boolean upRisingEdge = false;
    private static boolean upFallingEdge = false;
    private static boolean down = false;
    private static boolean downRisingEdge = false;
    private static boolean downFallingEdge = false;
    
    private static boolean z = false;
    private static boolean zRisingEdge = true;
    
    private static boolean x = false;
    private static boolean xRisingEdge = true;
    
    public static void setRight(boolean bool){
        if(!right && right != bool){
            rightRisingEdge = true;
        } else if (right != bool){
            rightFallingEdge = true;
        }
        
        right = bool;
    }
    public static void setLeft(boolean bool){
        if(!left && left != bool){
            leftRisingEdge = true;
        } else if (left != bool){
            leftFallingEdge = true;
        }
        
        left = bool;
    }
    public static void setUp(boolean bool){
        if(!up && up != bool){
            upRisingEdge = true;
        } else if (up != bool){
            upFallingEdge = true;
        }
        
        up = bool;
    }
    public static void setDown(boolean bool){
        if(!down && down != bool){
            downRisingEdge = true;
        } else if (down != bool){
            downFallingEdge = true;
        }
        
        down = bool;
    }
    public static void setZ(boolean bool){
        if(!z && z != bool){
            zRisingEdge = true;
        }
        
        z = bool;
    }
    public static void setX(boolean bool){
        if(!x && x != bool){
            xRisingEdge = true;
        }
        
        x = bool;
    }
    
    public static boolean getRightRisingEdge(){
        boolean result = rightRisingEdge;
        rightRisingEdge = false;
        return result;
    }
    public static boolean getLeftRisingEdge(){
        boolean result = leftRisingEdge;
        leftRisingEdge = false;
        return result;
    }
    public static boolean getUpRisingEdge(){
        boolean result = upRisingEdge;
        upRisingEdge = false;
        return result;
    }
    public static boolean getDownRisingEdge(){
        boolean result = downRisingEdge;
        downRisingEdge = false;
        return result;
    }
    public static boolean getZRisingEdge(){
        boolean result = zRisingEdge;
        zRisingEdge = false;
        return result;
    }
    public static boolean getXRisingEdge(){
        boolean result = xRisingEdge;
        xRisingEdge = false;
        return result;
    }
    
    public static boolean getRightFallingEdge(){
        boolean result = rightFallingEdge;
        rightFallingEdge = false;
        return result;
    }
    public static boolean getLeftFallingEdge(){
        boolean result = leftFallingEdge;
        leftFallingEdge = false;
        return result;
    }
    public static boolean getUpFallingEdge(){
        boolean result = upFallingEdge;
        upFallingEdge = false;
        return result;
    }
    public static boolean getDownFallingEdge(){
        boolean result = downFallingEdge;
        downFallingEdge = false;
        return result;
    }
    
    public static boolean getRight(){
        return right;
    }
    public static boolean getLeft(){
        return left;
    }
    public static boolean getUp(){
        return up;
    }
    public static boolean getDown(){
        return down;
    }
    
    public static void clear(){
        right = false;
        rightRisingEdge = false;
        rightFallingEdge = false;
        left = false;
        leftRisingEdge = false;
        leftFallingEdge = false;
        up = false;
        upRisingEdge = false;
        upFallingEdge = false;
        down = false;
        downRisingEdge = false;
        downFallingEdge = false;
        
        z = false;
        zRisingEdge = false;
        x = false;
        xRisingEdge = false;
    }
}





