import java.util.ArrayList;
import java.util.Scanner;

public class Ernest {
    public static void main(String[] args) {
        String h_line = "______________________________________";
        String banner = " _____ ____  _     _  ____  ____ _____\n"
                + "| ____|  _ \\| \\   | | ____|/ ___|_   _|\n"
                + "|  _| | |_) |  \\  | |  _|  \\___\\  | |\n"
                + "| |___|  _ /| | \\ | | |___ ___) | | |\n"
                + "|_____|_| \\ |_|  \\|_|_____||____/ |_|\n";
        String chatbot_name = "Ernest";

        System.out.println(h_line);
        System.out.println(banner);
        System.out.printf("Hi! I'm %s.\n", chatbot_name);
        System.out.println("How can I help you?");
        System.out.println(h_line);
        System.out.println("(Type \"bye\" to exit the chat)");

        // Input, Outputs and Arrays
        ArrayList<String> dataList = new ArrayList<>(100);

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("bye")) {
                    // Exit the chatbot
                    break;
                } else if (command.equalsIgnoreCase("list")) {
                    // Print list
                    System.out.println("Your to-do list is:");
                    for (int i = 0; i < dataList.size(); i++) {
                        System.out.println((i + 1) + ". " + dataList.get(i));
                    }
                } else {
                    // Add entered text into dataList
                    if (dataList.size() < 100) {
                        dataList.add(command);
                        System.out.println("Added to list: " + command);
                        System.out.println("Current list size: " + dataList.size() + "/100");
                    } else {
                        System.out.println("The list is full (100/100).");
                    }
                }
            }
        }

        System.out.println("Bye. See you again soon!");
        System.out.println(h_line);
    }
}
