## ✅ 온보딩 체크리스트
- [ ] 깃허브 리포지토리 클론
- [ ] 인텔리제이로 연 뒤 빌드 완료될 때 까지 대기
- [ ] 인코딩 설정: `Ctrl + Alt + S` encoding 설정
<img width="400" height="300" alt="image" src="https://github.com/user-attachments/assets/34e96550-b1ec-4bc1-9fa4-f436cbbf83f3" />

- [ ] 빌드 도구 IntelliJ 로 변경
<img width="400" height="300" alt="image" src="https://github.com/user-attachments/assets/da6028d9-51e4-4664-90df-13391e1e2098" />

- [ ] Enable Annotation Processing
- [ ] `/src/main/resources/application.yml` 파일 생성
      
    ```yaml
    spring:
      application:
        name: roadmap
      datasource:
        url: jdbc:mysql://localhost:3306/{DB_이름(ex. kllhy_roadmap)}
        username: {사용자_이름}
        password: {사용자_비밀번호}
        driver-class-name: com.mysql.cj.jdbc.Driver
      jpa:
        hibernate:
          ddl-auto: none
        show-sql: true
        properties:
          hibernate:
            format_sql: true
            dialect: org.hibernate.dialect.MySQL8Dialect
    ```
- [ ] DB 생성 스크립트

    ```sql
    # 예시
    CREATE DATABASE IF NOT EXISTS `DB_이름`;
    GRANT ALL PRIVILEGES ON DB_이름.* TO '사용자_이름'@'%';
    USE DB_이름;
    ```
