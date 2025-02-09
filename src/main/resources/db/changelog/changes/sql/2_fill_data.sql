INSERT INTO users (
  id,
  name,
  surname,
  patronymic,
  date_of_birth,
  phone_number,
  email,
  image,
  password,
  role
  )
VALUES
('6ec8150b-354c-417b-b28c-1da62d237c9f','admin','adminSurname',	null,'2020-08-21','+79271723175','admin',null,'$2a$10$Rvf1iUacVUz84GWX.zHBAeYAvk54UvTSXAWgHblN7wCME0JCIAWUS','ADMIN')
ON CONFLICT (id) DO NOTHING;
