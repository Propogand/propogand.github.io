val signingKeyId: String by project
val signingPassword: String by project
val signingKey: String by project
val ossrhUsername: String by project
val ossrhPassword: String by project
val sonatypeStagingProfileId: String by project

// Load properties from local.properties file or environment variables
val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    val properties = Properties().apply {
        load(FileInputStream(secretPropsFile))
    }
    properties.forEach { name, value ->
        project.extra[name.toString()] = value.toString()
    }
} else {
    project.extra["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    project.extra["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
    project.extra["sonatypeStagingProfileId"] = System.getenv("SONATYPE_STAGING_PROFILE_ID")
    project.extra["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    project.extra["signing.password"] = System.getenv("SIGNING_PASSWORD")
    project.extra["signing.key"] = System.getenv("SIGNING_KEY")
}

// Configure Nexus publishing
configure<PublishingExtension> {
    repositories {
        maven {
            name = "sonatype"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
}