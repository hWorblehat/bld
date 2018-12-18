buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("ch.epfl.scala:gradle-bloop_2.11:1.1.2")
    }
}

plugins {
    scala
    groovy
    `java-library`
    application
}

apply(plugin = "bloop")

val scalaMajorMinorVersion = "2.12"
val scalaVersion = "$scalaMajorMinorVersion.7"
val junitVersion = "5.3.+"

repositories {
    jcenter()
}

application {
    mainClassName = "org.bld.Main"
}

dependencies {
    api("org.scala-lang:scala-library:$scalaVersion")

    implementation("com.chuusai:shapeless_$scalaMajorMinorVersion:2.3.+")

    testImplementation("org.scalatest:scalatest_$scalaMajorMinorVersion:3.0.5")
//    testImplementation("org.spockframework:spock-core:1.2-groovy-2.5")

    testCompileOnly("junit:junit:4.12")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.3.+")
}

tasks.wrapper {
    gradleVersion = "5.0"
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Sync>("gatherRuntimeLibs") {
    destinationDir = file("$buildDir/runtimeClasspath")
    from(configurations.get(sourceSets.main.get().runtimeClasspathConfigurationName))
    from(tasks.jar)
}