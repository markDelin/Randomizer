import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class randomizer {
    private static int groupSize = 2; // default people per group
    private static int numberOfGroups = 0; // 0 means not set
    private static boolean useGroupSize = true; // true=group size, false=number of groups

    public static void main(String[] args) throws InterruptedException {
        // Initialize with some default names
        ArrayList<String> names = new ArrayList<>();
        names.add("Rowena Remolin");
        names.add("Julyannah Cazandra Alcoran");
        names.add("Reymark Delin");
        names.add("Jasper James Delgado");
        names.add("Khim Joy Calambas");
        names.add("Adrian Gapi");
        
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        
        System.out.println("╔════════════════════════════════╗");
        System.out.println("║      GROUP RANDOMIZER SYSTEM   ║");
        System.out.println("╚════════════════════════════════╝");
        
        while (true) {
            System.out.println("\nCurrent names (" + names.size() + "):");
            displayNames(names);
            System.out.println("\nCurrent grouping method: " + 
                (useGroupSize ? 
                    (groupSize + " people per group") : 
                    (numberOfGroups + " groups")));
            System.out.println("\nMenu:");
            System.out.println("1. Add more names");
            System.out.println("2. Remove names");
            System.out.println("3. Change group settings");
            System.out.println("4. Randomize groups (with animation)");
            System.out.println("5. Exit");
            System.out.print("\nChoose an option: ");
            
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number (1-5)");
                continue;
            }
            
            switch (choice) {
                case 1:
                    System.out.print("\nEnter names to add (separate by commas): ");
                    String newNames = scanner.nextLine();
                    String[] namesToAdd = newNames.split(",");
                    for (String name : namesToAdd) {
                        if (!name.trim().isEmpty()) {
                            names.add(name.trim());
                        }
                    }
                    System.out.println("\n" + namesToAdd.length + " names added successfully!");
                    break;
                    
                case 2:
                    System.out.println("\nCurrent names:");
                    displayNames(names);
                    System.out.print("\nEnter names to remove (separate by commas): ");
                    String namesToRemove = scanner.nextLine();
                    String[] removeNames = namesToRemove.split(",");
                    int removedCount = 0;
                    for (String name : removeNames) {
                        if (names.remove(name.trim())) {
                            removedCount++;
                        }
                    }
                    System.out.println("\n" + removedCount + " names removed successfully!");
                    break;
                    
                case 3:
                    System.out.println("\nCurrent grouping method: " + 
                        (useGroupSize ? 
                            (groupSize + " people per group") : 
                            (numberOfGroups + " groups")));
                    System.out.println("\nGrouping options:");
                    System.out.println("1. Set number of people per group");
                    System.out.println("2. Set number of groups");
                    System.out.print("Choose grouping method (1-2): ");
                    
                    int groupingChoice;
                    try {
                        groupingChoice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid choice. Keeping current settings.");
                        break;
                    }
                    
                    if (groupingChoice == 1) {
                        System.out.print("\nEnter number of people per group (current: " + groupSize + "): ");
                        try {
                            int newSize = Integer.parseInt(scanner.nextLine());
                            if (newSize < 1) {
                                System.out.println("Group size must be at least 1");
                            } else if (newSize > names.size()) {
                                System.out.println("Group size cannot be larger than number of names (" + names.size() + ")");
                            } else {
                                groupSize = newSize;
                                useGroupSize = true;
                                System.out.println("Group size changed to " + groupSize + " people per group");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number. Using current size.");
                        }
                    } else if (groupingChoice == 2) {
                        System.out.print("\nEnter number of groups (current: " + (numberOfGroups == 0 ? "not set" : numberOfGroups) + "): ");
                        try {
                            int newGroups = Integer.parseInt(scanner.nextLine());
                            if (newGroups < 1) {
                                System.out.println("Number of groups must be at least 1");
                            } else if (newGroups > names.size()) {
                                System.out.println("Number of groups cannot be larger than number of names (" + names.size() + ")");
                            } else {
                                numberOfGroups = newGroups;
                                useGroupSize = false;
                                System.out.println("Number of groups changed to " + numberOfGroups);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number. Using current setting.");
                        }
                    } else {
                        System.out.println("Invalid choice. Keeping current settings.");
                    }
                    break;
                    
                case 4:
                    if (names.size() < 2) {
                        System.out.println("\nNeed at least 2 names to form groups");
                        break;
                    }
                    
                    // Calculate groups based on current settings
                    int actualGroupSize;
                    if (useGroupSize) {
                        actualGroupSize = groupSize;
                    } else {
                        // Calculate group size based on number of groups
                        actualGroupSize = (int) Math.ceil((double) names.size() / numberOfGroups);
                    }
                    
                    // Shuffle animation
                    System.out.println("\nRandomizing groups...");
                    animateShuffling(names, random);
                    
                    // Create groups
                    System.out.println("\n╔════════════════════════════════╗");
                    System.out.println("║        RANDOMIZED GROUPS       ║");
                    System.out.println("╚════════════════════════════════╝");
                    
                    List<List<String>> groups = new ArrayList<>();
                    Collections.shuffle(names, random);
                    
                    if (useGroupSize) {
                        // Group by size
                        for (int i = 0; i < names.size(); i += actualGroupSize) {
                            int end = Math.min(i + actualGroupSize, names.size());
                            groups.add(names.subList(i, end));
                        }
                    } else {
                        // Group by number of groups
                        int namesPerGroup = names.size() / numberOfGroups;
                        int remainder = names.size() % numberOfGroups;
                        int index = 0;
                        
                        for (int i = 0; i < numberOfGroups; i++) {
                            int size = namesPerGroup + (i < remainder ? 1 : 0);
                            groups.add(names.subList(index, index + size));
                            index += size;
                        }
                    }
                    
                    // Display groups
                    for (int i = 0; i < groups.size(); i++) {
                        System.out.println("\nGroup " + (i + 1) + ":");
                        for (String member : groups.get(i)) {
                            System.out.println("  • " + member);
                        }
                    }
                    
                    System.out.println("\nPress enter to continue...");
                    scanner.nextLine();
                    break;
                    
                case 5:
                    System.out.println("\nSalamat! Exiting program...");
                    scanner.close();
                    return;
                    
                default:
                    System.out.println("\nInvalid choice. Please enter 1-5");
            }
        }
    }
    
    private static void displayNames(List<String> names) {
        for (int i = 0; i < names.size(); i++) {
            System.out.println((i + 1) + ". " + names.get(i));
        }
    }
    
    private static void animateShuffling(ArrayList<String> names, Random random) throws InterruptedException {
        int animationDuration = 3000; // 3 seconds
        long startTime = System.currentTimeMillis();
        
        String[] spinner = new String[] { "⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏" };
        int spinnerIndex = 0;
        
        while (System.currentTimeMillis() - startTime < animationDuration) {
            System.out.print("\r" + spinner[spinnerIndex] + " Shuffling names... ");
            spinnerIndex = (spinnerIndex + 1) % spinner.length;
            Thread.sleep(150);
        }
        
        System.out.print("\r" + "✓ Done shuffling!          \n");
    }
}
