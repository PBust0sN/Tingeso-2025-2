INSERT INTO client (client_id, avaliable, last_name, name, mail, phone_number, state, rut) VALUES
(1, TRUE, 'Gomez', 'Agustin', 'gomez.agustin@gmail.com', '933442211', 'activo', '21656345-2'),
(2, TRUE, 'Hernandez', 'Benajmin', 'hernandez.benja@gmail', '976541234', 'activo', '22567123-6'),
(3, TRUE, 'Peralta', 'Pablo', 'peralta.pablo@gmail.com', '978643531', 'activo', '12987876-2'),
(4, TRUE, 'Ramos', 'Diego', 'ramos.diego@gmail.com', '934231298', 'activo', '21453289-5'),
(5, FALSE, 'Villalobos', 'Manuel', 'villalobos.manuel@gmail.com', '923451234', 'activo', '17987345-K'),
(6, FALSE, 'Moreno', 'Daniel', 'moreno.daniel@gmail.com', '910293847', 'restringido', '98564312-4'),
(7, FALSE, 'Jimenez', 'Juan', 'jimenez.juan@gmail.com', '909347612', 'restringido', '25657321-8');

INSERT INTO staff(staff_id, staff_mail, staff_name, staff_rut, password) VALUES
(1, 'martinez.gonzalo@gmail.com', 'Gonzalo Martinez', '8456231-7', 'DameElCodigo23'),
(2, 'cavieres.fran@gmail.com', 'Francisca Cavieres', '34234987-4', 'Linux123'),
(3 ,'ortiz.marco@gmail.com', 'Marco Ortiz', '12765456-2', 'WindowsEnyoyer');

INSERT INTO loans(loan_id, client_id, staff_id, loan_type, date, delivery_date, return_date, amount, extra_charges) VALUES
(1, 1, 2, 'loan', '2025-08-25', '2025-09-25', '2025-09-09', 5000, 0),
(2, 2, 2, 'loan', '2025-07-23', '2025-07-23', '2025-08-02', 3000, 0),
(3, 3, 3, 'loan', '2025-07-31', '2025-07-31', '2025-08-15', 5000,0),
(4, 2, 3, 'return', '2025-08-17', '2025-08-17', '2025-08-17', 0,0);

