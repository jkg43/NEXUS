package uiComponents;

import manager.Module;
import ui.UI;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ModuleSelector extends Button
{

    private static final int HEIGHT = 40;
    private static final int WIDTH = 300;
    private static final Color NORMAL = new Color(150,150,150);
    private static final Color HOVERED = new Color(120,120,120);


    private final ArrayList<ModuleSelection> modules;

    private boolean menuOpen = false, menuHovered = false;

    public ModuleSelector(UI u)
    {
        super(u,0,0,HEIGHT,HEIGHT,NORMAL, HOVERED, c -> {
            ModuleSelector s = (ModuleSelector) c;
            s.toggleMenu();
        });
        modules = new ArrayList<>();
        int modY = HEIGHT;
        for(Module mod : ui.m.modules)
        {
            modules.add(new ModuleSelection(this,mod,modY,c->{
                ModuleSelection s  = (ModuleSelection) c;
                u.m.currentModule = s.module;
                s.selector.toggleMenu();
            }));
            modY+=HEIGHT;
        }
    }

    public void toggleMenu()
    {
        menuOpen = !menuOpen;
    }

    public boolean menuHovered()
    {
        return menuHovered;
    }

    @Override
    public void update() {
        super.update();
        menuHovered = false;
        if(isHovered())
        {
            menuHovered = true;
        }
        if(menuOpen)
        {
            for(ModuleSelection s : modules)
            {
                s.update();
                if(s.isHovered())
                {
                    menuHovered = true;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        g2d.setColor(Color.BLACK);
        for (int y = 8; y < 35; y+=10) {
            g2d.fillRect(5,y,30,5);
        }
        if(menuOpen)
        {
            for(ModuleSelection s : modules)
            {
                s.draw(g2d);
            }
        }
    }

    @Override
    public void checkMouseAndActivate(int mx, int my, int button) {
        super.checkMouseAndActivate(mx, my, button);
        if(menuOpen)
        {
            for(ModuleSelection s : modules)
            {
                s.checkMouseAndActivate(mx,my,button);
            }
        }
    }

    private static class ModuleSelection extends TextButton
    {

        public Module module;

        public ModuleSelector selector;

        public ModuleSelection(ModuleSelector selector, Module module, int y, Consumer<ActivatableUIComponent> f) {
            super(selector.ui,0,y,WIDTH,HEIGHT, module.name,NORMAL,HOVERED,Color.BLACK,f);
            this.module = module;
            this.selector = selector;
        }

    }




}
