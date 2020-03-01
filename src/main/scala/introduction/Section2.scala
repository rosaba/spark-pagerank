package introduction

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

/**
  * In this section you will practice some basic operations on "untyped" RDDs, that is RDD[Row].
  */
object Section2 {
  def main(args: Array[String]): Unit = {

    Section0.initSparkContext(Section0.getClass.getName)

    //obtain a reference to the initialized spark context
    val sc = SparkContext.getOrCreate()

    //create the data
    val types = List("a", "b", "c", "d")
    val divisors = List(2, 3, 5, 7)
    val data = for {(t, d) <- types.zip(divisors); x <- 1 to 100000; if x % d == 0} yield (t, x)
    val rdd = sc.parallelize(data).map(x => Row(x._1, x._2)).cache()

    /**
      * In the previous section you had the privilege of knowing the type of the rdd.
      * This is however not always the case.
      *
      * We will pretend, that you have some kind of complex data structure, which is represented
      * by the default RDD, RDD[Row]
      *
      * You will have to solve some of the tasks from the previous chapter using an RDD[Row] this time
      */


    /**
      * Count all entries of type "a"
      *
      * This time you will have to access the fields in another way
      * Checkout the "native primitive access" and "generic access" from the example in the docs
      * https://spark.apache.org/docs/latest/api/java/org/apache/spark/sql/Row.html
      *
      * and use one of them to complete the task
      */

    /*If you are unsure how the data looks like, you could print the first entry and check it out*/
    //println(rdd.take(1).head)

    val entriesACount = rdd.filter(x => x.getString(0).equals("a")).count()
    println(s"c(A) = $entriesACount")

    /**
      * Sum all entries of each type.
      *
      * Hint: this time use map first, to obtain the right data,
      *
      * Make sure that all results look plausible (pay attention to the datatypes)
      */
    //val entriesToSums= rdd.map(x => (x.getString(0), BigInt(x.getInt(1)))).reduceByKey(_+_).collect
    val entriesToSums= rdd.map(x => x.getString(0) -> BigInt(x.getInt(1))).reduceByKey(_+_).collect

    println(s"Entries to Sums: ${entriesToSums.toList}")

    /**
      * Convert the row rdd back to a (string,int) tuple rdd using the map operation
      */
    val rddTuple: RDD[(String, Int)] = rdd.map(x => x.getString(0) -> x.getInt(1))
    println(s"Before: RDD[${rdd.take(1).getClass.getSimpleName.replace("[]","")}]" +
      s" After: RDD[${rddTuple.take(1).getClass.getSimpleName.replace("[]","")}]")


    // For more details check the Spark RDD tutorial
    // https://spark.apache.org/docs/latest/rdd-programming-guide.html
  }

}