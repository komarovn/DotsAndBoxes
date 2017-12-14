# Dots and Boxes

## Description
Dots and Boxes is a pencil-and-paper game for two players.

## Rules
Starting with an empty grid of dots, two players take turns adding a single horizontal or vertical line between two unjoined adjacent dots. The player who completes the fourth side of a 1x1 box earns one point and takes another turn. The game ends when no more lines can be placed. The winner is the player with the most points.

The board may be of any size. When short on time, a 2x2 board (a square of 9 dots) is good for beginners. A 5x5 is good for experts.

## Let's Play!
Start the game with register:  
![Register](https://github.com/komarovn/DotsAndBoxes/blob/master/web/start.jpg)

Make moves:  
![Moves](https://github.com/komarovn/DotsAndBoxes/blob/master/web/proc.jpg)

Become a winner:  
![Winner](https://github.com/komarovn/DotsAndBoxes/blob/master/web/win.jpg)

## Installation
1. Run `compile-idl.bat` from root of project folder.
2. Start ORB with `run-orbd.bat`.
3. Run Server with argument `-s <type>` where `<type>` is either `tcp` or `corba`.
4. Run Client with the same argument as for Server.
