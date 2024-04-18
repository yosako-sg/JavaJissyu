# 研修: Java 実習用サービス加入者管理システム

このプロジェクトは、Java 実習用で使用するサービス加入者システムです。
Spring Framework 6(Spring Boot 3) を使用して作成されています。

**弊社 Java 研修受講者は、本リポジトリをフォークしてください。**

## 必要なソフトウエア

### Windows

以下のソフトウエアを、公式サイトからダウンロードしてインストールしてください。

- Java Development Kit 17 以降 (以下うちどれか一つを選択してインストール)
  - [Amazon Corrette](https://aws.amazon.com/jp/corretto)
  - [Eclipse Temurin](https://adoptium.net/temurin/releases/)
- [Visual Studio Code](https://azure.microsoft.com/ja-jp/products/visual-studio-code)
- [Git for Windows](https://gitforwindows.org/)

### Linux(Ubuntu)

以下のコマンドを実行して、必要なソフトウエアをインストールしてください。

```sh
sudo apt install git openjdk-17-jdk-headless
wget -o vscode.deb https://code.visualstudio.com/sha/download?build=stable&os=linux-deb-x64
sudo apt install ./vscode.deb
```

## 開発方法

本プロジェクトは、 Visual Studio Code(以下、VSCode) で開発することを想定しています。
VSCode を起動し、フォークしたリポジトリを適当な場所にクローンしてください。その後、クローンしたフォルダを開きます。
初回クローンしたフォルダを VSCode で開くと、本プロジェクトが利用している拡張機能のインストールを求められますので、インストールしてください。

なお、開発ドキュメントは所定の場所に格納しています。事前に講師が説明をしますので、その場所のドキュメントを参照してください。

## 実行およびデバッグ方法

クローンしたフォルダを VSCode で開いた状態で、F5 キーを押します。

## ビルド方法

ターミナルから以下のコマンドを実行します。

```sh
./gradlew bootWar
```

エラーない場合は、`build/lib` フォルダに、`webapp-0.0.1-SNAPSHOT.war` が作成されます。
このファイルは、`java -jar webapp-0.0.1-SNAPSHOT.war` とすることで実行できます。
