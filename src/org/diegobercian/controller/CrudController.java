package org.diegobercian.controller;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.diegobercian.repository.CrudRepository;
import org.diegobercian.service.Validation;
import org.diegobercian.ui.Async;
import org.diegobercian.reportes.GenerarReportes;
import org.diegobercian.system.Main;

/** Shared CRUD workflow: immutable input snapshot, background persistence, UI commit on success. */
public abstract class CrudController {
    protected Main escenarioPrincipal;
    protected final CrudRepository repository = new CrudRepository();
    protected TableView<Map<String,Object>> table;
    private String tableName, key, addProcedure, updateProcedure, deleteProcedure, report;
    private String[] addOrder, updateOrder;
    private List<String> columns;
    private Field[] fields;
    private Button add, edit, remove, reports;
    private enum Mode { VIEW, ADD, EDIT }
    private Mode mode = Mode.VIEW;
    private Map<String,Object> selected;
    private boolean busy;
    private int generation;
    private Pagination pages;
    private final Button refresh = new Button("Actualizar datos / Reintentar");
    private static final int PAGE_SIZE = 100;

    public static final class Choice {
        public final Object id; private final String label;
        Choice(Object id, Object label) { this.id=id; this.label=id+" · "+String.valueOf(label); }
        @Override public String toString() { return label; }
    }
    protected static final class Field {
        final String column, type; final int length; final Control control; final boolean optional;
        String foreignTable, foreignKey, foreignLabel;
        final Map<String,Choice> choices = new HashMap<>();
        Field(String column, String type, int length, Control control, boolean optional) {
            this.column=column; this.type=type; this.length=length; this.control=control; this.optional=optional;
            control.setAccessibleText(column);
            if (control instanceof TextField) ((TextField)control).setPromptText(type.equals("date") ? "AAAA-MM-DD" : column);
        }
        Field lookup(String table, String key, String label) { foreignTable=table;foreignKey=key;foreignLabel=label;return this; }
    }
    protected void configure(TableView<Map<String,Object>> table, String tableName, String[] columns, Field[] fields,
            Button add, Button edit, Button remove, Button reports, String addProcedure, String updateProcedure,
            String deleteProcedure, String[] addOrder, String[] updateOrder, String report) {
        this.table=table;this.tableName=tableName;this.columns=Arrays.asList(columns);key=columns[0];this.fields=fields;
        this.add=add;this.edit=edit;this.remove=remove;this.reports=reports;
        this.addProcedure=addProcedure;this.updateProcedure=updateProcedure;this.deleteProcedure=deleteProcedure;
        this.addOrder=addOrder;this.updateOrder=updateOrder;this.report=report;
        table.setPlaceholder(new Label("Cargando datos…"));
        table.getSelectionModel().selectedItemProperty().addListener((o,old,value) -> seleccionarElemento());
        table.getSortOrder().clear();
        for (TableColumn<Map<String,Object>,?> col : table.getColumns()) col.setSortable(false);
        pages=new Pagination(1,0); pages.setMaxPageIndicatorCount(5);
        pages.currentPageIndexProperty().addListener((o,old,value) -> { if (!busy) cargarDatos(); });
        refresh.setOnAction(e -> cargarDatos());
        refresh.setAccessibleText("Actualizar datos y catálogos o reintentar conexión");
        // Keep the original screen coordinates while giving the table a compact paging footer.
        if (table.getParent() instanceof AnchorPane) {
            AnchorPane parent=(AnchorPane)table.getParent();
            double x=table.getLayoutX(), y=table.getLayoutY(), width=table.getPrefWidth(), height=table.getPrefHeight();
            parent.getChildren().remove(table);
            HBox footer=new HBox(12,refresh,pages); footer.setPrefHeight(45);
            VBox box=new VBox(4,table,footer);box.setLayoutX(x);box.setLayoutY(y);box.setPrefSize(width,height);
            table.setPrefHeight(height-50); table.setMinHeight(100); VBox.setVgrow(table,Priority.ALWAYS);
            parent.getChildren().add(box);
        }
        state(); cargarDatos();
    }
    protected void column(TableColumn<Map<String,Object>,Object> col, String property) {
        col.setCellValueFactory(row -> new ReadOnlyObjectWrapper<>(row.getValue().get(property)));
    }
    public Main getEscenarioPrincipal() { return escenarioPrincipal; }
    public void setEscenarioPrincipal(Main main) { escenarioPrincipal=main; }

