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
        ArrayList<Task> tasksList = new ArrayList<>(100);

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
                    for (int i = 0; i < tasksList.size(); i++) {
                        String taskName = tasksList.get(i).getTaskName();
                        String isDone = (tasksList.get(i).getDone() ? "[X]" : "[ ]");
                        System.out.println((i + 1) + ". " + isDone + " " + taskName);
                    }
                } else if (command.toLowerCase().startsWith("mark ")) {
                    try {
                        int taskNumber = Integer.parseInt(command.substring(5).strip());

                        if (taskNumber >= 1 && taskNumber <= tasksList.size()) {
                            if (tasksList.get(taskNumber - 1).getDone() == false) {
                                tasksList.get(taskNumber - 1).setDone(true);
                                System.out.println("Well done! Marked task " + taskNumber + " as done.");
                            } else {
                                System.out.println("Sorry, task " + taskNumber + " is already done.");
                            }
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Usage: mark <task number>");
                    }
                }  else if (command.toLowerCase().startsWith("unmark ")) {
                    try {
                        int taskNumber = Integer.parseInt(command.substring(7).strip());

                        if (taskNumber >= 1 && taskNumber <= tasksList.size()) {
                            if (tasksList.get(taskNumber - 1).getDone() == true) {
                                tasksList.get(taskNumber - 1).setDone(false);
                                System.out.println("Ok, marked task " + taskNumber + " as not done yet.");
                            } else {
                                System.out.println("Sorry, task " + taskNumber + " is already marked as not done yet.");
                            }
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Usage: unmark <task number>");
                    }
                } else {
                    // Add entered text into dataList
                    if (tasksList.size() < 100) {
                        Task newTask = new Task(command);
                        tasksList.add(newTask);
                        System.out.println("Added to list: " + command);
                        System.out.println("Current list size: " + tasksList.size() + "/100");
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
