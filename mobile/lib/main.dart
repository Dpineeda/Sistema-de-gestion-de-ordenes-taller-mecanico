
import 'package:flutter/material.dart';

void main() {
  runApp(const TallerApp());
}

class TallerApp extends StatelessWidget {
  const TallerApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'El Último Arranque',
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF146C63)),
        scaffoldBackgroundColor: const Color(0xFFF2F5F7),
      ),
      home: const ReporteOrdenes(),
    );
  }
}

class Orden {
  final int numero;
  final String placa;
  final String cliente;
  final String mecanico;
  final String problema;
  final String estado;

  const Orden(this.numero, this.placa, this.cliente, this.mecanico,
      this.problema, this.estado);
}

class ReporteOrdenes extends StatefulWidget {
  const ReporteOrdenes({super.key});

  @override
  State<ReporteOrdenes> createState() => _ReporteOrdenesState();
}

class _ReporteOrdenesState extends State<ReporteOrdenes> {
  String filtro = 'TODAS';

  final List<Orden> ordenes = [
    const Orden(101, 'P-123ABC', 'Ana López', 'Carlos Pérez',
        'Cambio de aceite y filtros', 'PENDIENTE'),
    const Orden(102, 'P-456DEF', 'José Ramírez', 'María García',
        'Revisión de frenos', 'EN PROCESO'),
    const Orden(103, 'P-789GHI', 'Luisa Morales', 'Carlos Pérez',
        'Alineación y balanceo', 'FINALIZADA'),
    const Orden(104, 'P-321JKL', 'Pedro Castillo', 'María García',
        'Diagnóstico de motor', 'PENDIENTE'),
  ];

  int contar(String estado) {
    return ordenes.where((orden) => orden.estado == estado).length;
  }

  Color colorEstado(String estado) {
    if (estado == 'PENDIENTE') return const Color(0xFFAB6814);
    if (estado == 'EN PROCESO') return const Color(0xFF1769AA);
    if (estado == 'FINALIZADA') return const Color(0xFF16815B);
    return const Color(0xFF475569);
  }

  @override
  Widget build(BuildContext context) {
    final visibles = filtro == 'TODAS'
        ? ordenes
        : ordenes.where((orden) => orden.estado == filtro).toList();

    return Scaffold(
      appBar: AppBar(
        title: const Text('El Último Arranque'),
        backgroundColor: const Color(0xFF146C63),
        foregroundColor: Colors.white,
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          const Text('Reporte de órdenes de trabajo',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
          const SizedBox(height: 6),
          const Text('Consulta las reparaciones del taller.'),
          const SizedBox(height: 10),
          const Text('DATOS DE PRUEBA - Sin conexión a MySQL todavía',
              style: TextStyle(color: Color(0xFF9B5D15), fontSize: 12)),
          const SizedBox(height: 22),
          Wrap(
            spacing: 10,
            runSpacing: 10,
            children: [
              resumen('Total', ordenes.length, const Color(0xFF334155)),
              resumen('Pendientes', contar('PENDIENTE'), colorEstado('PENDIENTE')),
              resumen('En proceso', contar('EN PROCESO'), colorEstado('EN PROCESO')),
              resumen('Finalizadas', contar('FINALIZADA'), colorEstado('FINALIZADA')),
            ],
          ),
          const SizedBox(height: 24),
          const Text('Filtrar por estado',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
          const SizedBox(height: 8),
          Wrap(
            spacing: 8,
            children: ['TODAS', 'PENDIENTE', 'EN PROCESO', 'FINALIZADA', 'CANCELADA']
                .map((estado) => ChoiceChip(
                      label: Text(estado),
                      selected: filtro == estado,
                      onSelected: (_) {
                        setState(() {
                          filtro = estado;
                        });
                      },
                    ))
                .toList(),
          ),
          const SizedBox(height: 16),
          Text('${visibles.length} órdenes encontradas'),
          const SizedBox(height: 8),
          if (visibles.isEmpty)
            const Card(
              child: Padding(
                padding: EdgeInsets.all(24),
                child: Text('No hay órdenes para este estado.'),
              ),
            ),
          for (final orden in visibles) tarjetaOrden(orden),
        ],
      ),
    );
  }

  Widget resumen(String titulo, int cantidad, Color color) {
    return Container(
      width: 145,
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(14),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(titulo, style: const TextStyle(color: Colors.black54)),
          const SizedBox(height: 8),
          Text('$cantidad',
              style: TextStyle(
                  fontSize: 28, fontWeight: FontWeight.bold, color: color)),
        ],
      ),
    );
  }

  Widget tarjetaOrden(Orden orden) {
    final color = colorEstado(orden.estado);
    return Card(
      color: Colors.white,
      margin: const EdgeInsets.only(bottom: 10),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Orden #${orden.numero}',
                style: const TextStyle(
                    fontSize: 18, fontWeight: FontWeight.bold)),
            const SizedBox(height: 5),
            Text(orden.estado,
                style: TextStyle(color: color, fontWeight: FontWeight.bold)),
            const Divider(height: 20),
            Text('Placa: ${orden.placa}'),
            Text('Cliente: ${orden.cliente}'),
            Text('Mecánico: ${orden.mecanico}'),
            Text('Problema: ${orden.problema}'),
          ],
        ),
      ),
    );
  }
}
