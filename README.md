# fluentleniumサンプル
## fluentleniumとは
FluentLeniumは、Selenium WebDriverソースをシンプルに書けるJavaライブラリです。フランスの女性開発者Mathilde Lemee氏が中心となって開発を進めています。

## fluentleniumのドキュメント
[本家](https://github.com/FluentLenium/FluentLenium)

## fluentleniumでコードを作成するため、必要な知識
* CSS selector or DOM

## このサンプルの特徴
* よりコード量を減らすため、fluentlenium APIをさらに拡張しています。従い、テストケースはBrowserTestBaseを継承してください。各ページクラスはPageBaseを継承してください。
* プロパティファイルを提供しています。設定したいことがあれば、env.propertiesに設定してください。
/fluentlenium-sample/src/test/resources/env.properties
* 試験結果レポート（Excel）を自動作成します。テストコードにて画面ショットを作成する際にコメットを入力すると、画面ショットの説明として出力されます。レポート出力先はプロパティファイルに設定してください。レポートファイルはテストクラス毎に作成されます。レポートファイルにはテストメソッド毎にシートが作成されます。各シートに証跡の画面ショットが出力されます。
* レポートにはテストシナリオを出力することができます。テストメソッドに@MethodCommentを付けて、シナリオを記述すると、各シートの上端にシナリオが出力されます。

## 効果的な画面試験を行うために
* テストシナリオを必ず試験前に作成してください。
* 画面テストなので画面エレメントを特定することが必要です。ブラウザの開発モード（F12)を利用する要領を習得してください。
