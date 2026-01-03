package view;

import controller.FacilityController;
import model.Facility;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class FacilitiesPanel extends JPanel {
    private final FacilityController ctl;
    private final JTable table;
    private final FacilitiesTableModel model;
    private final CrudHeader header;

    public FacilitiesPanel(Frame owner, FacilityController ctl) {
        super(new BorderLayout(12,12));
        this.ctl = ctl;

        header = new CrudHeader("Facilities", "Add/Edit/Delete Facilities • Data saved/fetched from CSV");
        model = new FacilitiesTableModel(ctl.all());
        table = new JTable(model);
        Ui.styleTable(table);
        FilterUtil.attach(header.search, table);

        header.add.addActionListener(e -> addFacility(owner));
        header.edit.addActionListener(e -> editFacility(owner));
        header.del.addActionListener(e -> deleteFacility());
        header.refresh.addActionListener(e -> refresh());

        add(Ui.pad(header), BorderLayout.NORTH);
        add(Ui.pad(new JScrollPane(table)), BorderLayout.CENTER);
    }

    private void addFacility(Frame owner){
        try {
            Facility seed = ctl.blank();
            String[] labels = {"Facility name","Type","Postcode","Phone","Email","Capacity","Opening hours"};
            String[] init = {"","GP Surgery","","","","",""};
            SimpleFormDialog d = new SimpleFormDialog(owner, "New Facility", labels, init, new int[]{0,1});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            Facility f = new Facility(seed.facilityId, r[0], r[1], "", r[2], r[3], r[4], r[6], "", r[5], "");
            ctl.add(f);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void editFacility(Frame owner){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a facility first."); return; }
        int row = table.convertRowIndexToModel(v);

        try {
            Facility f0 = ctl.all().get(row);
            String[] labels = {"Facility ID","Facility name","Type","Postcode","Phone","Email","Capacity","Opening hours","Manager name","Specialities offered"};
            String[] init = {f0.facilityId,f0.facilityName,f0.facilityType,f0.postcode,f0.phoneNumber,f0.email,f0.capacity,f0.openingHours,f0.managerName,f0.specialitiesOffered};
            SimpleFormDialog d = new SimpleFormDialog(owner, "Edit Facility", labels, init, new int[]{1,2}, new int[]{0});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            Facility f = new Facility(f0.facilityId, r[1], r[2], f0.address, r[3], r[4], r[5], r[7], r[8], r[6], r[9]);
            ctl.set(row, f);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void deleteFacility(){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a facility first."); return; }
        int row = table.convertRowIndexToModel(v);

        if (!Message.confirm("Delete selected facility? This will auto-save to CSV.")) return;
        try { ctl.remove(row); refresh(); }
        catch (Exception ex){ Message.error(ex); }
    }

    private void refresh(){
        try { ctl.reloadAll(); model.setData(ctl.all()); }
        catch (Exception ex){ Message.error(ex); }
    }

    static class FacilitiesTableModel extends AbstractTableModel {
        private List<Facility> data;
        private final String[] cols = {"Facility ID","Name","Type","Postcode","Phone","Email","Capacity","Opening hours"};

        FacilitiesTableModel(List<Facility> data){ this.data = data; }
        void setData(List<Facility> data){ this.data=data; fireTableDataChanged(); }

        public int getRowCount(){ return data.size(); }
        public int getColumnCount(){ return cols.length; }
        public String getColumnName(int c){ return cols[c]; }
        public Object getValueAt(int r,int c){
            Facility x = data.get(r);
            return switch(c){
                case 0 -> x.facilityId;
                case 1 -> x.facilityName;
                case 2 -> x.facilityType;
                case 3 -> x.postcode;
                case 4 -> x.phoneNumber;
                case 5 -> x.email;
                case 6 -> x.capacity;
                default -> x.openingHours;
            };
        }
    }
}
