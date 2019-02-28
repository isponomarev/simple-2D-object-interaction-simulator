public class Simulator {
    private double time = 1;

//    public static void main(String[] args) {
//        Simulator sim = new Simulator();
//        Field field = new Field(1000, 1000);
//        sim.prepareField(field);
//    }
//
//    public void prepareField(Field field){
//        RectangleForm rect1 = new RectangleForm(5, 50, 50, 50, 50, 100, 100);
//        RectangleForm rect2 = new RectangleForm(15, 250, 250, -50, -50, 125, 125);
//        CircleForm circle1 = new CircleForm(2, 100, 750, 0, -50, 50);
//        CircleForm circle2 = new CircleForm(220, 100, 300, 0, 70, 30);
//        try {
//            field.setFigure(rect1, rect2, circle1, circle2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        for (int i = 1; i <= 10; i++) {
//            field.moveObjects(time);
//            System.out.println("Time: " + i);
//            if (rect1.getCollision(rect2))
//                System.out.println("BOOM!");
//            if (circle1.getCollision(circle2))
//                System.out.println("CRASH!");
//            field.printFieldObjects();
//        }
//    }

    public double getTime(){
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}

