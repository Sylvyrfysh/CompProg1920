package npj

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage

class Main: Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage!!.scene = FXMLUtils.getScene("adder", "Add")
        primaryStage.title = "Problem Adder"
        primaryStage.show()
    }

    fun launchInternal(args: Array<String>) {
        launch(*args)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java, *args)
        }
    }
}