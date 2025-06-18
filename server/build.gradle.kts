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

    // log4j
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")



    // 프로젝트 공통 부분
    implementation(project(":common"))
}