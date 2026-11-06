USE db_taller;

SELECT 'cliente' AS tabla, COUNT(*) AS total FROM cliente
UNION ALL SELECT 'mecanico', COUNT(*) FROM mecanico
UNION ALL SELECT 'repuesto', COUNT(*) FROM repuesto
UNION ALL SELECT 'servicio', COUNT(*) FROM servicio
UNION ALL SELECT 'vehiculo', COUNT(*) FROM vehiculo
UNION ALL SELECT 'orden', COUNT(*) FROM orden;

-- Consulta que utiliza la API REST del reporte de ordenes.
SELECT o.id_orden AS numero, v.placa, c.nombre AS cliente,
       m.nombre AS mecanico, o.descripcion_problema AS problema, o.estado
FROM orden o
JOIN vehiculo v ON o.id_vehiculo = v.id_vehiculo
JOIN cliente c ON v.id_cliente = c.id_cliente
JOIN mecanico m ON o.id_mecanico = m.id_mecanico
ORDER BY o.id_orden;
