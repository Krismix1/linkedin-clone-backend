# Linkedin Clone Backend

This repository keeps track of development of the Java Spring backend code of the Linkedin Clone project

### For developers

The project is developed using the IntelliJ IDEA, therefore most of the setup steps will be related to this IDE.

In order to setup the project with Prettier formatting tool on your workstation using IntelliJ IDEA:

1. In IntelliJ welcome screen choose the `Import Project` option and open the project folder
2. Choose `Import project from external model` option and choose `Maven` from the list and click finish
3. Run `npm install` inside of the project folder in order to install all necessary libraries locally.
4. Install a `Prettier` plugin in IntelliJ IDEA.
5. Navigate to `Languages & Frameworks > JavaScript > Prettier` inside `Preferences` of IntelliJ and choose desired `Node interpreter` and `Prettier package` (both should be autodetected by the IDE, if not the prettier package can be found in `node_modules/prettier` folder inside the project folder)
6. In order to trigger the formatter press `Shift+Command+A` (for macOS) and `Shift+Control+A` (for Windows/Linux) to trigger `Find Action` and search for `Reformat with Prettier` option