package modules;

import manager.CameraModule2D;
import ui.UI;
import uiComponents.UIComponent;

import java.awt.*;
import java.awt.geom.Point2D;

public class TreeGraphModule extends CameraModule2D {

    private TreeNode baseNode;


    public TreeGraphModule(UI u, String name) {
        super(u, name, "/tree/");
        baseNode = new TreeNode(u,"Base test longer string");
        baseNode.x = 200;
        baseNode.y = 50;
        components.add(baseNode);

        baseNode.addLeft("Left longer longer longer");
        baseNode.addRight("Right is a longer string");
    }


    private class TreeNode extends UIComponent
    {
        public TreeNode left, right;
        public String text;

        private TreeNodeAddButton leftButton,rightButton;

        private static final Stroke connectionStroke = new BasicStroke(3);

        public TreeNode(UI u, String text) {
            super(u);
            this.text = text;
            this.width = -1;
            isMovable = true;
            leftButton = new TreeNodeAddButton(u,this,true);
            rightButton = new TreeNodeAddButton(u,this,false);
            componentsToAdd.add(leftButton);
            componentsToAdd.add(rightButton);
        }

        public void addLeft(String s) {
            left = new TreeNode(ui,s);
            left.x = x - 100;
            left.y = y + 50;
            componentsToAdd.add(left);
            componentsToRemove.add(leftButton);
        }

        public void addRight(String s) {
            right = new TreeNode(ui,s);
            right.x = x + 100;
            right.y = y + 50;
            componentsToAdd.add(right);
            componentsToRemove.add(rightButton);
        }

        public void setText(String text) {
            this.text = text;
            this.width = -1;
        }

        public Point2D getTopCenterPoint() {
            return new Point(x + width/2,y);
        }

        public Point2D getBottomCenterPoint() {
            return new Point(x+width/2,y+height+5);
        }

        public void moveChildren(int dx, int dy) {
            if(left!=null) {
                left.x+=dx;
                left.y+=dy;
                left.moveChildren(dx,dy);
            }
            if(right!=null) {
                right.x+=dx;
                right.y+=dy;
                right.moveChildren(dx,dy);
            }
        }

        @Override
        public void translate(int dx, int dy) {
            super.translate(dx,dy);
            if(u.selectedComponent==this) {
                moveChildren(dx,dy);
            }
        }

        @Override
        public void update() {
            if(left==null) {
                leftButton.x = x + width / 4;
                leftButton.y = y + height + 10;
            }
            if(right==null) {
                rightButton.x = x + 3 * width / 4;
                rightButton.y = y + height + 10;
            }
        }

        @Override
        public void draw(Graphics2D g2d) {
            if(width==-1) {
                width = g2d.getFontMetrics().stringWidth(text);
                height = g2d.getFontMetrics().getHeight();
            }

            if(ui.selectedComponent == this) {
                g2d.setColor(Color.GRAY);
                g2d.fillRect(x,y,width,height);
            } else if (hoveredComponent == this) {
                g2d.setColor(new Color(150,150,150));
                g2d.fillRect(x,y,width,height);
            }
            g2d.setColor(Color.BLACK);

            g2d.drawString(text,x,y+height);

            g2d.setStroke(connectionStroke);

            Point2D bottom = getBottomCenterPoint();

            if(left != null) {
                Point2D leftTop = left.getTopCenterPoint();
                g2d.drawLine((int) bottom.getX(), (int) bottom.getY(), (int) leftTop.getX(), (int) leftTop.getY());
            }
            if(right != null) {
                Point2D rightTop = right.getTopCenterPoint();
                g2d.drawLine((int) bottom.getX(), (int) bottom.getY(), (int) rightTop.getX(), (int) rightTop.getY());
            }

        }

        @Override
        public void checkMouseAndActivate(int mx, int my, int button) {
            if(isPointInside(translatedMouseX, translatedMouseY)) {
                if(button==1) {
                    ui.selectedComponent = this;
                } else if(button==3) {
                    //TODO - create rmb context menu w/ option to change text
                    System.out.println("RMB Pressed on node with text: "+text);
                }
            }
        }



        private class TreeNodeAddButton extends uiComponents.Button {

            public TreeNode node;

            public boolean left;

            public static int SIZE = 16;

            public TreeNodeAddButton(UI u, TreeNode node, boolean left) {
                super(u, node.x, node.y, SIZE, SIZE, new Color(140,140,140), new Color(120,120,120),
                    c -> {
                        TreeNodeAddButton b = (TreeNodeAddButton) c;
                        if(b.left) {
                            b.node.addLeft("Adding Left");
                        } else {
                            b.node.addRight("Adding Right");
                        }
                    });
                this.node = node;
                this.left = left;
            }

            @Override
            public void draw(Graphics2D g2d) {
                g2d.setColor(Color.BLACK);
                g2d.fillRect(x+6,y,4,SIZE);
                g2d.fillRect(x,y+6,SIZE,4);
            }

            @Override
            public void checkMouseAndActivate(int mx, int my, int button) {
                if(isPointInside(translatedMouseX,translatedMouseY)) {
                    activate();
                }
            }
        }





    }


}