-- Disponible, Prestada, En reparación, Dada de baja, (INITIAL STATES)
-- REPOSITION VALUES ARE IN CLP
INSERT INTO tools(tool_id, category, tool_name, initial_state, disponibility, reposition_fee, loan_fee, diary_fine_fee, stock, low_dmg_fee, loan_count) VALUES
(1, 'Herramientas manuales', 'Martillo', 'Disponible', 'Disponible', 8000, 1500, 300, 25, 200, 0),
(2, 'Herramientas manuales', 'Destornillador Plano', 'Disponible', 'Disponible', 6000, 1200, 200, 30, 150, 0),
(3, 'Herramientas manuales', 'Destornillador Phillips', 'Disponible', 'Disponible', 6000, 1200, 200, 30, 150, 0),
(4, 'Herramientas manuales', 'Llave inglesa', 'Disponible', 'Disponible', 9000, 1800, 300, 20, 250, 0),
(5, 'Herramientas manuales', 'Juego de llaves Allen', 'Disponible', 'Disponible', 7000, 1500, 250, 18, 200, 0),
(6, 'Herramientas manuales', 'Alicate universal', 'Disponible', 'Disponible', 8000, 1600, 300, 22, 220, 0),
(7, 'Herramientas manuales', 'Sierra manual', 'Disponible', 'Disponible', 10000, 2000, 350, 12, 300, 0),
(8, 'Herramientas manuales', 'Cinta métrica', 'Disponible', 'Disponible', 5000, 800, 150, 40, 100, 0),
(9, 'Electroportátiles', 'Taladro percutor', 'Disponible', 'Disponible', 45000, 8000, 1500, 8, 1200, 0),
(10, 'Electroportátiles', 'Atornillador eléctrico', 'Disponible', 'Disponible', 35000, 6000, 1200, 10, 1000, 0),
(11, 'Electroportátiles', 'Amoladora angular', 'Disponible', 'Disponible', 35000, 7000, 1200, 9, 1100, 0),
(12, 'Electroportátiles', 'Sierra circular', 'Disponible', 'Disponible', 50000, 9000, 1800, 6, 1400, 0),
(13, 'Electroportátiles', 'Sierra de calar (jigsaw)', 'Disponible', 'Disponible', 32000, 6000, 1000, 10, 900, 0),
(14, 'Electroportátiles', 'Lijadora orbital', 'Disponible', 'Disponible', 30000, 6000, 900, 11, 800, 0),
(15, 'Electroportátiles', 'Rotomartillo', 'Disponible', 'Disponible', 70000, 10000, 2000, 5, 1800, 0),
(16, 'Jardinería', 'Cortacésped', 'Disponible', 'Disponible', 120000, 15000, 2500, 3, 3000, 0),
(17, 'Jardinería', 'Desbrozadora', 'Disponible', 'Disponible', 80000, 10000, 1800, 4, 2200, 0),
(18, 'Jardinería', 'Motosierra', 'Disponible', 'Disponible', 90000, 12000, 2000, 5, 2400, 0),
(19, 'Jardinería', 'Podadora de tijera', 'Disponible', 'Disponible', 25000, 4000, 600, 15, 500, 0),
(20, 'Jardinería', 'Soplador / Aspirador de hojas', 'Disponible', 'Disponible', 30000, 5000, 700, 7, 700, 0),
(21, 'Medición y nivelación', 'Nivel láser', 'Disponible', 'Disponible', 40000, 7000, 1000, 6, 1200, 0),
(22, 'Medición y nivelación', 'Multímetro digital', 'Disponible', 'Disponible', 15000, 3000, 500, 20, 400, 0),
(23, 'Medición y nivelación', 'Cinta métrica profesional', 'Disponible', 'Disponible', 8000, 1200, 200, 35, 150, 0),
(24, 'Medición y nivelación', 'Calibrador vernier', 'Disponible', 'Disponible', 12000, 2000, 300, 14, 250, 0),
(25, 'Construcción y elevación', 'Compactadora / Gimpy', 'Disponible', 'Disponible', 100000, 20000, 3000, 2, 3500, 0),
(26, 'Construcción y elevación', 'Generador portátil', 'Disponible', 'Disponible', 200000, 25000, 4000, 2, 5000, 0),
(27, 'Construcción y elevación', 'Andamio modular (unidad)', 'Disponible', 'Disponible', 50000, 8000, 1200, 10, 1500, 0),
(28, 'Construcción y elevación', 'Carretilla', 'Disponible', 'Disponible', 15000, 3000, 400, 25, 300, 0),
(29, 'Construcción y elevación', 'Plataforma elevadora pequeña', 'Disponible', 'Disponible', 250000, 30000, 5000, 1, 6000, 0),
(30, 'Especializadas', 'Soldadora MIG/MMA', 'Disponible', 'Disponible', 180000, 22000, 3500, 3, 4000, 0),
(31, 'Especializadas', 'Compresor de aire portátil', 'Disponible', 'Disponible', 140000, 18000, 3000, 4, 3200, 0);
INSERT INTO tools_loans(id, tool_id, loan_id) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 1, 2),
(4, 4, 3),
(5, 5, 4);

-- record_types (registro nuevas herramientas, préstamo, devolución, baja, reparación)
INSERT INTO records(record_id, client_id, loan_id, tool_id, record_date, record_amount, record_type) VALUES
(1, 1, 1, NULL,'2025-08-25', 5000, 'loan'),
(2, 2, 2, NULL,'2025-07-23', 3000, 'loan'),
(3, 3, 3, NULL,'2025-07-31', 5000, 'loan'),
(4, 2, 4, NULL,'2025-08-17', 0, 'return');

INSERT INTO client_loans(id_client_loans, client_id, loan_id) VALUES
(1,1, 1),
(2, 2, 2),
(3, 2,4),
(4, 3,3);

INSERT INTO fine(fine_id, loan_id, client_id, amount, type, state, date) VALUES
(1, NULL, 1, 3000, 'dmg fine', 'pendiente', '2025-04-23'),
(2, NULL, 3, 5000, 'dmg fine', 'pendiente', '2025-09-14');