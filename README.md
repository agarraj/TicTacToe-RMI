# TicTacToe-RMI
Distributed Tic Tac Toe, in a client-server setup using Java RMI protocol.

User will deal only with client. User should be prompted to start the game. As
soon as she wants to play, client will connect to server prompting to join the
game. At a time, only two clients can be connected to server. The server should send a
Busy/Wait message in case third client wants to connect.
The server should be able to run multiple games in parallel.
