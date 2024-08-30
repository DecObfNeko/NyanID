package moe.takanashihoshino.nyaniduserserver.utils.Command;

import java.util.Arrays;
import java.util.Scanner;

public class ConsoleInputHandler implements Runnable {
    private CommandManager commandManager;

    public ConsoleInputHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print(">");
                String input = scanner.nextLine();
                String[] parts = input.split(" ");
                String commandName = parts[0];
                String[] args = Arrays.copyOfRange(parts, 1, parts.length);
                commandManager.executeCommand(commandName, args);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
