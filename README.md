JavaFX-Tabs
===========

This Tutorial shows how to handle Tabs. In each tab there is a fxml included. Each included fxml gets a fx:id, the 
maincontroller gets access to the subcontroller. The rule is that if your fx:include has fx:id="x", then the controller 
from the corresponding FXML file can be injected into a variable with name xController.

Furthermore this Tutorial shows how u can start a method of a subcontroller from another subcontroller. Therefore a 
public static FXMLLoader has to be created in the Main.java, this FXMLLoader loads the Main.fxml as usual.
From the loader u get the MainController which grants you access to the other subcontrollers.

If you fetch this Tutorial also have a look at the FXML-files.

Have Fun
