plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.BackEnd"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("mysql:mysql-connector-java:8.0.33")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
	implementation("org.springframework.boot:spring-boot-starter-web")
 	implementation("com.cloudinary:cloudinary-http5:2.0.0")
    implementation("com.cloudinary:cloudinary-taglib:2.0.0")
    implementation("io.github.cdimascio:dotenv-java:2.2.4")
	
}


tasks.withType<Test> {
	useJUnitPlatform()
}
