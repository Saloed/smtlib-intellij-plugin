import com.intellij.testFramework.ParsingTestCase
import org.jetbrains.research.smtlib.SmtLibFileType
import org.jetbrains.research.smtlib.parser.SmtLibParserDefinition

class ParserTest : ParsingTestCase("parser", SmtLibFileType.FILE_EXTENSION, SmtLibParserDefinition()) {
    override fun getTestDataPath(): String = "src/test/testData"
    override fun skipSpaces(): Boolean = false
    override fun includeRanges(): Boolean = true

    fun testComments() = doTest(true)
    fun testJustHeader() = doTest(true)
    fun testExpression() = doTest(true)
    fun testSample() = doTest(true)
}