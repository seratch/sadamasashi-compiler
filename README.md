# sadamasashi-compiler - さだまさしコンパイラ

https://en.wikipedia.org/wiki/Masashi_Sada

Masashi Sada (さだ まさし Sada Masashi, born April 10, 1952) is a Japanese singer, lyricist, composer, novelist, actor, and a film producer.

Sada formed the folk duo Grape with Masami Yoshida in 1972, and they made their debut as recording artists a year afterward. The pair rose to fame owing to the hit song "Shourou Nagashi" (精霊流し) composed by Sada, which peaked at the number-two position on the Japanese Oricon chart in 1974. They broke up in 1976, after producing some hit singles including "En-kiri Dera" (縁切寺) and "Muen Zaka" (無縁坂).

Sada released his first solo album entitled Kikyorai shortly after Grape's dissolution. Following the commercial success of the number-one hit single "Amayadori" (雨やどり, Shelter from the rain) in 1977, he enjoyed a recording career as one of the most popular Japanese male artists during the late 1970s and the first half of the 1980s.

Throughout his career as a musician, Sada released over 35 solo albums and 70 singles, and multiple live albums or compilations. Since the release of Shourou Nagashi, published in 2001, Sada has also worked as a novelist.

## Motivation

I just created a Scala macro library to share lots of people the joy of Scala programming.

[さだまさし x IT Advent Calendar 2015](http://qiita.com/advent-calendar/2015/sadatech) 20 日目の記事のためにつくったライブラリです。詳細は記事参照。

## Install

```scala
scalaVersion := "2.11.7"
libraryDependencies += "com.github.seratch" %% "sadamasashi-compiler" % "0.1"
initialCommands := "import sadamasashi._"
```

## Examples on the REPL

If the given song title is correct, sadamasashi-compiler successfully returns `Song` object. 

```
scala> さだまさし.関白宣言
res0: sadamasashi.Song = Song(関白宣言)
```

`show` or `を観る` method invokes your machine's default browser to show an appropriate YouTube video. The following code will show you [this YouTube video](https://www.youtube.com/watch?v=RF5kyFirzpE).

```
scala> さだまさし.関白宣言.を観る
```

When the song title is incorrect, sadamasashi-compiler gives you a compilation error and shows you some suggestions instead.

```
scala> さだまさし.ライオン
<console>:14: error: これはさだまさしさんの曲名ではないようです。もしかして: 風に立つライオン (0.44), 愛の音 (0.40), 絵画館 (0.40), 指定券 (0.40), 地平線 (0.40)
       さだまさし.ライオン
       ^
```

When there is no appropriate candidates for the input, sadamasashi-compiler automatically invokes your default browser and shows you [Google search result page](https://www.google.co.jp/search?q=%E3%81%95%E3%81%A0%E3%81%BE%E3%81%95%E3%81%97+%E5%A4%A9%E7%9A%87%E3%81%AE%E6%96%99%E7%90%86%E7%95%AA) as well.

```
scala> さだまさし.天皇の料理番
<console>:14: error: これはさだまさしさんの曲名ではないようです。もしかして: いのちの理由 (0.36), どんぐり通信 (0.36), 転校生(ちょっとピンボケ) (0.36), 検察側の証人 (0.33), 天空の村に月が降る (0.31)
       さだまさし.天皇の料理番
       ^
```

If you'd love to learn more about Masashi Sada, you can infinitely watch Sada-san's video.

```
scala> さだまさし.もっと知りたい
<console>:14: error:
 "さだまさし 秘密" で YouTube 動画を検索しました。
 「秘密 さだまさし　COVER   演奏法」(https://www.youtube.com/watch?v=DHSKEQwswoQ) がオススメです。
 しっかりと理解を深めましょう。

       さだまさし.もっと知りたい
             ^
```

Enjoy Masashi Sada's world :)
