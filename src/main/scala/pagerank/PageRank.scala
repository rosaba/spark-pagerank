package pagerank

import org.apache.spark.rdd.RDD
import pagerank.models.Page

import scala.annotation.tailrec

object PageRank {

  /**
    * Computes the pageRank based on the given links.
    *
    * @param links the links
    * @param t     teleportation
    * @param delta minimum value for difference
    * @return pageRanks
    **/
  def computePageRank(links: RDD[(String, Set[String])], t: Double = 0.15, delta: Double = 0.01): RDD[(String, Double)] = {
    val n = links.count()
    val tNorm = t / n
    val ranks = links.mapValues(_ => 1.0 / n)

    @tailrec
    def inner(ranks: RDD[(String, Double)], links: RDD[(String, Set[String])], tNorm: Double, t: Double, delta: Double)
    : RDD[(String, Double)] = {

      //compute the contributions
      val contributions = computeContributions(ranks, links)

      //combine same keys and apply teleportation and damping factors after
      val newRanks = computeNewRanksFromContributions(contributions, tNorm, t)

      //print(newRanks.collect().toList)

      val diff = computeDifference(ranks, newRanks)
      //done, difference is small enough
      if (diff < delta)
        newRanks
      else
        inner(newRanks, links, tNorm, t, delta)
    }

    inner(ranks, links, tNorm, t, delta)
  }

  /**
    * Computes the contributions from the given ranks and links.
    *
    * See the tests and the description in the assignment sheet for more information.
    *
    *
    * HINTs:
    * join and flatMap might be useful
    *
    * - What happens if a page is never linked to? ex. {A->B, B->B}
    *   make sure that (A,0) is also in the result, so A doesn't get lost
    *
    * - also pay attention that A->{} should be treated as A->{A} and contribute (A,1)
    *
    */
  def computeContributions(ranks: RDD[(String, Double)], links: RDD[(String, Set[String])]): RDD[(String, Double)] = {

    links.join(ranks)
         .flatMap {

           case (url, (links, rank)) if links.isEmpty => List((url, rank))

           case (url, (links, rank)) if !links.contains(url) => links
             .map(page => (page, rank / links.size)).toList :+ (url, 0.0)

           case (url, (links, rank)) => links
             .map(page => (page, rank / links.size))
         }


  }

  /**
    *
    * Computes the new ranks from the contributions
    * The difference is computed the following way in pseudocode:
    *
    * foreach key:
    * - sum its values
    * multiply the values obtained in the previous step by (1-t) and add tNorm
    *
    **/
  def computeNewRanksFromContributions(contributions: RDD[(String, Double)], tNorm: Double, t: Double): RDD[(String, Double)] = {
    contributions.reduceByKey((x, y) => x + y).mapValues(v => tNorm + (1-t) * v)
  }

  /**
    *
    * Computes the difference between the old and new ranks.
    * The difference is computed the following way in pseudocode:
    *
    * foreach key:
    * - obtain the absolute value/modulus of its value from ranks subtracted its value in newRanks
    * sum the values
    *
    **/
  def computeDifference(ranks: RDD[(String, Double)], newRanks: RDD[(String, Double)]): Double = {
    ranks.join(newRanks).values.map(x => (x._1 - x._2).abs).sum()
  }

  /**
    * Extracts all links from the given page RDD. This is a 3 step process:
    *
    * 1. Project the pages in the form of Title -> Set(links.titles)
    * 2. Project all other links in the form link.title -> Set()
    * 3. Merge the 2 using the rdd union operation followed by a reduceByKey
    *
    * This results in all pages, who have links to be a pair of pageTitle -> Set (linkTitle, linkTitle..)
    * and all pages, who don't have any links in the form of pageTitle -> Set()
    *
    * For some examples see the test cases
    */
  def extractLinksFromPages(pages: RDD[Page]): RDD[(String, Set[String])] = {

   val p_with = pages.map(p => (p.title, p.links.flatMap(l => Set(l.title)).toSet))
    val p_without = pages.flatMap(p => p.links.map(l => (l.title, Set.empty[String])))
    p_with.union(p_without).reduceByKey(_++_) // oder: reduceByKey((x, y) => x), denn y ist null/leer

  }

}