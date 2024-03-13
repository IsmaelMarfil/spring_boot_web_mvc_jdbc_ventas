#Configuración Dockerfile para compilación en Jenkins local
#FROM amazoncorretto:17.0.9-alpine
#ADD target/spring_boot_web_mvc_jdbc_ventas.jar spring_boot_web_mvc_jdbc_ventas.jar
#ENTRYPOINT ["java", "-jar","spring_boot_web_mvc_jdbc_ventas.jar"]

#Configuración Dockerfile para compilación y despliegue en Render
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine
COPY --from=build /target/spring_boot_web_mvc_jdbc_ventas.jar spring_boot_web_mvc_jdbc_ventas.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","spring_boot_web_mvc_jdbc_ventas.jar"]