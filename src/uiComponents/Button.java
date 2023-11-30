package uiComponents;

import ui.UI;

import java.awt.*;
import java.util.function.Consumer;

public class Button extends ActivatableUIComponent
{

    public Color normalColor,hoverColor, currentColor;

    private boolean hovered = false;


    public Button(UI u, int x, int y, int w, int h, Color normalColor, Color hoverColor, Consumer<ActivatableUIComponent> f, UIComponent... t) {
        super(u, f, t);
        this.x=x;
        this.y=y;
        this.width=w;
        this.height=h;
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
    }

    public boolean isHovered()
    {
        return hovered;
    }

    @Override
    public void update() {
        int mx = ui.in.mouseX,my = ui.in.mouseY;
        if(mx>=x && my>=y && mx<=x+width && my<=y+height)
        {
            currentColor=hoverColor;
            hovered = true;
        }
        else
        {
            currentColor=normalColor;
            hovered = false;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(currentColor);
        g2d.fillRect(x, y, width, height);
    }

    @Override
    public void checkMouseAndActivate(int mx, int my, int button) {
        if(mx>=x && my>=y && mx<=x+width && my<=y+height)
        {
            activate();
        }
    }
}
