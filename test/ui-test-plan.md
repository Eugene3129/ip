# Ernest UI test plan

This plan exercises the stdin/console interface of
`src/main/java/ernest/Ernest.java`.

## Execution information

- Runtime and compiler: Java 25 (`java` and `javac`).
- Main class: `ernest.Ernest`.
- The test runner compiles all sources under `src/main/java` into a temporary
  directory before testing.
- Each test case runs in a fresh process. Input lines are sent in order, one
  command per line, and `bye` ends the session.
- Output is compared exactly after CRLF/CR line endings are normalized to LF.
  Extra output, missing output, ordering changes, and whitespace changes fail
  the case.
- The runner prints the console input and output for each case. It stops after
  the first failure and prints both the expected and actual output.

## Test case: Exit from welcome screen

Aim: Verify that Ernest displays its welcome screen and exits cleanly when the user enters `bye`.

Inputs:
```text
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Bye. See you again soon!
______________________________________
```

## Test case: Add and list a todo

Aim: Verify that a todo command adds a task and `list` displays it as incomplete.

Inputs:
```text
todo read the course notes
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Added to task list:
> [T][ ] read the course notes
Current list size: 1/100
______________________________________
Your to-do list is:
1. [T][ ] read the course notes
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Mark and unmark a task

Aim: Verify that `mark` changes a task to done and `unmark` changes it back to not done.

Inputs:
```text
todo submit the report
mark 1
unmark 1
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Added to task list:
> [T][ ] submit the report
Current list size: 1/100
______________________________________
Well done! Marked task 1 as done.
______________________________________
Ok, marked task 1 as not done yet.
______________________________________
Your to-do list is:
1. [T][ ] submit the report
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject an invalid command

Aim: Verify that Ernest reports an invalid command and remains available until `bye` is entered.

Inputs:
```text
launch rocket
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Sorry, please insert a valid command.
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Add and list a deadline

Aim: Verify that a deadline command stores the task description and due date, and `list` displays both values.

Inputs:
```text
deadline submit assignment /by Friday
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Added to task list:
> [D][ ] submit assignment (by: Friday)
Current list size: 1/100
______________________________________
Your to-do list is:
1. [D][ ] submit assignment (by: Friday)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Add and list an event

Aim: Verify that an event command stores the task description and time range, and `list` displays all event details.

Inputs:
```text
event team meeting /from 10am /to 11am
list
bye
```

Expected output:
```text
______________________________________
 _____ ____  _     _  ____  ____ _____
| ____|  _ \| \   | | ____|/ ___|_   _|
|  _| | |_) |  \  | |  _|  \___\  | |
| |___|  _ /| | \ | | |___ ___) | | |
|_____|_| \ |_|  \|_|_____||____/ |_|

Hi! I'm Ernest.
How can I help you?
______________________________________
(Type "bye" to exit the chat)
Added to task list:
> [E][ ] team meeting (from: 10am to: 11am)
Current list size: 1/100
______________________________________
Your to-do list is:
1. [E][ ] team meeting (from: 10am to: 11am)
______________________________________
Bye. See you again soon!
______________________________________
```
