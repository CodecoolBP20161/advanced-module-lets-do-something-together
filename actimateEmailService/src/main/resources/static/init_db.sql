CREATE TABLE IF NOT EXISTS email_address
(
  id SERIAL PRIMARY KEY,
  email TEXT,
  remoteId INTEGER ,
  sent BOOLEAN
);