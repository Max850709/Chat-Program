# Chat Program

This program provides multiple users to send messages at the same time in a private room. 

# Background
It is one of the assignment in my Master of Software Development in University of Utah, and it uses Java with Websocket to send the message within the program. 
The front end is JavaScript and HTML.

# Install
Git clone to your local repository
```
git clone https://github.com/Max850709/Chat-Program.git
```

# Usage
1. Use ternimal to start the program(at your root directory) 
```
$ java -jar ChatServer.jar
```
2. Turn on the web browser and go to localhost:8080 `127.0.0.1:8080`
3. Start chatting!

Limitation: User name and room name only allows English character!

# Example
![This is an image](https://github.com/Max850709/Chat-Program/blob/main/Resources/pic.png?raw=true)

```
 0                   1                   2                   3
 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
+-+-+-+-+-------+-+-------------+-------------------------------+
|F|R|R|R| opcode|M| Payload len |    Extended payload length    |
|I|S|S|S|  (4)  |A|     (7)     |             (16/64)           |
|N|V|V|V|       |S|             |   (if payload len==126/127)   |
| |1|2|3|       |K|             |                               |
+-+-+-+-+-------+-+-------------+ - - - - - - - - - - - - - - - +
|     Extended payload length continued, if payload len == 127  |
+ - - - - - - - - - - - - - - - +-------------------------------+
|                               |Masking-key, if MASK set to 1  |
+-------------------------------+-------------------------------+
| Masking-key (continued)       |          Payload Data         |
+-------------------------------- - - - - - - - - - - - - - - - +
:                     Payload Data continued ...                :
+ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - +
|                     Payload Data continued ...                |
+---------------------------------------------------------------+
```
