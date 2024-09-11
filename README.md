# JBomberMan

## Description

**JBomberman** is a Java-based remake of the classic Bomberman game highly inspired by *Super Bomberman* (SNES). The game is implemented using **Java Swing** library for graphics. 

## Project Structure

This project follows the **Model-View-Controller (*MVC*)** design pattern:
* `src/controller/`: contains classes responsible for handling user input and updating the model accordingly. This includes actions like moving players and placing bombs, as well as coordinating interactions between the **view** and the **model**.
* `src/view/`: includes classes dedicated to rendering the graphical interface of the game using the Java Swing library. 
* `src/model/`: contains classes that define the core aspects of the game. The model is designed to remain unaware of the view and controller implementations, ensuring that it manages game data and rules without knowledge of how the game is displayed or controlled.

## Usage

To start the game, if you are using an IDE like **IntelliJ** or **Eclipse**, you can simply run the `JBomberman` class inside the `src/` folder.

### Controls

* Arrow keys to move.
* Spacebar to place bombs.
