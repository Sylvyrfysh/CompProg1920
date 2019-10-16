package npj

import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.util.Callback
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.streams.toList

data class TestCase(val name: String, val problem: Problem) {
    val displayName = "${problem.name}:$name"
    private val inPath = problem.path.resolve(Paths.get("$name.in"))
    private val ansPath = problem.path.resolve(Paths.get("$name.ans"))

    var inText: String
        get() {
            return Files.readString(inPath)
        }
        set(value) {
            Files.writeString(inPath, value)
        }

    var ansText: String
        get() {
            return Files.readString(ansPath)
        }
        set(value) {
            Files.writeString(ansPath, value, StandardOpenOption.CREATE)
        }

    fun delete() {
        Files.deleteIfExists(inPath)
        Files.deleteIfExists(ansPath)
    }
}

data class Problem(val path: Path) {
    var url: String? = null
    val name = path.fileName.toString()
    val testCaseNames: List<String>
        get() {
            return Files.list(path).map { it.fileName.toString() }
                .filter { it.substring(it.indexOf('.') + 1) == "in" }
                .map { val p2 = it.lastIndexOf('.'); it.substring(0, p2) }
                .toList()
        }
    val numTestCases: Int
        get() {
            return testCaseNames.size
        }

    override fun equals(other: Any?): Boolean {
        if(other !is Problem) {
            return false
        }
        return name == other.name
    }

    companion object {
        fun createProblem(path: Path, url: String): Problem {
            Files.createDirectories(path)
            return Problem(path).apply { this.url = url }
        }
    }
}

class AddController {
    @FXML
    private lateinit var problemTable: TableView<Problem>
    @FXML
    private lateinit var problemTableNameColumn: TableColumn<Problem, String>
    @FXML
    private lateinit var problemTableNumTestCasesColumn: TableColumn<Problem, Int>

    @FXML
    private lateinit var loadButton: Button
    @FXML
    private lateinit var newCaseButton: Button
    @FXML
    private lateinit var removeCaseButton: Button
    @FXML
    private lateinit var editCaseButton: Button

    @FXML
    private lateinit var testCasesTable: TableView<TestCase>
    @FXML
    private lateinit var testCasesTableCaseName: TableColumn<TestCase, String>

    @FXML
    private lateinit var caseNameTextField: TextField
    @FXML
    private lateinit var inputTextArea: TextArea
    @FXML
    private lateinit var answerTextArea: TextArea
    @FXML
    private lateinit var onSaveCaseButton: Button
    @FXML
    private lateinit var onDeleteCaseButton: Button

    private lateinit var rootPath: Path

    @FXML
    private fun initialize() {
        rootPath = if(System.getenv("IDE") == "true") {
            Paths.get("").toAbsolutePath().parent.parent.parent
        } else {
            Paths.get("")
        }.resolve(Paths.get("resources", "CSAcademy"))

        initializeProblemTable()
        initializeCaseTable()

    }

    private fun allowTestCaseEntering(allow: Boolean) {
        caseNameTextField.isDisable = !allow
        inputTextArea.isDisable = !allow
        answerTextArea.isDisable = !allow
    }

    private fun initializeCaseTable() {
        testCasesTableCaseName.cellValueFactory = Callback {
            ReadOnlyStringWrapper(it.value.displayName)
        }

        testCasesTable.selectionModel.selectionMode = SelectionMode.SINGLE
        testCasesTable.selectionModel.selectedItemProperty().addListener { _, _, _ ->
            editCaseButton.isDisable = testCasesTable.selectionModel.selectedItem == null
            removeCaseButton.isDisable = testCasesTable.selectionModel.selectedItem == null
        }
    }

    private fun initializeProblemTable() {
        problemTableNameColumn.cellValueFactory = Callback<TableColumn.CellDataFeatures<Problem, String>, ObservableValue<String>>  {
            ReadOnlyStringWrapper(it.value.name)
        }

        problemTableNumTestCasesColumn.cellValueFactory = Callback<TableColumn.CellDataFeatures<Problem, Int>, ObservableValue<Int>>  {
            SimpleIntegerProperty(it.value.numTestCases).asObject()
        }

        problemTable.items.clear()
        problemTable.items.addAll(Files.list(rootPath).filter { Files.isDirectory(it) }.map{ Problem(it) }.toList())

        problemTable.selectionModel.selectionMode = SelectionMode.SINGLE
        problemTable.selectionModel.selectedItemProperty().addListener { _, _, _ ->
            loadButton.isDisable = problemTable.selectionModel.selectedItem == null
        }
    }

    private fun getSelectedProblem(): Problem? {
        return problemTable.selectionModel.selectedItem
    }

