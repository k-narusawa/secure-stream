# secure-stream

認証機能を持つアプリケーション

## 構成

[secure-stream-front](https://github.com/k-narusawa/secure-stream-front/tree/main)のバックエンド部分

### 機能

* ログイン認証
* ソーシャルログインによる認証
* PassKeyによる認証

## 初期設定

* サブモジュールの設定

```shell
git -C secure-stream-openapi config core.sparsecheckout true

# 確認コマンド
# `core.sparsecheckout`がtrueになっていることを確認する
git -C secure-stream-openapi config -l

# /kotlin-spring/ のみ記載
code .git/modules/secure-stream-openapi/info/sparse-checkout

git -C secure-stream-openapi read-tree -mu HEAD
```

## OpenAPIのRedoc

[リポジトリ](https://github.com/k-narusawa/secure-stream-openapi)

[ReDoc](https://k-narusawa.github.io/secure-stream-openapi)

## ソーシャルログイン

### 連携
```mermaid
sequenceDiagram
  front ->> api: プロバイダの認可リクエスト一覧リクエスト
  api -->> front : 認可リクエスト一覧返却
  front ->> provider: 認可リクエスト
  provider -->> api: 認可コード返却
  api ->> provider: アクセストークンリクエスト
  provider -->> api: アクセストークン返却
  api ->> provider: ユーザー情報取得
  provider -->> api: ユーザー情報返却
  api ->> api: ユーザー情報紐付け
  api ->> front: 連携完了画面へのリダイレクト

```

### 認証
```mermaid
sequenceDiagram
  front(auth) ->> auth: プロバイダの認可リクエスト一覧リクエスト
  auth -->> front(auth) : 認可リクエスト一覧返却
  front(auth) ->> provider: 認可リクエスト
  provider -->> api: 認可コード返却リクエスト
  api -->> auth: リダイレクト
  auth ->> provider: アクセストークンリクエスト
  provider -->> auth: アクセストークン返却
  auth ->> provider: ユーザー情報取得
  provider -->> auth: ユーザー情報返却
  auth ->> auth: ユーザー情報紐付き確認
  auth ->> front(auth): ログイン完了画面へのリダイレクト

```