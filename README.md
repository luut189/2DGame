# 2D Game (There will be more stuff added later) ![This is supposed to be my line-of-code count](https://tokei.rs/b1/github/luut189/2DGame?category=code)

## How to run

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

If you're running it via **Terminal** because you think you're cool:

```bash
git clone https://github.com/luut189/2DGame.git
cd 2DGame
cd src
javac -d ../bin App.java
java -classpath ../bin App
```

If you're using something else, **why?**

## Folder Structure

The workspace contains:

- `.vscode`: Visual Studio Code's settings

- `res`: Resources (*sprites*, *textures*,...)

- `src/dev/kyzel`: Sources

  - `entity`: Entity-related components

  - `gfx`: GUI-related component

  - `utils`: Utilities components (mostly include ***Loaders*** for the game)

  - `world`: World-related components

  - `App.java`: main class

Meanwhile, the compiled output files will be generated in the `bin` folder and ignored by `.gitignore` by default.

> If you are using Visual Studio Code, you can export `.jar` file if you have the `Extension Pack for Java` and it will be in the `build` folder.