    private lateinit var loadedProblem: Problem
    @FXML
    private fun onLoadProblem() {
        newCaseButton.isDisable = false
        onSaveCaseButton.isDisable = true
        onDeleteCaseButton.isDisable = true
        clearTestCaseArea()
        testCasesTable.items.clear()

        loadedProblem = getSelectedProblem()!!
        val tcs = loadedProblem.testCaseNames.map { TestCase(it, loadedProblem) }

        testCasesTable.items.addAll(tcs)
        allowTestCaseEntering(false)
    }

    @FXML
    private fun onNewProblem() {
        var path = Paths.get(AddController::class.java.protectionDomain.codeSource.location.toURI()).toAbsolutePath()
        path = if(!path.fileName.toString().endsWith("jar")) {
            path.parent.parent.parent.parent.parent.parent.parent
        } else {
            path.parent
        }
        val fxmlLoader = FXMLLoader()
        fxmlLoader.location = AddController::class.java.classLoader.getResource("Popup.fxml")
        val stage = Stage()
        val root = fxmlLoader.load<VBox>()
        val controller = fxmlLoader.getController<PopupController>()
        controller.setContFunction { name, url ->
            stage.close()
            if(name == null) {
                return@setContFunction
            }
            if(name in Files.list(rootPath).filter { Files.isDirectory(it) }.map{ it.fileName.toString() }.toList()) {
                val alert = Alert(Alert.AlertType.ERROR)
                alert.contentText = "A problem with that name already exists!"
                alert.showAndWait()
                return@setContFunction
            }
            val confirm = Alert(Alert.AlertType.CONFIRMATION)
            confirm.contentText = "Add problem with name $name?"
            val res = confirm.showAndWait()
            if(res.isEmpty) {
                return@setContFunction
            }
            if(res.get() != ButtonType.OK) {
                return@setContFunction
            }
            val jPath = Paths.get("jvm", "CompProg1920", "src", "main", "kotlin", "npj", "JVMProblemWrapper.kt")
            val jFullPath = path.resolve(jPath)
            val javaLines = Files.readAllLines(jFullPath).toMutableList()
            var javaInsertPoint = javaLines.indexOf(javaLines.first { it.trimStart().startsWith("class CSAcademy") })
            val javaNextParts = javaLines.drop(javaInsertPoint)
            javaInsertPoint += 1 + javaNextParts.indexOf(javaNextParts.first { it.trimStart().startsWith("companion") })
            javaLines.add(javaInsertPoint, "                @JvmField")
            javaLines.add(javaInsertPoint + 1, "                val $name = CSAcademy(\"$name\", \"$url\")")
            Files.write(jFullPath, javaLines)

            val pPath = Paths.get("python", "CompProg1920", "npj", "ProblemSets.py")
            val pFullPath = path.resolve(pPath)
            val pyLines = Files.readAllLines(pFullPath).toMutableList()
            val pyInsertPoint = 1 + pyLines.indexOf(pyLines.first { it.trimStart().startsWith("class CSAcademy:") })
            pyLines.add(pyInsertPoint, "        ${name.snakeCase()} = CSAcademy('$name',")
            pyLines.add(pyInsertPoint + 1, "${" ".repeat(name.snakeCase().length)}                     '$url')")
            Files.write(pFullPath, pyLines)

            val cHPath = Paths.get("c++", "src", "npj", "Problem.h")
            val cHFullPath = path.resolve(cHPath)
            val cCPath = Paths.get("c++", "src", "npj", "Problem.cpp")
            val cCFullPath = path.resolve(cCPath)

            val chLines = Files.readAllLines(cHFullPath).toMutableList()
            val cHInsertPoint = 2 + chLines.indexOf(chLines.first { it.trimStart().startsWith("class CSAcademy {") })
            chLines.add(cHInsertPoint, "            static const Problem *$name;")
            Files.write(cHFullPath, chLines)

            val ccLines = Files.readAllLines(cCFullPath).toMutableList()
            val cCInsertPoint =
                1 + ccLines.indexOf(ccLines.first { it.trimStart().startsWith("CREATE_CSACADEMY(OddDivisors)") })
            ccLines.add(cCInsertPoint, "    CREATE_CSACADEMY($name)")
            Files.write(cCFullPath, ccLines)

            Files.createDirectory(rootPath.resolve(name))
            initializeProblemTable()
            problemTable.selectionModel.select(Problem(rootPath.resolve(name)))
            onLoadProblem()
            allowTestCaseEntering(false)
        }
        stage.scene = Scene(root, root.prefWidth, root.prefHeight)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.show()
    }

    @FXML
    private fun onNewCase() {
        clearTestCaseArea()
        val cNames = getSelectedProblem()!!.testCaseNames
        var pCnt = 1
        while (cNames.contains(pCnt.toString())) {
            ++pCnt
        }
        loadTestCaseArea(pCnt.toString())
    }

