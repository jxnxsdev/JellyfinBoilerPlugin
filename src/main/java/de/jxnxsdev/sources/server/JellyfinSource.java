package de.jxnxsdev.sources.server;

import com.google.gson.JsonObject;
import de.jxnxsdev.jellyfin.Helpers;
import de.jxnxsdev.jellyfin.Shows;
import de.jxnxsdev.jellyfin.User;
import de.jxnxsdev.jellyfin.responses.*;
import de.jxnxsdev.swing.Base64ImagePanel;
import net.somewhatcity.boiler.api.*;
import net.somewhatcity.boiler.api.display.IBoilerDisplay;
import net.somewhatcity.boiler.api.util.CommandArgumentType;
import net.somewhatcity.boiler.api.util.SourceType;
import org.bukkit.command.CommandSender;


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

        panel.removeAll();

        panel.setSize(dimension);
        panel.setMinimumSize(dimension);
        panel.setMaximumSize(dimension);
        panel.setPreferredSize(dimension);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Color backgroundColor = new Color(0x101010);
        Color textColor = Color.WHITE;
        Color inputBackgroundColor = new Color(0x2c2c2c);
        Color buttonColor = new Color(0x00a4dc);

        panel.setBackground(backgroundColor);

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

        JLabel statusLabel = new JLabel("Please enter your username and password");
        statusLabel.setForeground(Color.RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setSize(new Dimension(400, 30));
        statusLabel.setMaximumSize(new Dimension(400, 30));

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

        loginButton.setBackground(buttonColor);
        loginButton.setForeground(textColor);
        loginButton.setFocusPainted(false);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter a username and password");
                return;
            }

            AuthenticateByNameResponse authenticateByNameResponse = User.authenticateByName(jsonObject.get("url").getAsString(), username, password);

            if (authenticateByNameResponse == null) {
                statusLabel.setText("Failed to authenticate user");
                return;
            }

            DrawViews(display, jsonObject, systemInfoPublicResponse, authenticateByNameResponse);
        });

        panel.doLayout();
        panel.validate();
    }

    private int currentView = 0;

    private void DrawViews(IBoilerDisplay display, JsonObject jsonObject, SystemInfoPublicResponse systemInfoPublicResponse, AuthenticateByNameResponse authenticateByNameResponse) {
        currentView = 0;
        Dimension dimension = new Dimension(display.width(), display.height());
        panel.removeAll();
        panel.setSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setBackground(new Color(0x101010));

        Color textColor = Color.WHITE;
        Color buttonColor = new Color(0x00a4dc);

        UserViewsResponse userViewsResponse = User.getUserViews(jsonObject.get("url").getAsString(), authenticateByNameResponse);

        if (userViewsResponse == null) {
            JLabel errorLabel = new JLabel("Failed to retrieve user views");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setBounds(dimension.width / 4, dimension.height / 2 - 15, dimension.width / 2, 30); // Centered error message
            panel.add(errorLabel);
            return;
        }

        userViewsResponse.items.removeIf(view -> !view.collectionType.equals("tvshows") && !view.collectionType.equals("movies"));

        if (userViewsResponse.items.isEmpty()) {
            JLabel errorLabel = new JLabel("This user/server does not have access to any video libraries");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setBounds(dimension.width / 4, dimension.height / 2 - 15, dimension.width / 2, 30); // Centered error message
            panel.add(errorLabel);
            return;
        }

        JLabel titleLabel = new JLabel("Select a Video Library");
        titleLabel.setFont(new Font("Arial", Font.BOLD, dimension.height / 20)); // Scaled title size
        titleLabel.setForeground(textColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 20, dimension.width, dimension.height / 20);

        int imageWidth = dimension.width / 3; // Image width is 1/3 of panel width
        int imageHeight = (int) (imageWidth * 1.5); // Maintain 2:3 aspect ratio
        Base64ImagePanel coverImage = new Base64ImagePanel(
                Helpers.getImage(jsonObject.get("url").getAsString(), userViewsResponse.items.get(currentView).id, 200, 200, "90")
        );
        coverImage.setBounds((dimension.width - imageWidth) / 2, dimension.height / 4, imageWidth, imageHeight / 2 - 30);

        coverImage.setPreferredSize(new Dimension(imageWidth, imageHeight));
        coverImage.setMaximumSize(new Dimension(imageWidth, imageHeight));
        coverImage.setMinimumSize(new Dimension(imageWidth, imageHeight));

        int selButtonWidth = imageWidth;
        int selButtonHeight = dimension.height / 25;
        int selButtonY = dimension.height - (dimension.height / 8) - selButtonHeight - 10;

        JButton selectButton = new JButton("Select Libary");
        selectButton.setBackground(buttonColor);
        selectButton.setForeground(textColor);
        selectButton.setFocusPainted(false);
        selectButton.setBounds((dimension.width / 2) - (selButtonWidth / 2), selButtonY, selButtonWidth, selButtonHeight);

        selectButton.addActionListener(e -> {
            if (userViewsResponse.items.get(currentView).collectionType.equals("tvshows")) {
                DrawItemsTVShows(display, jsonObject, systemInfoPublicResponse, authenticateByNameResponse, userViewsResponse.items.get(currentView).id);
            } else if (userViewsResponse.items.get(currentView).collectionType.equals("movies")) {
                DrawItemsMovies(display, jsonObject, systemInfoPublicResponse, authenticateByNameResponse, userViewsResponse.items.get(currentView).id);
            }
        });

        JLabel libraryTitle = new JLabel(userViewsResponse.items.get(currentView).name);
        libraryTitle.setFont(new Font("Arial", Font.BOLD, dimension.height / 30)); // Scaled dynamically
        libraryTitle.setForeground(textColor);
        libraryTitle.setHorizontalAlignment(SwingConstants.CENTER);
        libraryTitle.setBounds(0, dimension.height - (dimension.height / 8) - selButtonHeight - 30, dimension.width, dimension.height / 30);

        int buttonWidth = dimension.width / 10;
        int buttonHeight = dimension.height / 25;
        int buttonY = dimension.height - (dimension.height / 8);

        JButton backButton = new JButton("Back");
        backButton.setBackground(buttonColor);
        backButton.setForeground(textColor);
        backButton.setFocusPainted(false);
        backButton.setBounds(dimension.width / 4 - buttonWidth / 2, buttonY, buttonWidth, buttonHeight);

        JLabel pageNumber = new JLabel((currentView + 1) + " / " + userViewsResponse.items.size());
        pageNumber.setForeground(textColor);
        pageNumber.setFont(new Font("Arial", Font.PLAIN, dimension.height / 40));
        pageNumber.setHorizontalAlignment(SwingConstants.CENTER);
        pageNumber.setBounds((dimension.width - 100) / 2, buttonY, 100, buttonHeight);

        JButton nextButton = new JButton("Next");
        nextButton.setBackground(buttonColor);
        nextButton.setForeground(textColor);
        nextButton.setFocusPainted(false);
        nextButton.setBounds(3 * dimension.width / 4 - buttonWidth / 2, buttonY, buttonWidth, buttonHeight);

        backButton.addActionListener(e -> {
            if (currentView > 0) {
                currentView--;
                coverImage.setImage(Helpers.getImage(jsonObject.get("url").getAsString(), userViewsResponse.items.get(currentView).id, 400, 600, "90"));
                libraryTitle.setText(userViewsResponse.items.get(currentView).name);
                pageNumber.setText((currentView + 1) + " / " + userViewsResponse.items.size());
            }
        });

        nextButton.addActionListener(e -> {
            if (currentView < userViewsResponse.items.size() - 1) {
                currentView++;
                coverImage.setImage(Helpers.getImage(jsonObject.get("url").getAsString(), userViewsResponse.items.get(currentView).id, 400, 600, "90"));
                libraryTitle.setText(userViewsResponse.items.get(currentView).name);
                pageNumber.setText((currentView + 1) + " / " + userViewsResponse.items.size());
            }
        });

        panel.setLayout(null);
        panel.add(titleLabel);
        panel.add(coverImage);
        panel.add(selectButton);
        panel.add(libraryTitle);
        panel.add(backButton);
        panel.add(pageNumber);
        panel.add(nextButton);

        panel.revalidate();
        panel.repaint();
    }

    private int currentPage = 0;

    private void DrawItemsMovies(IBoilerDisplay display, JsonObject jsonObject, SystemInfoPublicResponse systemInfoPublicResponse, AuthenticateByNameResponse authenticateByNameResponse, String viewId) {
        currentPage = 0;

        Dimension dimension = new Dimension(display.width(), display.height());
        panel.removeAll();
        panel.setSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setBackground(new Color(0x101010));

        Color textColor = Color.WHITE;
        Color buttonColor = new Color(0x00a4dc);

        UserItemsResponse userItemsResponse = User.getUserItemsMovies(jsonObject.get("url").getAsString(), authenticateByNameResponse, viewId);

        if (userItemsResponse == null) {
            JLabel errorLabel = new JLabel("Failed to retrieve user items");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setBounds(dimension.width / 4, dimension.height / 2 - 15, dimension.width / 2, 30); // Centered error message
            panel.add(errorLabel);
            return;
        }

        userItemsResponse.items.removeIf(item -> !item.type.equalsIgnoreCase("movie"));

        if (userItemsResponse.items.isEmpty()) {
            JLabel errorLabel = new JLabel("This libary does not contain any movies");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setBounds(dimension.width / 4, dimension.height / 2 - 15, dimension.width / 2, 30); // Centered error message
            panel.add(errorLabel);
            return;
        }

        JLabel titleLabel = new JLabel("Select a Movie");
        titleLabel.setFont(new Font("Arial", Font.BOLD, dimension.height / 20)); // Scaled title size
        titleLabel.setForeground(textColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 20, dimension.width, dimension.height / 20);

        int imageWidth = dimension.width / 3; // Image width is 1/3 of panel width
        int imageHeight = (int) (imageWidth * 1.5); // Maintain 2:3 aspect ratio

        Base64ImagePanel coverImage = new Base64ImagePanel(
                Helpers.getImage(jsonObject.get("url").getAsString(), userItemsResponse.items.get(0).id, 200, 200, "90")
        );
        coverImage.setBounds((dimension.width - imageWidth) / 2, dimension.height / 4, imageWidth, imageHeight / 2 - 30);

        coverImage.setPreferredSize(new Dimension(imageWidth, imageHeight));
        coverImage.setMaximumSize(new Dimension(imageWidth, imageHeight));
        coverImage.setMinimumSize(new Dimension(imageWidth, imageHeight));

        int selButtonWidth = imageWidth;
        int selButtonHeight = dimension.height / 25;
        int selButtonY = dimension.height - (dimension.height / 8) - selButtonHeight - 10;

        JButton selectButton = new JButton("Play Movie");
        selectButton.setBackground(buttonColor);
        selectButton.setForeground(textColor);
        selectButton.setFocusPainted(false);
        selectButton.setBounds((dimension.width / 2) - (selButtonWidth / 2), selButtonY, selButtonWidth, selButtonHeight);

        selectButton.addActionListener(e -> {
            String playerURL = jsonObject.get("url").getAsString() + "/Items/" + userItemsResponse.items.get(currentEpisode).id + "/Download?api_key=" + authenticateByNameResponse.accessToken;
            JsonObject playerObject = new JsonObject();
            playerObject.addProperty("videourl", playerURL);
            display.source("jellyfinplayer", playerObject);
        });

        JLabel movieTitle = new JLabel(userItemsResponse.items.get(0).name);
        movieTitle.setFont(new Font("Arial", Font.BOLD, dimension.height / 30)); // Scaled dynamically
        movieTitle.setForeground(textColor);
        movieTitle.setHorizontalAlignment(SwingConstants.CENTER);
        movieTitle.setBounds(0, dimension.height - (dimension.height / 8) - selButtonHeight - 30, dimension.width, dimension.height / 30);

        int buttonWidth = dimension.width / 10;
        int buttonHeight = dimension.height / 25;
        int buttonY = dimension.height - (dimension.height / 8);

        JButton backButton = new JButton("Back");
        backButton.setBackground(buttonColor);
        backButton.setForeground(textColor);
        backButton.setFocusPainted(false);
        backButton.setBounds(dimension.width / 4 - buttonWidth / 2, buttonY, buttonWidth, buttonHeight);

        JLabel pageNumber = new JLabel((currentPage + 1) + " / " + userItemsResponse.items.size());
        pageNumber.setForeground(textColor);
        pageNumber.setFont(new Font("Arial", Font.PLAIN, dimension.height / 40));
        pageNumber.setHorizontalAlignment(SwingConstants.CENTER);
        pageNumber.setBounds((dimension.width - 100) / 2, buttonY, 100, buttonHeight);

        JButton nextButton = new JButton("Next");
        nextButton.setBackground(buttonColor);
        nextButton.setForeground(textColor);
        nextButton.setFocusPainted(false);
        nextButton.setBounds(3 * dimension.width / 4 - buttonWidth / 2, buttonY, buttonWidth, buttonHeight);

        backButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                coverImage.setImage(Helpers.getImage(jsonObject.get("url").getAsString(), userItemsResponse.items.get(currentPage).id, 400, 600, "90"));
                movieTitle.setText(userItemsResponse.items.get(currentPage).name);
                pageNumber.setText((currentPage + 1) + " / " + userItemsResponse.items.size());
            }
        });

        nextButton.addActionListener(e -> {
            if (currentPage < userItemsResponse.items.size() - 1) {
                currentPage++;
                coverImage.setImage(Helpers.getImage(jsonObject.get("url").getAsString(), userItemsResponse.items.get(currentPage).id, 400, 600, "90"));
                movieTitle.setText(userItemsResponse.items.get(currentPage).name);
                pageNumber.setText((currentPage + 1) + " / " + userItemsResponse.items.size());
            }
        });

        panel.setLayout(null);
        panel.add(titleLabel);
        panel.add(coverImage);
        panel.add(selectButton);
        panel.add(movieTitle);
        panel.add(backButton);
        panel.add(pageNumber);
        panel.add(nextButton);

        panel.revalidate();
        panel.repaint();

    }

    private void DrawItemsTVShows (IBoilerDisplay display, JsonObject jsonObject, SystemInfoPublicResponse systemInfoPublicResponse, AuthenticateByNameResponse authenticateByNameResponse, String viewId) {
        currentPage = 0;

        Dimension dimension = new Dimension(display.width(), display.height());
        panel.removeAll();
        panel.setSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setBackground(new Color(0x101010));

        Color textColor = Color.WHITE;
        Color buttonColor = new Color(0x00a4dc);

        UserItemsResponse userItemsResponse = User.getUserItemsShows(jsonObject.get("url").getAsString(), authenticateByNameResponse, viewId);

        // TODO: Fix list ordering for every page

        if (userItemsResponse == null) {
            JLabel errorLabel = new JLabel("Failed to retrieve user items");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setBounds(dimension.width / 4, dimension.height / 2 - 15, dimension.width / 2, 30); // Centered error message
            panel.add(errorLabel);
            return;
        }

        userItemsResponse.items.removeIf(item -> !item.type.equalsIgnoreCase("series"));

        if (userItemsResponse.items.isEmpty()) {
            JLabel errorLabel = new JLabel("This libary does not contain any TV Shows");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setBounds(dimension.width / 4, dimension.height / 2 - 15, dimension.width / 2, 30); // Centered error message
            panel.add(errorLabel);
            return;
        }

        JLabel titleLabel = new JLabel("Select a TV Show");
        titleLabel.setFont(new Font("Arial", Font.BOLD, dimension.height / 20)); // Scaled title size
        titleLabel.setForeground(textColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 20, dimension.width, dimension.height / 20);

        int imageWidth = dimension.width / 3; // Image width is 1/3 of panel width
        int imageHeight = (int) (imageWidth * 1.5); // Maintain 2:3 aspect ratio

        Base64ImagePanel coverImage = new Base64ImagePanel(
                Helpers.getImage(jsonObject.get("url").getAsString(), userItemsResponse.items.get(0).id, 200, 200, "90")
        );
        coverImage.setBounds((dimension.width - imageWidth * 2) / 2, dimension.height / 4, imageWidth, imageHeight / 2 - 30);

        coverImage.setPreferredSize(new Dimension(imageWidth, imageHeight));
        coverImage.setMaximumSize(new Dimension(imageWidth, imageHeight));
        coverImage.setMinimumSize(new Dimension(imageWidth, imageHeight));

        int selButtonWidth = imageWidth;
        int selButtonHeight = dimension.height / 25;
        int selButtonY = dimension.height - (dimension.height / 8) - selButtonHeight - 10;

        JButton selectButton = new JButton("Select TV Show");
        selectButton.setBackground(buttonColor);
        selectButton.setForeground(textColor);
        selectButton.setFocusPainted(false);
        selectButton.setBounds((dimension.width / 2) - (selButtonWidth / 2), selButtonY, selButtonWidth, selButtonHeight);

        selectButton.addActionListener(e -> {
            DrawSeasons(display, jsonObject, systemInfoPublicResponse, authenticateByNameResponse, userItemsResponse.items.get(currentPage).id);
        });

        JLabel tvShowTitle = new JLabel(userItemsResponse.items.get(0).name);
        tvShowTitle.setFont(new Font("Arial", Font.BOLD, dimension.height / 30)); // Scaled dynamically
        tvShowTitle.setForeground(textColor);
        tvShowTitle.setHorizontalAlignment(SwingConstants.CENTER);
        tvShowTitle.setBounds(0, dimension.height - (dimension.height / 8) - selButtonHeight - 30, dimension.width, dimension.height / 30);

        int buttonWidth = dimension.width / 10;
        int buttonHeight = dimension.height / 25;
        int buttonY = dimension.height - (dimension.height / 8);

        JButton backButton = new JButton("Back");
        backButton.setBackground(buttonColor);
        backButton.setForeground(textColor);
        backButton.setFocusPainted(false);
        backButton.setBounds(dimension.width / 4 - buttonWidth / 2, buttonY, buttonWidth, buttonHeight);

        JLabel pageNumber = new JLabel((currentPage + 1) + " / " + userItemsResponse.items.size());
        pageNumber.setForeground(textColor);
        pageNumber.setFont(new Font("Arial", Font.PLAIN, dimension.height / 40));
        pageNumber.setHorizontalAlignment(SwingConstants.CENTER);
        pageNumber.setBounds((dimension.width - 100) / 2, buttonY, 100, buttonHeight);

        JButton nextButton = new JButton("Next");
        nextButton.setBackground(buttonColor);
        nextButton.setForeground(textColor);
        nextButton.setFocusPainted(false);
        nextButton.setBounds(3 * dimension.width / 4 - buttonWidth / 2, buttonY, buttonWidth, buttonHeight);

        backButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                coverImage.setImage(Helpers.getImage(jsonObject.get("url").getAsString(), userItemsResponse.items.get(currentPage).id, 400, 600, "90"));
                tvShowTitle.setText(userItemsResponse.items.get(currentPage).name);
                pageNumber.setText((currentPage + 1) + " / " + userItemsResponse.items.size());
            }
        });

        nextButton.addActionListener(e -> {
            if (currentPage < userItemsResponse.items.size() - 1) {
                currentPage++;
                coverImage.setImage(Helpers.getImage(jsonObject.get("url").getAsString(), userItemsResponse.items.get(currentPage).id, 400, 600, "90"));
                tvShowTitle.setText(userItemsResponse.items.get(currentPage).name);
                pageNumber.setText((currentPage + 1) + " / " + userItemsResponse.items.size());
            }
        });

        panel.setLayout(null);
        panel.add(titleLabel);
        panel.add(coverImage);
        panel.add(selectButton);
        panel.add(tvShowTitle);
        panel.add(backButton);
        panel.add(pageNumber);
        panel.add(nextButton);

        panel.revalidate();
        panel.repaint();
    }

    private int currentSeason = 0;

    private void DrawSeasons (IBoilerDisplay display, JsonObject jsonObject, SystemInfoPublicResponse systemInfoPublicResponse, AuthenticateByNameResponse authenticateByNameResponse, String seriesId) {
        currentSeason = 0;

        Dimension dimension = new Dimension(display.width(), display.height());
        panel.removeAll();
        panel.setSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setBackground(new Color(0x101010));

        Color textColor = Color.WHITE;
        Color buttonColor = new Color(0x00a4dc);

        ShowsSeasonResponse showsSeasonResponse = Shows.getSeasons(jsonObject.get("url").getAsString(), authenticateByNameResponse, seriesId);

        if (showsSeasonResponse == null) {
            JLabel errorLabel = new JLabel("Failed to retrieve seasons");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setBounds(dimension.width / 4, dimension.height / 2 - 15, dimension.width / 2, 30); // Centered error message
            panel.add(errorLabel);
            return;
        }

        if (showsSeasonResponse.items.isEmpty()) {
            JLabel errorLabel = new JLabel("This TV Show does not contain any seasons");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setBounds(dimension.width / 4, dimension.height / 2 - 15, dimension.width / 2, 30); // Centered error message
            panel.add(errorLabel);
            return;
        }

        JLabel titleLabel = new JLabel("Select a Season");
        titleLabel.setFont(new Font("Arial", Font.BOLD, dimension.height / 20)); // Scaled title size
        titleLabel.setForeground(textColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 20, dimension.width, dimension.height / 20);

        int imageWidth = dimension.width / 3; // Image width is 1/3 of panel width
        int imageHeight = (int) (imageWidth * 1.5); // Maintain 2:3 aspect ratio

        Base64ImagePanel coverImage = new Base64ImagePanel(
                Helpers.getImage(jsonObject.get("url").getAsString(), showsSeasonResponse.items.get(0).id, 200, 200, "90")
        );

        coverImage.setBounds((dimension.width - imageWidth) / 2, dimension.height / 4, imageWidth, imageHeight / 2 - 30);

        coverImage.setPreferredSize(new Dimension(imageWidth, imageHeight));
        coverImage.setMaximumSize(new Dimension(imageWidth, imageHeight));
        coverImage.setMinimumSize(new Dimension(imageWidth, imageHeight));

        int selButtonWidth = imageWidth;
        int selButtonHeight = dimension.height / 25;
        int selButtonY = dimension.height - (dimension.height / 8) - selButtonHeight - 10;

        JButton selectButton = new JButton("Select Season");
        selectButton.setBackground(buttonColor);
        selectButton.setForeground(textColor);
        selectButton.setFocusPainted(false);
        selectButton.setBounds((dimension.width / 2) - (selButtonWidth / 2), selButtonY, selButtonWidth, selButtonHeight);

        selectButton.addActionListener(e -> {
            DrawEpisodes(display, jsonObject, systemInfoPublicResponse, authenticateByNameResponse, seriesId, showsSeasonResponse.items.get(currentSeason).id);
        });

        JLabel seasonTitle = new JLabel(showsSeasonResponse.items.get(0).name);
        seasonTitle.setFont(new Font("Arial", Font.BOLD, dimension.height / 30)); // Scaled dynamically
        seasonTitle.setForeground(textColor);
        seasonTitle.setHorizontalAlignment(SwingConstants.CENTER);
        seasonTitle.setBounds(0, dimension.height - (dimension.height / 8) - selButtonHeight - 30, dimension.width, dimension.height / 30);

        int buttonWidth = dimension.width / 10;
        int buttonHeight = dimension.height / 25;
        int buttonY = dimension.height - (dimension.height / 8);

        JButton backButton = new JButton("Back");
        backButton.setBackground(buttonColor);
        backButton.setForeground(textColor);
        backButton.setFocusPainted(false);
        backButton.setBounds(dimension.width / 4 - buttonWidth / 2, buttonY, buttonWidth, buttonHeight);

        JLabel pageNumber = new JLabel((currentSeason + 1) + " / " + showsSeasonResponse.items.size());
        pageNumber.setForeground(textColor);
        pageNumber.setFont(new Font("Arial", Font.PLAIN, dimension.height / 40));
        pageNumber.setHorizontalAlignment(SwingConstants.CENTER);
        pageNumber.setBounds((dimension.width - 100) / 2, buttonY, 100, buttonHeight);

        JButton nextButton = new JButton("Next");
        nextButton.setBackground(buttonColor);
        nextButton.setForeground(textColor);
        nextButton.setFocusPainted(false);
        nextButton.setBounds(3 * dimension.width / 4 - buttonWidth / 2, buttonY, buttonWidth, buttonHeight);

        backButton.addActionListener(e -> {
            if (currentSeason > 0) {
                currentSeason--;
                coverImage.setImage(Helpers.getImage(jsonObject.get("url").getAsString(), showsSeasonResponse.items.get(currentSeason).id, 400, 600, "90"));
                seasonTitle.setText(showsSeasonResponse.items.get(currentSeason).name);
                pageNumber.setText((currentSeason + 1) + " / " + showsSeasonResponse.items.size());
            }
        });

        nextButton.addActionListener(e -> {
            if (currentSeason < showsSeasonResponse.items.size() - 1) {
                currentSeason++;
                coverImage.setImage(Helpers.getImage(jsonObject.get("url").getAsString(), showsSeasonResponse.items.get(currentSeason).id, 400, 600, "90"));
                seasonTitle.setText(showsSeasonResponse.items.get(currentSeason).name);
                pageNumber.setText((currentSeason + 1) + " / " + showsSeasonResponse.items.size());
            }
        });

        panel.setLayout(null);
        panel.add(titleLabel);
        panel.add(coverImage);
        panel.add(selectButton);
        panel.add(seasonTitle);
        panel.add(backButton);
        panel.add(pageNumber);
        panel.add(nextButton);

        panel.revalidate();
        panel.repaint();

    }

    private int currentEpisode = 0;

    private void DrawEpisodes (IBoilerDisplay display, JsonObject jsonObject, SystemInfoPublicResponse systemInfoPublicResponse, AuthenticateByNameResponse authenticateByNameResponse, String seriesId, String seasonId) {
        currentEpisode = 0;

        Dimension dimension = new Dimension(display.width(), display.height());
        panel.removeAll();
        panel.setSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setBackground(new Color(0x101010));

        Color textColor = Color.WHITE;
        Color buttonColor = new Color(0x00a4dc);

        ShowsEpisodesResponse showsEpisodesResponse = Shows.getEpisodes(jsonObject.get("url").getAsString(), authenticateByNameResponse, seriesId, seasonId);

        if (showsEpisodesResponse == null) {
            JLabel errorLabel = new JLabel("Failed to retrieve episodes");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setBounds(dimension.width / 4, dimension.height / 2 - 15, dimension.width / 2, 30); // Centered error message
            panel.add(errorLabel);
            return;
        }

        if (showsEpisodesResponse.items.isEmpty()) {
            JLabel errorLabel = new JLabel("This season does not contain any episodes");
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setBounds(dimension.width / 4, dimension.height / 2 - 15, dimension.width / 2, 30); // Centered error message
            panel.add(errorLabel);
            return;
        }

        JLabel titleLabel = new JLabel("Select an Episode");
        titleLabel.setFont(new Font("Arial", Font.BOLD, dimension.height / 20)); // Scaled title size
        titleLabel.setForeground(textColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 20, dimension.width, dimension.height / 20);

        int imageWidth = dimension.width / 3; // Image width is 1/3 of panel width
        int imageHeight = (int) (imageWidth * 1.5); // Maintain 2:3 aspect ratio

        Base64ImagePanel coverImage = new Base64ImagePanel(
                Helpers.getImage(jsonObject.get("url").getAsString(), showsEpisodesResponse.items.get(0).id, 200, 200, "90")
        );

        coverImage.setBounds((dimension.width - imageWidth) / 2, dimension.height / 4, imageWidth, imageHeight / 2 - 30);

        coverImage.setPreferredSize(new Dimension(imageWidth, imageHeight));
        coverImage.setMaximumSize(new Dimension(imageWidth, imageHeight));
        coverImage.setMinimumSize(new Dimension(imageWidth, imageHeight));

        int selButtonWidth = imageWidth;
        int selButtonHeight = dimension.height / 25;
        int selButtonY = dimension.height - (dimension.height / 8) - selButtonHeight - 10;

        JButton selectButton = new JButton("Play Episode");
        selectButton.setBackground(buttonColor);
        selectButton.setForeground(textColor);
        selectButton.setFocusPainted(false);
        selectButton.setBounds((dimension.width / 2) - (selButtonWidth / 2), selButtonY, selButtonWidth, selButtonHeight);

        selectButton.addActionListener(e -> {
            String playerURL = jsonObject.get("url").getAsString() + "/Items/" + showsEpisodesResponse.items.get(currentEpisode).id + "/Download?api_key=" + authenticateByNameResponse.accessToken;
            JsonObject playerObject = new JsonObject();
            playerObject.addProperty("videourl", playerURL);
            display.source("jellyfinplayer", playerObject);
        });

        JLabel episodeTitle = new JLabel(showsEpisodesResponse.items.get(0).name);
        episodeTitle.setFont(new Font("Arial", Font.BOLD, dimension.height / 30)); // Scaled dynamically
        episodeTitle.setForeground(textColor);
        episodeTitle.setHorizontalAlignment(SwingConstants.CENTER);
        episodeTitle.setBounds(0, dimension.height - (dimension.height / 8) - selButtonHeight - 30, dimension.width, dimension.height / 30);

        int buttonWidth = dimension.width / 10;
        int buttonHeight = dimension.height / 25;
        int buttonY = dimension.height - (dimension.height / 8);

        JButton backButton = new JButton("Back");
        backButton.setBackground(buttonColor);
        backButton.setForeground(textColor);
        backButton.setFocusPainted(false);
        backButton.setBounds(dimension.width / 4 - buttonWidth / 2, buttonY, buttonWidth, buttonHeight);

        JLabel pageNumber = new JLabel((currentEpisode + 1) + " / " + showsEpisodesResponse.items.size());
        pageNumber.setForeground(textColor);
        pageNumber.setFont(new Font("Arial", Font.PLAIN, dimension.height / 40));
        pageNumber.setHorizontalAlignment(SwingConstants.CENTER);
        pageNumber.setBounds((dimension.width - 100) / 2, buttonY, 100, buttonHeight);

        JButton nextButton = new JButton("Next");
        nextButton.setBackground(buttonColor);
        nextButton.setForeground(textColor);
        nextButton.setFocusPainted(false);
        nextButton.setBounds(3 * dimension.width / 4 - buttonWidth / 2, buttonY, buttonWidth, buttonHeight);

        backButton.addActionListener(e -> {
            if (currentEpisode > 0) {
                currentEpisode--;
                coverImage.setImage(Helpers.getImage(jsonObject.get("url").getAsString(), showsEpisodesResponse.items.get(currentEpisode).id, 400, 600, "90"));
                episodeTitle.setText(showsEpisodesResponse.items.get(currentEpisode).name);
                pageNumber.setText((currentEpisode + 1) + " / " + showsEpisodesResponse.items.size());
            }
        });

        nextButton.addActionListener(e -> {
            if (currentEpisode < showsEpisodesResponse.items.size() - 1) {
                currentEpisode++;
                coverImage.setImage(Helpers.getImage(jsonObject.get("url").getAsString(), showsEpisodesResponse.items.get(currentEpisode).id, 400, 600, "90"));
                episodeTitle.setText(showsEpisodesResponse.items.get(currentEpisode).name);
                pageNumber.setText((currentEpisode + 1) + " / " + showsEpisodesResponse.items.size());
            }
        });

        panel.setLayout(null);
        panel.add(titleLabel);
        panel.add(coverImage);
        panel.add(selectButton);
        panel.add(episodeTitle);
        panel.add(backButton);
        panel.add(pageNumber);
        panel.add(nextButton);

        panel.revalidate();
        panel.repaint();
    }

    private Component lastComponent;

    @Override
    public void onClick(CommandSender sender, int x, int y, boolean right) {
        Component clicked = panel.getComponentAt(x, y);

        if(clicked instanceof JTextField || clicked instanceof JPasswordField) {
            clicked.setBackground(Color.darkGray);
            if(lastComponent != null && lastComponent != clicked) {
                lastComponent.setBackground(new Color(0x2c2c2c));
            }

            lastComponent = clicked;
        }

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