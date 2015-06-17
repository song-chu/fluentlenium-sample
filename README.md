# fluentleniumサンプル
## fluentleniumとは
FluentLeniumは、Selenium WebDriverソースをシンプルに書けるJavaライブラリです。
フランスの女性開発者Mathilde Lemee氏が中心となって開発を進めています。

## fluentleniumのドキュメント
ドキュメントは[本家](https://github.com/FluentLenium/FluentLenium)に詳細に書かれています。

## fluentleniumでコードを作成するため、必要な知識
* CSS selector or DOM
* jUnit

## このサンプルの特徴
* よりコード量を減らすため、fluentlenium APIをさらに拡張しています。
** テストケースはBrowserTestBaseを継承してください。
** ページクラスはPageBaseを継承してください。

* プロパティファイルを提供しています。
設定したいプロパティがあれば、env.propertiesに設定してください。
/fluentlenium-sample/src/test/resources/env.properties
設定したプロパティはEnv.javaから取得可能です。

* 試験結果レポート（Excel）を自動作成します。
レポートファイル（Excel）はテストクラス毎に作成されます。
レポートファイルにはテストメソッド毎にシートが作成されます。
各シートに証跡の画面ショットが出力されます。
テストコードにて画面ショットを作成する際にパラメータとしてコメットを入力すると、レポートにて画面ショットの説明として出力されます。
レポート出力先はプロパティファイルに設定してください。

* レポートにはテストシナリオを出力することができます。
テストメソッドに@MethodCommentを付けて、シナリオを記述すると、各シートの上端にシナリオが出力されます。

## 効果的な画面試験を行うために
* テストシナリオを必ず試験前に作成してください。

* ブラウザの開発モード（F12)を利用する要領を習得してください。
画面テストなので画面エレメントを特定するためには必須です。
