---
  layout: default.md
    title: "Developer Guide"
    pageNav: 3
---


# WhoAreYouAgain? Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="600" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="600" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Individuals with large extended families who struggle to keep track of relationships
* Users who frequently attend family gatherings and want to avoid awkward moments of forgetting names and relationships
* Individuals who want an organized way to store family details such as birthdays, nicknames, and notable events
* Users looking for an easy-to-use digital family directory
* Users who prefer typing to mouse interactions
* Users who are reasonably comfortable using CLI apps

**Value proposition**:

**WhoAreYouAgain?** is a family directory app designed to help users remember family members, track important dates,
and avoid social faux pas during family gatherings. It provides a structured way to manage extended family information,
ensuring that users never forget key relationships, birthdays, or inside jokes. The app simplifies family
record-keeping while adding a fun and practical element to family interactions.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​                                                                                                  | So that I can…​                            |
|----------|---------|---------------------------------------------------------------------------------------------------------------|--------------------------------------------|
| `* * *`  | user    | add a family member to my directory                                                                           | keep track of relatives                    |
| `* * *`  | user    | delete a family member                                                                                        | remove outdated or incorrect entries       |
| `* * *`  | user    | search for a family member by name                                                                            | quickly find their details                 |
| `* * *`  | user    | add a profile picture for each family member                                                                  | visually recognize them                    |
| `* * *`  | user    | store a family member’s contact details (phone, email, address)                                               | reach out to them easily                   |
| `* * *`  | user    | record a family member’s relationship to me in a friendly way (e.g., “Mom’s Brother” instead of just “Uncle”) | recall how we are connected                |
| `* * *`  | user    | add nicknames to a family member’s profile                                                                    | remember what they are called in my family |
| `* * *`  | user    | add notes about a family member (fun facts, shared memories and inside jokes)                                 | have a reference for conversation starters |

*{More to be added}*


# WhoAreYouAgain? - Use Cases  

## Use Case 1: Add Nickname to Family Member’s Profile  
**System**: WhoAreYouAgain?  
**Use case**: UC01 - Add Nickname  
**Actor**: User  

### Main Success Scenario (MSS):  
1. User selects a family member’s profile.  
2. System displays the family member’s details.  
3. User chooses to add/edit a nickname.  
4. System requests nickname input.  
5. User enters a nickname and confirms.  
6. System saves the nickname and updates the profile.  
   **Use case ends.**  

### Extensions:  
- **4a. User leaves the nickname field empty.**  
  - 4a1. System displays a warning that the nickname cannot be empty.  
  - 4a2. User enters a valid nickname.  
  - Use case resumes from step 5.  

- **5a. User cancels the action before confirmation.**  
  - 5a1. System discards changes.  
  - **Use case ends.**  

---

## Use Case 2: View a List of Upcoming Birthdays  
**System**: WhoAreYouAgain?  
**Use case**: UC02 - View Upcoming Birthdays  
**Actor**: User  

### Main Success Scenario (MSS):  
1. User navigates to the birthdays section.  
2. System retrieves and displays a list of upcoming birthdays.  
   **Use case ends.**  

### Extensions:  
- **2a. There are no upcoming birthdays.**  
  - 2a1. System displays a message: “No upcoming birthdays.”  
  - **Use case ends.**  

---

## Use Case 3: Add a Custom Family Event  
**System**: WhoAreYouAgain?  
**Use case**: UC03 - Add Custom Family Event  
**Actor**: User  

### Main Success Scenario (MSS):  
1. User selects “Add Event” from the events section.  
2. System requests event details (name, date, time, description, tagged family members).  
3. User enters event details and confirms.  
4. System saves the event and updates the event list.  
   **Use case ends.**  

### Extensions:  
- **2a. User leaves mandatory fields empty.**  
  - 2a1. System displays an error and prompts the user to enter the missing information.  
  - Use case resumes from step 3.  

- **3a. User cancels the action before confirmation.**  
  - 3a1. System discards changes.  
  - **Use case ends.**  

---

## Use Case 4: Search for a Family Member in the Tree  
**System**: WhoAreYouAgain?  
**Use case**: UC04 - Search Family Member  
**Actor**: User  

### Main Success Scenario (MSS):  
1. User opens the family tree view.  
2. User enters a family member’s name in the search bar.  
3. System searches and highlights the matching family member in the tree.  
   **Use case ends.**  

### Extensions:  
- **3a. No matching family member is found.**  
  - 3a1. System displays a message: “No results found.”  
  - **Use case ends.**  

---

## Use Case 5: Export Family Directory as CSV  
**System**: WhoAreYouAgain?  
**Use case**: UC05 - Export Family Directory  
**Actor**: User  

### Main Success Scenario (MSS):  
1. User selects the export option in settings.  
2. System prompts user to confirm export.  
3. User confirms.  
4. System generates and downloads the CSV file.  
   **Use case ends.**  

### Extensions:  
- **3a. User cancels the export.**  
  - 3a1. System aborts the operation.  
  - **Use case ends.**  

---

## Use Case 6: View Family Tree Visualization  
**System**: WhoAreYouAgain?  
**Use case**: UC06 - View Family Tree  
**Actor**: User  

### Main Success Scenario (MSS):  
1. User navigates to the family tree section.  
2. System displays a hierarchical family tree visualization.  
   **Use case ends.**  

### Extensions:  
- **2a. User zooms in or out.**  
  - 2a1. System adjusts the view accordingly.  
  - **Use case ends.**  

---

## Use Case 7: Relationship Calculator  
**System**: WhoAreYouAgain?  
**Use case**: UC07 - Calculate Family Relationship  
**Actor**: User  

### Main Success Scenario (MSS):  
1. User selects the relationship calculator feature.  
2. System prompts the user to enter two family member names.  
3. User enters names and confirms.  
4. System calculates and displays their relationship (e.g., “First Cousin Once Removed”).  
   **Use case ends.**  

### Extensions:  
- **3a. Entered names do not exist in the database.**  
  - 3a1. System displays an error message.  
  - **Use case ends.**  

---

## Use Case 8: Auntie Name Translator  
**System**: WhoAreYouAgain?  
**Use case**: UC08 - Convert Name to Cultural Nickname  
**Actor**: User  

### Main Success Scenario (MSS):  
1. User selects a family member’s profile.  
2. System provides a culturally appropriate nickname based on predefined rules (e.g., "Auntie Mei" instead of "Lisa Tan").  
   **Use case ends.**  

### Extensions:  
- **2a. No cultural nickname is available for the entered name.**  
  - 2a1. System notifies the user that no nickname was found.  
  - **Use case ends.**  

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 family members without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. All features should function without requiring an internet connection.
5. The application should launch and be ready for user interaction within 5 seconds on standard hardware.
6. The application should handle unexpected inputs gracefully with informative error messages.
7. All commands should execute and display results within 2 seconds on a standard computer system.
8. All family data should be stored locally.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
