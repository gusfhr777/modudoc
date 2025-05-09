plugins {
    java
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "com.piltong.modudoc"
    version = "1.0.0"

    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
