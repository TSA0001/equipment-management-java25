# 備品管理システム

Java 8 の Servlet/JSP アプリを Java 25・Jakarta EE 10・IBM WebSphere Application Server Liberty / Open Liberty へ移行した社内備品管理 Web アプリケーションです。MCP機能は含みません。

## 技術構成

| 項目 | 版・内容 |
|------|----------|
| Java | 25（IBM Semeru Runtime） |
| Web | Jakarta Servlet 6.0 + Jakarta Pages 3.1 + JSTL 3.0 |
| コンテナ | IBM WebSphere Application Server Liberty / Open Liberty 26.0.0.9 |
| ビルド | Maven 3.9.11（Java 25） |
| 実行環境 | Podman（単一コンテナ） |
| DB | H2（ファイル DB、Volume 永続化。旧 Tomcat 版とは別 Volume） |

Spring / JSF / JPA は使用しません。

コンテナイメージには OSS の Open Liberty を使用しています。同じ Liberty 機能（`pages-3.1`）を利用するため、IBM WebSphere Application Server Liberty 上でも動作する構成です。

## 前提

- macOS
- Podman 5.x
- Podman Machine（既存 Machine を利用。削除・再作成しない）

### Podman Machine の確認・起動

```bash
podman machine list
podman machine start podman-machine-default
podman system connection default podman-machine-default
```

本作業では `podman-machine-default` を使用します。  
`equipment-dev` は初回起動が emergency mode のため、現状は使いません。

Compose は本環境では `podman-compose` を使用します。

## ビルドと起動

```bash
cd equipment-management-java25
# HEALTHCHECK を有効にするため docker 形式でビルドする
BUILDAH_FORMAT=docker podman-compose up --build -d
```

## アクセス URL

この移行版は、旧Tomcat版およびMCP追加版と同時に動かせるよう、ホスト側ポートに `9083` を使用します。

| URL | 内容 | 権限 |
|-----|------|------|
| http://localhost:9083/equipment-management/ | トップ（`/home` へリダイレクト） | ログイン必須 |
| http://localhost:9083/equipment-management/login | ログイン画面 | 誰でも |
| http://localhost:9083/equipment-management/home | トップ画面 | ログイン必須 |
| http://localhost:9083/equipment-management/items | 備品一覧・検索 | ログイン必須 |
| http://localhost:9083/equipment-management/items/new | 備品登録 | 管理者のみ |
| http://localhost:9083/equipment-management/items/detail?id= | 備品詳細 | ログイン必須 |
| http://localhost:9083/equipment-management/items/edit?id= | 備品編集 | 管理者のみ |
| http://localhost:9083/equipment-management/items/delete?id= | 備品削除確認 | 管理者のみ |
| http://localhost:9083/equipment-management/loans/new?itemId= | 貸出 | ログイン必須 |
| http://localhost:9083/equipment-management/loans/return?itemId= | 返却 | ログイン必須 |
| http://localhost:9083/equipment-management/loans | 全貸出履歴 | 管理者のみ |
| http://localhost:9083/equipment-management/loans?mode=active | 全貸出中一覧 | 管理者のみ |
| http://localhost:9083/equipment-management/mypage/loans | 自分の貸出中一覧 | ログイン必須 |
| http://localhost:9083/equipment-management/mypage/history | 自分の貸出履歴 | ログイン必須 |
| http://localhost:9083/equipment-management/logout | ログアウト | ログイン必須 |
| http://localhost:9083/equipment-management/health | ヘルスチェック | 誰でも |
## 初期ユーザー

| ログイン ID | パスワード | 権限 | 表示名 |
|-------------|-----------|------|--------|
| `admin` | `admin123` | 管理者 | 管理者 |
| `user1` | `user1234` | 一般利用者 | 一般 太郎 |

> パスワードは SHA-256 ハッシュで保存しています（開発・検証用）。

## 実装済み機能

- **認証・認可**
  - ログイン / ログアウト（セッション固定化攻撃対策済み）
  - 未ログイン時は `/login` へリダイレクト（`AuthFilter`）
  - 管理者専用操作は `AdminFilter` および各 Servlet で二重チェック
- **備品管理（管理者）**
  - 備品登録（確認→完了フロー）
  - 備品編集
  - 論理削除
- **備品管理（全ログインユーザー）**
  - 備品一覧・検索（管理番号・備品名・カテゴリ・保管場所・状態）
  - 備品詳細
- **貸出・返却**
  - 利用可能備品の貸出
  - 返却（状態を「返却済み / 修理中」から選択）
  - 排他更新（VERSION）
- **履歴・マイページ**
  - 全貸出履歴 / 全貸出中一覧（管理者専用）
  - 自分の貸出中一覧・全履歴（マイページ）
  - 期限超過の識別
- **基盤**
  - H2 ファイル DB（`DB_PATH=/data/h2/equipment`、Volume 永続化）
  - 入力検証（管理番号形式、必須、日付など）

## よく使うコマンド

```bash
# 状態
podman-compose ps

# ログ
podman-compose logs -f
# または
podman logs -f equipment-java25-app

# 停止（Volume は削除しない）
podman-compose down

# 再ビルド
podman-compose up --build -d
```

## コンテナ内の Java 確認

```bash
podman exec equipment-java25-app java -version
```

`25.x` であること。

## Maven テスト（イメージビルド時）

Containerfile のビルドステージで `mvn clean package`（テスト含む）を実行します。

ローカルでテストのみ実行する場合:

```bash
cd equipment-management
mvn clean test
```

## ディレクトリ構成（抜粋）

```
equipment-management/
├─ Containerfile
├─ compose.yaml
├─ pom.xml
├─ src/main/java/.../
│   ├─ dao/        # DB アクセス（JDBC）
│   ├─ model/      # ドメインモデル / フォーム
│   ├─ service/    # ビジネスロジック
│   ├─ util/       # DB 初期化 / 接続 / パスワードハッシュ
│   ├─ validation/ # 入力検証
│   └─ web/        # Servlet / Filter
├─ src/main/resources/db/  # schema.sql / seed.sql
├─ src/main/webapp/        # JSP / CSS
└─ src/test/java/          # JUnit 4
```

## Volume について

`equipment-java25-db` Volume は Liberty 版 H2 永続化用です。  
旧 Tomcat 版の `equipment-db` は保持し、共有しません。  
`podman-compose down` では Volume を削除しません。  
**Volume を削除するとデータが失われます。明示指示がある場合のみ削除してください。**

## トラブルシューティング

- `connection refused` / Podman socket エラー → `podman machine start` を実行
- 9083 が使えない → 他プロセスを確認し、必要なら compose のポートを変更
- 起動に失敗 → `podman logs equipment-java25-app` で Liberty のメッセージを確認
