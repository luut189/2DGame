# 2D Game - Stew the Wanderer

![This is supposed to be my line-of-code count](https://tokei.rs/b1/github/luut189/2DGame?category=code)

## About this

This is an attempt at making a 2D game for the **ICS4UI**'s final project

- [Screenshots](#screenshots)

- [Keybindings](#keybindings)

- [How To Run](#how-to-run)

- [Folder Structure](#folder-structure)

## Screenshots

![Screenshot](screenshots/3.png)

![Screenshot](screenshots/5.png)

## Keybindings

Gameplay-related Keybinds:

- **W**: Move up

- **S**: Move down

- **D**: Move right

- **A**: Move left

- **Space**: Attack

- **M**: Toggle the map

- **Q**: Zoom the map out

- **E**: Zoom the map in

Application-related Keybinds:

- **ESC**: Pause the game

- **H**: Toggle the HUD

- **F**: Toggle between fullscreen mode and windowed mode

## How To Run

If you're using **Visual Studio Code**:

- If you clone this project into your local machine, good luck running it without **Code Runner**.

- Actually, even with **Code Runner**, you would probably need to edit the script inside `settings.json` in the `.vscode` folder.

- Your best bet is probably using your personal `settings.json` file.

- If you're using your own `setting.json` file, you **have to** include the `res` folder as a source folder along with the `src` folder by doing this:

```json
"java.project.sourcePaths": [
    "src",
    "res"
]
```

> With the `Extension Pack for Java`, you should be able to run the source code without the need of Code Runner.

If you're using **Replit**:

- I have it setup on **Replit** so you can just press `Run`.

- However, I **do not** recommend running it on **Replit** because of how big this project is.

If you're running it via **Terminal** because you think you're cool:

```bash
git clone https://github.com/luut189/2DGame.git
cd 2DGame
cd src
javac -cp . -d ../bin dev/kyzel/App.java
java -classpath ../bin dev.kyzel.App
```

If you're using something else, **why?**

> Using **IntelliJ IDEA** is understandable though.

## Folder Structure

The workspace contains:

- `.vscode`: Visual Studio Code's settings

- `res`: Resources (*sprites*, *textures*, *musics*,...)

- `src/dev/kyzel`: Sources

  - `game`: Game-related components

    - `entity`: Entity-related components

    - `menu`: Menu-related components

    - `world`: World-related components

  - `gfx`: GUI-related components
  
  - `sfx`: Sound-related component

  - `utils`: Utilities components (mostly include ***Loaders*** for the game)

  - `App.java`: main class

Meanwhile, the compiled output files will be generated in the `bin` folder and ignored by `.gitignore` by default.

> If you are using Visual Studio Code, you can export `.jar` file if you have the `Extension Pack for Java` and it will be in the `build` folder.
