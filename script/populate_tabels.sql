INSERT INTO hse.person(name, surname)
SELECT 'name2', 'surname2'
FROM generate_series(0, 1000);
​
INSERT INTO hse.student(phone)
SELECT k % 2 = 0
FROM generate_series(0, 1000) AS k;

update hse.person
set passport = md5(random()::varchar)
where phone is null;

update hse.person
set phone = (floor(random() * 80000000000 + 9999999999)::bigint)::varchar
where passport is not null;

update hse.person
set passport = md5(random()::varchar)
where phone is null;

update hse.person
set phone = (floor(random() * 80000000000 + 9999999999)::bigint)::varchar
where passport is not null;