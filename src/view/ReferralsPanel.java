package view;

import controller.ReferralController;
import model.Referral;
import persistence.Repository;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ReferralsPanel extends JPanel {
    private final ReferralController ctl;
    private final JTable table;
    private final ReferralsTableModel model;
    private final CrudHeader header;
    private final JButton export = new JButton("Export Letter");

    public ReferralsPanel(Frame owner, ReferralController ctl) {
        super(new BorderLayout(12,12));
        this.ctl = ctl;

        header = new CrudHeader("Referrals", "Add/Edit/Delete/Export Referrals • Data saved/fetched from CSV");
        JPanel headerWrap = new JPanel(new BorderLayout());
        headerWrap.setOpaque(false);
        headerWrap.add(header, BorderLayout.CENTER);

        JPanel extra = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,5));
        extra.setOpaque(false);
        extra.add(export);
        headerWrap.add(extra, BorderLayout.SOUTH);

        model = new ReferralsTableModel(ctl.all());
        table = new JTable(model);
        Ui.styleTable(table);
        FilterUtil.attach(header.search, table);

        header.add.addActionListener(e -> addReferral(owner));
        header.edit.addActionListener(e -> editReferral(owner));
        header.del.addActionListener(e -> deleteReferral());
        header.refresh.addActionListener(e -> refresh());
        export.addActionListener(e -> exportSelected());

        add(Ui.pad(headerWrap), BorderLayout.NORTH);
        add(Ui.pad(new JScrollPane(table)), BorderLayout.CENTER);
    }

    private void addReferral(Frame owner){
        try {
            Referral seed = ctl.blank();
            String[] labels = {"Patient ID","Referring clinician ID","Referred-to clinician ID","Urgency","Reason","Status"};
            String[] init = {"","","","Routine","", "Created"};
            SimpleFormDialog d = new SimpleFormDialog(owner, "New Referral", labels, init, new int[]{0,1,4});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            String today = Repository.today();
            Referral ref = new Referral(seed.referralId, r[0], r[1], r[2], "", "", today, r[3], r[4], "", "", r[5], "", "", today, today);
            ctl.add(ref);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void editReferral(Frame owner){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a referral first."); return; }
        int row = table.convertRowIndexToModel(v);

        try {
            Referral r0 = ctl.all().get(row);
            String[] labels = {"Referral ID","Patient ID","Referring clinician ID","Referred-to clinician ID","Urgency","Reason","Status"};
            String[] init = {r0.referralId,r0.patientId,r0.referringClinicianId,r0.referredToClinicianId,r0.urgencyLevel,r0.referralReason,r0.status};
            SimpleFormDialog d = new SimpleFormDialog(owner, "Edit Referral", labels, init, new int[]{1,2,5}, new int[]{0});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            Referral ref = new Referral(r0.referralId, r[1], r[2], r[3], r0.referringFacilityId, r0.referredToFacilityId, r0.referralDate,
                    r[4], r[5], r0.clinicalSummary, r0.requestedInvestigations, r[6], r0.appointmentId, r0.notes, r0.createdDate, Repository.today());
            ctl.set(row, ref);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void deleteReferral(){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a referral first."); return; }
        int row = table.convertRowIndexToModel(v);

        if (!Message.confirm("Delete selected referral? This will auto-save to CSV.")) return;
        try { ctl.remove(row); refresh(); }
        catch (Exception ex){ Message.error(ex); }
    }

    private void exportSelected(){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a referral first."); return; }
        int row = table.convertRowIndexToModel(v);
        try {
            File f = ctl.exportLetter(ctl.all().get(row));
            Message.info("Exported: " + f.getPath());
        } catch (Exception ex){ Message.error(ex); }
    }

    private void refresh(){
        try { ctl.reloadAll(); model.setData(ctl.all()); }
        catch (Exception ex){ Message.error(ex); }
    }

    static class ReferralsTableModel extends AbstractTableModel {
        private List<Referral> data;
        private final String[] cols = {"Referral ID","Patient ID","Referring clinician","Referred-to clinician","Urgency","Reason","Status"};

        ReferralsTableModel(List<Referral> data){ this.data = data; }
        void setData(List<Referral> data){ this.data=data; fireTableDataChanged(); }

        public int getRowCount(){ return data.size(); }
        public int getColumnCount(){ return cols.length; }
        public String getColumnName(int c){ return cols[c]; }
        public Object getValueAt(int r,int c){
            Referral x = data.get(r);
            return switch(c){
                case 0 -> x.referralId;
                case 1 -> x.patientId;
                case 2 -> x.referringClinicianId;
                case 3 -> x.referredToClinicianId;
                case 4 -> x.urgencyLevel;
                case 5 -> x.referralReason;
                default -> x.status;
            };
        }
    }
}
