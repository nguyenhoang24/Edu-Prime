plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.eduprime"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("io.jsonwebtoken:jjwt:0.12.3")
	// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
	implementation("org.redisson:redisson-spring-boot-starter:3.26.0")
	implementation("org.apache.pdfbox:pdfbox:2.0.4")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	// https://mvnrepository.com/artifact/joda-time/joda-time
	implementation("joda-time:joda-time:2.3")
	compileOnly("org.springframework.boot:spring-boot-starter-validation")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
	enabled = true
	manifest.attributes["Main-Class"] = "com.elearn.userservice.UserServiceApplication"
	manifest.attributes["Class-Path"] = configurations
			.runtimeClasspath
			.get()
			.joinToString(separator = " ") { file ->
				"lib/${file.name}"
			}
}

tasks.register<Copy>("copyLib") {
	from(configurations.runtimeClasspath)
	into("lib")
}
