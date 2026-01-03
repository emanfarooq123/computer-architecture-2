package view;

import javax.swing.*;
import javax.swing.table.*;

public final class FilterUtil {
    private FilterUtil(){}

    public static void attach(JTextField search, JTable table){
        TableRowSorter<? extends TableModel> sorter = (TableRowSorter<? extends TableModel>) table.getRowSorter();
        if (sorter == null) return;

        search.getDocument().addDocumentListener((DocListener) e -> {
            String q = search.getText().trim();
            if (q.isEmpty()) sorter.setRowFilter(null);
            else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(q)));
        });
    }
}