    @FXML
    private fun onRemoveCase() {
        removeTestCase(getSelectedTestCase()!!)
    }

    @FXML
    private fun onEditCase() {
        loadTestCaseArea(getSelectedTestCase()!!)
    }

    private fun getSelectedTestCase(): TestCase? {
        return testCasesTable.selectionModel.selectedItem
    }

    private var currentEditingTestCase: TestCase? = null

    private fun clearTestCaseArea() {
        currentEditingTestCase = null
        loadTestCaseArea("")
        onSaveCaseButton.isDisable = true
        onDeleteCaseButton.isDisable = true
        allowTestCaseEntering(false)
    }

    private fun loadTestCaseArea(tc: TestCase) {
        loadTestCaseArea(tc.name, tc.inText, tc.ansText)
        currentEditingTestCase = tc
    }

    private fun loadTestCaseArea(problemID: String, inText: String = "", ansText: String = "") {
        currentEditingTestCase = null
        caseNameTextField.text = problemID
        inputTextArea.text = inText
        answerTextArea.text = ansText

        onSaveCaseButton.isDisable = false
        onDeleteCaseButton.isDisable = false
        allowTestCaseEntering(true)
    }

    @FXML
    private fun onSaveCase() {
        if (caseNameTextField.text.isBlank()) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.contentText = "Must have a name for the test case!"
            alert.showAndWait()
            return
        }
        if(currentEditingTestCase?.name == caseNameTextField.text) { //didn't change the name
            currentEditingTestCase!!.inText = inputTextArea.text
            currentEditingTestCase!!.ansText = answerTextArea.text
            return
        }
        if(loadedProblem.testCaseNames.contains(caseNameTextField.text)) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.contentText = "A Test case with this name already exists!"
            alert.showAndWait()
            return
        }
        if(currentEditingTestCase != null) {
            val confirm = Alert(Alert.AlertType.CONFIRMATION)
            confirm.contentText = "You modified the name! Click OK to save a copy, or Change Name to delete the old test case!"
            confirm.buttonTypes.clear()
            confirm.buttonTypes.addAll(
                ButtonType.CANCEL,
                ButtonType("Change Name"),
                ButtonType.OK
            )
            val res = confirm.showAndWait()
            if(res.isEmpty) {
                return
            }
            when {
                res.get() == ButtonType.CANCEL -> return
                res.get() != ButtonType.OK -> {
                    currentEditingTestCase!!.delete()
                    currentEditingTestCase?.let { removeTestCase(it) }
                    currentEditingTestCase = null
                }
            }
        }
        currentEditingTestCase = currentEditingTestCase ?: TestCase(caseNameTextField.text, loadedProblem)
        currentEditingTestCase!!.inText = inputTextArea.text
        currentEditingTestCase!!.ansText = answerTextArea.text

        if(!testCasesTable.items.contains(currentEditingTestCase)) {
            testCasesTable.items.add(currentEditingTestCase)
        }
    }

    @FXML
    private fun onDeleteCase() {
        caseNameTextField.text = ""
        inputTextArea.text = ""
        answerTextArea.text = ""

        onSaveCaseButton.isDisable = true
        onDeleteCaseButton.isDisable = true

        currentEditingTestCase?.let { removeTestCase(it) }
        allowTestCaseEntering(false)
    }

    private var showAlert = true
    private fun removeTestCase(toRemove: TestCase) {
        if(showAlert) {
            val checkDelete = Alert(Alert.AlertType.CONFIRMATION)
            checkDelete.contentText = "Delete this test case?"
            checkDelete.buttonTypes.clear()
            checkDelete.buttonTypes.addAll(
                ButtonType.CANCEL,
                ButtonType("Confirm, Don't Ask Again"),
                ButtonType.OK
            )
            val res = checkDelete.showAndWait()
            if (res.isEmpty) {
                return
            }
            if (res.get() == ButtonType.CANCEL) {
                return
            } else if (res.get() != ButtonType.OK) {
                showAlert = false
            }
        }
        if(toRemove == currentEditingTestCase) {
            currentEditingTestCase = null
        }
        toRemove.delete()
        testCasesTable.items.remove(toRemove)
        testCasesTable.items = FXCollections.observableArrayList(testCasesTable.items)
        allowTestCaseEntering(false)
    }
}

private fun String.snakeCase(): String {
    val ret = StringBuilder()

    var lastLower = false

    for((idx, c) in this.withIndex()) {
        if(c.isUpperCase()) {
            if(lastLower) {
                ret.append('_')
            } else {
                if (idx + 1 < this.length && idx != 0) {
                    if (this[idx + 1].isLowerCase()) {
                        ret.append('_')
                    }
                }
            }
            ret.append(c.toLowerCase())
            lastLower = false
        } else {
            ret.append(c)
            lastLower = true
        }
    }

    return ret.toString()
}
