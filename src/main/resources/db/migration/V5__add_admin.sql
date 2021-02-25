INSERT INTO roles(role_name)	
VALUES 
('ADMIN'),
('LECTURER'),
('STUDENT');

INSERT INTO role_authorities(role_id, authority_id)	
VALUES 
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), 
(1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17), (1, 18), (1, 19), (1, 20),
(1, 21), (1, 22), (1, 23), (1, 24);

INSERT INTO users (user_username, user_password, user_person_id, user_enabled)
VALUES ('admin', '$2y$12$uXlGq0afzJ8NDurzneJuTeEcZqT99LmRnFfWjBMWZ21ZgrahO19Ye', 1, true);

INSERT INTO user_roles (user_id, user_role_id)
VALUES (1, 1);