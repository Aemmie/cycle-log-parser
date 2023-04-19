package ru.aemmie.cycle.overlay;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class Overlay extends JFrame {

    public Overlay(Integer width, Integer height) {
        super("The Cycle: Overlay");
        this.setUndecorated(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setAlwaysOnTop(true);
//        this.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", false);
        this.setType(JFrame.Type.UTILITY);
        this.setFocusableWindowState(false);

        DisplayMode displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        if (width == null) {
            width = displayMode.getWidth();
        }
        if (height == null) {
            height = displayMode.getHeight();
        }

        // -30 to not overlap with windows control panel at the bottom (sorry guys with left/right panels)
        Dimension d = new Dimension(width, height - 30);
        log.info("Screen size: %dx%d".formatted(width, height));
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
