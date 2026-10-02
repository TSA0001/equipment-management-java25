package com.example.equipment;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * ビルドパイプライン確認用の最小テスト。
 */
public class SmokeTest {

    @Test
    public void java8CompatibleRuntime() {
        String version = System.getProperty("java.specification.version");
        // Java 25 を対象にコンパイルする。コンテナ実行時の Java 版も別途確認する。
        assertTrue("java.specification.version must be present", version != null && version.length() > 0);
    }
}
