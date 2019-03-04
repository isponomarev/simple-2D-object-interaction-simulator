import java.util.HashSet;

public class Field {
    private int fieldWidth, fieldHeight;
    private RectangleForm borderLeft, borderRight, borderTop, borderBottom;
    private int borderSize = 2;
    private static final int BORDER_WEIGHT = Integer.MAX_VALUE;
    private HashSet<Figure> fieldObjects = new HashSet<>();

    public Field(int width, int height) {
        this.fieldWidth = width;
        this.fieldHeight = height;
        this.borderTop = new RectangleForm(BORDER_WEIGHT, 0, height, 0, 0, width, borderSize);
        this.borderBottom = new RectangleForm(BORDER_WEIGHT, 0, borderSize, 0, 0, width, borderSize);
        this.borderLeft = new RectangleForm(BORDER_WEIGHT, -borderSize, 0, 0, 0, borderSize, height);
        this.borderRight = new RectangleForm(BORDER_WEIGHT, width, 0, 0, 0, borderSize, height);
    }

    public void setFigure(Figure... figure) throws Exception {
        for (Figure f : figure) {
            if (!checkInside(f)) throw new Exception("Object is out of the field");
            if (checkIntersect(f)) throw new Exception("Object intersects with another object");
            fieldObjects.add(f);
        }
    }

    public boolean checkInside(Figure figure) {                   // check field borders
        double paramX = figure.getMainParameters()[0];            // horizontal distance from center of gravity
        double paramY = figure.getMainParameters()[1];            // vertical distance from center of gravity
        return figure.getX() - paramX >= 0
                && figure.getY() - paramY >= 0
                && figure.getX() + paramX <= fieldWidth
                && figure.getY() + paramY <= fieldHeight;
    }

    public boolean checkIntersect(Figure other) {                 // check position to set the figure
        for (Figure f : this.fieldObjects) {
            if (f.getCollision(other))
                return true;
        }
        return false;
    }

    public void moveObjects(double time) {
        for (Figure f : this.fieldObjects) {
            double timeBefore = correctBounceTime(f, time);
            f.move(timeBefore);
            f.move(time - timeBefore);
            for (Figure anyFigure : this.fieldObjects) {
                if (anyFigure != f) {
                    if (anyFigure.getCollision(f))
                        anyFigure.rebound(f);
                }
            }
        }
    }

    private double correctBounceTime(Figure figure, double time) {          // time before figure hitting the border
        double timeBefore = time;
        double paramX = figure.getMainParameters()[0];
        double paramY = figure.getMainParameters()[1];
        double x_start = figure.getX();
        double y_start = figure.getY();
        double x_finish = x_start + figure.getVx() * time;
        double y_finish = y_start + figure.getVy() * time;

        if (x_finish + paramX >= fieldWidth) {                              // right field border
            timeBefore = (fieldWidth - paramX - x_start) / figure.getVx();
            reflection(figure, -1, 1);
        }
        if (x_finish - paramX <= 0) {                                       // left field border
            timeBefore = (paramX - x_start) / figure.getVx();
            reflection(figure, -1, 1);
        }
        if (y_finish + paramY >= fieldHeight) {                             // upper field border
            timeBefore = (fieldHeight - paramY - y_start) / figure.getVy();
            reflection(figure, 1, -1);
        }
        if (y_finish - paramY <= 0) {                                       // bottom field border
            timeBefore = (paramY - y_start) / figure.getVy();
            reflection(figure, 1, -1);
        }
        if (Math.abs(figure.getVx()) == 0 || Math.abs(figure.getVy()) == 0) {
            return time;
        }
        return timeBefore;
    }

    private void reflection(Figure figure, int signVx, int signVy) {          // reflection vector from border
        figure.setVx(figure.getVx() * signVx);
        figure.setVy(figure.getVy() * signVy);
    }

    public void printFieldObjects() {
        HashSet<Figure> set = this.getFieldObjects();
        for (Figure f : set) {
            System.out.println(f);
        }
        System.out.println();
    }


    public int getFieldWidth() {
        return fieldWidth;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public void setFieldHeight(int fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public RectangleForm getBorderLeft() {
        return borderLeft;
    }

    public RectangleForm getBorderRight() {
        return borderRight;
    }

    public RectangleForm getBorderTop() {
        return borderTop;
    }

    public RectangleForm getBorderBottom() {
        return borderBottom;
    }

    public HashSet<Figure> getFieldObjects() {
        return fieldObjects;
    }

    @Override
    public String toString() {
        return "Field " +
                "[Width " + fieldWidth +
                " Height " + fieldHeight + "]" + "\n" +
                "Borders: " + "\n" +
                "UP: " + borderTop + "\n" +
                "DOWN: " + borderBottom + "\n" +
                "LEFT: " + borderLeft + "\n" +
                "RIGHT: " + borderRight + "\n";
    }
}
