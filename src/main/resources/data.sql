INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('User2', 'user2@ya.ru', '{noop}password'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3);

INSERT INTO RESTAURANT (name)
VALUES ('Сопка'),
       ('Сациви'),
       ('Густав и Густав');

INSERT INTO MENU (name, restaurant_id)
VALUES ('Меню дня', 1),
       ('Меню дня', 2);

INSERT INTO MENU (name, restaurant_id, creation_date)
VALUES ('Меню дня', 3, '2023-01-01');

INSERT INTO DISH (menu_id, name, price)
VALUES (1, 'Голубцы', 250),
       (1, 'Омлет', 150),
       (1, 'Суп', 90),
       (2, 'Суп', 90),
       (2, 'Омлет', 150),
       (2, 'Голубцы', 250),
       (2, 'Брускеты с паштетом', 350),
       (3, 'Суп', 90),
       (3, 'Цыпленок запеченый', 410),
       (3, 'Омлет', 150),
       (3, 'Брускеты с паштетом', 350),
       (3, 'Голубцы', 250);

INSERT INTO VOTE (user_id, restaurant_id, date_time)
VALUES (1, 1, '2023-04-04T09:00:00'),
       (1, 1, '2023-04-05T09:00:00'),
       (1, 1, '2023-04-06T09:00:00'),
       (1, 2, '2023-04-07T09:00:00');

INSERT INTO VOTE (user_id, restaurant_id, date_time)
VALUES (1, 1, CURRENT_TIMESTAMP()),
       (2, 1, CURRENT_TIMESTAMP());
