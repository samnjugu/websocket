# websocket
Java native websocket using JSR 356

Java websocket implementation using native java implementation
The socket broadcasts all incoming messages to all connected clients, Jetty is used as the webserver running the application, 
and a keep-alive message is sent out every 15 seconds to keep all connections alive

To enable use of the websocket across your webapplication attach it to the window like below.

window.App.socket = new WebSocket("ws://localhost:8080/restwebapp/events/");

This websocket idea is still a work in progress where I want to inmplement a publish/subscribe framework using websocket, with each incoming mesage having an ID and having a subscriber watching for a particular ID and processing it as needed.
I had the idea to do this when looking at the AWX theme for XBMC which has an option to use websockets to update the UI with. continious information beng sent by the back end like played and remaining time

