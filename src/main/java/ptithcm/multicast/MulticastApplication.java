package ptithcm.multicast;

import javax.swing.*;
import java.awt.*;

public class MulticastApplication extends JFrame implements Writable {
    public static final String DEFAULT_MULTICAST_GROUP = "224.0.0.10";
    public static final int DEFAULT_PORT = 7;

    private Receiver receiver;

    private JTextField multicastGroupField;
    private JTextField portField;
    private JTextField nameField;
    private JTextField messageField;
    private JTextArea outputArea;
    private JButton stopListenButton;
    private JButton listenButton;
    private JButton sendButton;
    private JCheckBox broadcastCheckbox;

    public MulticastApplication() {
        setTitle("UDP Multicast / Broadcast");
        setSize(635, 400);
        setResizable(false);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial", Font.BOLD, 15));
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel firstRow = new JPanel();
        firstRow.setBackground(Color.WHITE);
        firstRow.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(firstRow);

        firstRow.add(new JLabel("Multicast group"));
        multicastGroupField = new JTextField(10);
        multicastGroupField.setText(DEFAULT_MULTICAST_GROUP);
        firstRow.add(multicastGroupField);
        firstRow.add(new JLabel("Port"));
        portField = new JTextField(5);
        portField.setText(String.valueOf(DEFAULT_PORT));
        firstRow.add(portField);
        listenButton = new JButton("Start listening");
        firstRow.add(listenButton);
        stopListenButton = new JButton("Stop listening");
        firstRow.add(stopListenButton);
        firstRow.add(new JLabel("Name"));
        nameField = new JTextField(6);
        firstRow.add(nameField);


        JPanel secondRow = new JPanel();
        secondRow.setBackground(Color.WHITE);
        secondRow.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(secondRow);
        topPanel.add(secondRow);

        secondRow.add(new JLabel("Message"));
        messageField = new JTextField(43);
        secondRow.add(messageField);
        broadcastCheckbox = new JCheckBox("Broadcast");
        broadcastCheckbox.setBackground(Color.WHITE);
        secondRow.add(broadcastCheckbox);
        sendButton = new JButton(" Send ");
        secondRow.add(sendButton);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        listenButton.addActionListener(e -> listenButtonClicked());
        stopListenButton.addActionListener(e -> stopListenButtonClicked());
        sendButton.addActionListener(e -> sendButtonClicked());

        enableListenButton(true);
    }

    public static void main(String[] args) {
        MulticastApplication app = new MulticastApplication();
        app.setVisible(true);
        app.write("UDP Multicast / Broadcast");
        app.write("-".repeat(50));
        app.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void write(String message) {
        SwingUtilities.invokeLater(() -> outputArea.append(message + "\n"));
    }

    public void listenButtonClicked() {
        String group;
        int port;

        group = multicastGroupField.getText();
        try {
            port = Integer.parseInt(portField.getText());
        } catch(NumberFormatException e) {
            write("ERROR: Entered port is not a number");
            return;
        }

        try {
            receiver = new Receiver(this, port, group);
            receiver.start();
        } catch (Exception e) {
            write("ERROR: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        enableListenButton(false);
    }

    public void stopListenButtonClicked() {
        receiver.stopListening();
        enableListenButton(true);
    }

    public void sendButtonClicked() {
        String group;
        int port;
        String message;
        boolean isBroadcast;
        String senderName;

        isBroadcast = broadcastCheckbox.isSelected();
        message = messageField.getText();
        group = multicastGroupField.getText();
        senderName = nameField.getText();
        try {
            port = Integer.parseInt(portField.getText());
        } catch(NumberFormatException e) {
            write("ERROR: Entered port is not a number");
            return;
        }

        if (senderName.length() == 0) {
            write("ERROR: Must have a name");
            return;
        }

        String completeMessage = senderName + ":" + message;

        try {
            if(isBroadcast) {
                Sender.sendBroadcast(completeMessage, port);
                write("Broadcast message sent: " + message);
            }

            else {
                Sender.sendDatagram(completeMessage, group, port);
                write(senderName + " sent a Multicast message to " + group + ": " + message);
            }
        } catch (Exception e) {
            write("ERROR: " + e.getMessage());
        }
    }

    void enableListenButton(boolean enable) {
        listenButton.setEnabled(enable);
        stopListenButton.setEnabled(!enable);
        multicastGroupField.setEditable(enable);
        nameField.setEditable(enable);
        portField.setEditable(enable);
    }
}
