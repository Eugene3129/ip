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

## Test case: Accept case-insensitive task commands

Aim: Verify that task types and task markers are case-insensitive while task descriptions preserve their entered casing.

Inputs:
```text
TODO Buy Milk
DEADLINE Submit Report /BY Friday
EVENT Team Meeting /FROM 10am /TO 11am
todoist invalid
unmarkable 1
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
Ok, I've added to the task list:
> [T][ ] Buy Milk
Current list size: 1/100
______________________________________
Ok, I've added to the task list:
> [D][ ] Submit Report (by: Friday)
Current list size: 2/100
______________________________________
Ok, I've added to the task list:
> [E][ ] Team Meeting (from: 10am to: 11am)
Current list size: 3/100
______________________________________
Sorry, please insert a valid command.
______________________________________
Sorry, please insert a valid command.
______________________________________
Your to-do list is:
1. [T][ ] Buy Milk
2. [D][ ] Submit Report (by: Friday)
3. [E][ ] Team Meeting (from: 10am to: 11am)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject empty task details

Aim: Verify that deadline and event commands reject missing descriptions, dates, and times.

Inputs:
```text
deadline /by Friday
deadline submit report /by
event /from 10am /to 11am
event meeting /from /to 11am
event meeting /from 10am /to
todo valid task
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
Missing deadline description. Please try again.
______________________________________
Missing deadline date. Please try again.
______________________________________
Missing event description. Please try again.
______________________________________
Missing event start time. Please try again.
______________________________________
Missing event end time. Please try again.
______________________________________
Ok, I've added to the task list:
> [T][ ] valid task
Current list size: 1/100
______________________________________
Your to-do list is:
1. [T][ ] valid task
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject missing arguments

Aim: Verify that commands without required arguments print a specific message and that Ernest continues processing later commands.

Inputs:
```text
mark
unmark
todo
deadline
event
todo keep working
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
Missing task number. Please refer to the task list and try again.
______________________________________
Missing task number. Please refer to the task list and try again.
______________________________________
Missing task description. Please try again.
______________________________________
Missing task description. Please try again.
______________________________________
Missing task description. Please try again.
______________________________________
Ok, I've added to the task list:
> [T][ ] keep working
Current list size: 1/100
______________________________________
Your to-do list is:
1. [T][ ] keep working
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Add and list mixed task types

Aim: Verify that todo, deadline, and event commands can be interleaved while preserving their insertion order and details.

Inputs:
```text
todo review the lecture
deadline submit the assignment /by Friday
event project discussion /from 2pm /to 3pm
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
Ok, I've added to the task list:
> [T][ ] review the lecture
Current list size: 1/100
______________________________________
Ok, I've added to the task list:
> [D][ ] submit the assignment (by: Friday)
Current list size: 2/100
______________________________________
Ok, I've added to the task list:
> [E][ ] project discussion (from: 2pm to: 3pm)
Current list size: 3/100
______________________________________
Your to-do list is:
1. [T][ ] review the lecture
2. [D][ ] submit the assignment (by: Friday)
3. [E][ ] project discussion (from: 2pm to: 3pm)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Ignore invalid task numbers

Aim: Verify that marking and unmarking task numbers outside the list leaves the existing task incomplete and in the same position.

Inputs:
```text
todo keep this task
mark 0
mark 2
unmark 0
unmark -1
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
Ok, I've added to the task list:
> [T][ ] keep this task
Current list size: 1/100
______________________________________
Invalid task number. Please refer to the task list and try again.
______________________________________
Invalid task number. Please refer to the task list and try again.
______________________________________
Invalid task number. Please refer to the task list and try again.
______________________________________
Invalid task number. Please refer to the task list and try again.
______________________________________
Your to-do list is:
1. [T][ ] keep this task
______________________________________
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
Ok, I've added to the task list:
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
Ok, I've added to the task list:
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

## Test case: Reject malformed task commands

Aim: Verify that Ernest reports a task-specific validation message when a deadline or event command omits its marker.

Inputs:
```text
deadline submit assignment by Friday
event team meeting from 10am to 11am
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
Deadline must include a /by date.
______________________________________
Event must include a /from time.
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
Ok, I've added to the task list:
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
Ok, I've added to the task list:
> [E][ ] team meeting (from: 10am to: 11am)
Current list size: 1/100
______________________________________
Your to-do list is:
1. [E][ ] team meeting (from: 10am to: 11am)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Handle mixed-case state commands

