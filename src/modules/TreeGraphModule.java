package modules;

import manager.CameraModule2D;
import ui.UI;
import uiComponents.TextButton;
import uiComponents.TextInput;
import uiComponents.UIComponent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TreeGraphModule extends CameraModule2D {

    private TreeNode baseNode;

    TextInput fileSelector;


    public TreeGraphModule(UI u, String name) {
        super(u, name, "/tree/");
        baseNode = new TreeNode(u,"Base test longer string");
        baseNode.x = 200;
        baseNode.y = 50;
        components.add(baseNode);

        baseNode.addLeft("Left longer longer longer");
        baseNode.addRight("Right is a longer string");

        TreeNodeSaveButton saveButton = new TreeNodeSaveButton(u);
        staticComponents.add(saveButton);

        TreeNodeLoadButton loadButton = new TreeNodeLoadButton(u);
        staticComponents.add(loadButton);

        fileSelector = new TextInput(u,0,u.HEIGHT-80,150,40,"test1.txt",(x)->{});
        staticComponents.add(fileSelector);
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
            leftButton = null;
        }

        public void addRight(String s) {
            right = new TreeNode(ui,s);
            right.x = x + 100;
            right.y = y + 50;
            componentsToAdd.add(right);
            componentsToRemove.add(rightButton);
            rightButton = null;
        }

        public void addLeft() {
            TextInput textIn = new TextInput(u,x + width / 4-50,y + height + 10,100,40,"",
                (x)->{
                    TextInput t = (TextInput) x;
                    TreeNode node = (TreeNode) t.targets[0];
                    String s = t.builder.toString();
                    if(!s.isBlank()) {
                        node.addLeft(s);
                        componentsToRemove.add(t);
                    }
                },this);
            componentsToAdd.add(textIn);
            u.selectedComponent = textIn;
            u.in.typing = true;
        }

        public void addRight() {
            TextInput textIn = new TextInput(u,x + 3 * width / 4-50,y + height + 10,100,40,"",
                (x)->{
                    TextInput t = (TextInput) x;
                    TreeNode node = (TreeNode) t.targets[0];
                    String s = t.builder.toString();
                    if(!s.isBlank()) {
                        node.addRight(s);
                        componentsToRemove.add(t);
                    }
                },this);
            componentsToAdd.add(textIn);
            u.selectedComponent = textIn;
            u.in.typing = true;
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

        public void removeAllChildren() {
            if(left!=null) {
                left.removeAllChildren();
                componentsToRemove.add(left);
            } else {
                componentsToRemove.add(leftButton);
            }
            if(right!=null) {
                right.removeAllChildren();
                componentsToRemove.add(right);
            } else {
                componentsToRemove.add(rightButton);
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
                            b.node.addLeft();
                        } else {
                            b.node.addRight();
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


    /*
    file format:
    first 2 lines reserved for file info

    following lines have form:
    <left index>,<right index>,<x>,<y>,<text>

    indices are based on the line the node is on, starting with the third line (after file info)

    base node should be on the first data line (index 0)

    no connected node means index of -1

     */

    private class TreeNodeSaveButton extends TextButton {
        public TreeNodeSaveButton(UI u) {
            super(u, 0,u.HEIGHT-40,100,40,"Save",Color.green,Color.green.darker(),Color.black,
                (x)->{
                    TreeNodeSaveButton b = (TreeNodeSaveButton) x;
                    b.saveFile();
                    System.out.println("Saving Tree Module");
                });
        }

        public void saveFile() {
            String filepath = modulePath + fileSelector.getText();


            try (FileWriter out = new FileWriter(filepath)){
                ArrayList<TreeNodeSaveData> nodes = new ArrayList<>();

                fillNodeList(nodes,baseNode);

//                for(TreeNodeSaveData d : nodes) {
//                    System.out.println("Index: "+d.index+", Left: "+d.leftIndex+", Right: "+d.rightIndex+
//                        ", Text: "+d.node.text);
//                }

                out.write("Tree Graph\n"+nodes.size()+" nodes\n");

                for(TreeNodeSaveData d : nodes) {
                    out.write(d.leftIndex+","+d.rightIndex+","+d.x+","+d.y+","+d.node.text+"\n");
                }

            } catch(IOException e) {
                System.err.println("Could not save tree file at "+filepath);
            }
        }

        private TreeNodeSaveData fillNodeList(ArrayList<TreeNodeSaveData> nodes,TreeNode currentNode) {
            TreeNodeSaveData current = new TreeNodeSaveData(currentNode,nodes.size(),currentNode.x,currentNode.y);
            nodes.add(current);
            if(currentNode.left!=null) {
                TreeNodeSaveData left = fillNodeList(nodes,currentNode.left);
                current.leftIndex = left.index;
            }
            if(currentNode.right!=null) {
                TreeNodeSaveData right = fillNodeList(nodes,currentNode.right);
                current.rightIndex = right.index;
            }
            return current;
        }

        private static class TreeNodeSaveData {
            public TreeNode node;
            public int index,leftIndex,rightIndex,x,y;

            public TreeNodeSaveData(TreeNode node, int index,int x,int y) {
                this.node = node;
                this.index = index;
                this.leftIndex = -1;
                this.rightIndex = -1;
                this.x = x;
                this.y = y;
            }
        }
    }

    private class TreeNodeLoadButton extends TextButton {

        public TreeNodeLoadButton(UI u) {
            super(u, 100,u.HEIGHT-40,100,40,"Load",Color.red,Color.red.darker(),Color.black,
                (x)->{
                    TreeNodeLoadButton b = (TreeNodeLoadButton) x;
                    b.loadFile();
                    System.out.println("Loading Tree Module");
                });
        }

        public void loadFile() {
            String filepath = modulePath + fileSelector.getText();

            try(BufferedReader in = new BufferedReader(new FileReader(filepath))) {
                //consume info lines
                in.readLine();
                in.readLine();

                String line = in.readLine();

                ArrayList<TreeNodeLoadData> nodeData = new ArrayList<>();

                while(line != null) {
                    String[] data = line.split(",");

                    nodeData.add(new TreeNodeLoadData(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]), Integer.parseInt(data[3]), data[4]));

                    line = in.readLine();
                }

                if(!nodeData.isEmpty()) {
                    TreeNode base = new TreeNode(u,nodeData.get(0).text);
                    base.x = nodeData.get(0).x;
                    base.y = nodeData.get(0).y;

                    fillChildren(nodeData,nodeData.get(0),base);

                    baseNode.removeAllChildren();
                    componentsToRemove.add(baseNode);

                    baseNode = base;
                    componentsToAdd.add(base);

                } else {
                    System.err.println("No nodes in file");
                }

            } catch (IOException e) {
                System.err.println("Could not load file at "+filepath);
            }
        }

        private void fillChildren(ArrayList<TreeNodeLoadData> data, TreeNodeLoadData currentData, TreeNode currentNode) {
            if(currentData.leftIndex!=-1) {
                TreeNodeLoadData leftData = data.get(currentData.leftIndex);
                currentNode.addLeft(leftData.text);

                fillChildren(data,leftData,currentNode.left);
            }
            if(currentData.rightIndex!=-1) {
                TreeNodeLoadData rightData = data.get(currentData.rightIndex);
                currentNode.addRight(rightData.text);
                fillChildren(data,rightData,currentNode.right);
            }
            currentNode.x = currentData.x;
            currentNode.y = currentData.y;
        }

        private static class TreeNodeLoadData {
            int leftIndex,rightIndex,x,y;
            String text;

            public TreeNodeLoadData(int leftIndex, int rightIndex, int x, int y, String text) {
                this.leftIndex = leftIndex;
                this.rightIndex = rightIndex;
                this.x = x;
                this.y = y;
                this.text = text;
            }
        }

    }


}
