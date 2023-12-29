package manager;

import ui.UI;
import uiComponents.UIComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class CameraModule2D extends Module {

    private final AffineTransform camTransform;
    private static final double DEFAULT_MOVE_SPEED = 10;
    private double moveSpeed = DEFAULT_MOVE_SPEED;

    public int translatedMouseX, translatedMouseY;

    public UIComponent hoveredComponent = null;

    public UIComponent draggedComponent = null;

    public ArrayList<UIComponent> staticComponents;


    public CameraModule2D(UI u, String name, String modulePath) {
        super(u, name, modulePath);
        camTransform = new AffineTransform();
        staticComponents = new ArrayList<>();
    }

    public void processKeyCode(int code) {
        switch(code) {
            case KeyEvent.VK_UP -> camTransform.translate(0,moveSpeed);
            case KeyEvent.VK_DOWN -> camTransform.translate(0,-moveSpeed);
            case KeyEvent.VK_LEFT -> camTransform.translate(moveSpeed,0);
            case KeyEvent.VK_RIGHT -> camTransform.translate(-moveSpeed,0);
        }
    }

    public void processMouseDrag(int dx, int dy, MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            double moveFactor = moveSpeed / DEFAULT_MOVE_SPEED;
            if(draggedComponent == null && hoveredComponent != null) {
                draggedComponent = hoveredComponent;
            }
            if(hoveredComponent == null || !hoveredComponent.isMovable) {
                camTransform.translate(dx * moveFactor,dy * moveFactor);
            } else {
                draggedComponent.translate((int) (dx * moveFactor), (int) (dy * moveFactor));
            }
        }
    }

    public void scaleAroundPoint(double scaleFactor, int px, int py) {
        Point2D translatePos = new Point();
        try {
            camTransform.inverseTransform(new Point(px, py),translatePos);
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException(e);
        }
        camTransform.translate(translatePos.getX(),translatePos.getY());
        camTransform.scale(scaleFactor,scaleFactor);
        camTransform.translate(-translatePos.getX(),-translatePos.getY());
        moveSpeed /= scaleFactor;
    }

    @Override
    public void draw(Graphics2D g2d)
    {
        AffineTransform oldTransform = g2d.getTransform();

        g2d.transform(camTransform);

        super.draw(g2d);

        g2d.setTransform(oldTransform);

        for(UIComponent c : staticComponents) {
            if(!c.isHidden) {
                c.draw(g2d);
            }
        }
    }

    @Override
    public void update() {
        Point2D translatePos = new Point();
        try {
            camTransform.inverseTransform(new Point(u.in.mouseX, u.in.mouseY),translatePos);
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException(e);
        }
        translatedMouseX = (int) translatePos.getX();
        translatedMouseY = (int) translatePos.getY();

        boolean anyComponentsHovered = false, currentStillHovered = false;
        UIComponent newHoveredComponent = null;
        for(UIComponent c : components) {
            if(c.isPointInside(translatedMouseX,translatedMouseY)) {
                if(c==hoveredComponent) {
                    currentStillHovered = true;
                } else {
                    newHoveredComponent = c;
                }
                anyComponentsHovered = true;
            }
        }

        //if comp already hovered, do nothing
        if(!anyComponentsHovered) {
            hoveredComponent = null;
        } else if(!currentStillHovered || hoveredComponent == null) {
            hoveredComponent = newHoveredComponent;
        }

        super.update();

        for(UIComponent c : staticComponents) {
            c.update();
        }
    }

}
