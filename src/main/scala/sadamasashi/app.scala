package sadamasashi

import java.awt.Desktop
import java.net.{ URLEncoder, URI }
import com.google.api.services.youtube.model.SearchResult
import org.apache.lucene.search.spell.LevensteinDistance
import scala.language.dynamics
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context
import skinny.activeimplicits.StringImplicits
import skinny.nlp.SkinnyJapaneseAnalyzerFactory
import skinny.logging.LoggerProvider

object SadaMasashi extends Dynamic with StringImplicits {

  implicit lazy val japaneseAnalyzer = {
    val dictionary = readLines("dictionary.txt").mkString("\n")
    SkinnyJapaneseAnalyzerFactory.create(dictionary)
  }

  lazy val songTitles: Seq[String] = readLines("song_titles.txt").mkString.split(",")
  lazy val hiraganaSongTitles: Seq[String] = songTitles.map(_.hiragana)
  lazy val katakanaSongTitles: Seq[String] = songTitles.map(_.katakana)
  lazy val romajiSongTitles: Seq[String] = songTitles.map(_.romaji)

  lazy val songTitleCandidates: Seq[String] = {
    songTitles ++
      hiraganaSongTitles ++
      katakanaSongTitles ++
      romajiSongTitles
  }

  def findCorrectSongTitle(title: String): Option[String] = {
    songTitles.find(t => Set(t.hiragana, t.katakana, t.romaji).contains(title))
  }

  private[this] lazy val distance = new LevensteinDistance

  def suggestSongTitles(title: String): Seq[(String, Float)] = {
    val hiraganaTitle = title.hiragana
    (songTitles.map(t => (t, distance.getDistance(t, title))) ++
      hiraganaSongTitles.map(t => (t, distance.getDistance(t, hiraganaTitle))))
      .filter { case (_, similarity) => similarity >= 0.3F }
      .sortWith { case ((_, a), (_, b)) => a > b }
      .flatMap { case (t, s) => findCorrectSongTitle(t).map(t => (t, s)) }
      .take(5)
  }

  private[this] val r = new scala.util.Random()
  def randomSongTitle: String = songTitles.apply(r.nextInt(songTitles.size))

  // 曲名を指定
  def selectDynamic(title: String): Song = macro SongCompilerMacro.selectDynamic

  // さださんの全てを知り尽くすまでコンパイルは失敗し続けます
  def learnMore: SadaMasashi.type = macro SongCompilerMacro.learnMore
  def もっと知りたい: SadaMasashi.type = macro SongCompilerMacro.learnMore
  def もっと学びたい: SadaMasashi.type = macro SongCompilerMacro.learnMore
}

case class SadaVideo(key: String) {
  import scala.collection.JavaConverters._
  import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
  import com.google.api.client.http.apache.ApacheHttpTransport
  import com.google.api.client.json.jackson2.JacksonFactory
  import com.google.api.services.youtube.YouTube

  def find(title: String): Option[SearchResult] = {
    val youTube = new YouTube.Builder(new ApacheHttpTransport, new JacksonFactory, new GoogleCredential)
      .setApplicationName("sadamasashi-compiler").build()
    val youTubeSearch = youTube.search.list("id,snippet")
    youTubeSearch.setType("video")
    youTubeSearch.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
    youTubeSearch.setMaxResults(50L)
    val query = s"さだまさし ${title}"
    youTubeSearch.setQ(query)
    youTubeSearch.setKey(key)
    val youTubeResponse = youTubeSearch.execute()
    youTubeResponse.getItems.asScala
      .filter(_.getSnippet.getTitle.contains("さだまさし"))
      .headOption
  }

}

case class Song(title: String) {

  def show(): Unit = {
    sys.env.get("GOOGLE_API_KEY") match {
      case Some(key) =>
        SadaVideo(key).find(title) match {
          case Some(video) =>
            val uri = new URI(s"https://www.youtube.com/watch?v=${video.getId.getVideoId}")
            Desktop.getDesktop.browse(uri)
          case _ =>
            sys.error("YouTube で動画が見つかりませんでした。")
        }
      case _ =>
        val googleUri = new URI("https://console.developers.google.com/apis/credentials")
        Desktop.getDesktop.browse(googleUri)
        sys.error("環境変数 GOOGLE_API_KEY を設定してください。")
    }
  }

  def を観る(): Unit = show()
}

object SongCompilerMacro extends LoggerProvider {

  def selectDynamic(c: Context)(title: c.Expr[String]): c.Tree = {
    import c.universe._
    val Literal(Constant(t: String)) = title.tree
    if (SadaMasashi.songTitles.contains(t)) {
      // NOOP
    } else if (SadaMasashi.songTitleCandidates.contains(t)) {
      val correctTitle = SadaMasashi.findCorrectSongTitle(t).getOrElse(t)
      logger.warn(s"""気を利かせておきましたが、正しい曲名は "${correctTitle}" です。""")
      return q"_root_.sadamasashi.Song(title = ${correctTitle})"
    } else {
      val suggestions = {
        val ss = SadaMasashi.suggestSongTitles(t)
        ss.headOption.foreach {
          case (_, s) if s <= 0.5F =>
            val google = new URI(s"https://www.google.co.jp/search?q=${URLEncoder.encode(s"さだまさし ${t}", "UTF-8")}")
            Desktop.getDesktop.browse(google)
          case _ =>
        }
        val str = ss.map { case (t, s) => "%s (%1.2f)".format(t, s) }.mkString(", ")
        if (str.isEmpty) "" else s"もしかして: $str"
      }
      c.error(c.enclosingPosition, s"これはさだまさしさんの曲名ではないようです。$suggestions")
    }
    q"_root_.sadamasashi.Song(title = $title)"
  }

  def learnMore(c: Context): c.Tree = {
    import c.universe._
    sys.env.get("GOOGLE_API_KEY") match {
      case Some(key) =>
        val title = SadaMasashi.randomSongTitle
        SadaVideo(key).find(title) match {
          case Some(video) =>
            val uri = new URI(s"https://www.youtube.com/watch?v=${video.getId.getVideoId}")
            Desktop.getDesktop.browse(uri)
            c.error(c.enclosingPosition,
              s"""|
              | "さだまさし ${title}" で YouTube 動画を検索しました。
              | 「${video.getSnippet.getTitle}」(${uri}) がオススメです。
              | しっかりと理解を深めましょう。
              |""".stripMargin)
          case _ => c.error(c.enclosingPosition,
            s"""|
            | "さだまさし ${title}" で YouTube 動画を検索しました。
            | オススメの動画が見つかりませんでした。
            | もう一度お試しください。
            |""".stripMargin)
        }
      case _ =>
        val googleUri = new URI("https://console.developers.google.com/apis/credentials")
        Desktop.getDesktop.browse(googleUri)
        c.error(c.enclosingPosition,
          s"""|
          | 環境変数 GOOGLE_API_KEY に Google の API キーを設定すると無限にさだまさしさんについて学ぶことができます。
          | $googleUri にアクセスして API キーを発行・確認して
          | export GOOGLE_API_KEY={API キーの値}
          | のように設定してください。
          |""".stripMargin)
    }
    q"_root_.sadamasashi.SadaMasashi"
  }
}
