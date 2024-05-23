package com.example.vp3.JFrames;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MahasiswaFrame {
    private DefaultTableModel tableModel;
    private JTable table;

    public MahasiswaFrame() {
        JFrame jFrame = new JFrame("Aplikasi Mahasiswa");
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nama");
        tableModel.addColumn("NIM");

        loadDataFromDatabase();

        table = new JTable(tableModel);
        JScrollPane pane = new JScrollPane(table);

        JButton button = new JButton("Tambah Mahasiswa");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createInputFrame();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(button, BorderLayout.NORTH);
        panel.add(pane, BorderLayout.CENTER);

        jFrame.getContentPane().add(panel);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setBounds(100, 100, 400, 300);
    }

    private void loadDataFromDatabase() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/mahasiswaku?" +
                    "user=root&password=");
            String query = "SELECT * FROM mahasiswa";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            // Menghapus semua baris yang ada di model tabel
            tableModel.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                String nim = rs.getString("nim");
                tableModel.addRow(new Object[]{id, nama, nim});
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void createInputFrame() {
        JFrame inputFrame = new JFrame("Tambah Mahasiswa");

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField nimField = new JTextField();
        JButton saveButton = new JButton("Simpan");

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Nama:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("NIM:"));
        inputPanel.add(nimField);
        inputPanel.add(new JLabel());
        inputPanel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String name = nameField.getText();
                String nim = nimField.getText();

                Connection conn = null;
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://localhost/mahasiswaku?" +
                            "user=root&password=");
                    String query = "INSERT INTO mahasiswa (id, nama, nim) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, nim);
                    preparedStatement.executeUpdate();

                    loadDataFromDatabase();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (conn != null) conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

                inputFrame.dispose();
            }
        });

        inputFrame.getContentPane().add(inputPanel);
        inputFrame.pack();
        inputFrame.setVisible(true);
        inputFrame.setBounds(150, 150, 300, 200);
    }
}
