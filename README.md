# CyberTigerScoreboard
A scoring graph system, inspired by the official CyberPatriot Scoring Engine and Scoreboard

## Story
This was created to allow students at my High School to better prepare for the CyberPatriot competitions in a real-life competitive scenario. Many scoring engines were hard to configure and/or had limited customization. Only one had online integration but difficult to configure, so I created this. It consists of a Java graph system and a netcat based port. By default it listens on port 1947, but can be changed. Currently this is a scoreboard only and relies on existing scoring systems such as [CyberPatriot Scoring Engine](https://www.uscyberpatriot.org/competition/training-materials/practice-images) to score the virtual machine. All that CyberTigerScoreboard's client portion does is send the parsed scoring information to a central server. The server then displays the received scoring info on a graph.

## Usage
Have the local firewall allow traffic through port 1947. It can be on a local intranet or internet. The client (images) have to send tcp data to that port (can be done with netcat, powershell, many things), in the format of `hostname score`. The hostname can be any way to identify a particular user, such as team name. It is strictly for display purposes on the graph.

## Pics
### Mockup
![Mockup](/res/mockup.png)
### Working all teams display
![Mockup](/res/AllTeams.png)
### Working specific team display
![Mockup](/res/MultiOSOneTeam.png)

GNU/GPL V3 license
