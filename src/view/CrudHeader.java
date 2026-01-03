package view;

import javax.swing.*;
import java.awt.*;

public class CrudHeader extends JPanel {
    public final JTextField search = new JTextField(22);
    public final JButton add = new JButton("Add");
    public final JButton edit = new JButton("Edit");
    public final JButton del = new JButton("Delete");
    public final JButton refresh = new JButton("Refresh");

    public CrudHeader(String title, String subtitle) {
        super(new BorderLayout(12,12));
        JPanel left = new JPanel(new GridLayout(2,1));
        left.setOpaque(false);
        left.add(Ui.h1(title));
        left.add(Ui.h2(subtitle));

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        rightTop.setOpaque(false);
        rightTop.add(new JLabel("Search:"));
        rightTop.add(search);

        JPanel rightBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        rightBottom.setOpaque(false);
        rightBottom.add(refresh);
        rightBottom.add(add);
        rightBottom.add(edit);
        rightBottom.add(del);

        JPanel right = new JPanel(new GridLayout(2,1,0,6));
        right.setOpaque(false);
        right.add(rightTop);
        right.add(rightBottom);

        setOpaque(false);
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
    }
}
