package vista;

import dao.DepartamentoDAO;
import dao.EspacioDAO;
import dao.ProfesorDAO;
import dao.ReservaDAO;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modelo.*;
import servicio.CentroEducativo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class MainFX extends Application {
    private final BorderPane root = new BorderPane();
    private CentroEducativo centro;

    static void main(String[] args){ launch(args); }

    @Override public void start(Stage stage){

        try {

            centro = new CentroEducativo(
                    new DepartamentoDAO(),
                    new ProfesorDAO(),
                    new EspacioDAO(),
                    new ReservaDAO()
            );

            root.setTop(crearMenu());
            root.setCenter(pantallaInicio());

            stage.setScene(new Scene(root, 1100, 680));

            stage.setTitle(
                    "Gestión de reservas - Centro Educativo"
            );

            stage.show();

        } catch (SQLException | IOException e) {

            mostrarError(
                    "Error de conexión",
                    "No se pudo iniciar la aplicación:\n"
                            + e.getMessage()
            );
        }

    }

    @Override
    public void stop() throws SQLException {
        if (centro.getConexion() != null) {
            centro.getConexion().close();
        }
    }

    private MenuBar crearMenu(){
        Menu archivo=new Menu("Archivo");
        MenuItem salir=new MenuItem("Salir"); salir.setOnAction(e->System.exit(0)); archivo.getItems().addAll(salir);

        Menu dep=new Menu("Departamentos");
        MenuItem ldep=new MenuItem("Listar"); ldep.setOnAction(e->root.setCenter(pantallaDepartamentos()));
        MenuItem ndep=new MenuItem("Nuevo"); ndep.setOnAction(e->root.setCenter(pantallaAltaDepartamento())); dep.getItems().addAll(ldep,ndep);

        Menu prof=new Menu("Profesores");
        MenuItem lprof=new MenuItem("Listar"); lprof.setOnAction(e->root.setCenter(pantallaProfesores()));
        MenuItem nprof=new MenuItem("Nuevo"); nprof.setOnAction(e->root.setCenter(pantallaAltaProfesor())); prof.getItems().addAll(lprof,nprof);

        Menu esp=new Menu("Espacios");
        MenuItem lesp=new MenuItem("Listar"); lesp.setOnAction(e->root.setCenter(pantallaEspacios()));
        MenuItem nesp=new MenuItem("Nuevo"); nesp.setOnAction(e->root.setCenter(pantallaAltaEspacio())); esp.getItems().addAll(lesp,nesp);

        Menu res=new Menu("Reservas");
        MenuItem lres=new MenuItem("Listar"); lres.setOnAction(e->root.setCenter(pantallaReservas()));
        MenuItem nres=new MenuItem("Nueva reserva"); nres.setOnAction(e->root.setCenter(pantallaNuevaReserva())); res.getItems().addAll(lres,nres);
        return new MenuBar(archivo,dep,prof,esp,res);
    }

    private VBox pantallaInicio(){
        VBox box=new VBox(12); box.setPadding(new Insets(30));
        Label t=new Label("GESTIÓN DE RESERVAS"); t.setStyle("-fx-font-size:28px;-fx-font-weight:bold;");
        Label s=new Label("Fuente de datos: Base de Datos MariaSQL"); s.setStyle("-fx-font-size:16px;");
        box.getChildren().addAll(t,s,new Label("Utilice el menú para gestionar departamentos, profesores, espacios y reservas."));
        return box;
    }

    private VBox pantallaDepartamentos(){
        TableView<Departamento> tabla=new TableView<>();
        TableColumn<Departamento,String> id=new TableColumn<>("ID"); id.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getIdDepartamento()));
        TableColumn<Departamento,String> nombre=new TableColumn<>("Nombre"); nombre.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getNombre()));
        tabla.getColumns().addAll(id,nombre); cargar(tabla,centro.getDepartamentos());
        Button modificar=new Button("Modificar seleccionado");
        modificar.setOnAction(e->{Departamento d=tabla.getSelectionModel().getSelectedItem(); if(d==null){mostrarError("Aviso","Seleccione un departamento.");return;} try{root.setCenter(pantallaModificarDepartamento(d));}catch(Exception ex){mostrarError("No se puede modificar",ex.getMessage());}});
        Button eliminar=new Button("Eliminar seleccionado");
        eliminar.setOnAction(e->{ Departamento d=tabla.getSelectionModel().getSelectedItem(); if(d==null){mostrarError("Aviso","Seleccione un departamento.");return;} try{centro.bajaDepartamento(d.getIdDepartamento());cargar(tabla,centro.getDepartamentos());}catch(Exception ex){mostrarError("No se puede eliminar",ex.getMessage());}});
        return contenedor("DEPARTAMENTOS",tabla,modificar,eliminar);
    }

    private VBox pantallaAltaDepartamento(){
        TextField id=campo("ID"), nombre=campo("Nombre");
        Button guardar=new Button("Guardar");
        guardar.setOnAction(e->{
            try{
                centro.altaDepartamento(new Departamento(id.getText().trim(),nombre.getText().trim()));
                mostrarInfo("Departamento creado.");
                root.setCenter(pantallaDepartamentos());
            } catch (Exception ex){
                mostrarError("Error",ex.getMessage());
            }
        });
        return formulario("NUEVO DEPARTAMENTO",new Control[]{id,nombre,guardar},new String[]{"ID","Nombre"});
    }

    private VBox pantallaModificarDepartamento(Departamento d){
        TextField nombre=campo("Nombre");
        Button guardar=new Button("Guardar");

        // positionCaret: Hace que el cursor de texto aparezca detrás de este
        nombre.setText(d.getNombre());
        nombre.positionCaret(nombre.getText().length());

        guardar.setOnAction(e->{
            try {
                d.setNombre(nombre.getText().trim());
                centro.modificarDepartamento(d);
                mostrarInfo("Departamento modificado.");

                nombre.setText(d.getNombre());

                root.setCenter(pantallaDepartamentos());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        return formulario("MODIFICAR DEPARTAMENTO",new Control[]{nombre,guardar},new String[]{"Nombre"});
    }

    private VBox pantallaProfesores(){
        TableView<Profesor> tabla=new TableView<>();
        TableColumn<Profesor,String> id=new TableColumn<>("ID"); id.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getIdProfesor()));
        TableColumn<Profesor,String> nombre=new TableColumn<>("Nombre"); nombre.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getNombre()+ " " + d.getValue().getApellidos()));
        TableColumn<Profesor,String> email=new TableColumn<>("Email"); email.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getEmail()));
        TableColumn<Profesor,String> dep=new TableColumn<>("Departamento"); dep.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getDepartamento()==null?"":d.getValue().getDepartamento().getNombre()));
        tabla.getColumns().addAll(id,nombre,email,dep); cargar(tabla,centro.getProfesores());
        Button modificar=new Button("Modificar seleccionado");
        modificar.setOnAction(e->{Profesor p=tabla.getSelectionModel().getSelectedItem(); if(p==null){mostrarError("Aviso","Seleccione un profesor.");return;}try{root.setCenter(pantallaModificarProfesor(p));}catch(Exception ex){mostrarError("No se puede modificar",ex.getMessage());}});
        Button eliminar=new Button("Eliminar seleccionado");
        eliminar.setOnAction(e->{Profesor p=tabla.getSelectionModel().getSelectedItem();if(p==null){mostrarError("Aviso","Seleccione un profesor.");return;}try{centro.bajaProfesor(p.getIdProfesor());cargar(tabla,centro.getProfesores());}catch(Exception ex){mostrarError("No se puede eliminar",ex.getMessage());}});
        Button contar = new Button("Contar reservas del profesor");
        contar.setOnAction(e ->{Profesor p=tabla.getSelectionModel().getSelectedItem();if(p==null){mostrarError("Aviso","Seleccione un profesor.");return;}try{int total=centro.contarReservasProfesor(p.getIdProfesor());mostrarInfo("El profesor " + p.getNombre() + " " + p.getApellidos() + " tiene " + total + (total != 1 ? " reservas" : " reserva") + " no" + (total != 1 ? " canceladas" : " cancelada") + ".");} catch(Exception ex){mostrarError("Error al contar reservas",ex.getMessage());}});
        return contenedor("PROFESORES",tabla,modificar,eliminar,contar);
    }

    private VBox pantallaAltaProfesor(){
        TextField id=campo("ID"),nombre=campo("Nombre"),apellidos=campo("apellidos"),email=campo("Email");
        ComboBox<Departamento> dep=new ComboBox<>(FXCollections.observableArrayList(centro.getDepartamentos())); dep.setMaxWidth(Double.MAX_VALUE);
        Button guardar=new Button("Guardar");
        guardar.setOnAction(e->{try{centro.altaProfesor(new Profesor(id.getText().trim(),nombre.getText().trim(),apellidos.getText(),email.getText().trim(),dep.getValue()));mostrarInfo("Profesor creado.");root.setCenter(pantallaProfesores());}catch(Exception ex){mostrarError("Error",ex.getMessage());}});
        VBox b=new VBox(10);b.setPadding(new Insets(30));Label t=new Label("NUEVO PROFESOR");t.setStyle("-fx-font-size:22px;-fx-font-weight:bold;");b.getChildren().addAll(t,new Label("ID"),id,new Label("Nombre"),nombre,new Label("Apellidos"),apellidos,new Label("Email"),email,new Label("Departamento"),dep,guardar);return b;
    }

    private VBox pantallaModificarProfesor(Profesor p){
        TextField nombre=campo("Nombre"),apellidos=campo("apellidos"),email=campo("Email");
        ComboBox<Departamento> dep=new ComboBox<>(FXCollections.observableArrayList(centro.getDepartamentos())); dep.setMaxWidth(Double.MAX_VALUE);
        Button guardar=new Button("Guardar");

        nombre.setText(p.getNombre());
        nombre.positionCaret(nombre.getText().length());
        apellidos.setText(p.getApellidos());
        apellidos.positionCaret(apellidos.getText().length());
        email.setText(p.getEmail());
        email.positionCaret(email.getText().length());
        dep.setValue(p.getDepartamento());

        guardar.setOnAction(e->{
            try {
                p.setNombre(nombre.getText().trim());
                p.setApellidos(apellidos.getText().trim());
                p.setEmail(email.getText().trim());
                p.setDepartamento(dep.getValue());
                centro.modificarProfesor(p);
                mostrarInfo("Profesor modificado.");

                nombre.setText(p.getNombre());
                apellidos.setText(p.getApellidos());
                email.setText(p.getEmail());
                dep.setValue(p.getDepartamento());

                root.setCenter(pantallaProfesores());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        return formulario("MODIFICAR PROFESOR",new Control[]{nombre,apellidos,email,dep,guardar},new String[]{"Nombre", "Apellidos", "Email", "Departamento"});
    }

    private VBox pantallaEspacios(){
        TableView<Espacio> tabla=new TableView<>();
        TableColumn<Espacio,String> id=new TableColumn<>("ID");id.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getIdEspacio()));
        TableColumn<Espacio,String> nombre=new TableColumn<>("Nombre");nombre.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getNombre()));
        TableColumn<Espacio,String> tipo=new TableColumn<>("Tipo");tipo.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getTipo()));
        TableColumn<Espacio,String> ubicacion=new TableColumn<>("Ubicación");ubicacion.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getUbicacion()));
        TableColumn<Espacio,Number> cap=new TableColumn<>("Capacidad");cap.setCellValueFactory(d->new SimpleIntegerProperty(d.getValue().getCapacidad()));
        TableColumn<Espacio,String> equip=new TableColumn<>("Equipamiento");equip.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getEquipamiento()));
        TableColumn<Espacio,String> disponible=new TableColumn<>("Disponible");disponible.setCellValueFactory(d->new SimpleStringProperty(d.getValue().isDisponible()?"Sí":"No"));
        tabla.getColumns().addAll(id,nombre,tipo,ubicacion,cap,equip,disponible);cargar(tabla,centro.getEspacios());
        Button cambiar = new Button("Cambiar disponibilidad");
        cambiar.setOnAction(e->{Espacio x = tabla.getSelectionModel().getSelectedItem();if(x==null) {mostrarError("Aviso", "Seleccione un espacio.");return;}try{centro.cambiarDisponibilidad(x.getIdEspacio());tabla.refresh();mostrarInfo("Disponibilidad actualizada");} catch (Exception ex) {mostrarError("Error al cambiar disponibilidad", ex.getMessage());}});
        Button modificar=new Button("Modificar seleccionado");
        modificar.setOnAction(e->{Espacio x = tabla.getSelectionModel().getSelectedItem();if(x==null){mostrarError("Aviso","Seleccione un espacio.");return;}try{root.setCenter(pantallaModificarEspacio(x));}catch(Exception ex){mostrarError("No se puede modificar",ex.getMessage());}});
        Button eliminar=new Button("Eliminar seleccionado");
        eliminar.setOnAction(e->{Espacio x =tabla.getSelectionModel().getSelectedItem();if(x==null){mostrarError("Aviso","Seleccione un espacio.");return;}try{centro.bajaEspacio(x.getIdEspacio());cargar(tabla,centro.getEspacios());}catch(Exception ex){mostrarError("No se puede eliminar",ex.getMessage());}});
        return contenedor("ESPACIOS",tabla,modificar,eliminar,cambiar);
    }

    private VBox pantallaAltaEspacio(){
        TextField id=campo("ID"),nombre=campo("Nombre"),tipo=campo("Tipo"),ubicacion=campo("Ubicación"),cap=campo("Capacidad"),equip=campo("Equipamiento");
        Button guardar=new Button("Guardar");
        guardar.setOnAction(e->{try{centro.altaEspacio(new Espacio(id.getText().trim(),nombre.getText().trim(),tipo.getText().trim(),ubicacion.getText(),Integer.parseInt(cap.getText().trim()),equip.getText(),true));mostrarInfo("Espacio creado.");root.setCenter(pantallaEspacios());}catch(Exception ex){mostrarError("Error",ex.getMessage());}});
        VBox b=new VBox(10);b.setPadding(new Insets(30));Label t=new Label("NUEVO ESPACIO");t.setStyle("-fx-font-size:22px;-fx-font-weight:bold;");b.getChildren().addAll(t,new Label("ID"),id,new Label("Nombre"),nombre,new Label("Tipo"),tipo,new Label("Ubicación"),ubicacion,new Label("Capacidad"),cap,new Label("Equipamiento"),equip, guardar);return b;
    }

    private VBox pantallaModificarEspacio(Espacio e){
        TextField nombre=campo("Nombre"),tipo=campo("Tipo"),ubi=campo("Ubicación"),cap=campo("Capacidad"),equip=campo("Equipamiento");
        Button guardar=new Button("Guardar");

        nombre.setText(e.getNombre());
        nombre.positionCaret(nombre.getText().length());
        tipo.setText(e.getTipo());
        tipo.positionCaret(tipo.getText().length());
        ubi.setText(e.getUbicacion());
        ubi.positionCaret(ubi.getText().length());
        cap.setText(String.valueOf(e.getCapacidad()));
        cap.positionCaret(cap.getText().length());
        equip.setText(e.getEquipamiento());
        equip.positionCaret(equip.getText().length());

        guardar.setOnAction(ev->{
            try {
                e.setNombre(nombre.getText().trim());
                e.setTipo(tipo.getText().trim());
                e.setUbicacion(ubi.getText().trim());
                e.setCapacidad(Integer.parseInt(cap.getText().trim()));
                e.setEquipamiento(equip.getText().trim());
                centro.modificarEspacio(e);
                mostrarInfo("Espacio modificado.");

                nombre.setText(e.getNombre());
                tipo.setText(e.getTipo());
                ubi.setText(e.getUbicacion());
                cap.setText(String.valueOf(e.getCapacidad()));
                equip.setText(e.getEquipamiento());

                root.setCenter(pantallaEspacios());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        return formulario("MODIFICAR ESPACIO",new Control[]{nombre,tipo,ubi,cap,equip,guardar},new String[]{"Nombre", "Tipo", "Ubicación", "Capacidad", "Equipamiento"});
    }

    private VBox pantallaReservas(){
        TableView<Reserva> tabla=new TableView<>();
        TableColumn<Reserva,String> id=new TableColumn<>("ID");id.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getIdReserva()));
        TableColumn<Reserva,String> fecha=new TableColumn<>("Fecha");fecha.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getFecha().toString()));
        TableColumn<Reserva,String> horario=new TableColumn<>("Horario");horario.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getHoraInicio()+" - "+d.getValue().getHoraFin()));
        TableColumn<Reserva,String> profesor=new TableColumn<>("Profesor");profesor.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getProfesor().getNombre() + " " + d.getValue().getProfesor().getApellidos()));
        TableColumn<Reserva,String> espacio=new TableColumn<>("Espacio");espacio.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getEspacio().getNombre()));
        TableColumn<Reserva,Number> asistentes=new TableColumn<>("Asistentes");asistentes.setCellValueFactory(d->new SimpleIntegerProperty(d.getValue().getNumAsistentes()));
        TableColumn<Reserva,String> estado=new TableColumn<>("Estado");estado.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getEstado().toString()));
        TableColumn<Reserva,String> motivo=new TableColumn<>("Motivo");motivo.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getMotivo()));
        tabla.getColumns().addAll(id,fecha,horario,profesor,espacio,asistentes,estado,motivo);cargar(tabla,centro.getReservas());
        Button cancelar=new Button("Cancelar reserva seleccionada");
        cancelar.setOnAction(e->{
            Reserva r=tabla.getSelectionModel().getSelectedItem();
            if(r==null){
                mostrarError("Aviso","Seleccione una reserva.");
                return;
            }
            try {centro.cancelarReserva(r.getIdReserva());tabla.refresh();mostrarInfo("Reserva cancelada correctamente.");} catch (Exception ex) {mostrarError("Error", ex.getMessage());}});
        Button confirmar=new Button("Confirmar reserva seleccionada");
        confirmar.setOnAction(e->{
            Reserva r=tabla.getSelectionModel().getSelectedItem();
            if(r==null){
                mostrarError("Aviso","Seleccione una reserva.");
                return;
            }
            try {centro.confirmarReserva(r.getIdReserva());tabla.refresh();mostrarInfo("Reserva confirmada correctamente.");} catch (Exception ex) {mostrarError("Error", ex.getMessage());}});
        Button finalizar=new Button("Finalizar reserva seleccionada");
        finalizar.setOnAction(e->{
            Reserva r=tabla.getSelectionModel().getSelectedItem();
            if(r==null){
                mostrarError("Aviso","Seleccione una reserva.");
                return;
            }
            try {centro.finalizarReserva(r.getIdReserva());tabla.refresh();mostrarInfo("Reserva finalizada correctamente.");} catch (Exception ex) {mostrarError("Error", ex.getMessage());}});
        Button modificar=new Button("Modificar seleccionada");
        modificar.setOnAction(e->{Reserva r = tabla.getSelectionModel().getSelectedItem();if(r==null){mostrarError("Aviso","Seleccione una reserva.");return;}try{root.setCenter(pantallaModificarReserva(r));}catch(Exception ex){mostrarError("No se puede modificar",ex.getMessage());}});
        Button eliminar=new Button("Eliminar seleccionada");
        eliminar.setOnAction(e->{Reserva r =tabla.getSelectionModel().getSelectedItem();if(r==null){mostrarError("Aviso","Seleccione una reserva.");return;}try{centro.eliminarReserva(r.getIdReserva());cargar(tabla,centro.getReservas());}catch(Exception ex){mostrarError("No se puede eliminar",ex.getMessage());}});
        return contenedor("RESERVAS",tabla,modificar,eliminar,confirmar,finalizar,cancelar);
    }

    private VBox pantallaNuevaReserva(){
        TextField id=campo("ID"),inicio=campo("HH:mm"),fin=campo("HH:mm"),asist=campo("Número de asistentes"),motivo=campo("Motivo");DatePicker fecha=new DatePicker(LocalDate.now());
        ComboBox<Profesor> profesor=new ComboBox<>(FXCollections.observableArrayList(centro.getProfesores()));profesor.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Espacio> espacio=new ComboBox<>(FXCollections.observableArrayList(centro.getEspacios()));espacio.setMaxWidth(Double.MAX_VALUE);
        Button guardar=new Button("Crear reserva");guardar.setOnAction(e->{
            try{centro.crearReserva(new Reserva(id.getText().trim(),fecha.getValue(),LocalTime.parse(inicio.getText().trim()),LocalTime.parse(fin.getText().trim()),profesor.getValue(),espacio.getValue(),Integer.parseInt(asist.getText().trim()),EstadoReserva.Pendiente,motivo.getText()));mostrarInfo("Reserva creada.");root.setCenter(pantallaReservas());}catch(Exception ex){mostrarError("No se puede crear la reserva",ex.getMessage());}
        });
        VBox b=new VBox(10);b.setPadding(new Insets(30));Label t=new Label("NUEVA RESERVA");t.setStyle("-fx-font-size:22px;-fx-font-weight:bold;");b.getChildren().addAll(t,new Label("ID"),id,new Label("Fecha"),fecha,new Label("Profesor"),profesor,new Label("Espacio"),espacio,new Label("Hora inicio (HH:mm)"),inicio,new Label("Hora fin (HH:mm)"),fin,new Label("Asistentes"),asist,new Label("Motivo"),motivo,guardar);return b;
    }

    private VBox pantallaModificarReserva(Reserva r){
        TextField inicio=campo("HH:mm"),fin=campo("HH:mm"),asist=campo("Número de asistentes"),motivo=campo("Motivo");DatePicker fecha=new DatePicker(LocalDate.now());
        ComboBox<Profesor> pro=new ComboBox<>(FXCollections.observableArrayList(centro.getProfesores()));pro.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Espacio> esp=new ComboBox<>(FXCollections.observableArrayList(centro.getEspacios()));esp.setMaxWidth(Double.MAX_VALUE);
        Button guardar=new Button("Guardar");

        fecha.setValue(r.getFecha());
        inicio.setText(String.valueOf(r.getHoraInicio()));
        inicio.positionCaret(inicio.getText().length());
        fin.setText(String.valueOf(r.getHoraFin()));
        fin.positionCaret(fin.getText().length());
        pro.setValue(r.getProfesor());
        esp.setValue(r.getEspacio());
        asist.setText(String.valueOf(r.getNumAsistentes()));
        asist.positionCaret(asist.getText().length());
        motivo.setText(r.getMotivo());
        motivo.positionCaret(motivo.getText().length());

        guardar.setOnAction(e->{
            try {
                r.setFecha(fecha.getValue());
                r.setHoraInicio(LocalTime.parse(inicio.getText().trim()));
                r.setHoraFin(LocalTime.parse(fin.getText().trim()));
                r.setProfesor(pro.getValue());
                r.setEspacio(esp.getValue());
                r.setNumAsistentes(Integer.parseInt(asist.getText().trim()));
                r.setMotivo(motivo.getText().trim());
                centro.modificarReserva(r);
                mostrarInfo("Reserva modificada.");

                fecha.setValue(r.getFecha());
                inicio.setText(String.valueOf(r.getHoraInicio()));
                fin.setText(String.valueOf(r.getHoraFin()));
                pro.setValue(r.getProfesor());
                esp.setValue(r.getEspacio());
                asist.setText(String.valueOf(r.getNumAsistentes()));
                motivo.setText(r.getMotivo());

                root.setCenter(pantallaReservas());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        return formulario("MODIFICAR RESERVA",new Control[]{fecha,inicio,fin,pro,esp,asist,motivo,guardar},new String[]{"Fecha", "Hora inicio (HH:mm)", "Hora fin (HH:mm)", "Profesor", "Espacio", "Asistentes", "Motivo"});
    }

    private <T> void cargar(TableView<T> tabla,java.util.List<T> datos){tabla.setItems(FXCollections.observableArrayList(datos));tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);}
    private VBox contenedor(String titulo,TableView<?> tabla,Button... botones){VBox b=new VBox(12);b.setPadding(new Insets(25));Label t=new Label(titulo);t.setStyle("-fx-font-size:22px;-fx-font-weight:bold;");HBox acciones=new HBox(10,botones);VBox.setVgrow(tabla,Priority.ALWAYS);b.getChildren().addAll(t,tabla,acciones);return b;}
    private VBox formulario(String titulo,Control[] controles,String[] etiquetas){VBox b=new VBox(10);b.setPadding(new Insets(30));Label t=new Label(titulo);t.setStyle("-fx-font-size:22px;-fx-font-weight:bold;");b.getChildren().add(t);for(int i=0;i<controles.length;i++){if(i<etiquetas.length)b.getChildren().add(new Label(etiquetas[i]));b.getChildren().add(controles[i]);}return b;}
    private TextField campo(String prompt){TextField f=new TextField();f.setPromptText(prompt);return f;}
    public void mostrarInfo(String m){new Alert(Alert.AlertType.INFORMATION,m,ButtonType.OK).showAndWait();}
    public void mostrarError(String titulo,String m){Alert alert = new Alert(Alert.AlertType.ERROR,m==null?"Error desconocido":m,ButtonType.OK);alert.setTitle(titulo);alert.setHeaderText("Se ha producido un error");alert.showAndWait();}
}