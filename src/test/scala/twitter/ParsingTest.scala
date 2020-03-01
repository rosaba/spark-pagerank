package twitter

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import testing.FunSuiteWithSparkContext
import utils.IOUtils

@RunWith(classOf[JUnitRunner])
class ParsingTest extends FunSuiteWithSparkContext {

  test("Date Parsing Successful") {
    val date = "Sun Sep 21 15:05:39 +0000 2014"
    val TDate = TwitterUtilities.getTwitterDate(date)
    assert(TDate.toString === "2014-09-21T15:05:39Z")
  }

  test("Parsing Test") {
    val tweet = "{\"created_at\":\"Sun Sep 21 15:05:39 +0000 2014\",\"id\":513705624245653505,\"id_str\":\"513705624245653505\",\"text\":\"This is a tweet\",\"truncated\":false,\"user\":{\"id\":2533019970,\"name\":\"donald\"},\"lang\":\"en\"}"

    val firstTweet = TwitterUtilities.parse(tweet).head
    assert(firstTweet.date.toString === "2014-09-21T15:05:39Z")
    assert(firstTweet.userName === "donald")
    assert(firstTweet.text === "This is a tweet")
    assert(firstTweet.lang === "en")
  }

  test("Parse All") {
    var twitterData = IOUtils.RDDFromFile("tweets-big.txt")
    val onlyTweets = twitterData.flatMap(TwitterUtilities.parse).cache

    assert(twitterData.count === 21326)
    assert(onlyTweets.count === 17570)
  }
}
