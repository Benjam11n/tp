package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;

/**
 * Represents the command history in the address book.
 * Guarantees: details are present and not null.
 */
public class CommandHistory implements ReadOnlyCommandHistory {
    private static final Logger logger = LogsCenter.getLogger(CommandHistory.class);
    private static final int MAX_HISTORY_SIZE = 100;
    private final List<String> commandList;
    private int currentIndex = 0;

    public CommandHistory() {
        commandList = new ArrayList<>();
    }

    /**
     * Creates a CommandHistory using the List in the {@code toBeCopied}.
     */
    public CommandHistory(ReadOnlyCommandHistory toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Replaces the contents of the command list with {@code commands}.
     */
    public void setCommands(List<String> commands) {
        logger.fine("Setting commands: " + commands.size() + " commands.");
        this.commandList.clear();
        this.commandList.addAll(commands);
        trimToSize();
    }

    /**
     * Resets the existing data of this {@code CommandHistory} with {@code newData}.
     */
    public void resetData(ReadOnlyCommandHistory newData) {
        requireNonNull(newData);
        setCommands(newData.getCommandHistory());
    }

    /**
     * Adds a command to the command history.
     */
    public void addCommand(String command) {
        logger.fine("Adding command to history: " + command + ".");
        commandList.add(command);
        trimToSize();
    }

    /**
     * Ensures the history doesn't exceed the maximum size.
     */
    private void trimToSize() {
        if (commandList.size() > MAX_HISTORY_SIZE) {
            logger.info("Trimming command history.");
            commandList.subList(0, commandList.size() - MAX_HISTORY_SIZE).clear();
        }
    }

    @Override
    public List<String> getCommandHistory() {
        return new ArrayList<>(commandList);
    }

    @Override
    public String getPreviousCommand() {
        if (canNavigateBackward()) {
            this.currentIndex++;
            String command = commandList.get(commandList.size() - currentIndex);
            logger.fine("Navigating to previous command: " + command + ".");
            return command;
        }

        logger.info("Attempted to get previous command but none available.");
        return null;
    }

    @Override
    public String getNextCommand() {
        if (canNavigateForward()) {
            this.currentIndex--;
            // Special case: If we've returned to the beginning,
            // return empty string to clear the command box
            if (currentIndex == 0) {
                logger.fine("Navigated to beginning of command history.");
                return "";
            }
            String command = commandList.get(commandList.size() - currentIndex);
            logger.fine("Navigating to next command: " + command + ".");
            return command;
        }

        logger.info("Attempted to get next command but none available.");
        return null;
    }

    @Override
    public void resetNavigation() {
        this.currentIndex = 0;
    }

    @Override
    public void setIndex(int index) {
        this.currentIndex = index;
    }

    @Override
    public boolean canNavigateBackward() {
        return this.currentIndex < commandList.size();
    }

    @Override
    public boolean canNavigateForward() {
        return this.currentIndex > 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CommandHistory)) {
            return false;
        }

        CommandHistory otherCommandHistory = (CommandHistory) other;
        return commandList.equals(otherCommandHistory.commandList);
    }

    @Override
    public int hashCode() {
        return commandList.hashCode();
    }

}
