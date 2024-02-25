plugins {
    id("maven-publish")
    id("signing")
}

val androidSourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    if (project.plugins.findPlugin("com.android.library") != null) {
        // For Android libraries
        from(android.sourceSets["main"].java.srcDirs)
        from(android.sourceSets["main"].kotlin.srcDirs)
    } else {
        // For pure Kotlin libraries, in case you have them
        from(sourceSets["main"].java.srcDirs)
        from(sourceSets["main"].kotlin.srcDirs)
    }
}

artifacts {
    add("archives", androidSourcesJar)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                // Add the constants in another file
                groupId = PUBLISH_GROUP_ID
                artifactId = PUBLISH_ARTIFACT_ID
                version = PUBLISH_VERSION

                // Two artifacts, the `aar` (or `jar`) and the sources
                if (project.plugins.findPlugin("com.android.library") != null) {
                    from(components["release"])
                } else {
                    from(components["java"])
                }

                artifact(androidSourcesJar.get())

                pom {
                    name.set(PUBLISH_ARTIFACT_ID)
                    description.set("Math sdk")
                    url.set("https://github.com/Propogand/MathSdk")
                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("https://github.com/Propogand/MathSdk/LICENSE.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("MathSdk")
                            name.set("Vladislav")
                            email.set("reshgand@yandex.ru")
                        }
                    }
                    scm {
                        connection.set("scm:github.com/Propogand/MathSdk.git")
                        developerConnection.set("scm:git:ssh:git@github.com:Propogand/MathSdk.git")
                        url.set("https://github.com/Propogand/MathSdk")
                    }
                }
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        rootProject.extra["signing.keyId"] as String,
        rootProject.extra["signing.key"] as String,
        rootProject.extra["signing.password"] as String
    )
    sign(publishing.publications)
}