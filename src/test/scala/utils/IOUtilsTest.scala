package utils

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import testing.FunSuiteWithSparkContext

@RunWith(classOf[JUnitRunner])
class IOUtilsTest extends FunSuiteWithSparkContext {
  test("RDDFromFile") {
    val result = IOUtils.RDDFromFile("testNotAResource.txt", isAResource = false)
    assert(result.count() == 4)
    assert(result.collect().last === "3 (your boat)")
  }

  test("RDDFromFile Resource") {
    val result = IOUtils.RDDFromFile("test.txt")
    assert(result.count() == 4)
    assert(result.collect().last === "3 (your boat)")
  }
}
