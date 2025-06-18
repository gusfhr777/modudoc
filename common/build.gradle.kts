plugins {
    id("java")
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.fxmisc.richtext:richtextfx:0.11.5")

}

tasks.test {
    useJUnitPlatform()
}