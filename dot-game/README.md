# Dot Game

The goal of this exercise is to create a game. In the game, dots move from the top to the bottom of the screen. A player tries to click on the dots, and receives points when they are successful.

![mockup](https://cdn.glitch.com/5637e272-75b8-4d60-af04-8b0adc1b8093%2Fdot-game-scoreboard.png)


## Guidelines for Completing the Exercise

- Your application should work in current Chrome. 
- You can edit any file in the project, and add any assets you require (see below).
- You may look up anything you'd like.
- You should not use a JavaScript library.
- You must write at least the CSS necessary to achieve the basic layout of the game; you may also write additional CSS to improve the design of the game. 
- The project is set up to use Sass, but you may also author plain CSS, or add a different CSS preprocessor.
- Your finished code should be of a quality that you would submit to your peers for a code review. 
- If you have questions, respond to the original email where the exercise was shared with you; your questions will be answered within one business day. 
- When you are ready to share your work, respond to the original email; please include a link to this page, _and the approximate amount of time you spent on the exercise_.

## Building the Game

- The game starts when the page loads. 
- When the game starts, a new dot should appear on the playing area. 
- Dots fall at a constant rate. A player should be able to use a slider to control the speed at which dots fall; at the slider's left-most position, dots should fall at a speed of 10px per second, and at the slider's right-most position, should fall at a speed of 100px per second.
- New dots appear at a random horizontal position at the top of the box. A dot should not "hang" off the left or right edge of the screen. Dots should vary randomly in size from 10px in diameter to 100px in diameter.
- When a player touches or clicks a dot, the dot should disappear from the box and a new dot should appear at the top of the page 1000ms later. The score should be incremented by a value inversely proportional to the size of the dot, with 10px dots worth 10 points, and 100px dots worth 1 point.
- A new dot should also appear every 1000ms.

You can view your running application by clicking the "Show" link in the upper left-hand corner of the page. 

## Creating and Using Assets

To create a new asset, click on the "assets" directory in the front-end section; drag the file from you computer to the browser window.

To use an asset, click on the "assets" directory in the front-end section; then, click the "Copy URL" button to get the asset's URL.