    @FXML public void cargarDatos() {
        if (busy || mode != Mode.VIEW) return;
        final int request=++generation, offset=pages.getCurrentPageIndex()*PAGE_SIZE;
        setBusy(true);
        Async.run(() -> {
            Map<String,Object> result=new HashMap<>();
            long count=repository.count(tableName);
            int actualOffset=(int)Math.min(offset,Math.max(0,(count-1)/PAGE_SIZE)*PAGE_SIZE);
            result.put("count",count);
            result.put("page",actualOffset/PAGE_SIZE);
            result.put("rows",repository.page(tableName,key,columns,actualOffset,PAGE_SIZE));
            for (Field f:fields) if (f.foreignTable!=null) result.put(f.column,repository.query("SELECT "+f.foreignKey+","+f.foreignLabel+" FROM "+f.foreignTable+" ORDER BY "+f.foreignKey));
            return result;
        }, data -> {
            if (request!=generation) return;
            for (Field f:fields) if (f.foreignTable!=null) {
                f.choices.clear(); List<Choice> items=new ArrayList<>();
                for (Map<String,Object> row:(List<Map<String,Object>>)data.get(f.column)) {
                    Choice choice=new Choice(row.get(f.foreignKey),row.get(f.foreignLabel));
                    f.choices.put(String.valueOf(choice.id),choice); items.add(choice);
                }
                ((ComboBox<Choice>)f.control).setItems(FXCollections.observableArrayList(items));
            }
            table.setItems(FXCollections.observableArrayList((List<Map<String,Object>>)data.get("rows")));
            pages.setPageCount(Math.max(1,(int)(((Long)data.get("count")+PAGE_SIZE-1)/PAGE_SIZE)));
            pages.setCurrentPageIndex((Integer)data.get("page"));
            table.setPlaceholder(new Label("No hay registros"));
            setBusy(false);
        }, error -> { setBusy(false);table.setPlaceholder(new Label("No se cargaron los datos. Pulse Reintentar."));Async.error(error); });
    }
    @FXML public void seleccionarElemento() {
        if (mode!=Mode.VIEW || busy) return;
        selected=table.getSelectionModel().getSelectedItem();
        if (selected==null) return;
        for (Field f:fields) {
            Object value=selected.get(f.column);
            if (f.control instanceof TextField) ((TextField)f.control).setText(value==null?"":String.valueOf(value));
            else ((ComboBox<Choice>)f.control).getSelectionModel().select(f.choices.get(String.valueOf(value)));
        }
    }
    @FXML public void agregar() {
        if (busy) return;
        if (mode==Mode.ADD) { guardar(); return; }
        if (mode!=Mode.VIEW) return;
        mode=Mode.ADD;selected=null;limpiarControles();state();fields[0].control.requestFocus();
    }
    @FXML public void editar() {
        if (busy) return;
        if (mode==Mode.EDIT) { actualizar();return; }
        if (table.getSelectionModel().getSelectedItem()==null) { Async.error(new IllegalArgumentException("Seleccione un registro para editar."));return; }
        seleccionarElemento(); mode=Mode.EDIT;state();
    }
    private Map<String,Object> snapshot() {
        Map<String,Object> values=new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Field f:fields) {
            if (f.control instanceof ComboBox) {
                Choice choice=((ComboBox<Choice>)f.control).getValue();
                if (choice==null) throw new IllegalArgumentException(f.column+": seleccione una opción.");
                values.put(f.column,choice.id);
            } else values.put(f.column,Validation.parse(f.column,f.type,f.length,((TextField)f.control).getText(),f.optional));
        }
        return values;
    }
    @FXML public void guardar() { persist(false); }
    @FXML public void actualizar() { persist(true); }
    private void persist(boolean updating) {
        if (busy || mode!=(updating?Mode.EDIT:Mode.ADD)) return;
        try {
            Map<String,Object> values=snapshot();
            if (updating) values.put(key,selected.get(key));
            List<Object> params=new ArrayList<>();
            for (String column:updating?updateOrder:addOrder) params.add(values.containsKey(column)?values.get(column):selected.get(column));
            setBusy(true);
            Async.run(() -> { repository.call(updating?updateProcedure:addProcedure,params);return true; }, ok -> {
                mode=Mode.VIEW;selected=null;setBusy(false);limpiarControles();cargarDatos();
            }, error -> {setBusy(false);Async.error(error);});
        } catch (IllegalArgumentException e) { Async.error(e); }
    }
    @FXML public void eliminar() {
        if (busy) return;
        if (mode!=Mode.VIEW) { cancelar();return; }
        Map<String,Object> row=table.getSelectionModel().getSelectedItem();
        if (row==null) { Async.error(new IllegalArgumentException("Seleccione un registro para eliminar."));return; }
        Alert confirm=new Alert(Alert.AlertType.CONFIRMATION,"¿Eliminar el registro "+row.get(key)+"? Los registros con movimientos se conservan.",ButtonType.YES,ButtonType.NO);
        if (confirm.showAndWait().orElse(ButtonType.NO)!=ButtonType.YES) return;
        Object id=row.get(key);setBusy(true);
        Async.run(() -> { repository.call(deleteProcedure,Collections.singletonList(id));return true; }, ok -> {
            selected=null;setBusy(false);limpiarControles();cargarDatos();
        }, error -> {setBusy(false);Async.error(error);});
    }
    @FXML public void cancelar() { if (busy) return;mode=Mode.VIEW;selected=null;limpiarControles();state(); }
    @FXML public void reportes() {
        if (busy) return;
        if (mode!=Mode.VIEW) {cancelar();return;}
        if (report==null) {
            Alert info=new Alert(Alert.AlertType.INFORMATION,"Este módulo no dispone de un reporte. Consulte sus registros en la tabla.");
            info.setHeaderText("Reportes");info.show();return;
        }
        Map<String,Object> params=new HashMap<>();
        if ("Factura".equals(tableName)) {
            Map<String,Object> row=table.getSelectionModel().getSelectedItem();
            if (row==null) {Async.error(new IllegalArgumentException("Seleccione una factura para imprimir."));return;}
            params.put("numeroFactura",row.get(key));
        }
        GenerarReportes.mostrarReportes(report,tableName,params);
    }
    public void imprimirReporteClientes() { GenerarReportes.mostrarReportes("Reporte.jasper","Clientes",new HashMap<>()); }
    public void imprimirReporteProductos() { GenerarReportes.mostrarReportes("ProductosReporte.jasper","Productos",new HashMap<>()); }
    public void imprimirReporteProveedores() { GenerarReportes.mostrarReportes("ProveedoresFactura.jasper","Proveedores",new HashMap<>()); }
    public void imprimirReporteFacturas() {reportes();}
    public void limpiarControles() {
        for(Field f:fields) if(f.control instanceof TextField) ((TextField)f.control).clear();else ((ComboBox<?>)f.control).getSelectionModel().clearSelection();
        table.getSelectionModel().clearSelection();
    }
    private void setBusy(boolean value) {busy=value;state();}
    private void state() {
        table.setDisable(busy || mode!=Mode.VIEW);pages.setDisable(busy || mode!=Mode.VIEW);refresh.setDisable(busy || mode!=Mode.VIEW);
        add.setText(mode==Mode.ADD?"Guardar":"Agregar");edit.setText(mode==Mode.EDIT?"Actualizar":"Editar");
        remove.setText(mode!=Mode.VIEW?"Cancelar":"Eliminar");reports.setText(mode!=Mode.VIEW?"Cancelar":"Reportes");
        add.setDisable(busy || mode==Mode.EDIT);edit.setDisable(busy || mode==Mode.ADD);remove.setDisable(busy);reports.setDisable(busy);
        for (Field f:fields) {
            boolean enabled=!busy && mode!=Mode.VIEW && !(mode==Mode.EDIT && f.column.equalsIgnoreCase(key));
            if (f.control instanceof TextField) ((TextField)f.control).setEditable(enabled);else f.control.setDisable(!enabled);
        }
    }
}
