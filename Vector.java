public class Vector{
  private double a;
  private double b;
  private double theta;
  private double r;
  
  public Vector(double r, double theta){
    this.r = r;
    this.theta = theta;
    recalculateFromPolar();
  }
  
  public Vector(){
    a = 0;
    b = 0;
    theta = 0;
    r = 0;
  }
  
  public Vector(Vector v){
    a = v.a();
    b = v.b();
    theta = v.theta();
    r = v.r();
  }
  
  public double a(){ return a; }
  public double b(){ return b; }
  public double theta(){ return theta; }
  public double r(){ return r; }
  
  public void setA(double number){
    a = number;
    recalculateFromRectangular();
  }
  public void setB(double number){
    b = number;
    recalculateFromRectangular();
  }
  public void setTheta(double number){
   theta = number;
   recalculateFromPolar();
  }
  public void setR(double number){
   r = number;
   recalculateFromPolar();
  }
  
  public void setAB(double a, double b){
      this.a = a;
      this.b = b;
      
      recalculateFromRectangular();
  }
  
  private void recalculateFromRectangular(){
    r = Math.sqrt(a*a + b*b);
    theta = Math.atan2(b, a);
  }
  private void recalculateFromPolar(){
    a = r * Math.cos(theta);
    b = r * Math.sin(theta);
  }
  
  public void add(Vector v){
    a += v.a();
    b += v.b();
    recalculateFromRectangular();
  }
  public static Vector sum(Vector v1, Vector v2){
    Vector v = new Vector();
    v.setA(v1.a());
    v.setB(v1.b());
    v.add(v2);
    return v;
  }
  
  public void subtract(Vector v){
    a -= v.a();
    b -= v.b();
    recalculateFromRectangular();
  }
  
  public void multiply(Double d){
    a *= d;
    b *= d;
    recalculateFromRectangular();
  }
  public static Vector product(Vector v1, double d){
    Vector v = new Vector(v1);
    v.multiply(d);
    return v;
  }
}



















