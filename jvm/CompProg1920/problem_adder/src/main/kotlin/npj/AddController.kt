package npj

import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
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
    }

    @FXML
    private fun onNewProblem() {
        var path = Paths.get(AddController::class.java.protectionDomain.codeSource.location.toURI()).toAbsolutePath()
        path = if(!path.fileName.toString().endsWith("jar")) {
            path.parent.parent.parent.parent.parent.parent.parent
        } else {
            path.parent
        }
        val jPath = Paths.get("jvm", "CompProg1920", "src", "main", "kotlin", "npj", "JVMProblemWrapper.kt")
        val jFullPath = path.resolve(jPath)
        val lines = Files.readAllLines(jFullPath).map(String::trim)
        var insertPoint = lines.indexOf(lines.first { it.startsWith("class CSAcademy") })
        val nextParts = lines.drop(insertPoint)
        insertPoint += 1 + nextParts.indexOf(nextParts.first { it.startsWith("companion") })
        val cObjParts = nextParts.drop(1 + nextParts.indexOf(nextParts.first { it.startsWith("companion") }))
        val parts = cObjParts.dropLast(cObjParts.size - cObjParts.indexOf(cObjParts.first { it.startsWith("}") })).filter { it.startsWith("val") }
        parts.forEach(::println)

        //TODO: Popup
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
        problemTable.items = FXCollections.observableArrayList(problemTable.items)
    }
}