# CPscoring
A scoring graph system, inspired by the official CyberPatriot Scoring engine

## Story
This was created to allow students at my High School to better prepare for the CyberPatriot competitions in a real-life competitive scenario. Many scoring engines were hard to config�ure and/or had limited customization. Only one had online integration but difficult to configure, so I created this. It consists of a Java graph system and a netcat based port. By default it listens on port 1099, but can be changed.

## Usage
Have the local firewall allow traffic through port 1099. It can be on a local intranet or internet. The client (images) have to send tcp data to that port (can be done with netcat, powershell, many things), in the format of `hostname score`. The hostname can be any way to identify a particular user, such as team name. It is strictly for display purposes on the graph.

## Pics
![Mockkup](/res/mockup.png)


GNU/GPL V3 license
