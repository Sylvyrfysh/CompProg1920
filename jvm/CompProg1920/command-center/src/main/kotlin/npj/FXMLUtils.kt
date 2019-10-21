package npj

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane

object FXMLUtils {
    fun getScene(vararg parts: String): Scene {
        val root = getFXML(*parts)

        return Scene(root, root.prefWidth, root.prefHeight)
    }

    fun getFXML(vararg parts: String): Pane {
        return getLoader(parts).load()
    }

    fun <T> getFXML(vararg parts: String): Pair<Pane, T> {
        val loader = getLoader(parts)
        val root = loader.load<Pane>()
        val controller = loader.getController<T>()

        return Pair(root, controller)
    }

    fun <T> getFXMLScene(vararg parts: String): Pair<Scene, T> {
        val loader = getLoader(parts)
        val root = loader.load<Pane>()
        val controller = loader.getController<T>()

        return Pair(Scene(root, root.prefWidth, root.prefHeight), controller)
    }

    private fun getLoader(parts: Array<out String>): FXMLLoader {
        val loader = FXMLLoader()
        loader.location = FXMLLoader::class.java.classLoader.getResource("fxml/" + parts.joinToString("/").run { if (endsWith(".fxml")) this else "$this.fxml" })

        return loader
    }
}