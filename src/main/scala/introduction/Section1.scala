package introduction

import org.apache.spark.SparkContext

/**
  * In this section you will practice some basic operations on RDDs.
  *
  * NOTE: complete Section0 before starting this.
  *
  */
object Section1 {
  def main(args: Array[String]): Unit = {

    Section0.initSparkContext(Section0.getClass.getName)

    //obtain a reference to the initialized spark context
    val sc = SparkContext.getOrCreate()

    //create the data
    val types = List("a", "b", "c", "d")
    val divisors = List(2, 3, 5, 7)
    val data = for {(t, d) <- types.zip(divisors); x <- 1 to 100000; if x % d == 0} yield (t, x)
    val rdd = sc.parallelize(data).cache()

    /**
      * Count all entries of type "a"
      *
      * Hint: use filter and count
      */
    val entriesACount = rdd.filter(x => x._1.equals("a")).count()
    println(s"c(A) = $entriesACount")

    /**
      * Sum all entries of type "c"
      *
      * Hint:use filter, map and sum
      */
    val entriesBsum = rdd.filter(x => x._1.equals("b")).map(x => x._2).sum()
    println(s"\u2211(B) = $entriesBsum")

    /**
      * Sum all entries of each type.
      *
      * Hint: use reduceByKey followed by collect
      *
      * - Do all results look plausible?
      *
      * - What happens if you omit collect?
      */
    val entriesToSums:Array[(String,Int)] = rdd.reduceByKey(_ + _).collect()

    println(s"Entries to Sums: ${entriesToSums.toList}")

    /**
      * Count all entries of each type
      * Use map, reduceByKey and collect
      *
      * Hint:In the map operation, map the values to identity
      */
    val entriesToCounts:Array[(String,Int)] = rdd.map(x => (x._1, 1)).reduceByKey(_+_).collect()
    println(s"Entries to Counts: ${entriesToCounts.toList}")

    /**
      * Count all entries of each type
      * Use mapValues, reduceByKey and collect
      *
      * Hint:In the mapValues operation, map to identity
      */
    val entriesToCounts2:Array[(String,Int)] = rdd.mapValues(x => 1).reduceByKey(_ + _).collect()
    println(s"Entries to Counts MapValues: ${entriesToCounts2.toList}")


    /**
      * Count all entries of each type
      * Use the built in countByKey
      *
      */
    val entriesToCounts3 = rdd.countByKey()
    println(s"Entries to Counts CountByKey: ${entriesToCounts3}")

    //Checkout the internal implementation of countByKey for PairRDDs
    //https://github.com/apache/spark/blob/9b1f6c8bab5401258c653d4e2efb50e97c6d282f/core/src/main/scala/org/apache/spark/rdd/PairRDDFunctions.scala#L370

    //Checkout what pairRDDs are
    //https://spark.apache.org/docs/latest/rdd-programming-guide.html#working-with-key-value-pairs

    Section0.tearDownSparkContext()
  }
}