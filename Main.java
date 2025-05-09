// import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.List;

public class Main extends JFrame {
    private JTextArea namesInput;
    private JSpinner groupSizeSpinner;
    private JList<String> resultList;
    private DefaultListModel<String> listModel;
    private JButton randomizeButton;
    private JButton copyButton;
    private JCheckBox darkModeCheckbox;
    private JComboBox<String> presetNamesCombo;

    private final String[][] PRESET_NAMES = {
        {"Student 1", "Student 2", "Student 3", "Student 4", "Student 5", "Student 6", "Student 7", "Student 8", "Student 9", "Student 10"},
        {"Leb", "Khim", "Jasper", "Adrian", "Carlos", "Cyrus", "Miguel", "Rowena", "Rico", "Raffy"}
    };

    private final String[] PRESET_CATEGORIES = {
        "Sample Student Numbers",
        "Sample Names"
    };


    public Main() {
        initComponents();
        setModernLook();
    }

    private void initComponents() {
        // Main window setup
        setTitle("Modern Group Randomizer");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with modern border
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Input area
        namesInput = new JTextArea();
        namesInput.setLineWrap(true);
        namesInput.setBorder(BorderFactory.createTitledBorder("Enter Names (One per line)"));

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        controlPanel.add(new JLabel("Group Size:"));
        groupSizeSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
        controlPanel.add(groupSizeSpinner);

        randomizeButton = new JButton("Randomize Groups");
        randomizeButton.addActionListener(e -> randomizeGroups());
        controlPanel.add(randomizeButton);

        darkModeCheckbox = new JCheckBox("Dark Mode");
        darkModeCheckbox.addActionListener(e -> toggleDarkMode());
        controlPanel.add(darkModeCheckbox);

        // Preset Names Dropdown
        presetNamesCombo = new JComboBox<>(PRESET_CATEGORIES);
        presetNamesCombo.addActionListener(e -> loadPresetNames());
        controlPanel.add(presetNamesCombo);

        // Result area
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setFont(new Font("Arial", Font.PLAIN, 14));
        resultList.setBorder(BorderFactory.createTitledBorder("Randomized Groups"));

        // Copy button
        copyButton = new JButton("Copy Results");
        copyButton.addActionListener(e -> copyToClipboard());

        // Layout
        mainPanel.add(new JScrollPane(namesInput), BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(resultList), BorderLayout.CENTER);
        controlPanel.setPreferredSize(new Dimension(600, 40));
        mainPanel.add(copyButton, BorderLayout.PAGE_END);

        add(mainPanel);
    }

    private void loadPresetNames() {
        int selectedIndex = presetNamesCombo.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < PRESET_NAMES.length) {
            StringBuilder names = new StringBuilder();
            for (String name : PRESET_NAMES[selectedIndex]) {
                names.append(name).append("\n");
            }
            namesInput.setText(names.toString());
        }
    }

    private void setModernLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void toggleDarkMode() {
        try {
            if (darkModeCheckbox.isSelected()) {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
            } else {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            }
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void randomizeGroups() {
        String inputText = namesInput.getText().trim();
        if (inputText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter some names first!", "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] names = inputText.split("\\r?\\n");
        List<String> nameList = new ArrayList<>();
        for (String name : names) {
            String trimmedName = name.trim();
            if (!trimmedName.isEmpty()) {
                nameList.add(trimmedName);
            }
        }

        if (nameList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter valid names!", "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int groupSize = (int) groupSizeSpinner.getValue();
        if (groupSize > nameList.size()) {
            JOptionPane.showMessageDialog(this, 
                "Group size cannot be larger than the number of names!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Collections.shuffle(nameList);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < nameList.size(); i += groupSize) {
            result.append("Group ").append((i / groupSize) + 1).append(":\n");
            for (int j = i; j < Math.min(i + groupSize, nameList.size()); j++) {
                result.append("• ").append(nameList.get(j)).append("\n");
            }
            result.append("\n");
        }

        listModel.clear();
        String[] groups = result.toString().split("\n");
        for (String group : groups) {
            if (!group.trim().isEmpty()) {
                listModel.addElement(group);
            }
        }
    }

    private void copyToClipboard() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < listModel.getSize(); i++) {
            text.append(listModel.getElementAt(i)).append("\n");
        }
        StringSelection selection = new StringSelection(text.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        JOptionPane.showMessageDialog(this, "Results copied to clipboard!", "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
