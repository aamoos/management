FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너로 복사
COPY target/*.jar app.jar

# 컨테이너에서 실행할 명령어 설정
CMD ["java", "-jar", "app.jar"]
