import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashSet;

public class Visualization extends Application {
    private Stage window;
    private Scene scene;
    private Simulator simulator;
    private Field field;
    private HashSet<Shape> shapes = new HashSet<>();            // objects on the field
    private StackPane centerMenu;
    private int fieldW = 800, fieldH = 600;

    @Override
    public void start(Stage primaryStage) {
        simulator = new Simulator();
        window = primaryStage;
        window.setTitle("2D object interaction simulator");

        BorderPane root = new BorderPane();
        root.setMinSize(800, 380);
        root.setTop(setTopMenu());
        root.setBottom(setBottomMenu());
        root.setLeft(setLeftMenu());
        root.setRight(setRightMenu());
        root.setCenter(setCenterField(fieldW, fieldH));

        scene = new Scene(root, fieldW + 100, fieldH + 80);

        window.setWidth(fieldW + 106);
        window.setHeight(fieldH + 113);
        window.setMinWidth(806);
        window.setMinHeight(413);
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Node setCenterField(int width, int height) {
        centerMenu = new StackPane();

        centerMenu.setAlignment(Pos.BOTTOM_LEFT);
        field = new Field(width, height);
        System.out.println(field);

        RectangleForm top = field.getBorderTop();
        Rectangle topBorder = new Rectangle(top.getWidth(), top.getHeight());
        topBorder.setTranslateX(top.getX());
        topBorder.setTranslateY(-top.getY());

        RectangleForm bottom = field.getBorderBottom();
        Rectangle bottomBorder = new Rectangle(bottom.getWidth(), bottom.getHeight());
        bottomBorder.setTranslateX(bottom.getX());
        bottomBorder.setTranslateY(bottom.getY());

        RectangleForm left = field.getBorderLeft();
        Rectangle leftBorder = new Rectangle(left.getWidth(), left.getHeight());
        leftBorder.setTranslateX(left.getX());
        leftBorder.setTranslateY(left.getY());

        RectangleForm right = field.getBorderRight();
        Rectangle rightBorder = new Rectangle(right.getWidth(), right.getHeight());
        rightBorder.setTranslateX(right.getX());
        rightBorder.setTranslateY(right.getY());

        centerMenu.setPrefSize(field.getFieldWidth(), field.getFieldHeight());
        centerMenu.setStyle("-fx-background-color: #FFFFFF");

        setCoordinateAxes();
        centerMenu.getChildren().addAll(topBorder, bottomBorder, leftBorder, rightBorder);
        return centerMenu;
    }

    public Node setTopMenu() {
        HBox topMenu = new HBox(10);
        topMenu.setMinHeight(40);

        Label fieldLabel = new Label("FIELD SIZE");
        fieldLabel.setTextFill(Color.RED);
        fieldLabel.setFont(new Font(20));
        fieldLabel.setTranslateX(-10);

        Label fWidthLabel = new Label("WIDTH");
        TextField fWidthInput = new TextField(Integer.toString(fieldW));
        fWidthInput.setPrefSize(50, 20);

        Label fHeightLabel = new Label("HEIGHT");
        TextField fHeightInput = new TextField(Integer.toString(fieldH));
        fHeightInput.setPrefSize(50, 20);

        Button fieldSizeButton = new Button("CREATE");
        // create new field with new settings
        fieldSizeButton.setOnAction(event -> {
            fieldW = textToInt(fWidthInput);
            fieldH = textToInt(fHeightInput);
            if (fieldW > 0 && fieldH > 0) {
                field.setFieldWidth(fieldW);
                field.setFieldHeight(fieldH);
                field.getFieldObjects().clear();
                shapes.clear();
                try {
                    start(window);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else new AlertWindow().display("ERROR", "Incorrect data");
        });
        topMenu.setAlignment(Pos.CENTER);
        topMenu.setStyle("-fx-background-color: #FFE4C4");
        topMenu.getChildren().addAll(fieldLabel, fWidthLabel, fWidthInput, fHeightLabel,
                fHeightInput, fieldSizeButton);
        return topMenu;
    }

    public Node setBottomMenu() {
        HBox bottomMenu = new HBox(10);
        bottomMenu.setMinHeight(40);

        Label figureLabel = new Label("FIGURE");
        figureLabel.setTextFill(Color.RED);
        figureLabel.setFont(new Font(20));
        figureLabel.setTranslateX(-10);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().add("Rectangle");
        choiceBox.getItems().add("Circle");
        choiceBox.getSelectionModel().select(0);

        Label mInputLabel = new Label("m");
        TextField mInput = new TextField("10");
        Label vxInputLabel = new Label("Vx");
        TextField vxInput = new TextField("10");
        Label vyInputLabel = new Label("Vy");
        TextField vyInput = new TextField("20");
        Label xInputLabel = new Label("x");
        TextField xInput = new TextField("100");
        Label yInputLabel = new Label("y");
        TextField yInput = new TextField("100");
        Label wInputLabel = new Label("w");
        TextField wInput = new TextField("50");
        Label hInputLabel = new Label("h");
        TextField hInput = new TextField("50");
        resizeTextFields(mInput, vxInput, vyInput, xInput, yInput, wInput, hInput);

        // change the list of parameters depending on the figure
        choiceBox.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    if (choiceBox.getSelectionModel().getSelectedIndex() == 1) {
                        wInputLabel.setText("R");
                        hInputLabel.setVisible(false);
                        hInput.setVisible(false);
                    } else {
                        wInputLabel.setText("w");
                        hInputLabel.setVisible(true);
                        hInput.setVisible(true);
                    }
                });

        Button figureAddButton = new Button("ADD");
        // add figure to the list of figures
        figureAddButton.setOnAction(event -> {
            if (choiceBox.getSelectionModel().getSelectedIndex() == 0 && textToInt(wInput) > 0 && textToInt(hInput) > 0 &&
                    textToInt(mInput) >= 0 && textToInt(xInput) >= 0 && textToInt(yInput) >= 0) {
                RectangleForm tempRectObj = new RectangleForm(textToInt(mInput),
                        textToInt(xInput), textToInt(yInput),
                        textToInt(vxInput), textToInt(vyInput),
                        textToInt(wInput), textToInt(hInput));
                try {
                    checkFigurePosition(tempRectObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (choiceBox.getSelectionModel().getSelectedIndex() == 1 && textToInt(wInput) > 0 &&
                    textToInt(mInput) >= 0 && textToInt(xInput) >= 0 && textToInt(yInput) >= 0) {
                CircleForm tempCircleObj = new CircleForm(textToInt(mInput),
                        textToInt(xInput), textToInt(yInput),
                        textToInt(vxInput), textToInt(vyInput),
                        textToInt(wInput));
                try {
                    checkFigurePosition(tempCircleObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else new AlertWindow().display("ERROR", "Incorrect data");
        });
        bottomMenu.setAlignment(Pos.CENTER);
        bottomMenu.setStyle("-fx-background-color: #FFE4C4");
        bottomMenu.getChildren().addAll(figureLabel, choiceBox, mInputLabel, mInput,
                vxInputLabel, vxInput, vyInputLabel, vyInput,
                xInputLabel, xInput, yInputLabel, yInput,
                wInputLabel, wInput, hInputLabel, hInput, figureAddButton);
        return bottomMenu;
    }

    public Node setLeftMenu() {
        VBox leftMenu = new VBox(10);
        leftMenu.setMinWidth(40);
        leftMenu.setStyle("-fx-background-color: #FFE4C4");
        return leftMenu;
    }

    public Node setRightMenu() {
        VBox rightMenu = new VBox(10);
        rightMenu.setMinWidth(60);

        Label timeLabel = new Label("TIME");
        timeLabel.setTextFill(Color.GREEN);
        timeLabel.setFont(new Font(20));

        TextField timeInput = new TextField("0");
        timeInput.setMaxWidth(50);

        Button plusButton = new Button("+");
        Button minusButton = new Button("-");
        plusButton.setOnAction(event -> {                       // increase time multiplier
            double t = Double.parseDouble(timeInput.getText());
            t++;
            timeInput.setText(Double.toString(t));
        });
        minusButton.setOnAction(event -> {                      // decrease time multiplier
            double t = Double.parseDouble(timeInput.getText());
            t--;
            timeInput.setText(Double.toString(t));
        });
        Button selectButton = new Button("RUN");
        selectButton.setTranslateY(10);
        selectButton.setOnAction(event -> {                     //select time for simulator
            double t = Double.parseDouble(timeInput.getText());
            simulator.setTime(t);
            moveObjects();
            //printSetObjects();
        });

        Button exitButton = new Button("EXIT");
        exitButton.setTranslateY(50);
        exitButton.setTextFill(Color.RED);
        exitButton.setOnAction(event -> window.close());

        rightMenu.setAlignment(Pos.TOP_CENTER);
        rightMenu.setStyle("-fx-background-color: #FFE4C4");
        rightMenu.getChildren().addAll(timeLabel, plusButton, timeInput, minusButton,
                selectButton, exitButton);
        return rightMenu;
    }

    // check position of figure before set on the field
    private void checkFigurePosition(Figure figure) throws Exception {
        try {
            field.setFigure(figure);
            setFigureOnField(figure);
        } catch (Exception e) {
            new AlertWindow().display("ERROR", e.getMessage());
        }
    }

    // show figures on the field
    private void setFigureOnField(Figure figure) {
        if (figure instanceof RectangleForm) {
            Rectangle tempRect = new Rectangle(((RectangleForm) figure).getWidth(), ((RectangleForm) figure).getHeight());
            tempRect.setFill(Color.color(Math.random(), Math.random(), Math.random()));
            tempRect.setTranslateX(figure.getX() - ((RectangleForm) figure).getWidth() / 2);
            tempRect.setTranslateY(-figure.getY() + ((RectangleForm) figure).getHeight() / 2);
            tempRect.setId("" + figure.getId());
            tempRect.setOnMouseClicked(event -> {
                new AlertWindow().display("INFO", figure.toString());       // show info about figure on the field
            });
            shapes.add(tempRect);
            centerMenu.getChildren().add(tempRect);
        }
        if (figure instanceof CircleForm) {
            Circle tempCircle = new Circle(((CircleForm) figure).getRadius());
            tempCircle.setFill(Color.color(Math.random(), Math.random(), Math.random()));
            tempCircle.setTranslateX(figure.getX() - tempCircle.getRadius());
            tempCircle.setTranslateY(-figure.getY() + tempCircle.getRadius());
            tempCircle.setId("" + figure.getId());
            tempCircle.setOnMouseClicked(event -> {
                new AlertWindow().display("INFO", figure.toString());       // show info about figure on the field
            });
            shapes.add(tempCircle);
            centerMenu.getChildren().add(tempCircle);
        }
    }

    // move objects across the field depending on the time
    private void moveObjects() {
        field.moveObjects(simulator.getTime());
        for (Figure f : field.getFieldObjects()) {
            for (Shape shape : shapes) {
                if (shape.getId().equals("" + f.getId())) {
                    shape.setTranslateX(f.getX() - f.getMainParameters()[0]);
                    shape.setTranslateY(-f.getY() + f.getMainParameters()[1]);
                }
            }
        }
    }

    private void resizeTextFields(TextField... fields) {
        for (TextField t : fields) {
            t.setPrefSize(45, 20);
        }
    }

    private int textToInt(TextField text) throws NumberFormatException {
        try {
            int num = Integer.parseInt(text.getText());
            return num;
        } catch (NumberFormatException e) {
            new AlertWindow().display("ERROR", "Enter integer value");
        }
        return -1;
    }

    // set axes on the field
    private void setCoordinateAxes() {
        for (int i = 50; i < fieldW; i = i + 50) {
            Line line = new Line(i, 0, i, fieldH);
            line.setTranslateX(i);
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(1);
            centerMenu.getChildren().addAll(line);
        }
        for (int i = 50; i < fieldH; i += 50) {
            Line line = new Line(0, i, fieldW, i);
            line.setTranslateY(-i);
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(1);
            centerMenu.getChildren().addAll(line);
        }
    }

    // pop-up window
    class AlertWindow {
        public void display(String title, String message) {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(title);
            window.setMinWidth(350);
            window.setMinHeight(250);

            Label label = new Label(message);
            label.setFont(Font.font(20));

            Button closeButton = new Button("CLOSE!");
            closeButton.setOnAction(event -> window.close());

            VBox layout = new VBox(20);
            layout.getChildren().addAll(label, closeButton);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.show();
        }
    }
}

