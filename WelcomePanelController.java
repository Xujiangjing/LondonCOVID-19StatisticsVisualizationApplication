import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The WelcomePanelController class is responsible for handling user
 * interactions within the Welcome Panel of the application. This class manages
 * the initialization and dynamic behavior of UI components defined in the
 * corresponding FXML file. It serves as the bridge between the application's
 * logic and its graphical interface, ensuring that the welcome panel displays
 * appropriate introductory texts and tips to guide the user at the start of the
 * application.
 *
 *
 * @author Jiangjing Xu & Hongyuan Zhao & Lei Ding
 * @version 24.03.2024
 */
public class WelcomePanelController {
    @FXML
    private BorderPane welcomePanel;// The root pane of the welcome panel.
    @FXML
    private Text welcomeText1, welcomeText2, welcomeText3, welcomeText4, welcomeTip1, welcomeTip1_1, welcomeTip2,
            welcomeTip2_2, welcomeTip3, welcomeTip3_1, welcomeTip4, welcomeTip4_1, welcomeTip5, welcomeTip5_1;
    @FXML
    private VBox welcomeTextVBox;

    /**
     * Constructor for objects of class WelcomePanelController
     */
    public WelcomePanelController() {

    }

    /**
     * Returns the root BorderPane of the welcome panel. This can be used to
     * integrate the welcome panel into other scenes or to perform additional
     * manipulations on the panel.
     *
     * @return The root BorderPane of the welcome panel.
     */
    public BorderPane getView() {
        return welcomePanel;
    }

}
