# JPA いろいろ

- [spring.jpa.open-in-view の 警告が出る](#springjpaopen-in-view-の-警告が出る)
- [application.properties における JPA の設定](#applicationproperties-における-jpa-の設定)
- [リレーションの FetchType と N+1 問題](#リレーションの-fetchtype-と-n1-問題)
- [Exampleを利用した検索 - QBE(Query by Example)](#exampleを利用した検索---qbequery-by-example)
- [Specificationを利用した動的なクエリ生成](#specificationを利用した動的なクエリ生成)
- [Specificationでページング時に join fetch を行うと例外が発生](#specificationでページング時に-join-fetch-を行うと例外が発生)
---

## spring.jpa.open-in-view の 警告が出る
Spring Data JPA を使うと起動時に spring.jpa.open-in-view に関する警告が出る。この設定はデフォルトで true になっているが、明示的に設定しろというもの。

この設定は、View で初めてアクセスしたエンティティに対する遅延読み込みを許可する（Open Session in View）かどうかを決めるもので、false になっていると、サービスクラスでロードしていなかったエンティティに View でアクセスするとエラーとなる。

true/false どちらにすべきかは賛否両論があるため、自分の好みで決定する。個人的にはあらかじめどのエンティティを使用するのかはすべて把握しておくべきと考えるので false の方がいいと思う。

参考: https://tosi-tech.net/2018/08/open-session-in-view-pattern/


## application.properties における JPA の設定
|設定|説明|
|--|--|
|spring.jpa.open-in-view=false                   | Open Session in View を許可しない |
|spring.jpa.hibernate.ddl-auto=none              | JPA のエンティティクラスに従ったテーブル自動作成に関する設定（none:何もしない。テーブルは別途作成する）|
|spring.jpa.show-sql=true                        | JPA が発行する SQL をログに出力する |
|spring.jpa.properties.hibernate.format_sql=true | SQL のログを改行して見やすく出力する |
|logging.level.org.hibernate.orm.jdbc.bind=trace | SQL のパラメータの値を出力する |


## リレーションの FetchType と N+1 問題

エンティティのリレーションには @OneToOne, @ManyToOne, @OneToMany, @ManyToMany がある。これらリレーション先のデータを DB からデータを取得するタイミングを FetchType といい、FetchType.LAZY では実際にそのデータにアクセスされるまで取得をせず、FetchType.EAGER では最初にリレーション先のデータのまとめて取得する。各リレーションのデフォルトの FetchType は以下。

|リレーション|FetchType|
|---|---|
|@OneToOne|EAGER|
|@ManyToOne|EAGER|
|@OneToMany|LAZY|
|@ManyToMany|LAZY|

FetchType.EAGER であっても JPQL を使用すると N+1問題が発生するため、基本的にはすべてのリレーションを FetchType.LAZY とし、N+1問題が発生するところでは join fetch 等を行うようにする


## Exampleを利用した検索 - QBE(Query by Example)

例となるオブジェクトを用意して、その例のプロパティにマッチするデータを検索できる。ageフィールドに値をセットしたオブジェクトを用意して同じ age のデータを検索するといった使い方ができる

参考: https://qiita.com/rubytomato@github/items/b685d33308c57e99d4e2

ただし Exampel が使えるのは findById や findAll など QueryByExampleExecutor インターフェースで定義されているメソッドのみで、JSQL や NamedEntityGraph とは一緒に使用できない。つまり join fetch が行えないため、join fetch が必要な場合は Example ではなく Specifivation を利用する


## Specificationを利用した動的なクエリ生成

検索機能では検索パラメータの有無によってクエリを変更する必要がある。これは Specificaton を使って実現可能。

参考: https://qiita.com/showichiro/items/e62bc4b3bdfe13ae82df


## Specificationでページング時に join fetch を行うと例外が発生

ページングのために Page を返す findAll では、`Page#getTotalElements` で使用するために select count が実行される。このとき「フェッチしたのに SELECT で使っていない」という意味で
```
java.lang.IllegalArgumentException: org.hibernate.QueryException: query specified join fetching, but the owner of the fetched association was not present in the select list
```
という例外が発生する。これを回避するため、クエリの戻り値の型が Long のとき(=select count)は join fetch しないようにする。

参考: https://hepokon365.hatenablog.com/entry/2021/12/31/160502

