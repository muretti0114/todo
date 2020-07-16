# ToDoApp - Kobe Spiral ToDo アプリケーション

2020-07-16 中村

Kobe Spiral のSpring Bootの題材，ToDo管理を行うWebアプリケーションのサンプル実装．

## デプロイ方法

1. MySQLにデータベースtodoを作成

```
$ mysql -u root -p
password: *****

mysql> create database 'todo';
mysql> create user todo identified by 'todotodo';
mysql> grant all on todo.* to todo;
```

2. resources/applications.propertiesを適宜編集して，MySQLサーバを向くようにする
1. VSCodeで実行してローカルでテスト．http://localhost:9500/
1. warを作る gradlew.bat war
1. ToDoApp.war をサーバのTomcatにデプロイ
1.  ブラウザでアクセス http://サーバIPアドレス:8080/ToDoApp/

## 使い方
1. 初めて起動する場合には，タイトル画面でユーザIDをAdministratorにしてログインする
1. 「ユーザを管理画面へ」をクリックし，適宜ユーザを作成する
1. 作成が完了したら「タイトル」に戻る
1. 作成したユーザIDでログインする
1. フォームからToDoを新規作成すると，ToDoのリストに追加される
1. タイトルをクリックすると，ToDoの詳細を確認できる．タイトルや詳細説明を更新できる．
1. Doneをクリックすると，Doneのリストに移動する
1. Reopenをクリックすると，ToDoのリストに移動する
1. 上部の「みんなのToDo」をクリックすると，登録ユーザ全員分のリストが表示される

## 既知の問題
- 「みんなのToDo画面」でToDoを操作すると，「個人のToDoの画面」に戻ってしまう


## システム構成
### フロントエンド
タイトル画面
- index.html - タイトル画面（静的ページ）

ToDo画面
- todolist.html - ToDoリスト画面
- todoform.html - 個別ToDo確認・更新
- todoerror.html - エラー画面

ユーザ画面
- userform.html - ユーザ登録フォーム
- userlist.html - ユーザ一覧画面

管理画面
- admin.html - 管理コンソール

### バックエンド
コントローラ
- ToDoController.java - ToDo画面遷移
- UserController.java - ユーザ画面遷移
- ToDoControllerAdvice.java - 例外ハンドラ
- ToDoRestController.java - ToDoのREST-API
- UserRestController.java - ユーザのREST-API

サービス
- ToDoService.java - ToDoのドメインサービス
- UserService.java - ユーザのドメインサービス

リポジトリ
- ToDoRepository.java - ToDoのリポジトリ
- UserRepository.java - ユーザのリポジトリ

エンティティ
- ToDo.java - ToDoのエンティティ
- User.java - ユーザのエンティティ

DTO
- ToDoForm.java - ToDoの入力フォーム
- ToDoDto.java - ToDoのDTO
- UserForm.java - ユーザの入力フォーム
- UserDto.java - ユーザのDTO

例外
- ToDoException.java - ToDoアプリ用のビジネス例外（非検査例外）

## ノウハウ

### バリデーション周り
- フォームからの入力は，Springのバリデータで検査
- 例外はToDoControllerAdviceでまとめてキャッチして，エラーページにまとめて飛ばす

#### Thymeleaf周りのハマった点
- データの受け渡し： コントローラ → HTMLテンプレ
    - キー文字列で属性名，バリューはオブジェクトをそのまま渡す．簡単．
```
   model.addAttribute("foo", fooObject);
```
- データの受け渡し： HTMLフォーム → コントローラ
    - \<form th:object="${bar}"> というフォームから受け取る場合は，コントローラの引数で，@ModelAttribute("bar")を指定する．
    - @ModelAttribute の引数がない場合には，自動的に推論して，Barクラスを探しに行こうとする．
    - ${todoform}だと，Todoformを探しに行こうとして失敗する．ToDoFormに渡したければ，${toDoForm}とするか，ちゃんと引数を指定する
    - 引数のBindingResultは，バリデーションするパラメータのすぐ後ろに置くこと
```
@PostMapping("．．．")
public String someMethod(..., @ModelAttribute("bar") @Validated BarForm form, BindingResult bindingResult, Model model) {
  if (bindingResult.hasError()) {
      //エラーがあったら，modelのbar属性にエラーが自動的にセットされる
      return "sameform"; //元のフォームに戻ってエラー表示
  }

}
```