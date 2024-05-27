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

public class LahveEvidence {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Lahve> lahveList;

    public LahveEvidence() {
        lahveList = new ArrayList<>();

        frame = new JFrame("Evidencia plastových lahví");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Objem (ml)", "Barva", "Cena bez DPH", "DPH (%)"}, 0);
        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Přidat láhev");
        addButton.addActionListener(new AddButtonActionListener());
        buttonsPanel.add(addButton);

        JButton deleteButton = new JButton("Smazat láhev");
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
            String objem = JOptionPane.showInputDialog("Zadejte objem láhve (ml):");
            String barva = JOptionPane.showInputDialog("Zadejte barvu láhve:");
            String cenaBezDph = JOptionPane.showInputDialog("Zadejte cenu láhve bez DPH:");
            String dph = JOptionPane.showInputDialog("Zadejte DPH (%):");

            if (objem != null && barva != null && cenaBezDph != null && dph != null) {
                Lahve lahve = new Lahve(Integer.parseInt(objem), barva, Double.parseDouble(cenaBezDph), Double.parseDouble(dph));
                lahveList.add(lahve);
                tableModel.addRow(new Object[]{lahve.getObjem(), lahve.getBarva(), lahve.getCenaBezDph(), lahve.getDph()});
            }
        }
    }

    private class DeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                lahveList.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            }
        }
    }

    private class ExportButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (FileWriter writer = new FileWriter("lahve.txt")) {
                for (Lahve lahve : lahveList) {
                    writer.write(lahve.getObjem() + ";" + lahve.getBarva() + ";" + lahve.getCenaBezDph() + ";" + lahve.getDph() + "\n");
                }
                JOptionPane.showMessageDialog(frame, "Údaje byly úspěšně exportovány do souboru lahve.txt");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Chyba při exportu do souboru: " + ex.getMessage());
            }
        }
    }

    private class CalculateButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double sumCenaBezDph = 0;
            double sumCenaSdph = 0;
            int sumObjem = 0;

            for (Lahve lahve : lahveList) {
                sumCenaBezDph += lahve.getCenaBezDph();
                sumCenaSdph += lahve.getCenaBezDph() * (1 + lahve.getDph() / 100);
                sumObjem += lahve.getObjem();
            }

            double avgCenaBezDph = sumCenaBezDph / lahveList.size();
            double avgCenaSdph = sumCenaSdph / lahveList.size();
            double avgObjem = (double) sumObjem / lahveList.size();

            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            JOptionPane.showMessageDialog(frame,
                    "Průměrná cena láhve bez DPH: " + decimalFormat.format(avgCenaBezDph) + "\n" +
                            "Průměrná cena láhve s DPH: " + decimalFormat.format(avgCenaSdph) + "\n" +
                            "Průměrný objem láhve: " + decimalFormat.format(avgObjem));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LahveEvidence::new);
    }

    private static class Lahve {
        private int objem;
        private String barva;
        private double cenaBezDph;
        private double dph;

        public Lahve(int objem, String barva, double cenaBezDph, double dph) {
            this.objem = objem;
            this.barva = barva;
            this.cenaBezDph = cenaBezDph;
            this.dph = dph;
        }

        public int getObjem() {
            return objem;
        }

        public String getBarva() {
            return barva;
        }

        public double getCenaBezDph() {
            return cenaBezDph;
        }

        public double getDph() {
            return dph;
        }
    }
}