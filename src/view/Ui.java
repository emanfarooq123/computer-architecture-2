package view;

import javax.swing.*;
import java.awt.*;

public final class Ui {
    private Ui(){}

    public static void styleTable(JTable t){
        t.setRowHeight(28);
        t.setFillsViewportHeight(true);
        t.setAutoCreateRowSorter(true);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0,1));
    }

    public static JPanel pad(Component c){
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    public static JLabel h1(String text){
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 18f));
        return l;
    }

    public static JLabel h2(String text){
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.PLAIN, 12f));
        l.setForeground(new Color(145,150,160));
        return l;
    }
}
