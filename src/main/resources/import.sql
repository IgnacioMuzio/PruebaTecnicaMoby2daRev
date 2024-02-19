INSERT INTO candidates (id,first_name,last_name,document_type,document_number,birth_date) VALUES (1,'Nacho','Muzio','DNI',42678588,"2000-06-15");
INSERT INTO candidates (id,first_name,last_name,document_type,document_number,birth_date) VALUES (2,'Ignacio','Romano','LC',42678588,"2000-06-15");
INSERT INTO technologies (id,name,version) VALUES (1,'java','21');
INSERT INTO technologies (id,name,version) VALUES (2,'java','17');
INSERT INTO candidate_technology (id,candidate_id,technology_id,years_of_experience) VALUES (1,1,1,3);
INSERT INTO candidate_technology (id,candidate_id,technology_id,years_of_experience) VALUES (2,2,2,2);