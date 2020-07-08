import com.intellij.psi.formatter.FormatterTestCase
import org.jetbrains.research.smtlib.SmtLibFileType


class FormatterTest : FormatterTestCase() {
    override fun getTestDataPath(): String = "src/test/testData"
    override fun getBasePath(): String = "formatter"
    override fun getFileExtension(): String = SmtLibFileType.FILE_EXTENSION

    fun testJustHeader() = doTest()
    fun testExpression() = doTest()
    fun testSample1() = doTest()
    fun testSample2() = doTest()
}
