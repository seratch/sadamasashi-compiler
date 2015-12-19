import scala.language.reflectiveCalls

package object sadamasashi {

  val 佐田雅志 = SadaMasashi
  val さだまさし = 佐田雅志
  val まっさん = さだまさし

  type Closable = { def close() }

  def using[R <: Closable, A](resource: R)(f: R => A): A = {
    try f(resource) finally try resource.close() catch { case scala.util.control.NonFatal(_) => }
  }

  def readLines(filename: String): Seq[String] = {
    using(getClass.getClassLoader.getResourceAsStream(filename)) { f =>
      scala.io.Source.fromInputStream(f).getLines.toIndexedSeq
    }
  }
}
