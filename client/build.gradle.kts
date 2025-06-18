plugins {
    // Java 애플리케이션 번들링 & 실행
    id("application")
    // JavaFX 지원 플러그인
    id("org.openjfx.javafxplugin") version "0.1.0"
}


javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml","javafx.web")
}

application {
    // Main 클래스 패키지 경로에 맞춰 수정하세요
    mainClass = "com.piltong.modudoc.client.ClientApp"
}


dependencies {
    // JavaFX 컨트롤, FXML 모듈
    implementation("org.openjfx:javafx-controls:21")
    implementation("org.openjfx:javafx-fxml:21")
    implementation("org.openjfx:javafx-web:21")
    implementation("org.fxmisc.richtext:richtextfx:0.11.5")

    // 프로젝트 공통 부분
    implementation(project(":common"))
}

