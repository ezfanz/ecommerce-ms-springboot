-- V1__insert_users.sql
-- This script inserts a sample user into the `users` table.

INSERT INTO user (
    username,
    password,
    email,
    first_name,
    last_name,
    phone_number,
    address,
    created_at,
    updated_at
) VALUES (
    'Irfan96',                             -- username
    'P@ssw0rd',                            -- password (bcrypt hashed for 'password')
    'irfanzulkefly144@gmail.com.com',      -- email
    'Irfan',                               -- first_name
    'Zulkefly',                             -- last_name
    '60193205891',                           -- phone_number
    'Clio 2 Residence @, Lbh IRC, Ioi Resort, 62502 Putrajaya',                      -- address
    NOW(),                                    -- created_at
    NOW()                                     -- updated_at
);
