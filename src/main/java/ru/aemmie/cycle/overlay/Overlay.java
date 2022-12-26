package ru.aemmie.cycle.overlay;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import ru.aemmie.cycle.overlay.component.EventLogBlock;
import ru.aemmie.cycle.overlay.component.ServerBlock;
import ru.aemmie.cycle.overlay.component.TimeBlock;

import javax.swing.*;
import java.awt.*;

public class Overlay extends JFrame {

    public Overlay() {
        super("The Cycle: log parser");
        this.setUndecorated(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setAlwaysOnTop(true);
//        this.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", false);
        this.setType(JFrame.Type.UTILITY);
        this.setFocusableWindowState(false);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        Dimension d = new Dimension(width, height - 30);
        this.setSize(d);
        this.setMinimumSize(d);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);

        rightPanel.add(new ServerBlock());
        rightPanel.add(new TimeBlock());
        rightPanel.add(new EventLogBlock());

        this.getContentPane().add(rightPanel, BorderLayout.EAST);

        this.pack();
        this.setVisible(true);
        setTransparent(this);
    }

    private static void setTransparent(Component w) {
        WinDef.HWND hwnd = new WinDef.HWND();
        hwnd.setPointer(Native.getComponentPointer(w));
        int wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE);
        wl = wl | WinUser.WS_EX_LAYERED | WinUser.WS_EX_TRANSPARENT;
        User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl);
    }

}
