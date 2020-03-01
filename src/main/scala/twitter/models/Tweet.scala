package twitter.models

import java.time.OffsetDateTime

/**
  * @param date     date of the tweet
  * @param userName name of the user that created the tweet
  * @param text     text of the tweet
  * @param lang     language of the tweet
  */
case class Tweet(date: OffsetDateTime, userName: String, text: String, lang: String)