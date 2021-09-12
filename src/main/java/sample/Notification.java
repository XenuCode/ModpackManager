package sample;

import java.awt.*;

public class Notification {
    public static void displayNotification(String caption,String message, TrayIcon.MessageType messageType) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image);
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);

        tray.add(trayIcon);

        trayIcon.displayMessage(caption,message, messageType);
    }
}