Aim: Verify that state-changing commands are recognized regardless of command-word casing and that the final state is correct.

Inputs:
```text
todo prepare slides
MaRk 1
LiSt
UnMaRk 1
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
Ok, I've added to the task list:
> [T][ ] prepare slides
Current list size: 1/100
______________________________________
Well done! Marked task 1 as done.
______________________________________
Your to-do list is:
1. [T][X] prepare slides
______________________________________
Ok, marked task 1 as not done yet.
______________________________________
Your to-do list is:
1. [T][ ] prepare slides
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Preserve state after repeated state commands

Aim: Verify that repeated mark and unmark commands report the invalid transition without changing the task's state.

Inputs:
```text
todo check the answer
mark 1
mark 1
unmark 1
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
Ok, I've added to the task list:
> [T][ ] check the answer
Current list size: 1/100
______________________________________
Well done! Marked task 1 as done.
______________________________________
Sorry, task 1 is already done.
______________________________________
Ok, marked task 1 as not done yet.
______________________________________
Sorry, task 1 is already marked as not done yet.
______________________________________
Your to-do list is:
1. [T][ ] check the answer
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Keep task order after an invalid task command

Aim: Verify that a malformed task command is rejected without consuming a task number or affecting later valid tasks.

Inputs:
```text
todo first task
event missing markers
deadline second task /by tomorrow
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
Ok, I've added to the task list:
> [T][ ] first task
Current list size: 1/100
______________________________________
Event must include a /from time.
______________________________________
Ok, I've added to the task list:
> [D][ ] second task (by: tomorrow)
Current list size: 2/100
______________________________________
Your to-do list is:
1. [T][ ] first task
2. [D][ ] second task (by: tomorrow)
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject reversed event markers

Aim: Verify that an event whose `/to` marker appears before `/from` is rejected and does not add an invalid task.

Inputs:
```text
event invalid order /to 11am /from 10am
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
The /to marker must come after /from.
______________________________________
Your to-do list is:
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Reject nonnumeric state arguments

Aim: Verify that nonnumeric mark and unmark arguments are rejected without changing the state of a valid task.

Inputs:
```text
todo verify state
mark abc
unmark xyz
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
Ok, I've added to the task list:
> [T][ ] verify state
Current list size: 1/100
______________________________________
Task number must be an integer.
______________________________________
Task number must be an integer.
______________________________________
Well done! Marked task 1 as done.
______________________________________
Ok, marked task 1 as not done yet.
______________________________________
Your to-do list is:
1. [T][ ] verify state
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Handle surrounding whitespace

Aim: Verify that leading, trailing, and tab whitespace around commands does not change their meaning.

Inputs:
```text
  todo	spaced task
  mark	1
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
Ok, I've added to the task list:
> [T][ ] spaced task
Current list size: 1/100
______________________________________
Well done! Marked task 1 as done.
______________________________________
Your to-do list is:
1. [T][X] spaced task
______________________________________
Bye. See you again soon!
______________________________________
```

## Test case: Delete tasks safely

Aim: Verify that deletion removes the requested task, renumbers the remaining
task, and rejects missing, nonnumeric, and out-of-range task numbers.

Inputs:
```text
todo first task
todo second task
delete 1
mark 1
delete
delete abc
delete 2
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
Ok, I've added to the task list:
> [T][ ] first task
Current list size: 1/100
______________________________________
Ok, I've added to the task list:
> [T][ ] second task
Current list size: 2/100
______________________________________
Ok, I've deleted this task from the task list:
> [T][ ] first task
Current list size: 1/100
______________________________________
Well done! Marked task 1 as done.
______________________________________
Missing task number. Please refer to the task list and try again.
______________________________________
Task number must be an integer.
______________________________________
Invalid task number. Please refer to the task list and try again.
______________________________________
Your to-do list is:
1. [T][X] second task
______________________________________
Bye. See you again soon!
______________________________________
```
