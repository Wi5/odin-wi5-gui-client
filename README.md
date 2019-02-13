odin-wi5-gui-client
===================

This is a Java-based client that connects to a Wi-5 controller, and can show the current status of the network.

The Wi-5 controller MUST run the application SmartApSelection of the GUI branch (see https://github.com/Wi5/odin-wi5-controller/tree/GUI).

You have to indicate the IP address of the Wi-5 controller in the file `RequestHandler.java` https://github.com/Wi5/odin-wi5-gui-client/blob/master/src/httpRequestHandler/RequestHandler.java

You can use e.g. Eclipse for running the client.

These are the steps:

- Start the Wi-5 controller with SmartApSelection. Your console MUST support X windows.
- In the Wi-5 controller, you will see a window telling you to start the Agents and click OK.
- Start all the agents and click Ok.
- In the Wi-5 controller, you will see another window telling you to click once the clients have started.