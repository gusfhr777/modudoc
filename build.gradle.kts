plugins {
    id("java")
}


repositories {
    mavenCentral()
}

dependencies {
    // test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")


}

subprojects {
    group = "com.piltong"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
    }
}




tasks.test {
    useJUnitPlatform()
}