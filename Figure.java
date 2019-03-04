public abstract class Figure {
    private int id, m;                            // m - weight
    private double x, y, vx, vy;                  // x,y - center coordinates, vx,vy - velocity vectors
    private static int nextId;                    // figure counter

    public Figure(int m, double x, double y, double vx, double vy) {
        this.id = nextId++;
        this.m = m;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    abstract double getArea();
    abstract double[] getMainParameters();              // parameters for finding the center of gravity of figure
    abstract boolean isInside(double x, double y);      // point inside the figure?

    public void move(double time){
        this.x += this.vx * time;
        this.y += this.vy * time;
    }

    public void rebound(Figure other){                  // rebound figure from another figure
        double firstX = this.vx, firstY = this.vy;
        // Vx1' = 2 * m2 * Vx2 + (m1 - m2) * Vx1 / (m1 + m2)
        this.vx = (2 * other.m * other.vx + (this.m - other.m) * this.vx) / (this.m + other.m);
        this.vy = (2 * other.m * other.vy + (this.m - other.m) * this.vy) / (this.m + other.m);
        // Vx2' = 2 * m1 * Vx1 + (m2 - m1) * Vx2 / (m2 + m1)
        other.vx = (2 * this.m * firstX + (other.m - this.m) * other.vx) / (other.m + this.m);
        other.vy = (2 * this.m * firstY + (other.m - this.m) * other.vy) / (other.m + this.m);
    }

    public boolean getCollision(Figure other){        // check 9 points of figure (center, N, NE, E, SE, S, SW, W, NW)
        double paramX = other.getMainParameters()[0];
        double paramY = other.getMainParameters()[1];
        return this.isInside(other.x+paramX, other.y+paramY) ||
                this.isInside(other.x+paramX, other.y-paramY) ||
                this.isInside(other.x-paramX, other.y+paramY) ||
                this.isInside(other.x-paramX, other.y-paramY) ||
                this.isInside(other.x, other.y+paramY) ||
                this.isInside(other.x, other.y-paramY) ||
                this.isInside(other.x+paramX, other.y) ||
                this.isInside(other.x-paramX, other.y) ||
                this.isInside(other.x, other.y);
    }

    public int getId() {
        return id;
    }

    public int getM() {
        return m;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getRoundingValue(double value){
        return Math.round(value * 100d)/100d;
    }

}
