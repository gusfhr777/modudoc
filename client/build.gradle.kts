plugins {
    // Java 애플리케이션 번들링 & 실행
    application
    // JavaFX 지원 플러그인
    id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    // Main 클래스 패키지 경로에 맞춰 수정하세요
    mainClass.set("com.piltong.modudoc.client.Main")
}

java {
    // 호환 JDK 버전
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
dependencies {
    // JavaFX 컨트롤, FXML 모듈
    implementation("org.openjfx:javafx-controls:21")
    implementation("org.openjfx:javafx-fxml:21")

    // 테스트용 JUnit
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // 웹소켓 API
    implementation("org.java-websocket:Java-WebSocket:1.5.6")
}




tasks.test {
    useJUnitPlatform()
}
