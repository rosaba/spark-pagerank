package twitter

import org.apache.spark.rdd.RDD
import twitter.models.Tweet

/**
  * @param tData twitter data rdd
  */
class TwitterAnalyzer(tData: RDD[Tweet]) {

  /*
   * Write a function that counts the number of tweets using the german language
   */
  def getGermanTweetsCount: Long = {
   tData.filter(tw => tw.lang.equals("de")).count()
  }

  /*
   * Extracts the texts of all german tweets (all tweets with the "de" locale)
   */
  def getGermanTweetTexts: Array[String] = {
    tData.filter(tw => tw.lang.equals("de")).map(x => x.text).collect()
  }

  /**
    * Counts the number of unique german users (all users that tweeted using the "de" locale)
    */
  def numberOfGermanTweetsPerUser: Array[(String, Int)] = {
    tData.filter(tw => tw.lang.equals("de")).map(x => (x.userName, 1)).reduceByKey(_+_).collect
  }

  /**
    * Counts the number of tweets per country
    */
  def numberOfTweetsPerCountry: Array[(String, Int)] = {
    tData.map(x => (x.lang, 1)).reduceByKey(_+_).collect()
  }

  /**
    * Extracts the top 10 hashtags in english tweets (tweets with the "en" locale).
    *
    * Hints:
    * Use the [[TwitterAnalyzer.getHashtags()]] function to obtain the hashtags from the text of a tweet.
    * Checkout RDD.takeOrdered
    * https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.RDD@takeOrdered(num:Int)(implicitord:Ordering[T]):Array[T]
    * or RDD.top
    * https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.RDD@top(num:Int)(implicitord:Ordering[T]):Array[T]
    * and Ordering.by
    */
  def getTopTenEnglishHashtags: List[(String, Int)] = {
    tData
      .filter(_.lang.equals("en"))
      .map(_.text)
      .map(TwitterAnalyzer.getHashtags)
      .flatMap(x => x)
      .map(x => (x, 1))
      .reduceByKey(_+_)
      .top(10)(Ordering[Int].on(x => x._2))
      .toList
  }
}

object TwitterAnalyzer {

  def getHashtags(text: String): List[String] = {
    if (text.isEmpty || text.length == 1) List()
    else if (text.head == '#') {
      val tag = text.takeWhile(x => x != ' ')
      val rest = text.drop(tag.length)
      tag :: getHashtags(rest)
    }
    else getHashtags(text.tail)
  }
}