// AgendaGUI.java
package agenda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AgendaGUI extends JFrame {
    private final ContactManager manager;
    private final DefaultListModel<Contact> listModel;
    private final JList<Contact> contactList;
    private final JTextField tfName, tfPhone, tfEmail, tfExtra; // tfExtra: birthday or company
    private final JComboBox<String> cbType;

    public AgendaGUI() {
        super("Agenda POO - Java Swing");
        manager = new ContactManager();

        // GUI setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8,8));

        // Left: Lista
        listModel = new DefaultListModel<>();
        contactList = new JList<>(listModel);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane leftScroll = new JScrollPane(contactList);
        leftScroll.setPreferredSize(new Dimension(380, 0));
        add(leftScroll, BorderLayout.WEST);

        // Right: formulario y botones
        JPanel right = new JPanel();
        right.setLayout(new BorderLayout(6,6));
        add(right, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(6,2,6,6));
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        form.add(new JLabel("Tipo:"));
        cbType = new JComboBox<>(new String[]{"Personal", "Business"});
        form.add(cbType);

        form.add(new JLabel("Nombre:"));
        tfName = new JTextField();
        form.add(tfName);

        form.add(new JLabel("Teléfono:"));
        tfPhone = new JTextField();
        form.add(tfPhone);

        form.add(new JLabel("Email:"));
        tfEmail = new JTextField();
        form.add(tfEmail);

        form.add(new JLabel("Fecha o Compañía:"));
        tfExtra = new JTextField();
        form.add(tfExtra);

        form.add(new JLabel("Relación / Cargo:"));
        // using same tfExtra for simplicity and popup later for jobTitle or relation if needed
        // add a helper label
        form.add(new JLabel("<html>Si Personal: relación<br>Si Business: cargo</html>"));

        right.add(form, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton btnAdd = new JButton("Añadir");
        JButton btnDelete = new JButton("Eliminar");
        JButton btnEdit = new JButton("Editar");
        JButton btnSearch = new JButton("Buscar");
        JButton btnRefresh = new JButton("Refrescar");
        buttons.add(btnAdd);
        buttons.add(btnEdit);
        buttons.add(btnDelete);
        buttons.add(btnSearch);
        buttons.add(btnRefresh);

        right.add(buttons, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(4,4));
        JTextField tfSearch = new JTextField();
        bottom.add(new JLabel("Buscar por nombre:"), BorderLayout.WEST);
        bottom.add(tfSearch, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Events
        btnAdd.addActionListener(e -> onAdd());
        btnDelete.addActionListener(e -> onDelete());
        btnEdit.addActionListener(e -> onEdit());
        btnSearch.addActionListener(e -> {
            String q = tfSearch.getText();
            updateList(manager.searchByName(q));
        });
        btnRefresh.addActionListener(e -> updateList(manager.getAllContacts()));

        contactList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) loadSelectedToForm();
            }
        });

        // seed example data
        seedExampleData();
        updateList(manager.getAllContacts());
    }

    private void seedExampleData() {
        manager.addContact(new PersonalContact("Ana Lopez", "3101234567", "ana@mail.com", "05/07/2005", "Amiga"));
        manager.addContact(new BusinessContact("Carlos Perez", "3127654321", "carlos@empresa.com", "Innova S.A.", "Gerente"));
    }

    private void onAdd() {
        String type = (String) cbType.getSelectedItem();
        String name = tfName.getText().trim();
        String phone = tfPhone.getText().trim();
        String email = tfEmail.getText().trim();
        String extra = tfExtra.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ("Personal".equals(type)) {
            // treat extra as birthday; ask for relation with input dialog
            String relation = JOptionPane.showInputDialog(this, "Relación (ej. Amigo, Familiar):", "Relación", JOptionPane.PLAIN_MESSAGE);
            if (relation == null) return; // cancel pressed
            PersonalContact p = new PersonalContact(name, phone, email, extra, relation);
            manager.addContact(p);
        } else {
            // Business
            String company = extra;
            String job = JOptionPane.showInputDialog(this, "Cargo / Puesto:", "Cargo", JOptionPane.PLAIN_MESSAGE);
            if (job == null) return;
            BusinessContact b = new BusinessContact(name, phone, email, company, job);
            manager.addContact(b);
        }
        clearForm();
        updateList(manager.getAllContacts());
    }

    private void onDelete() {
        Contact sel = contactList.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un contacto para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int opt = JOptionPane.showConfirmDialog(this, "Eliminar: " + sel.getName() + " ?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            manager.removeContact(sel);
            updateList(manager.getAllContacts());
        }
    }

    private void onEdit() {
        Contact sel = contactList.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un contacto para editar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // Simple edit: load form, delete old and let user Add again (to keep example simple)
        loadSelectedToForm();
        manager.removeContact(sel);
        updateList(manager.getAllContacts());
    }

    private void loadSelectedToForm() {
        Contact sel = contactList.getSelectedValue();
        if (sel == null) return;
        tfName.setText(sel.getName());
        tfPhone.setText(sel.getPhone());
        tfEmail.setText(sel.getEmail());

        if (sel instanceof PersonalContact) {
            cbType.setSelectedItem("Personal");
            PersonalContact p = (PersonalContact) sel;
            tfExtra.setText(p.getBirthday());
            // we can't set relation in simple label; will be requested on add
        } else if (sel instanceof BusinessContact) {
            cbType.setSelectedItem("Business");
            BusinessContact b = (BusinessContact) sel;
            tfExtra.setText(b.getCompany());
            // jobTitle will be asked when re-adding
        }
    }

    private void updateList(java.util.List<Contact> data) {
        listModel.clear();
        for (Contact c : data) listModel.addElement(c);
    }

    private void clearForm() {
        tfName.setText("");
        tfPhone.setText("");
        tfEmail.setText("");
        tfExtra.setText("");
    }

    public static void main(String[] args) {
        // Run GUI on EDT
        SwingUtilities.invokeLater(() -> {
            AgendaGUI gui = new AgendaGUI();
            gui.setVisible(true);
        });
    }
}
