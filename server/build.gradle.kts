plugins {
    id("java")
    id("application")
}

application {
    mainClass = "com.piltong.modudoc.server.ServerApp"
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")


    // 프로젝트 공통 부분
    implementation(project(":common"))
}

tasks.test {
    useJUnitPlatform()
}