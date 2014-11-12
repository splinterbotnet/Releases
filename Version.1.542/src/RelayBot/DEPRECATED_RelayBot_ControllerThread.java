/**
 * 
 * Only one RelayBot_ControllerThread is spawned per connection from each Controller.
 * This thread listens to all data received from the Controller, it will then throw this line into each connected implant in the connected implant array lists's determine command to perform an action
 * 
 * this receive a new execution command from the controller, this thread performs the following
 * 
 * while(controllerIsConnected && (line = brInController.readLine)!=null))
 * {
 * 		for(each connected implants in the arraylist)
 * 		{
 * 			arraylist_RelayBot_ImplantThreads[i].sendCommandToImplant(line, arraylist of printWriters)
 * 		}
 * 
 * }//end while
 * 
 * such that each RelayBot_Implant will get the line to execute, and now send this line to the real implant connected
 * connected implant executes the command, and then provides its response to each printWriter instance in its arraylist of printWriters to respond to
 * 
 * sendResponseToController(line, arraylist of printwriters)
 * 
 * @author Solomon Sonya
 */


package RelayBot;

public class DEPRECATED_RelayBot_ControllerThread {

}
