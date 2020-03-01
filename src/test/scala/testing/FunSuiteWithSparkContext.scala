package testing

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

trait FunSuiteWithSparkContext extends FunSuite with BeforeAndAfterAll {
  //crank down spark logging severity, so INFO is not shown
  Logger.getLogger("org").setLevel(Level.WARN)

  val sc: SparkContext = SparkContext.getOrCreate(new SparkConf().setAppName("test").setMaster("local[*]"))

  override protected def afterAll(): Unit = {

    if (sc != null) {
      sc.stop
      println("Spark stopped......")
    }
    else println("ERROR: Cannot stop spark: reference lost.")
  }
}