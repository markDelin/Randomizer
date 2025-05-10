import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GroupRandomizerGUI extends JFrame {
    private ArrayList<String> names = new ArrayList<>();
    private int groupSize = 2; // default people per group
    private int numberOfGroups = 0; // 0 means not set
    private boolean useGroupSize = true; // true=group size, false=number of groups
    private Random random = new Random();
    
    private DefaultListModel<String> namesListModel = new DefaultListModel<>();
    private JList<String> namesList = new JList<>(namesListModel);
    private JLabel statusLabel = new JLabel("Ready");
    private JLabel groupingMethodLabel = new JLabel("Grouping method: " + groupSize + " people per group");
    
    public GroupRandomizerGUI() {
        // Initialize with default names
        names.add("Rowena Remolin");
        names.add("Julyannah Cazandra Alcoran");
        names.add("Reymark Delin");
        names.add("Jasper James Delgado");
        names.add("Khim Joy Calambas");
        names.add("Adrian Gapi");
        updateNamesList();
        
        setTitle("Group Randomizer System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title label
        JLabel titleLabel = new JLabel("GROUP RANDOMIZER SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Center panel for names list
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Names List (" + names.size() + ")"));
        
        namesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(namesList);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel for name management
        JPanel nameButtonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton addButton = new JButton("Add Names");
        JButton removeButton = new JButton("Remove Selected");
        
        addButton.addActionListener(e -> addNames());
        removeButton.addActionListener(e -> removeSelectedNames());
        
        nameButtonPanel.add(addButton);
        nameButtonPanel.add(removeButton);
        centerPanel.add(nameButtonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Right panel for controls
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        // Grouping settings panel
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Group Settings"));
        
        groupingMethodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        settingsPanel.add(groupingMethodLabel);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        ButtonGroup groupMethodGroup = new ButtonGroup();
        JRadioButton groupSizeRadio = new JRadioButton("People per group", useGroupSize);
        JRadioButton numGroupsRadio = new JRadioButton("Number of groups", !useGroupSize);
        groupMethodGroup.add(groupSizeRadio);
        groupMethodGroup.add(numGroupsRadio);
        
        groupSizeRadio.addActionListener(e -> {
            useGroupSize = true;
            updateGroupingMethodLabel();
        });
        
        numGroupsRadio.addActionListener(e -> {
            useGroupSize = false;
            updateGroupingMethodLabel();
        });
        
        settingsPanel.add(groupSizeRadio);
        settingsPanel.add(numGroupsRadio);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sizePanel.add(new JLabel("Value:"));
        JTextField sizeField = new JTextField(5);
        sizeField.setText(String.valueOf(useGroupSize ? groupSize : numberOfGroups));
        sizePanel.add(sizeField);
        
        JButton setSizeButton = new JButton("Set");
        setSizeButton.addActionListener(e -> {
            try {
                int value = Integer.parseInt(sizeField.getText());
                if (value < 1) {
                    JOptionPane.showMessageDialog(this, "Value must be at least 1", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (useGroupSize) {
                    if (value > names.size()) {
                        JOptionPane.showMessageDialog(this, 
                            "Group size cannot be larger than number of names (" + names.size() + ")", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    groupSize = value;
                } else {
                    if (value > names.size()) {
                        JOptionPane.showMessageDialog(this, 
                            "Number of groups cannot be larger than number of names (" + names.size() + ")", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    numberOfGroups = value;
                }
                updateGroupingMethodLabel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        sizePanel.add(setSizeButton);
        
        settingsPanel.add(sizePanel);
        rightPanel.add(settingsPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Randomize button
        JButton randomizeButton = new JButton("Randomize Groups");
        randomizeButton.addActionListener(e -> randomizeGroups());
        rightPanel.add(randomizeButton);
        
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        // Status bar
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void updateNamesList() {
        namesListModel.clear();
        for (String name : names) {
            namesListModel.addElement(name);
        }
        namesList.setModel(namesListModel);
    }
    
    private void updateGroupingMethodLabel() {
        groupingMethodLabel.setText("Grouping method: " + 
            (useGroupSize ? 
                (groupSize + " people per group") : 
                (numberOfGroups + " groups")));
    }
    
    private void addNames() {
        String input = JOptionPane.showInputDialog(this, "Enter names to add (separate by commas):");
        if (input != null && !input.trim().isEmpty()) {
            String[] namesToAdd = input.split(",");
            for (String name : namesToAdd) {
                if (!name.trim().isEmpty()) {
                    names.add(name.trim());
                }
            }
            updateNamesList();
            statusLabel.setText(namesToAdd.length + " names added successfully!");
        }
    }
    
    private void removeSelectedNames() {
        int[] selectedIndices = namesList.getSelectedIndices();
        if (selectedIndices.length == 0) {
            statusLabel.setText("No names selected to remove");
            return;
        }
        
        // Remove from highest index to lowest to avoid shifting issues
        for (int i = selectedIndices.length - 1; i >= 0; i--) {
            names.remove(selectedIndices[i]);
        }
        
        updateNamesList();
        statusLabel.setText(selectedIndices.length + " names removed successfully!");
    }
    
    private void randomizeGroups() {
        if (names.size() < 2) {
            statusLabel.setText("Need at least 2 names to form groups");
            return;
        }
        
        // Calculate groups based on current settings
        int actualGroupSize;
        if (useGroupSize) {
            actualGroupSize = groupSize;
        } else {
            // Calculate group size based on number of groups
            if (numberOfGroups == 0) {
                statusLabel.setText("Please set number of groups first");
                return;
            }
            actualGroupSize = (int) Math.ceil((double) names.size() / numberOfGroups);
        }
        
        // Shuffle the names immediately (no animation)
        Collections.shuffle(names, random);
        
        // Show the groups directly
        showGroups(actualGroupSize);
    }
    
    private void showGroups(int actualGroupSize) {
        // Create groups
        List<List<String>> groups = new ArrayList<>();
        
        if (useGroupSize) {
            // Group by size
            for (int i = 0; i < names.size(); i += actualGroupSize) {
                int end = Math.min(i + actualGroupSize, names.size());
                groups.add(new ArrayList<>(names.subList(i, end)));
            }
        } else {
            // Group by number of groups
            int namesPerGroup = names.size() / numberOfGroups;
            int remainder = names.size() % numberOfGroups;
            int index = 0;
            
            for (int i = 0; i < numberOfGroups; i++) {
                int size = namesPerGroup + (i < remainder ? 1 : 0);
                groups.add(new ArrayList<>(names.subList(index, index + size)));
                index += size;
            }
        }
        
        // Create dialog to display groups
        JDialog groupsDialog = new JDialog(this, "Randomized Groups", true);
        groupsDialog.setSize(400, 400);
        groupsDialog.setLocationRelativeTo(this);
        groupsDialog.setLayout(new BorderLayout());
        
        JPanel groupsPanel = new JPanel();
        groupsPanel.setLayout(new BoxLayout(groupsPanel, BoxLayout.Y_AXIS));
        
        for (int i = 0; i < groups.size(); i++) {
            JPanel groupPanel = new JPanel();
            groupPanel.setLayout(new BorderLayout());
            groupPanel.setBorder(BorderFactory.createTitledBorder("Group " + (i + 1)));
            
            DefaultListModel<String> groupModel = new DefaultListModel<>();
            for (String member : groups.get(i)) {
                groupModel.addElement(member);
            }
            
            JList<String> groupList = new JList<>(groupModel);
            groupPanel.add(new JScrollPane(groupList), BorderLayout.CENTER);
            groupsPanel.add(groupPanel);
            groupsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        groupsDialog.add(new JScrollPane(groupsPanel), BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> groupsDialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        groupsDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        groupsDialog.setVisible(true);
        statusLabel.setText("Groups randomized successfully!");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GroupRandomizerGUI gui = new GroupRandomizerGUI();
            gui.setVisible(true);
        });
    }
}
