# CyberTigerScoreboard
A scoring graph system, inspired by the official CyberPatriot Scoring Engine and Scoreboard

## Story
This was created to allow students at my High School to better prepare for the CyberPatriot competitions in a real-life competitive scenario. Many scoring engines were hard to configure and/or had limited customization. Only one had online integration but difficult to configure (and now defunct), so I created this. It consists of a JavaFX graph system and a "server" which gets data from a Microsoft Azure server. It is currently using my Azure server through Microsoft Image, but has a wizard to configure to use your own Microsoft SQL server, not necessarily limited to Azure. Currently this is a scoreboard only and relies on an existing scoring system which can upload data to an SQL server, in the format of `teamname-OS`, `Score`, `Time`, where `score` and `time` are `ints`. `teamname-OS` should have the OS part starting with either `lin` or `win`, and a custom name after that. The graph then displays the received scoring info from the SQL DB on a graph.

## Usage
The images should be using a Microsoft SQL command line tool on a loop or startup job or daemon to simply client side setup. Here, you simply need to go to `Server --> Start Azure server` and follow the prompts to get started

## Demo

[Youtube Link](https://youtu.be/TiNSWCAyXwM)

## Pics
### Mockup
![Mockup](/res/mockup.png)
### Working all teams display
![All Teams](/res/AllTeams.png)
### Working specific team display
![Specific Team](/res/MultiOSOneTeam.png)

GNU/GPL V3 license
