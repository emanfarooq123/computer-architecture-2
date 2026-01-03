package view;

import controller.*;
import persistence.Repository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardFrame extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel cardPanel = new JPanel(cards);

    private JButton activeButton;

    // Colors
    private static final Color SIDEBAR_BG = new Color(28, 30, 36);
    private static final Color BTN_DEFAULT = new Color(28, 30, 36);
    private static final Color BTN_HOVER = new Color(45, 48, 56);
    private static final Color BTN_ACTIVE = new Color(60, 65, 85);
    private static final Color BTN_TEXT = new Color(220, 225, 235);

    public DashboardFrame() {
        super("Healthcare Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 700));

        // Data init
        Repository repo = Repository.get();
        String folder = "data";
        try {
            repo.loadAll(folder);
        } catch (Exception ex) {
            Message.error(ex);
        }

        // Controllers
        PatientController patientCtl = new PatientController(folder);
        ClinicianController clinicianCtl = new ClinicianController(folder);
        FacilityController facilityCtl = new FacilityController(folder);
        StaffController staffCtl = new StaffController(folder);
        AppointmentController appointmentCtl = new AppointmentController(folder);
        PrescriptionController prescriptionCtl = new PrescriptionController(folder);
        ReferralController referralCtl = new ReferralController(folder);

        // Views
        cardPanel.add(new AppointmentsPanel(this, appointmentCtl), "appointments");
        cardPanel.add(new PatientsPanel(this, patientCtl), "patients");
        cardPanel.add(new CliniciansPanel(this, clinicianCtl), "clinicians");
        cardPanel.add(new FacilitiesPanel(this, facilityCtl), "facilities");
        cardPanel.add(new StaffPanel(this, staffCtl), "staff");
        cardPanel.add(new PrescriptionsPanel(this, prescriptionCtl), "prescriptions");
        cardPanel.add(new ReferralsPanel(this, referralCtl), "referrals");

        setLayout(new BorderLayout());
        add(sidebar(), BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);

        cards.show(cardPanel, "appointments");
        setLocationRelativeTo(null);
    }

    private JComponent sidebar() {
        JPanel side = new JPanel(new BorderLayout());
        side.setPreferredSize(new Dimension(240, 10));
        side.setBackground(SIDEBAR_BG);
        side.setBorder(BorderFactory.createMatteBorder(
                0, 0, 0, 1, new Color(45, 48, 56)));

        // ===== Header =====
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(20, 18, 12, 18));
        header.setBackground(SIDEBAR_BG);

        JLabel title = new JLabel("Clinic Console");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Healthcare Management System");
        subtitle.setFont(subtitle.getFont().deriveFont(13f));
        subtitle.setForeground(new Color(160, 165, 175));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(14));
        header.add(separator);

        // ===== Navigation =====
        JPanel nav = new JPanel(new GridLayout(0, 1, 0, 10));
        nav.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));
        nav.setBackground(SIDEBAR_BG);

        nav.add(navBtn("Appointments", "appointments"));
        nav.add(navBtn("Patients", "patients"));
        nav.add(navBtn("Clinicians", "clinicians"));
        nav.add(navBtn("Facilities", "facilities"));
        nav.add(navBtn("Staff", "staff"));
        nav.add(navBtn("Prescriptions", "prescriptions"));
        nav.add(navBtn("Referrals", "referrals"));

        side.add(header, BorderLayout.NORTH);
        side.add(nav, BorderLayout.CENTER);

        return side;
    }

    private JButton navBtn(String label, String key) {
        JButton b = new JButton(label);

        // Center text
        b.setHorizontalAlignment(SwingConstants.CENTER);

        // Size & font
        b.setPreferredSize(new Dimension(200, 44));
        b.setFont(b.getFont().deriveFont(14f));

        // Flat style
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setContentAreaFilled(true);
        b.setOpaque(true);

        // Colors
        b.setBackground(BTN_DEFAULT);
        b.setForeground(BTN_TEXT);

        // Hover + active behavior
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (b != activeButton) {
                    b.setBackground(BTN_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (b != activeButton) {
                    b.setBackground(BTN_DEFAULT);
                }
            }
        });

        b.addActionListener(e -> {
            cards.show(cardPanel, key);
            setActiveButton(b);
        });

        // First button active by default
        if (activeButton == null) {
            setActiveButton(b);
        }

        return b;
    }

    private void setActiveButton(JButton b) {
        if (activeButton != null) {
            activeButton.setBackground(BTN_DEFAULT);
        }
        activeButton = b;
        activeButton.setBackground(BTN_ACTIVE);
    }
}
