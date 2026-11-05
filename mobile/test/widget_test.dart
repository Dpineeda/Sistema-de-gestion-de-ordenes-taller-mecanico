import 'package:flutter_test/flutter_test.dart';
import 'package:mobile/main.dart';

void main() {
  testWidgets('Verificar pantalla del taller', (WidgetTester tester) async {
    await tester.pumpWidget(const TallerApp());

    expect(find.text('El Último Arranque'), findsOneWidget);
    expect(find.text('Reporte de órdenes de trabajo'), findsOneWidget);
  });
}
