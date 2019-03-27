BEGIN;

INSERT INTO UserProfile 
(user_profile, user_level) VALUES 
('ADMIN', 2),
('SUPER_USER', 1),
('NORMAL_USER', 0);

INSERT INTO ProbeUser 
(user_name, user_password, user_profile, creator, creation_date) VALUES 
('tester','tester','ADMIN','rita','2018-11-12T18:00:00');

INSERT INTO TestType 
(test_type) VALUES 
('P2P'),
('VBC'),
('VGC'),
('REC'),
('MPTY'),
('SMS');

COMMIT;