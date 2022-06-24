INSERT INTO roles VALUES (DEFAULT, 'ADMIN'),(DEFAULT, 'USER'),(DEFAULT, 'LOCKED');

INSERT INTO tag VALUES (DEFAULT ,'clammish'),(DEFAULT,'subproofs'),
                       (DEFAULT,'gesticulacious'),
                       (DEFAULT,'inadjustability'),(DEFAULT,'curryfavour'),
                       (DEFAULT,'high-blazing'),(DEFAULT,'put-out'),
                       (DEFAULT,'twin-tractor'),(DEFAULT,'endomysium'),
                       (DEFAULT,'murthering');

INSERT INTO users VALUES (DEFAULT,'awakable@epam.test.com','garmentworker','awakable',2),
                         (DEFAULT,'buddles@epam.test.com','preshows','buddles',2),
                         (DEFAULT,'Euploeinae@epam.test.com','adiaphorism','Euploeinae',2),
                         (DEFAULT,'devocate@epam.test.com','unhidden','devocate',1);

INSERT INTO gift_certificate VALUES (DEFAULT,'2022-03-08 15:33:15.000000','hooker-out Blase self-abhorring pseudotropine axemaster testing archdeceiver outsetting orientating',11,'2022-05-22 16:16:01.557016','fanwise tornado-swept certificate',1690.83),
                                    (DEFAULT,'2022-04-14 16:56:02.000000','Lymantriidae shacklebone hematozzoa didactive glorification biochemically analabos anecdotist',56,'2022-05-22 16:16:01.567089','dimethylanthranilate Hentrich certificate',4400.86);

INSERT INTO orders VALUES (DEFAULT,'2022-05-12 18:34:48.000000',1690.83,3),
                          (DEFAULT,'2022-03-18 14:27:48.000000',6091.69,1);

INSERT INTO orders_has_certificates VALUES (1,1),(2,1),(2,2);

INSERT INTO certificate_has_tag VALUES (1,2),(2,2),(1,6),(2,6),(1,7),(1,8),(1,9),(1,10);
