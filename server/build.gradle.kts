plugins {
    id("application")
}

application {
    mainClass = "com.piltong.modudoc.server.ServerApp"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mysql:mysql-connector-j:8.0.33")


    // 프로젝트 공통 부분
    implementation(project(":common"))
}