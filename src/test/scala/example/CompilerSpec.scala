package example

import org.scalatest._

class CompilerSpec extends FunSpec with Matchers {

  import sadamasashi._

  describe("正しい曲名") {
    it("コンパイルできる") {
      さだまさし.関白宣言 should equal(Song("関白宣言"))

      さだまさし.案山子 should equal(Song("案山子"))
      さだまさし.かかし should equal(Song("案山子"))
      さだまさし.カカシ should equal(Song("案山子"))
      さだまさし.kakashi should equal(Song("案山子"))

      さだまさし.`Song for a friend` should equal(Song("Song for a friend"))
    }
  }

  describe("正しくない曲名") {
    it("コンパイルできない") {
      "val song = さだまさし.関白" shouldNot compile
    }
  }

  /* ブラウザが起動するので..

  describe("もっと知りたい") {
    it("should not be compiled") {
      "val res = さだまさし.もっと知りたい" shouldNot compile
      "val res = さだまさし.もっと学びたい" shouldNot compile
      "val res = SadaMasashi.learnMore" shouldNot compile
    }
  }

  describe("動画を観たい") {
    it("should open the default browser") {
      さだまさし.夢見る人.を観る
      さだまさし.精霊流し.を観る
      さだまさし.道化師のソネット.を観る
    }
  }
  */

}
