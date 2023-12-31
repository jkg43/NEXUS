package uiComponents;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ContextMenu extends UIComponent {


    ArrayList<ContextSelection> options;

    boolean menuHovered = false;

    /**
     *  length of all input arrays must be the same
     *  each array of targets is for consecutive funcs
     */
    public ContextMenu(int x, int y, int width, int optionHeight) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = optionHeight;
        options = new ArrayList<>();
        ui.currentContextMenu = this;
    }

    public void addOption(String label,Consumer<ActivatableUIComponent> f, UIComponent... t) {
        options.add(new ContextSelection(x, y + height * options.size(), width, height, label, f, t));
    }

    public boolean menuHovered() {
        return menuHovered;
    }

    @Override
    public void update() {
        menuHovered = false;
        for(ContextSelection s : options) {
            s.update();
            if(s.isHovered()) {
                menuHovered = true;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        for(ContextSelection s : options) {
            s.draw(g2d);
        }
    }

    @Override
    public void checkMouseAndActivate(int mx, int my, int button) {
        for(ContextSelection s : options) {
            s.checkMouseAndActivate(mx,my,button);
        }
    }


    private static class ContextSelection extends TextButton {

        public ContextSelection(int x, int y, int w, int h, String label, Consumer<ActivatableUIComponent> f, UIComponent... t) {
            super(x,y,w,h,label,new Color(100,100,100),new Color(120,120,120),Color.BLACK,f,t);
        }

        @Override
        public void activate() {
            super.activate();
            ui.currentContextMenu = null;
        }

    }


}
