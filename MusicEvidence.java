import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MusicEvidence {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Skladby> skladbyList;

    public MusicEvidence() {
        skladbyList = new ArrayList<>();

        frame = new JFrame("Evidence skladeb");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Název","Autor","Délka skladby (s)","Cena"}, 0);
        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Přidat skladbu");
        addButton.addActionListener(new AddButtonActionListener());
        buttonsPanel.add(addButton);

        JButton deleteButton = new JButton("Smazat skladbu");
        deleteButton.addActionListener(new DeleteButtonActionListener());
        buttonsPanel.add(deleteButton);

        JButton exportButton = new JButton("Exportovat do souboru");
        exportButton.addActionListener(new ExportButtonActionListener());
        buttonsPanel.add(exportButton);

        JButton calculateButton = new JButton("Spočítat průměry");
        calculateButton.addActionListener(new CalculateButtonActionListener());
        buttonsPanel.add(calculateButton);

        frame.add(buttonsPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    private class AddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nazev = JOptionPane.showInputDialog("Zadejte název skladby:");
            String autor = JOptionPane.showInputDialog("Zadejte autora:");
            String delka = JOptionPane.showInputDialog("Zadejte délku skladby:");
            String cena = JOptionPane.showInputDialog("Zadejte cenu:");

            if (nazev != null && autor != null && delka != null && cena != null) {
                Skladby skladby = new Skladby(nazev, autor, Double.parseDouble(delka), Double.parseDouble(cena));
                skladbyList.add(skladby);
                tableModel.addRow(new Object[]{skladby.getNazev(), skladby.getAutor(), skladby.getDelka(), skladby.getCena()});
            }
        }
    }

    private class DeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                skladbyList.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            }
        }
    }

    private class ExportButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (FileWriter writer = new FileWriter("skladby.txt")) {
                for (Skladby skladby : skladbyList) {
                    writer.write(skladby.getNazev() + ";" + skladby.getAutor() + ";" + skladby.getDelka() + ";" + skladby.getCena() + "\n");
                }
                JOptionPane.showMessageDialog(frame, "Údaje byly úspěšně exportovány do souboru skladby.txt");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Chyba při exportu do souboru: " + ex.getMessage());
            }
        }
    }

    private class CalculateButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double sumCena = 0;
            double prumCena = 0;


            for (Skladby skladby : skladbyList) {
                sumCena += skladby.getCena();
            }

            double avgCena = sumCena / skladbyList.size();

            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            JOptionPane.showMessageDialog(frame,
                    "Průměrná cena skladby: " + decimalFormat.format(avgCena) + "\n" +
                            "Celková cena skladeb: " + decimalFormat.format(sumCena));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MusicEvidence::new);
    }

    private static class Skladby {
        private String nazev;
        private String autor;
        private double delka;
        private double cena;

        public Skladby(String nazev, String autor, double delka, double cena) {
            this.nazev = nazev;
            this.autor = autor;
            this.delka = delka;
            this.cena = cena;
        }

        public String getNazev() {
            return nazev;
        }

        public String getAutor() {
            return autor;
        }

        public double getDelka() {
            return delka;
        }

        public double getCena() {
            return cena;
        }
    }
}