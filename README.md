# Tennis Scoreboard

Веб-приложение для отслеживания счёта теннисных матчей в реальном времени. Учебный проект на Java Servlets + Hibernate.

## Стек

- Jakarta Servlets (Tomcat 10)
- Hibernate ORM + HikariCP
- H2 (in-memory)
- JSP
- Gradle

## Возможности

- Создание матча с двумя игроками
- Ведение счёта в реальном времени: очки → геймы → сеты
- Поддержка тай-брейка и правил дюса/преимущества
- Просмотр истории матчей с пагинацией и фильтрацией по имени игрока

## Страницы

| URL | Описание |
|-----|----------|
| `/new-match` | Форма создания матча |
| `/match-score?uuid=...` | Текущий счёт матча |
| `/matches` | История завершённых матчей |

## Запуск

Перед деплоем нужно один раз создать скрипт, указав путь до своего Tomcat:
```bash
echo "sudo -A cp ./build/libs/app.war ${Путь_до_tomcat}/webapps/tennis-scoreboard.war" > src/deploy.sh
```

Затем из корня проекта:
```bash
./gradlew app:deployWar
```

Приложение будет доступно на `http://localhost:8080/tennis-scoreboard`.
