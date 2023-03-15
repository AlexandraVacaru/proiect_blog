INSERT INTO ROLE VALUES(100, 'ROLE_USER');
INSERT INTO ROLE VALUES(200, 'ROLE_ADMIN');

INSERT INTO USER VALUES(1000, '2022-03-28', 'userTest1@yahoo.com', 'User', 'User',
                        '$2a$10$UqZwp.SFT/f80rRAvODpvu8iVyBKF812QgJv2pzg4PFWZJjge5//m',
                        'https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg',
                        'userTest1', null);
INSERT INTO USER VALUES(2000, '2022-03-28', 'userTest2@yahoo.com', 'User2', 'User',
                        '$2a$10$VOEpUvqjLrWkX.W2.UMcjOlrwnkrdq4q3z1L420KUtB4xkw3KAH7G',
                        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSxHWFss7T4f3QifjwCTUJ-VGqffPBBDI1VlQ&usqp=CAU',
                        'userTest2', NULL);

INSERT INTO USER_ROLE VALUES (1000, 100);
INSERT INTO USER_ROLE VALUES (1000, 200);
INSERT INTO USER_ROLE VALUES (2000, 100);

INSERT INTO BLOG VALUES(1000, 'OTHER',
                        'Lorem ipsum dolor sit amet, consectetur adipiscing elit.',
                        '2022-04-14 13:22:47', 'blog1', 1000);
INSERT INTO BLOG VALUES(2000, 'OTHER',
                        'Lorem ipsum dolor sit amet, consectetur adipiscing elit.',
                        '2022-04-14 13:22:47', 'Test1', 1000);

INSERT INTO USER_LIKE VALUES (1000,2000);