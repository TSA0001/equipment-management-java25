# AGENTS.md

## 現在の移行方針

- **Java 25**（Jakarta EE 10 / WAS Liberty 26.0.0.9）を使用する
- 既存 Java 8 の Servlet + JSP + JSTL + JDBC アプリを移行対象とする
- 既存の Web 画面・Service・DAO の動作を維持する

## 継続する制約

- Spring / Spring Boot / JSF / JPA / Hibernate 禁止
- Servlet + JSP + JSTL + JDBC
- Podman 単一コンテナ（Docker Desktop 必須にしない）
- 既存 Podman Machine / Volume を勝手に削除しない

## 作業方針

- 仕様正本: 親ディレクトリの `81-備品管理システム要求仕様.md`
- 段階実装。第1段階は最小アプリのみ（本リポジトリ現状）
- 各段階でビルド・テスト・起動確認

## Compose

```bash
BUILDAH_FORMAT=docker podman-compose up --build -d
```
