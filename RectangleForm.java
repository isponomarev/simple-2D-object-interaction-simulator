import java.util.Objects;

public class RectangleForm extends Figure {
    private double width, height;

    public RectangleForm(int m, double x, double y, double vx, double vy, double width, double height) {
        super(m, x, y, vx, vy);
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double[] getMainParameters() {
        double[] arr = {width/2, height/2};
        return arr;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "#" + getId() + " RECTANGLE: " + "\n" +
                " [x=" + getX() + ", y=" + getY() +
                ", width=" + width + ", height=" + height + "]" + "\n" +
                " Speed: Vx=" + getVx() + ", Vy=" + getVy() + "\n" +
                " Weight=" + getM() + ", Area=" + getArea();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RectangleForm)) return false;
        RectangleForm that = (RectangleForm) o;
        return Integer.compare(that.getM(), getM()) == 0 &&
                Double.compare(that.getX(), getX()) == 0 &&
                Double.compare(that.getY(), getY()) == 0 &&
                Double.compare(that.getVx(), getVx()) == 0 &&
                Double.compare(that.getVy(), getVy()) == 0 &&
                Double.compare(that.width, width) == 0 &&
                Double.compare(that.height, height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getM(), super.getX(), super.getY(), super.getVx(), super.getVy(), width, height);
    }
}
