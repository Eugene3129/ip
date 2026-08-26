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

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("bye")) {
                    break;
                }

                System.out.println(command); // Echo the user's command
            }
        }

        System.out.println("Bye. See you again soon!");
        System.out.println(h_line);
    }
}
