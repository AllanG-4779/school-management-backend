INSERT INTO `profiles` (profile_name)
VALUES ('ADMINISTRATOR'),
       ('TEACHER'),
       ('STUDENT'),
       ('PARENT');
INSERT INTO `personal_information` (first_name, last_name, national_id, email, phone, personal_id, dob, profile_id)
VALUES ('Allan', 'Onyango', '36974949', 'allang4779@gmail.com', '254796407365', 'J17/4779/2018', '1999-07-13', 1);

INSERT INTO roles (role_name)
VALUES ('CREATE_STUDENT'),
       ('CREATE_TEACHER'),
       ('SYSTEM_ADMIN');
INSERT INTO profile_role_mapping (profile_id, role_id)
VALUES (1, 3);