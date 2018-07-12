/**
 * After running this script, call the function like this (in MySQL):
 *
 *    CALL CREATE_USERS(1, 10)
 *
 * In that example, 1 is the starting number and 10 is the ending number (inclusive) for the users you want to create
 */

DELIMITER $$
DROP PROCEDURE IF EXISTS CREATE_USERS $$
CREATE PROCEDURE CREATE_USERS(startval INTEGER, endval INTEGER)
   BEGIN
      DECLARE a INT Default startval ;
      simple_loop: LOOP
      	DELETE FROM jhi_user WHERE login = CONCAT('user', a, '@saturn.com');
         INSERT INTO jhi_user VALUES(NULL, CONCAT('user', a, '@saturn.com'), '$2a$10$LrTP69Zrj.F.7ikWR3H38.3Z.YzlOPb2IxAbjFPoiySbvMPNMoyA6', 'Test', 'User', CONCAT('user', a, '@saturn.com'), 1, 'en', NULL, NULL, 'USER', 'admin', CURDATE(), NULL, 'admin', NULL);
         SET a=a+1;
         IF a= endval + 1 THEN
            LEAVE simple_loop;
         END IF;
   END LOOP simple_loop;
END $$