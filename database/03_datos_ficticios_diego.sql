-- Archivo convertido del PDF "Datos ficticios de ejemplo para db.pdf" de Diego.
-- Datos de DEMOSTRACION (NO datos reales de clientes).
-- NO borra tablas ni registros previos.
-- ADVERTENCIA: datos existentes con la misma clave o placa no se reemplazan.
-- En particular, P-123ABC puede existir ya en tu base local con datos distintos.
-- Este archivo NO incluye INSERT INTO orden; conserva tus ordenes actuales.
-- Cargar este mismo archivo en dos computadoras NO sincroniza sus bases en vivo.
-- Ejecutar despues de crear las nueve tablas de db_taller.

-- Datos ficticios de ejemplo para db_taller.
-- Ejecutar despues de crear las tablas. Las comprobaciones evitan duplicar
-- estos registros si el script se ejecuta mas de una vez.
USE db_taller;
START TRANSACTION;
INSERT INTO cliente (id_cliente, nombre, telefono, correo)
SELECT 'C001', 'Ana Lopez', '55501001', 'ana.lopez@example.com'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C001');
INSERT INTO cliente (id_cliente, nombre, telefono, correo)
SELECT 'C002', 'Carlos Mendez', '55501002', 'carlos.mendez@example.com'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C002');
INSERT INTO cliente (id_cliente, nombre, telefono, correo)
SELECT 'C003', 'Maria Garcia', '55501003', 'maria.garcia@example.com'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C003');
INSERT INTO cliente (id_cliente, nombre, telefono, correo)
SELECT 'C004', 'Jose Ramirez', '55501004', 'jose.ramirez@example.com'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C004');
INSERT INTO cliente (id_cliente, nombre, telefono, correo)
SELECT 'C005', 'Sofia Castillo', '55501005', 'sofia.castillo@example.com'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C005');
INSERT INTO cliente (id_cliente, nombre, telefono, correo)
SELECT 'C006', 'Diego Morales', '55501006', 'diego.morales@example.com'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C006');
INSERT INTO cliente (id_cliente, nombre, telefono, correo)
SELECT 'C007', 'Lucia Herrera', '55501007', 'lucia.herrera@example.com'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C007');
INSERT INTO cliente (id_cliente, nombre, telefono, correo)
SELECT 'C008', 'Andres Gomez', '55501008', 'andres.gomez@example.com'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C008');
INSERT INTO cliente (id_cliente, nombre, telefono, correo)
SELECT 'C009', 'Valeria Diaz', '55501009', 'valeria.diaz@example.com'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C009');
INSERT INTO cliente (id_cliente, nombre, telefono, correo)
SELECT 'C010', 'Miguel Torres', '55501010', 'miguel.torres@example.com'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C010');
INSERT INTO mecanico (nombre, telefono)
SELECT 'Roberto Aguilar', '55502001'
WHERE NOT EXISTS (SELECT 1 FROM mecanico WHERE nombre = 'Roberto Aguilar' AND telefono =
'55502001');
INSERT INTO mecanico (nombre, telefono)
SELECT 'Elena Fuentes', '55502002'
WHERE NOT EXISTS (SELECT 1 FROM mecanico WHERE nombre = 'Elena Fuentes' AND telefono =
'55502002');
INSERT INTO mecanico (nombre, telefono)
SELECT 'Mario Pineda', '55502003'
WHERE NOT EXISTS (SELECT 1 FROM mecanico WHERE nombre = 'Mario Pineda' AND telefono =
'55502003');
INSERT INTO mecanico (nombre, telefono)
SELECT 'Patricia Reyes', '55502004'
WHERE NOT EXISTS (SELECT 1 FROM mecanico WHERE nombre = 'Patricia Reyes' AND telefono =
'55502004');
INSERT INTO mecanico (nombre, telefono)
SELECT 'Fernando Cifuentes', '55502005'
WHERE NOT EXISTS (SELECT 1 FROM mecanico WHERE nombre = 'Fernando Cifuentes' AND
telefono = '55502005');
INSERT INTO mecanico (nombre, telefono)
SELECT 'Gabriela Soto', '55502006'
WHERE NOT EXISTS (SELECT 1 FROM mecanico WHERE nombre = 'Gabriela Soto' AND telefono =
'55502006');
INSERT INTO mecanico (nombre, telefono)
SELECT 'Oscar Velasquez', '55502007'
WHERE NOT EXISTS (SELECT 1 FROM mecanico WHERE nombre = 'Oscar Velasquez' AND telefono =
'55502007');
INSERT INTO mecanico (nombre, telefono)
SELECT 'Carmen Barrios', '55502008'
WHERE NOT EXISTS (SELECT 1 FROM mecanico WHERE nombre = 'Carmen Barrios' AND telefono =
'55502008');
INSERT INTO mecanico (nombre, telefono)
SELECT 'Julio Alvarado', '55502009'
WHERE NOT EXISTS (SELECT 1 FROM mecanico WHERE nombre = 'Julio Alvarado' AND telefono =
'55502009');
INSERT INTO mecanico (nombre, telefono)
SELECT 'Daniela Estrada', '55502010'
WHERE NOT EXISTS (SELECT 1 FROM mecanico WHERE nombre = 'Daniela Estrada' AND telefono =
'55502010');
INSERT INTO repuesto (nombre, descripcion, precio, existencia, minimo_en_stock)
SELECT 'Filtro de aceite', 'Filtro de aceite para motor', 45.00, 20, 5
WHERE NOT EXISTS (SELECT 1 FROM repuesto WHERE nombre = 'Filtro de aceite');
INSERT INTO repuesto (nombre, descripcion, precio, existencia, minimo_en_stock)
SELECT 'Filtro de aire', 'Filtro de aire para motor', 85.00, 15, 4
WHERE NOT EXISTS (SELECT 1 FROM repuesto WHERE nombre = 'Filtro de aire');
INSERT INTO repuesto (nombre, descripcion, precio, existencia, minimo_en_stock)
SELECT 'Bujia', 'Bujia estandar para motor a gasolina', 35.00, 40, 8
WHERE NOT EXISTS (SELECT 1 FROM repuesto WHERE nombre = 'Bujia');
INSERT INTO repuesto (nombre, descripcion, precio, existencia, minimo_en_stock)
SELECT 'Pastillas de freno delanteras', 'Juego de pastillas para eje delantero', 320.00, 10, 3
WHERE NOT EXISTS (SELECT 1 FROM repuesto WHERE nombre = 'Pastillas de freno delanteras');
INSERT INTO repuesto (nombre, descripcion, precio, existencia, minimo_en_stock)
SELECT 'Aceite 10W-30', 'Aceite para motor, envase de un litro', 55.00, 30, 8
WHERE NOT EXISTS (SELECT 1 FROM repuesto WHERE nombre = 'Aceite 10W-30');
INSERT INTO repuesto (nombre, descripcion, precio, existencia, minimo_en_stock)
SELECT 'Refrigerante', 'Refrigerante para sistema de enfriamiento, un litro', 40.00, 18, 5
WHERE NOT EXISTS (SELECT 1 FROM repuesto WHERE nombre = 'Refrigerante');
INSERT INTO repuesto (nombre, descripcion, precio, existencia, minimo_en_stock)
SELECT 'Bateria 12V', 'Bateria automotriz de 12 voltios', 950.00, 6, 2
WHERE NOT EXISTS (SELECT 1 FROM repuesto WHERE nombre = 'Bateria 12V');
INSERT INTO repuesto (nombre, descripcion, precio, existencia, minimo_en_stock)
SELECT 'Correa de accesorios', 'Correa para accesorios del motor', 180.00, 8, 3
WHERE NOT EXISTS (SELECT 1 FROM repuesto WHERE nombre = 'Correa de accesorios');
INSERT INTO repuesto (nombre, descripcion, precio, existencia, minimo_en_stock)
SELECT 'Liquido de frenos', 'Liquido de frenos DOT 3, envase de medio litro', 60.00, 12, 4
WHERE NOT EXISTS (SELECT 1 FROM repuesto WHERE nombre = 'Liquido de frenos');
INSERT INTO repuesto (nombre, descripcion, precio, existencia, minimo_en_stock)
SELECT 'Plumillas limpiaparabrisas', 'Par de plumillas limpiaparabrisas', 110.00, 9, 3
WHERE NOT EXISTS (SELECT 1 FROM repuesto WHERE nombre = 'Plumillas limpiaparabrisas');
INSERT INTO servicio (descripcion, costo_manodeobra)
SELECT 'Cambio de aceite y filtro', 100.00
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE descripcion = 'Cambio de aceite y filtro');
INSERT INTO servicio (descripcion, costo_manodeobra)
SELECT 'Diagnostico general', 150.00
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE descripcion = 'Diagnostico general');
INSERT INTO servicio (descripcion, costo_manodeobra)
SELECT 'Afinacion de motor', 350.00
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE descripcion = 'Afinacion de motor');
INSERT INTO servicio (descripcion, costo_manodeobra)
SELECT 'Revision de frenos', 180.00
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE descripcion = 'Revision de frenos');
INSERT INTO servicio (descripcion, costo_manodeobra)
SELECT 'Cambio de pastillas de freno', 250.00
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE descripcion = 'Cambio de pastillas de freno');
INSERT INTO servicio (descripcion, costo_manodeobra)
SELECT 'Alineacion y balanceo', 300.00
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE descripcion = 'Alineacion y balanceo');
INSERT INTO servicio (descripcion, costo_manodeobra)
SELECT 'Revision del sistema de enfriamiento', 200.00
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE descripcion = 'Revision del sistema de enfriamiento');
INSERT INTO servicio (descripcion, costo_manodeobra)
SELECT 'Cambio de bateria', 100.00
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE descripcion = 'Cambio de bateria');
INSERT INTO servicio (descripcion, costo_manodeobra)
SELECT 'Reparacion de suspension', 500.00
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE descripcion = 'Reparacion de suspension');
INSERT INTO servicio (descripcion, costo_manodeobra)
SELECT 'Revision de sistema electrico', 250.00
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE descripcion = 'Revision de sistema electrico');
INSERT INTO vehiculo (placa, marca, modelo, anio, color, id_cliente)
SELECT 'P-123ABC', 'Toyota', 'Corolla', 2018, 'Blanco', 'C001'
WHERE NOT EXISTS (SELECT 1 FROM vehiculo WHERE placa = 'P-123ABC')
AND EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C001');
INSERT INTO vehiculo (placa, marca, modelo, anio, color, id_cliente)
SELECT 'P-234BCD', 'Honda', 'Civic', 2020, 'Gris', 'C002'
WHERE NOT EXISTS (SELECT 1 FROM vehiculo WHERE placa = 'P-234BCD')
AND EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C002');
INSERT INTO vehiculo (placa, marca, modelo, anio, color, id_cliente)
SELECT 'P-345CDE', 'Mazda', '3', 2017, 'Rojo', 'C003'
WHERE NOT EXISTS (SELECT 1 FROM vehiculo WHERE placa = 'P-345CDE')
AND EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C003');
INSERT INTO vehiculo (placa, marca, modelo, anio, color, id_cliente)
SELECT 'P-456DEF', 'Hyundai', 'Tucson', 2021, 'Negro', 'C004'
WHERE NOT EXISTS (SELECT 1 FROM vehiculo WHERE placa = 'P-456DEF')
AND EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C004');
INSERT INTO vehiculo (placa, marca, modelo, anio, color, id_cliente)
SELECT 'P-567EFG', 'Kia', 'Sportage', 2019, 'Azul', 'C005'
WHERE NOT EXISTS (SELECT 1 FROM vehiculo WHERE placa = 'P-567EFG')
AND EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C005');
INSERT INTO vehiculo (placa, marca, modelo, anio, color, id_cliente)
SELECT 'P-678FGH', 'Nissan', 'Sentra', 2016, 'Plateado', 'C006'
WHERE NOT EXISTS (SELECT 1 FROM vehiculo WHERE placa = 'P-678FGH')
AND EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C006');
INSERT INTO vehiculo (placa, marca, modelo, anio, color, id_cliente)
SELECT 'P-789GHJ', 'Mitsubishi', 'Lancer', 2015, 'Verde', 'C007'
WHERE NOT EXISTS (SELECT 1 FROM vehiculo WHERE placa = 'P-789GHJ')
AND EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C007');
INSERT INTO vehiculo (placa, marca, modelo, anio, color, id_cliente)
SELECT 'P-890HJK', 'Ford', 'Ranger', 2022, 'Blanco', 'C008'
WHERE NOT EXISTS (SELECT 1 FROM vehiculo WHERE placa = 'P-890HJK')
AND EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C008');
INSERT INTO vehiculo (placa, marca, modelo, anio, color, id_cliente)
SELECT 'P-901JKL', 'Chevrolet', 'Spark', 2014, 'Amarillo', 'C009'
WHERE NOT EXISTS (SELECT 1 FROM vehiculo WHERE placa = 'P-901JKL')
AND EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C009');
INSERT INTO vehiculo (placa, marca, modelo, anio, color, id_cliente)
SELECT 'P-012KLM', 'Volkswagen', 'Jetta', 2023, 'Gris', 'C010'
WHERE NOT EXISTS (SELECT 1 FROM vehiculo WHERE placa = 'P-012KLM')
AND EXISTS (SELECT 1 FROM cliente WHERE id_cliente = 'C010');
COMMIT;

-- Comprobacion de registros tras ejecutar el archivo:
SELECT 'cliente' AS tabla, COUNT(*) AS total FROM cliente
UNION ALL SELECT 'mecanico', COUNT(*) FROM mecanico
UNION ALL SELECT 'repuesto', COUNT(*) FROM repuesto
UNION ALL SELECT 'servicio', COUNT(*) FROM servicio
UNION ALL SELECT 'vehiculo', COUNT(*) FROM vehiculo
UNION ALL SELECT 'orden', COUNT(*) FROM orden;

-- Verifica la placa que ya utilizaste en tus tres ordenes de ejemplo.
SELECT v.placa, v.marca, v.modelo, v.anio, v.color, c.id_cliente, c.nombre AS cliente
FROM vehiculo v JOIN cliente c ON c.id_cliente = v.id_cliente
WHERE v.placa = 'P-123ABC';
