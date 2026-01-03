package view;

import controller.StaffController;
import model.Staff;
import persistence.Repository;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class StaffPanel extends JPanel {
    private final StaffController ctl;
    private final JTable table;
    private final StaffTableModel model;
    private final CrudHeader header;

    public StaffPanel(Frame owner, StaffController ctl) {
        super(new BorderLayout(12,12));
        this.ctl = ctl;

        header = new CrudHeader("Staff", "Add/Edit/Delete Staff • Data saved/fetched from CSV");
        model = new StaffTableModel(ctl.all());
        table = new JTable(model);
        Ui.styleTable(table);
        FilterUtil.attach(header.search, table);

        header.add.addActionListener(e -> addStaff(owner));
        header.edit.addActionListener(e -> editStaff(owner));
        header.del.addActionListener(e -> deleteStaff());
        header.refresh.addActionListener(e -> refresh());

        add(Ui.pad(header), BorderLayout.NORTH);
        add(Ui.pad(new JScrollPane(table)), BorderLayout.CENTER);
    }

    private void addStaff(Frame owner){
        try {
            Staff seed = ctl.blank();
            String[] labels = {"First name","Last name","Role","Phone","Email","Facility ID","Employment status"};
            String[] init = {"","","Receptionist","","","","Active"};
            SimpleFormDialog d = new SimpleFormDialog(owner, "New Staff", labels, init, new int[]{0,1,2});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            Staff s = new Staff(seed.staffId, r[0], r[1], r[2], "", r[5], r[3], r[4], r[6], Repository.today(), "", "");
            ctl.add(s);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void editStaff(Frame owner){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a staff member first."); return; }
        int row = table.convertRowIndexToModel(v);

        try {
            Staff s0 = ctl.all().get(row);
            String[] labels = {"Staff ID","First name","Last name","Role","Phone","Email","Facility ID","Employment status"};
            String[] init = {s0.staffId,s0.firstName,s0.lastName,s0.role,s0.phoneNumber,s0.email,s0.facilityId,s0.employmentStatus};
            SimpleFormDialog d = new SimpleFormDialog(owner, "Edit Staff", labels, init, new int[]{1,2,3}, new int[]{0});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            Staff s = new Staff(s0.staffId, r[1], r[2], r[3], s0.department, r[6], r[4], r[5], r[7], s0.startDate, s0.lineManager, s0.accessLevel);
            ctl.set(row, s);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void deleteStaff(){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a staff member first."); return; }
        int row = table.convertRowIndexToModel(v);

        if (!Message.confirm("Delete selected staff member? This will auto-save to CSV.")) return;
        try { ctl.remove(row); refresh(); }
        catch (Exception ex){ Message.error(ex); }
    }

    private void refresh(){
        try { ctl.reloadAll(); model.setData(ctl.all()); }
        catch (Exception ex){ Message.error(ex); }
    }

    static class StaffTableModel extends AbstractTableModel {
        private List<Staff> data;
        private final String[] cols = {"Staff ID","First name","Last name","Role","Phone","Email","Facility ID","Status"};

        StaffTableModel(List<Staff> data){ this.data = data; }
        void setData(List<Staff> data){ this.data=data; fireTableDataChanged(); }

        public int getRowCount(){ return data.size(); }
        public int getColumnCount(){ return cols.length; }
        public String getColumnName(int c){ return cols[c]; }
        public Object getValueAt(int r,int c){
            Staff x = data.get(r);
            return switch(c){
                case 0 -> x.staffId;
                case 1 -> x.firstName;
                case 2 -> x.lastName;
                case 3 -> x.role;
                case 4 -> x.phoneNumber;
                case 5 -> x.email;
                case 6 -> x.facilityId;
                default -> x.employmentStatus;
            };
        }
    }
}
