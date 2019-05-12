# Deny & Conquer
####Introduction
Deny & Conquer is a fun multiplayer game implemented in *Java* used to demonstrate various distributed system topics like coordination and fault tolerance.

In terms of the gaming aspect, the goal of the player is to color more cells than the opponent. Before a game session, the host can configure game settings like coloring width, number of grid cells, and percentage threshold of a cell. Once a player colors the percentage threshold, then the player owns the cell. No two players can color the same cell at the same time. A player cannot color multiple cells at the same time.

#### System Design:
Deny & Conquer uses a *multithreaded client-server architecture*. The host first runs a server. Then every player including the host connects to the server as a client. The host also creates separate threads for each client to handle communication.

Every client contains their own game state (grid) that is updated by polling the server game state. A client can make changes to the game state by sending commands that are queued and processed by the server. This is commonly known as the *Command Pattern*. The server must determine the order of the generated commands which ties nicely into the next section, Coordination.

#### Coordination:
Since no two players can color the same cell at the same time, it is important to determine which player began coloring the cell first. This is done through comparing timestamps of each command generated by the clients. Different machines will likely have different system time, therefore an initial task of calculating the offset (in ms) between client and server time is done before the game starts. Clients will generate future timestamps taking into account the offset.

#### Fault Tolerance:
Before the game starts, each client is sent a list containing the IPs of all the other players in the game. Once a client detects no response from the server (through polling), it will move down the list to connect to the next available host. If a client realizes that all of its predecessors in the list is unavailable, it will become the host by creating a separate server thread. The game will resume as normal once reconnection is successful.

## Screenshots:
**_Configure Game Settings_**<br/>
<img src="screenshots/configuration.png" width=500>

**_Game Starting Screens_**<br/>
<img src="screenshots/startingscreen.png" width=500>

**_Gameplay (beginning)_**<br/>
<img src="screenshots/gamestart.gif" width=500>

**_Gameplay (end)_**<br/>
<img src="screenshots/gameend.gif" width=500>

**_Results Screen_**<br/>
<img src="screenshots/winningmessage.png" width=500>
