package npj

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage

class Main: Application() {
    override fun start(primaryStage: Stage?) {
        val loader = FXMLLoader()
        loader.location = Main::class.java.classLoader.getResource("Add.fxml")
        val root = loader.load<VBox>()

        primaryStage!!.scene = Scene(root, root.prefWidth, root.prefHeight)
        primaryStage.title = "Problem Adder"
        primaryStage.show()
    }

    fun launchInternal(args: Array<String>) {
        launch(*args)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main().launchInternal(args)
        }
    }
}