package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SimpleFormDialog extends JDialog {
    private String[] result;
    private final JTextField[] fields;

    public SimpleFormDialog(Frame owner, String title, String[] labels, String[] initial, int[] requiredIdx) {
        this(owner, title, labels, initial, requiredIdx, new int[0]);
    }

    public SimpleFormDialog(Frame owner, String title, String[] labels, String[] initial, int[] requiredIdx, int[] readOnlyIdx) {
        super(owner, title, true);
        fields = new JTextField[labels.length];

        Set<Integer> req = new HashSet<>();
        for (int i : requiredIdx) req.add(i);

        Set<Integer> ro = new HashSet<>();
        for (int i : readOnlyIdx) ro.add(i);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(7,7,7,7);
        gc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            JLabel l = new JLabel(labels[i] + (req.contains(i) ? " *" : ""));
            fields[i] = new JTextField(22);
            if (initial != null && i < initial.length) fields[i].setText(initial[i] == null ? "" : initial[i]);

            if (ro.contains(i)) {
                fields[i].setEditable(false);
                fields[i].setEnabled(false);
            }

            gc.gridx = 0; gc.gridy = i; gc.weightx = 0;
            form.add(l, gc);
            gc.gridx = 1; gc.weightx = 1;
            form.add(fields[i], gc);
        }

        JButton cancel = new JButton("Cancel");
        JButton save = new JButton("Save");
        cancel.addActionListener(e -> dispose());
        save.addActionListener(e -> {
            for (int i : requiredIdx) {
                if (fields[i].getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, labels[i] + " is required.");
                    return;
                }
            }
            result = new String[labels.length];
            for (int i = 0; i < labels.length; i++) result[i] = fields[i].getText().trim();
            dispose();
        });

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.add(cancel);
        btns.add(save);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(btns, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public String[] result() { return result; }
}
