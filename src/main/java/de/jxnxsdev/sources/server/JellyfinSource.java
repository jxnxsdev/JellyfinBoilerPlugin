package de.jxnxsdev.sources.server;

import com.google.gson.JsonObject;
import de.jxnxsdev.jellyfin.Helpers;
import de.jxnxsdev.jellyfin.responses.SystemInfoPublicResponse;
import net.somewhatcity.boiler.api.CreateArgument;
import net.somewhatcity.boiler.api.CreateCommandArguments;
import net.somewhatcity.boiler.api.IBoilerSource;
import net.somewhatcity.boiler.api.SourceConfig;
import net.somewhatcity.boiler.api.display.IBoilerDisplay;
import net.somewhatcity.boiler.api.util.CommandArgumentType;
import net.somewhatcity.boiler.api.util.SourceType;
import org.bukkit.command.CommandSender;
import org.jellyfin.sdk.Jellyfin;
import org.jellyfin.sdk.api.client.ApiClient;


import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@SourceConfig(
    sourceType = SourceType.SERVER
)
@CreateCommandArguments(arguments = {
    @CreateArgument(name = "url", type = CommandArgumentType.GREEDY_STRING)
})

public class JellyfinSource implements IBoilerSource {
    private Jellyfin jellyfin;

    private JPanel panel = new JPanel();
    private Point lastClick;

    private boolean Error;
    private String ErrorMessage;

    @Override
    public void load(IBoilerDisplay display, JsonObject jsonObject) {
        String url = jsonObject.get("url").getAsString();

        if (url == null || url.isEmpty()) {
            Error = true;
            ErrorMessage = "No URL provided";
            return;
        }

        if (!Helpers.validateURL(url)) {
            Error = true;
            ErrorMessage = "The provided URL is invalid or unreachable";
            return;
        }

        SystemInfoPublicResponse systemInfoPublicResponse = Helpers.getSystemInfo(url);

        if (systemInfoPublicResponse == null) {
            Error = true;
            ErrorMessage = "Failed to retrieve system information";
            return;
        }
;

        DrawLogin(display, jsonObject, systemInfoPublicResponse);
    }

    @Override
    public void unload() {

    }

    @Override
    public void draw(Graphics2D g2, Rectangle viewport) {
        if (Error) {
            g2.setColor(Color.BLACK);
            g2.fill(viewport);

            g2.setColor(Color.RED);
            g2.drawString(ErrorMessage, viewport.width / 2, viewport.height / 2);
        } else {
            panel.paint(g2);
        }
    }


    private void DrawLogin(IBoilerDisplay display, JsonObject jsonObject, SystemInfoPublicResponse systemInfoPublicResponse) {
        Dimension dimension = new Dimension(display.width(), display.height());

        panel.setSize(dimension);
        panel.setMinimumSize(dimension);
        panel.setMaximumSize(dimension);
        panel.setPreferredSize(dimension);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Set Jellyfin dark theme colors
        Color backgroundColor = new Color(0x101010);
        Color textColor = Color.WHITE;
        Color inputBackgroundColor = new Color(0x2c2c2c);
        Color buttonColor = new Color(0x00a4dc);

        panel.setBackground(backgroundColor);

        // Create components
        JLabel titleLabel = new JLabel("Login to your Jellyfin Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(textColor);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField usernameField = new JTextField();
        usernameField.setBackground(inputBackgroundColor);
        usernameField.setForeground(textColor);
        usernameField.setSize(new Dimension((int) (dimension.getWidth() / 2), 15));
        usernameField.setMaximumSize(new Dimension((int) (dimension.getWidth() / 2), 15));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(textColor);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(inputBackgroundColor);
        passwordField.setForeground(textColor);
        passwordField.setSize(new Dimension((int) (dimension.getWidth() / 2), 15));
        passwordField.setMaximumSize(new Dimension((int) (dimension.getWidth() / 2), 15));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);


        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(100, 30));

        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add components to the panel
        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(loginButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(statusLabel);
        panel.add(Box.createVerticalGlue());

        // Style components
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(textColor);
        loginButton.setFocusPainted(false);

        // Add action listener to the login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter a username and password");
                return;
            }

            // Perform login
            System.out.println("Logging in with username: " + username + " and password: " + password);
        });

        panel.doLayout();
        panel.validate();
    }

    @Override
    public void onClick(CommandSender sender, int x, int y, boolean right) {
        Component clicked = panel.getComponentAt(x, y);
        lastClick = new Point(x, y);

        if(clicked instanceof JButton button) {
            button.doClick(50);
        }
    }

    @Override
    public void onInput(CommandSender sender, String input) {
        Component selected = panel.getComponentAt(lastClick);

        if(selected instanceof JTextField textField) {
            textField.setText(input);
        }
    }
}