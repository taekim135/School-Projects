# Name: Taegyun Kim
# Student #: 251255729
# CS 3357 Assignment 3
# 2023/11/08

import sys
from socket import socket, AF_INET, SOCK_STREAM
from snake import *


# Method to check user key input
def keyControls(key):
    if key == pygame.K_LEFT:
        control = "left"
    elif key == pygame.K_RIGHT:
        control = "right"
    elif key == pygame.K_UP:
        control = "up"
    elif key == pygame.K_DOWN:
        control = "down"
    elif key == pygame.K_ESCAPE:
        control = "quit"
        clientSocket.send(control.encode())
        print("ESC DETECTED. Shutting down Snake Game")
        clientSocket.close()
        pygame.quit()
        exit(0)
    elif key == pygame.K_r:
        print("Game Reset")
        control = "reset"
    else:
        return

    clientSocket.send(control.encode())
    parseState()


# Method for parsing the game state received from the server
def parseState():
    try:
        gameState = clientSocket.recv(500).decode()
        try:
            # Parse the game state into snake bodies and snacks
            snakePosition = gameState.split("|")[0]
            snackLocation = gameState.split("|")[1]

            snakeBody = snakePosition.split("*")
            snackCubes = snackLocation.split("**")

            x, y = map(int, snakeBody[0].strip("()").split(","))
            s = snake((255, 0, 0), (x, y))
            s.body = []
            s.draw(window)

            try:
                # Draw snake body and snacks
                for snakeBodies in snakeBody:
                    x, y = map(int, snakeBodies.strip("()").split(","))
                    body = cube((x, y))
                    s.body.append(body)
                try:
                    window.fill((0, 0, 0))
                    drawGrid(width, rows, window)
                    s.draw(window)
                except:
                    print("Body drawing failed")
                try:
                    for snackCube in snackCubes:
                        x, y = map(int, snackCube.strip("()").split(","))
                        snack = cube((x, y), color=(0, 255, 0))
                        snack.draw(window)
                except:
                    print("Snack drawing failed")

                pygame.display.update()
            except:
                print("Body drawing failed")
        except Exception as e:
            print(e)
            print("Parsing failed")
    except Exception as e:
        print(e)
        print("Game State not received from the server")
        pass


# Method to draw the game grid
def drawGrid(width, rows, surface):
    sizeBetween = width // rows

    x = 0
    y = 0
    for l in range(rows):
        x = x + sizeBetween
        y = y + sizeBetween

        pygame.draw.line(surface, (255, 255, 255), (x, 0), (x, width))
        pygame.draw.line(surface, (255, 255, 255), (0, y), (width, y))


if __name__ == "__main__":
    global width, rows, s, snack, control, count

    portNum = 5555
    localServerIP = "localhost"
    clientSocket = socket(AF_INET, SOCK_STREAM)

    # Connect to the server
    try:
        clientSocket.connect((localServerIP, portNum))
        print("Client connected to Snake server")
    except:
        print("Client failed to connect to Snake server")
        print("Shutting down")
        clientSocket.close()
        sys.exit(0)

    width = 500
    height = 500
    rows = 20

    pygame.init()

    window = pygame.display.set_mode((width, height))
    pygame.display.set_caption("Snake Game")
    drawGrid(width, rows, window)

    print("press 'r' to reset the game")
    print("press the esc key to quit the game")

    # get the initial starting point for snake & snacks
    clientSocket.send("get".encode())
    gameState = clientSocket.recv(500).decode()

    # based on the initial game state received, draw the snake and snacks
    snakePosition = gameState.split("|")[0]
    snackLocation = gameState.split("|")[1]

    snakeBody = snakePosition.split("*")
    snackCubes = snackLocation.split("**")

    x, y = map(int, snakeBody[0].strip("()").split(","))
    s = snake((255, 0, 0), (x, y))
    s.draw(window)

    for snackCube in snackCubes:
        x, y = map(int, snackCube.strip("()").split(","))
        snack = cube((x, y), color=(0, 255, 0))
        snack.draw(window, eyes=False)

    pygame.display.update()

    while True:
        events = pygame.event.get()

        # If not events found, send get status to the server
        if not events:
            clientSocket.send("get".encode())
            parseState()
        else:
            for event in events:
                if event.type == pygame.KEYDOWN:
                    keyControls(event.key)