import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.models.ProductRelease

plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.18.1"
}

group = "de.kq42"
version = "1.8.1"

tasks.withType<JavaCompile> {
    // platform 261+ runs on JBR 21; class files built with the local JDK 17 load fine there
    options.release = 17
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2026.1.4") { useInstaller = false }
    }
}

tasks.runIde {
    // sandbox only: dump the macOS system-shortcut table (the conflict-check basis) to idea.log
    jvmArgs("-Ddebug.system.shortcuts=true")
}

intellijPlatform {
    // resource-only plugin: nothing to index for searchable options
    buildSearchableOptions = false

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "261"
            // keymap-only plugin: no API surface, don't cap compatibility
            untilBuild = provider { null }
        }
    }

    pluginVerification {
        // The latest build of every IDE major version the plugin supports, oldest to newest, plus the
        // current EAP — picked from JetBrains' release feed on each run, so "newest" never goes stale.
        // The oldest one proves we use no API younger than sinceBuild; the newest ones catch what later
        // versions make internal, deprecate or remove (1.8.0 shipped such usages because only 2026.1
        // was checked). Same filter as printProductsReleases below, which lists the selection.
        ides {
            select {
                types = listOf(IntelliJPlatformType.IntellijIdea)
                channels = listOf(ProductRelease.Channel.RELEASE, ProductRelease.Channel.EAP)
                sinceBuild = "261"
            }
        }
    }

    publishing {
        // ./gradlew publishPlugin — set PUBLISH_TOKEN to a Marketplace permanent token
        // (https://plugins.jetbrains.com/author/me/tokens). The FIRST release must still be uploaded
        // manually on the web (it goes through moderation); this automates every release after that.
        token = providers.environmentVariable("PUBLISH_TOKEN")
    }

    // Optional author signing (Marketplace also signs on its side, so it is not required). Configured
    // only when the key env vars are present, so publishPlugin still works unsigned without them.
    if (System.getenv("CERTIFICATE_CHAIN") != null) {
        signing {
            certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
            privateKey = providers.environmentVariable("PRIVATE_KEY")
            password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
        }
    }
}

tasks.printProductsReleases {
    // ./gradlew printProductsReleases — the IDE builds verifyPlugin checks against
    types = listOf(IntelliJPlatformType.IntellijIdea)
    channels = listOf(ProductRelease.Channel.RELEASE, ProductRelease.Channel.EAP)
    sinceBuild = "261"
}
