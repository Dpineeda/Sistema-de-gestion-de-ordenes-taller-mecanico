
import 'package:flutter/material.dart';

import 'dart:convert';
import 'package:http/http.dart' as http;

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
  String busqueda = '';

  
List<Orden> ordenes = [];
bool cargando = false;
String? error;

Future<void> cargarOrdenes() async {
  setState(() {
    cargando = true;
    error = null;
  });

  try {
    final respuesta = await http.get(
      Uri.parse('http://localhost:8080/api/ordenes'),
    );

    if (respuesta.statusCode != 200) {
      throw Exception('Error HTTP ${respuesta.statusCode}');
    }

    final datos = jsonDecode(
      utf8.decode(respuesta.bodyBytes),
    ) as List<dynamic>;

    final lista = datos.map((dato) {
      return Orden(
        (dato['numero'] as num).toInt(),
        dato['placa'].toString(),
        dato['cliente'].toString(),
        dato['mecanico'].toString(),
        dato['problema'].toString(),
        dato['estado'].toString(),
      );
    }).toList();

    if (!mounted) return;

    setState(() {
      ordenes = lista;
      cargando = false;
    });
  } catch (e) {
    if (!mounted) return;

    setState(() {
      error = 'Error al consultar las órdenes: $e';
      cargando = false;
    });
  }
}


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
    
final visibles = ordenes.where((orden) {
  final coincideEstado =
      filtro == 'TODAS' || orden.estado == filtro;

  final texto = busqueda.trim().toLowerCase();

  final coincideBusqueda =
      orden.placa.toLowerCase().contains(texto) ||
      orden.cliente.toLowerCase().contains(texto) ||
      orden.mecanico.toLowerCase().contains(texto);

  return coincideEstado && coincideBusqueda;
}).toList();

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
          
ElevatedButton.icon(
  onPressed: cargando ? null : cargarOrdenes,
  icon: const Icon(Icons.refresh),
  label: const Text('Consultar órdenes de MySQL'),
),
if (cargando) const LinearProgressIndicator(),
if (error != null)
  Text(
    error!,
    style: const TextStyle(color: Colors.red),
  ),

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
          
TextField(
  decoration: InputDecoration(
    labelText: 'Buscar por placa, cliente o mecánico',
    prefixIcon: const Icon(Icons.search),
    border: OutlineInputBorder(
      borderRadius: BorderRadius.circular(12),
    ),
    filled: true,
    fillColor: Colors.white,
  ),
  onChanged: (valor) {
    setState(() {
      busqueda = valor;
    });
  },
),
const SizedBox(height: 12),

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
