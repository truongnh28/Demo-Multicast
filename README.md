# Multicast-visualization-demo

This is a small project to see how multicast work (not an application). This project can work on different computers in same local network.

## Features

- Allow 2 or more computers connect each other in same local network.
- Can be change role to a Sender or a Receiver by clicking the button "Start listening".
- Receivers has been listening can see the name of the Sender and the message Sender send to them.

## How to run

- Make sure you have java on your machine, at least Java 8. If you don't, go and install it. If you allow your computer receive non-secure messages, you can go ahead. If you block the non-secure messages, block the firewall. Because the multicast use UDP protocol, which is not secure, your computer will ignore them.
- When you finish your first step, if you have an IDE (Eclipse, IntelliJ IDEA,...), go to Main.java and run it. If you don't, type in command line these 2 lines:

```java
javac Main.java
java Main
```

- Then you will see the UI show up. You have to test your receiver working by click "Start listening". If you see the ERROR, stop the program by clicking the "X" button on the bottom right but don't close the terminal or the cmd. Go to file Receiver.java.
