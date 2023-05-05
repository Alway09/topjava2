[Проект TopJava-2](https://javaops.ru/view/topjava2)
===============================

#### Разбор решения [выпускного проекта TopJava](https://github.com/JavaOPs/topjava/blob/master/graduation.md)
- Исходный код взят из миграции TopJava на Spring Boot (без еды)
- На основе этого репозитория на курсе будет выполняться выпускной проект "Голосование за рестораны"

-------------------------------------------------------------
- Stack: [JDK 17](http://jdk.java.net/17/), Spring Boot 3.x, Lombok, H2, Caffeine Cache, SpringDoc OpenApi 2.x, Mapstruct, Liquibase 
- Run: `mvn spring-boot:run` in root directory.
-----------------------------------------------------
Technical Requirement (Техническое задание)
-----------------------------------------------------
### English:

Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) without frontend.

The task is:

***Build a voting system for deciding where to have lunch.***

2 types of users: admin and regular users
+ Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price).
+ Menu changes each day (admins do the updates).
+ Users can vote for a restaurant they want to have lunch at today.
+ Only one vote counted per user.
+ If user votes again the same day:
    + If it is before 11:00 we assume that he changed his mind;
    + If it is after 11:00 then it is too late, vote can't be change.
+ Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it (better - link to Swagger).

### Русский:

Разработать REST API используя Hibernate/Spring/SpringMVC (Spring-Boot желательно!) без фронтэнда.

Задание:

***Создать систему голосования для принятия решения о месте обеда.***

2 типа пользователей: админ и обычный пользователь
+ Админ может создавать ресторан и его меню дня (обычно 2-5 наименований, только название блюда и его цена).
+ Меню меняется каждый день (изменения вносит админ).
+ Пользователи могут голосовать за ресторан, где они хотят отобедать сегодня.
+ Один пользователь может проголосовать единожды.
+ В том случае, если пользователь голосует снова в тот же день:
    + Если это происходит до 11:00, то считать что пользователь изменил свое мнение;
    + Если это происходит после 11:00, значит слишком поздно, голос нельзя изменить.
+ Каждый ресторан предоставляет новое меню ежедневно.

В качестве результата предоставить ссылку на github репозиторий. Он должен содержать код приложения, README.md с документацией API и несколько curl команд для тестирования (а лучше ссылку на Swagger).

-----------------------------------------------------
[REST API documentation](http://localhost:8080/swagger-ui.html)  
Креденшелы:
```
User:  user@yandex.ru / password
Admin: admin@gmail.com / admin
Guest: guest@gmail.com / guest
```