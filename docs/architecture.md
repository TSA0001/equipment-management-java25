# 備品管理システム — Java 25 / Liberty アーキテクチャ

```
Browser
  → Filter (CharacterEncodingFilter / AuthFilter)
  → Servlet
  → Service
  → DAO
  → JDBC / H2 (file: /data/h2/equipment)
  ↑
JSP
```

起動時に `AppBootstrapListener` が schema / seed を未初期化時のみ投入する。実行基盤は Java 25 の IBM WebSphere Application Server Liberty 26.0.0.9 で、`pages-3.1` を有効化する。
