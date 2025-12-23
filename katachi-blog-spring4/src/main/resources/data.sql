INSERT INTO category (
	name
) VALUES
('本校'),
('本町第２校'),
('B型'),
('四ツ橋校');

INSERT INTO "user" (
	name, email, password, role
) VALUES
('管理者1', 'contact@miraino-katachi.co.jp', '$2a$10$i4Muik//i6mHteRh4nC..uBXsquVAsz7tm0MVm7.vgBmACew1xL7K', 'ROLE_ADMIN'),
('管理者2', 't_shiba@miraino-katachi.co.jp', '$2a$10$i4Muik//i6mHteRh4nC..uBXsquVAsz7tm0MVm7.vgBmACew1xL7K', 'ROLE_ADMIN'),
('一般ユーザー1', 'example@miraino-katachi.co.jp', '$2a$10$i4Muik//i6mHteRh4nC..uBXsquVAsz7tm0MVm7.vgBmACew1xL7K', 'ROLE_GENERAL');

INSERT INTO post (
	user_id,
	category_id,
	slug,
	title,
	thumbnail,
	body
) VALUES
(1, 1, '202510010000', 'タイトル1', null, '本文1'),
(1, 2, '202510010100', 'タイトル2', null, '本文2'),
(1, 3, '202510010200', 'タイトル3', null, '本文3'),
(1, 4, '202510010300', 'タイトル4', null, '本文4'),
(2, 1, '202510010400', 'タイトル5', null, '本文5'),
(2, 2, '202510010500', 'タイトル6', null, '本文6'),
(2, 3, '202510010600', 'タイトル7', null, '本文7'),
(2, 4, '202510010700', 'タイトル8', null, '本文8');

INSERT INTO comment (
	user_id,
	post_id,
	body
) VALUES
(3, 1, 'コメント1'),
(1, 1, 'コメント2'),
(2, 1, 'コメント3'),
(2, 1, 'コメント4');
