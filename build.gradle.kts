
// 자바 플러그인 적용
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
    apply(plugin = "java")

    group = "com.piltong"
    version = "1.0-SNAPSHOT"


    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }

}


tasks.test {
    useJUnitPlatform()
}