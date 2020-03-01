package utils

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object IOUtils {

  /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
   *                                                                                 *
   *                              Read Text Files                                    *
   *                                                                                 *
  \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

  /**
    * Takes a Path and a boolean value, indicating if the path is a resource path.
    * If this is the case, returns a new path containing the resource path root, followed by the given path.
    * If this is not the case (isAResource == false) the original path is just returned.
    *
    * Hint: Use getClass.getClassLoader.getResource to obtain a resource url, whose path can be extracted
    *
    * https://docs.oracle.com/javase/7/docs/api/java/lang/Class.html#getResource(java.lang.String)
    * https://docs.oracle.com/javase/7/docs/api/java/net/URL.html#getPath()
    *
    * @example getPath("Test.txt",false) => "Test.txt"
    * @example getPath("Test.txt",true) => file:target/scala-2.12/test-classes/"Test.txt"
    */
  private def getPath(path: String, isAResource: Boolean): String = {
    if (isAResource) getClass.getClassLoader.getResource(path).toString()
    else path
  }

  /**
    * Reads a text file into a Spark RDD.
    * Uses [[IOUtils.getPath()]] to obtain the actual path of the text file.
    *
    * Hint:
    *
    * Checkout how a reference to the SparkContext was obtained in
    * the introduction Sections [[introduction.Section0.main()]]
    *
    * Checkout this section of the Spark RDD tutorial
    * https://spark.apache.org/docs/latest/rdd-programming-guide.html#external-datasets
    */
  def RDDFromFile(path: String, isAResource: Boolean = true): RDD[String] = {
    val sc = SparkContext.getOrCreate()
    sc.textFile(getPath(path, isAResource))
  }

  /**
    * Reads a json file into a Spark RDD.
    */
  def RDDFromJsonFile[T](path: String, isAResource: Boolean = true)(implicit m: Manifest[T]): RDD[T] = {
    RDDFromFile(path, isAResource).map(JsonUtils.fromJson[T])
  }
}