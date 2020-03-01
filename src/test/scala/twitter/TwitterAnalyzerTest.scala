package twitter

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import testing.FunSuiteWithSparkContext
import utils.IOUtils

@RunWith(classOf[JUnitRunner])
class TwitterAnalyzerTest extends FunSuiteWithSparkContext {

  var TWA: TwitterAnalyzer = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    var twitterData = IOUtils.RDDFromFile("tweets-big.txt")
    var onlyTweets = twitterData.flatMap(TwitterUtilities.parse).cache
    println(onlyTweets.count)
    TWA = new TwitterAnalyzer(onlyTweets)
  }

  test("Number of German Tweets") {
    val nr = TWA.getGermanTweetsCount
    assert(nr === 90)
  }
  test("Number of Tweets Per Country") {

    val numberOfTweetsPerCountry = TWA.numberOfTweetsPerCountry
    val nTweetsPerCountryAsMap = numberOfTweetsPerCountry.toMap

    assert(numberOfTweetsPerCountry.length === 40)
    assert(nTweetsPerCountryAsMap("en") === 5150)
  }

  test("number of tweets per user") {

    val res = TWA.numberOfGermanTweetsPerUser
    assert(res.forall(x => x._2 == 5))
  }

  test("Texts of the German Tweets") {

    val data = TWA.getGermanTweetTexts
    //data.foreach(println)
    assert(data.contains("Oh wie ich dich liebe PAX\n#pax #ikea #ikeapaxsystem #paxcloset #michaelkors #longchamp #louisvuitton #rayban #lac"))
    assert(data.contains("Ein neuer Erfolg: Landwirt! Versuche doch auch dein Glück! http://t.co/fNkt54x3FG #android,#androidgames,#gameinsight"))
  }

  test("Extraction of Hashtags") {

    val tweet = "This is a Hashtag #super #mega and this is another #hashtag"
    val res = TwitterAnalyzer.getHashtags(tweet).sorted
    assert(res === List("#hashtag", "#mega", "#super"))
  }

  test("Top ten hashtags") {
    val ht = TWA.getTopTenEnglishHashtags
    ht.foreach(println)
    assert(ht.head._2 == 35 || ht.tail.head._2 == 25)
  }

}
