package seedu.address.model;

import java.util.List;

/**
 * Unmodifiable view of command history.
 */
public interface ReadOnlyCommandHistory {
    /**
     * Returns an unmodifiable view of the command history.
     */
    List<String> getCommandHistory();

    /**
     * Returns the previous command in the history.
     */
    String getPreviousCommand();

    /**
     * Returns the next command in the history.
     */
    String getNextCommand();

    /**
     * Resets the navigation position in the command history.
     */
    void resetNavigation();

    /**
     * Set the navigation position in the command history.
     */
    void setIndex(int index);

    /**
     * Returns true if there are previous commands to navigate to.
     */
    boolean canNavigateBackward();

    /**
     * Returns true if there are next commands to navigate to.
     */
    boolean canNavigateForward();
}
