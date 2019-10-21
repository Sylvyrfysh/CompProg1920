package npj.problemadder

import javafx.fxml.FXML
import javafx.scene.control.TextField

class PopupController {
    private lateinit var onContinue: (String?, String?) -> Unit
    fun setContFunction(cf: (String?, String?) -> Unit) {
        onContinue = cf
    }

    @FXML
    private lateinit var nameTextField: TextField
    @FXML
    private lateinit var urlTextField: TextField

    @FXML
    private fun onCancel() {
        onContinue(null, null)
    }

    @FXML
    private fun onOK() {
        onContinue(nameTextField.text, urlTextField.text)
    }
}