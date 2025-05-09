plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    java
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // 프로젝트 공통 부분
    implementation(project(":common"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.bootRun {
    args = listOf()
}

tasks.register<JavaExec>("runDev") {
    group = "application"
    description = "빠르게 테스트용 서버 실행"

    mainClass.set("com.piltong.modudoc.server.ModudocServerApplication")
    classpath = sourceSets["main"].runtimeClasspath

    // JVM 옵션 필요시 예시
    jvmArgs = listOf("-Dspring.profiles.active=dev")

    // 환경 변수 필요시 예시
    environment("MY_ENV", "debug-mode")
}