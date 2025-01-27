# Jellyfin Source for Boiler

## Overview  
This plugin adds a source to the [Boiler](https://modrinth.com/plugin/boiler) Minecraft plugin, allowing users to display various sources (videos, images, etc.) within vanilla Minecraft.

---

## Can I Use This?  
**Currently, no.**  
Boiler requires a separate client mod that is not publicly released. This mod enables high-FPS, high-quality video streaming on Fabric modloader clients without lagging the server. It also bypasses the limitations of resolution and color space imposed by maps.

---

## How to Set Up  
1. Place the plugin in your Paper Server's `plugins` folder.  
2. Install the following dependencies:  
   - Boiler  
   - Map Engine  
   - Map Engine Media Extension  
   - Simple Voice Chat  

3. Enable client support in the Boiler configuration file.

---

## How to Use  

1. **Create a display:**  
   Use an existing display or create a new one:  
   `/boiler display create (corner1) (corner2)`

2. **Set the display source to Jellyfin:**  
   `/boiler display source (id) jellyfin (url)`

3. **Interact with the display:**  
   Right-click the display to interact with it. It behaves like normal mouse clicks on a screen.

4. **Enter credentials:**  
   Select the input boxes and use the following command to enter your credentials:  
   `/boiler display action (id) text (text)`

**Note:**  
The URL must be the base URL of your Jellyfin instance, without any additional paths appended to it.
