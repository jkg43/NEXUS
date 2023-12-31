package uiComponents;

import java.awt.*;
import java.util.function.Consumer;

public class Button extends ActivatableUIComponent
{

    public Color normalColor,hoverColor, currentColor;

    private boolean hovered = false;


    public Button(int x, int y, int w, int h, Color normalColor, Color hoverColor, Consumer<ActivatableUIComponent> f, UIComponent... t) {
        super(f, t);
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
        if(isPointInside(mx,my) && !ui.contextMenuHovered())
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
        if(isPointInside(mx,my))
        {
            activate();
        }
    }
}
