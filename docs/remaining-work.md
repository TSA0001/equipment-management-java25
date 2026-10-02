# Java 25 移行版 — 引き継ぎメモ

## 現在の到達点

- Java 8 / `javax.*` の Servlet/JSP/JDBC アプリを Java 25 / Jakarta EE 10 へ移行済み
- 実行基盤を Apache Tomcat 9 から IBM WebSphere Application Server Liberty / Open Liberty 26.0.0.9 へ移行済み
- Web画面、Service、DAO、JDBC/H2 の既存機能を維持
- MCP機能はこのリポジトリには含めない

## 動作確認

```bash
cd "/Users/tsano/00-works/111Bob-Java8縛りからのBob-MCP/equipment-management-java25"
mvn clean test
BUILDAH_FORMAT=docker podman-compose up --build -d
curl http://127.0.0.1:9083/equipment-management/health
```

Web UI: `http://localhost:9083/equipment-management/login`

## 次の派生

MCP Tool を追加する場合は、兄弟プロジェクトの `equipment-management-mcp` を使用する。
