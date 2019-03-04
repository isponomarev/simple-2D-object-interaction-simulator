import java.util.Objects;

public class CircleForm extends Figure {
    private double radius;

    public CircleForm(int m, double x, double y, double vx, double vy, double radius) {
        super(m, x, y, vx, vy);
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.round(Math.pow(radius, 2) * Math.PI);
    }

    @Override
    public double[] getMainParameters() {
        double[] arr = {radius, radius};
        return arr;
    }

    @Override
    boolean isInside(double x, double y) {
        // (x1 - x2)^2 + (y1 - y2)^2 < R^2
        return Math.sqrt(Math.pow((x - this.getX()), 2)
                + Math.pow((y - this.getY()), 2)) < this.radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "#" + getId() + " CIRCLE: " + "\n" +
                " [x=" + getRoundingValue(getX()) + ", y=" + getRoundingValue(getY()) +
                ", radius=" + radius + "]" + "\n" +
                " Speed: Vx=" + getRoundingValue(getVx()) + ", Vy=" + getRoundingValue(getVy()) + "\n" +
                " Weight=" + getM() + ", Area=" + getArea();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CircleForm)) return false;
        CircleForm that = (CircleForm) o;
        return Integer.compare(that.getM(), getM()) == 0 &&
                Double.compare(that.getX(), getX()) == 0 &&
                Double.compare(that.getY(), getY()) == 0 &&
                Double.compare(that.getVx(), getVx()) == 0 &&
                Double.compare(that.getVy(), getVy()) == 0 &&
                Double.compare(that.radius, radius) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getM(), super.getX(), super.getY(), super.getVx(), super.getVy(), radius);
    }
}
