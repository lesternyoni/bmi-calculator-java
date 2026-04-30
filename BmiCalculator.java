import java.util.Scanner;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

public class BmiCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);
        
        List<String> history = new ArrayList<>(); 
        
        theHeader("WELCOME TO THE HEALTH & BMI TRACKER");
        System.out.print("Please enter your name: ");
        String userName = scanner.nextLine();
        
        boolean running = true;
        
        while (running) {
            showMainMenu(userName);
            int menuChoice = getValidIntInput(scanner, "Enter your choice (1-4): ", 1, 4);
            
            switch (menuChoice) {
                case 1:
                    performBmiCalculation(scanner, userName, history);
                    break;
                case 2:
                    showBmiInformation();
                    break;
                case 3:
                    showHistory(userName, history);
                    break;
                case 4:
                    running = false;
                    theHeader("GOODBYE, " + userName.toUpperCase() + "! STAY HEALTHY! AND GET A CATS");
                    break;
            }
        }
        
        scanner.close();
    }
    
    public static void theHeader(String title) {
        System.out.println("\n========================================================");
        int borders = (54 - title.length()) / 2;
        for (int i = 0; i < borders; i++) {
            System.out.print(" ");
        }
        System.out.println(title);
        System.out.println("========================================================");
    }
    
    public static void showMainMenu(String name) {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("Hello, " + name + "! What would you like to do?");
        System.out.println("1. Calculate my BMI");
        System.out.println("2. What is BMI? (Read Information)");
        System.out.println("3. View My Calculation History");
        System.out.println("4. Exit(and get a cat)");
    }
    
    public static void performBmiCalculation(Scanner scanner, String userName, List<String> history) {
        theHeader("BMI CALCULATION");
        
        int genderChoice = getGenderChoice(scanner);
        int age = (int) getValidDoubleInput(scanner, "\nEnter your age in years: ", 2, 120);
        int unitChoice = getUnitChoice(scanner);
        
        double weight = (unitChoice == 1) ? 
            getValidDoubleInput(scanner, "Enter weight in Kilograms (Kg): ", 10, 600) : 
            getValidDoubleInput(scanner, "Enter weight in Pounds (lbs): ", 22, 1300);
        
        double height = (unitChoice == 1) ? 
            getValidDoubleInput(scanner, "Enter height in meters (m): ", 0.5, 2.5) : 
            getValidDoubleInput(scanner, "Enter height in inches (in): ", 20, 120);
        
        double bmi = calculateBmiValue(unitChoice, weight, height);
        int category = determineCategory(bmi, age, genderChoice);
        
        System.out.println("\n--------------------------------------------------------");
        System.out.printf("RESULT: Your BMI is %.2f\n", bmi);
        printCategoryFeedback(category);
        System.out.println("--------------------------------------------------------");
        
        calculateAndShowPreferableWeight(height, unitChoice, age, genderChoice);
        
        System.out.println("--------------------------------------------------------");
        
        String genderStr = (genderChoice == 1) ? "Female" : "Male";
        String unitStr = (unitChoice == 1) ? "Metric" : "Imperial";
        String record = String.format("Age: %d" + "\n" + "Gender: %s" + "\n" + "System: %s" + "\n" + "Weight: %.2f" + "\n" + "Height: %.2f" + "\n" + "BMI: %.2f", age, genderStr, unitStr, weight, height, bmi);
        history.add(record);
        
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
        scanner.nextLine();		
		
    }
    
    public static double calculateBmiValue(int unitChoice, double weight, double height) {
        if (unitChoice == 1) {
            return weight / (height * height);
        } else {
            return (703 * weight) / (height * height); 
        }
    }
    
    public static int determineCategory(double bmi, int age, int genderChoice) {
        double lowerBase = 18.5;
        double normalBase = 25.0;
        double overweightBase = 30.0;
        
        if (age < 18) {
            lowerBase -= 1.0;
            normalBase -= 1.0;
            overweightBase -= 1.0;
        } else if (age >= 65) {
            lowerBase += 1.5; 
            normalBase += 2.0;
            overweightBase += 2.0;
        }
        
        if (genderChoice == 1) { 
            normalBase -= 0.5;
        } else if (genderChoice == 2) {
            normalBase += 0.5;
        }
        
        if (bmi < lowerBase) return 1;
        if (bmi < normalBase) return 2;
        if (bmi < overweightBase) return 3;
        return 4;
    }
    
	public static void calculateAndShowPreferableWeight(double height, int unitChoice, int age, int genderChoice) {
        double lowerBase = 18.5;
        double normalBase = 25.0;
        
        if (age < 18) {
            lowerBase -= 1.0;
            normalBase -= 1.0;
        } else if (age >= 65) {
            lowerBase += 1.5; 
            normalBase += 2.0;
        }
        
        if (genderChoice == 1) { 
            normalBase -= 0.5;
        } else if (genderChoice == 2) {
            normalBase += 0.5;
        }

        double preferableMinWeight;
        double preferableMaxWeight;
        
        if (unitChoice == 1) {
            preferableMinWeight = lowerBase * (height * height);
            preferableMaxWeight = normalBase * (height * height);
            System.out.printf("TARGET: For your height (%.2fm), your preferable weight is between %.1f kg and %.1f kg.\n", height, preferableMinWeight, preferableMaxWeight);
        } else {
            preferableMinWeight = (lowerBase * height * height) / 703;
            preferableMaxWeight = (normalBase * height * height) / 703;
            System.out.printf("TARGET: For your height (%.2fin), your preferable weight is between %.1f lbs and %.1f lbs.\n", height, preferableMinWeight, preferableMaxWeight);
        }
    }
	
    public static void printCategoryFeedback(int category) {
        switch (category) {
            case 1:
                System.out.println("STATUS: Underweight");
                System.out.println("ADVICE: Consider a eating more food, it's nice...and get a cat");
                break;
            case 2:
                System.out.println("STATUS: Normal weight");
                System.out.println("ADVICE: Good job! Maintain your current healthy lifestyle...have sea food too and get a cat");
                break;
            case 3:
                System.out.println("STATUS: Overweight");
                System.out.println("ADVICE: Consider eating less food and working out, if you want...and get a cat");
                break;
            case 4:
                System.out.println("STATUS: Obese");
                System.out.println("ADVICE: Not to alarm you but...consider consulting a healthcare professional...asap...and get a cat");
                break;
        }
    }
    
    public static void showBmiInformation() {
        theHeader("WHAT IS BMI?");
        System.out.println("Body Mass Index (BMI) is a cheap and easy method");
        System.out.println("for weight category: underweight, healthy weight, overweight, and obesity.\n");
        
        System.out.println("Standard BMI Ranges (Adults):");
        System.out.println("  * Under 18.5   -> Underweight");
        System.out.println("  * 18.5 - 24.9  -> Normal Weight");
        System.out.println("  * 25.0 - 29.9  -> Overweight");
        System.out.println("  * 30.0 & Above -> Obese\n");
        
        System.out.println("--------------------------------------------------------");
    }
    
    public static void showHistory(String userName, List<String> history) {
        theHeader(userName.toUpperCase() + "'S BMI HISTORY");
        if (history.isEmpty()) {
            System.out.println("No calculations performed yet.");
        } else {
            for (int i = 0; i < history.size(); i++) {
                System.out.println((i + 1) + ". " + history.get(i));
                System.out.println("   -------------------------------------------------");
            }
        }
    }
	
    public static int getGenderChoice(Scanner scanner) {
        System.out.println("\nPlease select your gender:");
        System.out.println("1. Female (f)");
        System.out.println("2. Male (m)");
        return getValidIntInput(scanner, "Choice: ", 1, 2);
    }
    
    public static int getUnitChoice(Scanner scanner) {
        System.out.println("\nPlease select preferred unit of measurement:");
        System.out.println("1. Metric (kg, m)");
        System.out.println("2. Imperial (lbs, in)");
        return getValidIntInput(scanner, "Choice: ", 1, 2);
    }
    
    public static int getValidIntInput(Scanner scanner, String prompt, int min, int max) {
        int value = 0;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value >= min && value <= max) {
                    break;
                } else {
                    System.out.println(">> Invalid choice. Please choose between " + min + " and " + max + ".");
                }
            } else {
                System.out.println(">> Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return value;
    }
    
    public static double getValidDoubleInput(Scanner scanner, String prompt, double min, double max) {
        double value = 0.0d;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                value = scanner.nextDouble();
                if (value >= min && value <= max) {
                    break;
                } else {
                    System.out.printf(">> Please enter a value between %.1f and %.1f.\n", min, max);
                }
            } else {
                System.out.println(">> Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }
        return value;
    }
	
}
