# Gitlet Design Document
author: Lucas Salim

## Design Document Guidelines

Please use the following format for your Gitlet design document. Your design
document should be written in markdown, a language that allows you to nicely
format and style a text file. Organize your design document in a way that
will make it easy for you or a course-staff member to read.

## 1. Classes and Data Structures

Include here any class definitions. For each class list the instance
variables and static variables (if any). Include a ***brief description***
of each variable and its purpose in the class. Your explanations in
this section should be as concise as possible. Leave the full
explanation to the following sections. You may cut this section short
if you find your document is too wordy.


###Main
The Main is the entry point of the program. After compiling, one can run
`java gitlet.Main init` to initialize our version system control. The code
consists of a switch case checking whether the first argument matches one of
the known commands. Of course, this only happens after asserting the the first
argument is not null.

###Blob
This class is a representation of the content of a file. Here we can store the content
of the file in some other place (like another file).
#### Fields:
* public static File BLOBS_FOLDER: the file to store the Blob.
* private String _content: the string used to store the content of Blob.

###tree
This class is a representation of a mapping between a file name and its Blob (content).
It uses the HashMap data structure to make such mapping.
#### Fields:
* public HashMap<String, Blob> myFiles: data structure used to store a file name and its content.

###Commit
This class is a representation of a commit. A commit consists of a combinations of
log messages, other metadata (commit date, author, etc.), a reference to a tree,
and references to parent commits.

#### Fields:
* public static final File COMMIT_FOLDER: The folder to store the committing tree.
* private static Commit HEAD: pointer to the last commit in our history.
* private static Commit Master: the master branch.
* private String _message: a message of a given commit.
* private String _timestamp: stores the date/time of commit.
* private HashMap<String, Blob> _tree: stores the tree of a commit.
* private String _parent: points to the commit before this.

###Logic
This class is where the Logic of our program isd written.

#### Fields:
*  public static File CWD: a file for the current working directory.
*  public static File GITLET_FOLDER: a file for the hidden gitlet folder.


## 2. Algorithms

This is where you tell us how your code works. For each class, include
a high-level description of the methods in that class. That is, do not
include a line-by-line breakdown of your code, but something you would
write in a javadoc comment above a method, ***including any edge cases
you are accounting for***. We have read the project spec too, so make
sure you do not repeat or rephrase what is stated there.  This should
be a description of how your code accomplishes what is stated in the
spec.


The length of this section depends on the complexity of the task and
the complexity of your design. However, simple explanations are
preferred. Here are some formatting tips:

* For complex tasks, like determining merge conflicts, we recommend
  that you split the task into parts. Describe your algorithm for each
  part in a separate section. Start with the simplest component and
  build up your design, one piece at a time. For example, your
  algorithms section for Merge Conflicts could have sections for:

    * Checking if a merge is necessary.
    * Determining which files (if any) have a conflict.
    * Representing the conflict in the file.

* Try to clearly mark titles or names of classes with white space or
  some other symbols.

## 3. Persistence

Describe your strategy for ensuring that you don’t lose the state of your program
across multiple runs. Here are some tips for writing this section:

* This section should be structured as a list of all the times you
  will need to record the state of the program or files. For each
  case, you must prove that your design ensures correct behavior. For
  example, explain how you intend to make sure that after we call
  `java gitlet.Main add wug.txt`,
  on the next execution of
  `java gitlet.Main commit -m “modify wug.txt”`,
  the correct commit will be made.

* A good strategy for reasoning about persistence is to identify which
  pieces of data are needed across multiple calls to Gitlet. Then,
  prove that the data remains consistent for all future calls.

* This section should also include a description of your .gitlet
  directory and any files or subdirectories you intend on including
  there.

## 4. Design Diagram

Attach a picture of your design diagram illustrating the structure of your
classes and data structures. The design diagram should make it easy to
visualize the structure and workflow of your program.

