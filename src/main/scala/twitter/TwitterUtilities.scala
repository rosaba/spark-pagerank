package twitter

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

import twitter.models.Tweet
import utils.JsonUtils

object TwitterUtilities {
  val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss X uuuu", Locale.ENGLISH)

  /*
  {
  "created_at": "Thu Apr 06 15:24:15 +0000 2017",
  "id_str": "850006245121695744",
  "text": "1\/ Today we\u2019re sharing our vision for the future of the Twitter API platform!\nhttps:\/\/t.co\/XweGngmxlP",
  "user": {
    "id": 2244994945,
    "name": "Twitter Dev",
    "screen_name": "TwitterDev",
    "location": "Internet",
    "url": "https:\/\/dev.twitter.com\/",
    "description": "Your official source for Twitter Platform news, updates & events. Need technical help? Visit https:\/\/twittercommunity.com\/ \u2328\ufe0f #TapIntoTwitter"
  },
  "place": {   
  },
  "entities": {
    "hashtags": [      
    ],
    "urls": [
      {
        "url": "https:\/\/t.co\/XweGngmxlP",
        "unwound": {
          "url": "https:\/\/cards.twitter.com\/cards\/18ce53wgo4h\/3xo1c",
          "title": "Building the Future of the Twitter API Platform"
        }
      }
    ],
    "user_mentions": [
    ]
  },
  "lang":"en"
}
*/

  /**
    * Parses a given tweet in the Twitter Data JSON Format using [[JsonUtils.parseJson()]]
    * and extracts the date, username, text and language of the tweet into a [[Tweet]] object.
    * The [[TwitterUtilities.getTwitterDate()]] function is used to parse the date.
    * If the line is not a valid json string, None is returned instead.
    *
    * Hints:
    * Analyse the Twitter Data Format (an entry is pasted above for convenience).
    * The date corresponds to the "created_at" field, the userName to the "name" field
    * and the text and language to the "text" and "lang" fields of the json string.
    *get
    * Pay attention, that not all lines are actual tweets and don't comply to the above format.
    * (Checkout the tweets.txt File in the test resources)
    * Handle this by returning None
    *
    * If you are not too familiar with scala's Option class, checkout
    * https://www.scala-lang.org/api/2.12.x/scala/Option.html
    * and
    * https://www.tutorialspoint.com/scala/scala_options.htm
    *
    * If you are having trouble with casting checkout
    * https://alvinalexander.com/scala/how-to-cast-objects-class-instance-in-scala-asinstanceof
    */
  def parse(jsonString: String): Option[Tweet] = {

    val tweet = JsonUtils.parseJson(jsonString)
    tweet match {

      case Some(map: Map[String, Any]) =>
        if (map.contains("created_at") && map.contains("user") && map.contains("text") && map.contains("lang"))
          Option(Tweet(getTwitterDate(map("created_at").toString), map("user").asInstanceOf[Map[String, Any]]("name").toString, map("text").toString, map("lang").toString))
        else None

      case None => None
    }
  }

  def getTwitterDate(date: String): OffsetDateTime = {
    try {
      OffsetDateTime.parse(date, dtf)
    } catch {
      case e: Exception =>
        println(date)
        OffsetDateTime.now
    }
  }
}