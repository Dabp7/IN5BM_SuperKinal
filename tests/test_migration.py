"""Migration regression against a disposable localhost MySQL server only."""
import argparse,pathlib,subprocess
p=argparse.ArgumentParser();p.add_argument('--mysql',required=True);p.add_argument('--port',required=True,type=int);args=p.parse_args()
if args.port==3306:raise SystemExit('Refusing default MySQL port; use the isolated runner')
root=pathlib.Path(__file__).resolve().parents[1]
cli=[args.mysql,'--no-defaults','-h127.0.0.1','-P'+str(args.port),'-uroot','--batch','--skip-column-names','--default-character-set=utf8mb4']
flags=getattr(subprocess,'CREATE_NO_WINDOW',0)
def run(sql,success=True):
 r=subprocess.run(cli,input=sql.encode('utf-8'),capture_output=True,creationflags=flags)
 if success and r.returncode:raise AssertionError(r.stderr.decode(errors='replace'))
 if not success and not r.returncode:raise AssertionError('Expected migration rejection')
 return r.stdout.decode('utf-8').strip()
name='migration_audit_test'
run(f'DROP DATABASE IF EXISTS {name}; CREATE DATABASE {name}; USE {name};\n'+(root/'tests/fixtures/legacy-schema.sql').read_text(encoding='utf-8'))
fixture="""
INSERT INTO Usuarios(ingresoUsuario,ingresoClave) VALUES ('legacy','old password');
INSERT INTO Clientes VALUES (1,'123','Nombre','Apellido','Direccion','12345678','test@example.com');
INSERT INTO Proveedores VALUES (1,'123','Proveedor','Apellido','Direccion','Razon','Contacto','');
INSERT INTO CargoEmpleado VALUES (1,'Cargo','Descripcion');
INSERT INTO TipoProducto VALUES (1,'Tipo');
INSERT INTO Empleados VALUES (1,'Nombre','Apellido',1000,'Direccion','Dia',1);
INSERT INTO Productos VALUES ('P1','Producto',14,13.5,12.5,'',7,1,1);
INSERT INTO Compras VALUES (1,'2026-10-20','Compra',999);
INSERT INTO DetalleCompra VALUES (1,10,10,'P1',1);
INSERT INTO Factura VALUES (1,'Abierta',999,'2026-10-20',1,1);
INSERT INTO DetalleFactura VALUES (1,14,3,1,'P1');
"""
run(f'USE {name};\n'+fixture)
migration=(root/'database/migrations/V002__auditoria.sql').read_text(encoding='utf-8').replace('DBKinalExpress2020527',name)
run(f"USE {name};INSERT INTO Usuarios(ingresoUsuario,ingresoClave) VALUES ('legacy','duplicate');")
run(migration,False)
assert run(f'USE {name}; SELECT COUNT(*) FROM Usuarios;')=='2'
assert run(f'USE {name}; SELECT totalFactura FROM Factura;')=='999.00'
run(f'USE {name};DELETE FROM Usuarios WHERE idUsuario=2;UPDATE Factura SET fechaFactura="2026-02-30";')
run(migration,False)
assert run(f'USE {name}; SELECT totalFactura FROM Factura;')=='999.00'
run(f'USE {name};UPDATE Factura SET fechaFactura="2026-10-20";')
run(migration)
assert run(f'USE {name}; SELECT totalDocumento FROM Compras;')=='100.00'
assert run(f'USE {name}; SELECT totalFactura FROM Factura;')=='42.00'
assert run(f'USE {name}; SELECT existencia FROM Productos;')=='7'
assert run(f'USE {name}; SELECT COUNT(*) FROM DetalleFactura;')=='1'
assert run(f'USE {name}; SELECT ingresoClave FROM Usuarios;')=='old password'
assert run(f'USE {name}; SELECT variacion FROM MovimientosInventario;')=='7'
run(f"USE {name};CALL sp_agregarDetalleFactura(2,1,1,'P1');")
assert run(f'USE {name}; SELECT existencia FROM Productos;')=='6'
assert run(f'USE {name}; SELECT totalFactura FROM Factura;')=='56.00'
run(f"USE {name};DELETE FROM Productos WHERE codigoProducto='P1';",False)
print('PASS: migration preserves rows, balances and historical prices; repairs totals; rejects duplicate users and invalid dates before altering data.')
