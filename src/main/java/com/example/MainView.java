package com.example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.example.model.Categoria;
import com.example.model.Equipos;
import com.example.model.Prestamos;
import com.example.model.Usuario;
import com.example.service.CrudService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("GestiÃ³n CRUD - Sistema de Prestamo de Equipos")
@Route("")
public class MainView extends VerticalLayout {

    private final CrudService crudService;

    public MainView(CrudService crudService) {
        this.crudService = crudService;
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        TabSheet tabs = new TabSheet();
        tabs.setWidthFull();
        tabs.add("Equipos", crearSeccionEquipos());
        tabs.add("Categorias", crearSeccionCategorias());
        tabs.add("Prestamos", crearSeccionPrestamos());
        tabs.add("Usuarios", crearSeccionUsuarios());

        add(new H2("Sistema de Prestamo de Equipos - CRUD"), tabs);
    }

    // ==========================================
    // 1. EQUIPOS
    // ==========================================
    private Component crearSeccionEquipos() {
        VerticalLayout layout = new VerticalLayout();

        TextField idField = new TextField("ID");
        TextField codigoEquiposField = new TextField("Código");
        TextField nombreEquiposField = new TextField("Nombre");
        TextField categoriaField = new TextField("Categoría");
        TextField descripcionField = new TextField("Descripción");
        TextField estadoField = new TextField("Estado");
        TextField precioField = new TextField("Precio");
        TextField stockField = new TextField("Stock");

        FormLayout form = new FormLayout(idField, codigoEquiposField, nombreEquiposField,
            categoriaField, descripcionField, estadoField, precioField, stockField);

        Grid<Equipos> grid = new Grid<>(Equipos.class, false);
        grid.addColumn(Equipos::getIdEquipos).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Equipos::getCodigoEquipos).setHeader("Código").setAutoWidth(true);
        grid.addColumn(Equipos::getNombreEquipos).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Equipos::getCategoria).setHeader("Categoría").setAutoWidth(true);
        grid.addColumn(Equipos::getDescripcion).setHeader("Descripción").setAutoWidth(true);
        grid.addColumn(Equipos::getEstado).setHeader("Estado").setAutoWidth(true);
        grid.addColumn(Equipos::getPrecio).setHeader("Precio").setAutoWidth(true);
        grid.addColumn(Equipos::getStock).setHeader("Stock").setAutoWidth(true);

        Runnable refrescar = () -> grid.setItems(crudService.listarTodos(Equipos.class));
        refrescar.run();

        grid.asSingleSelect().addValueChangeListener(e -> {
            Equipos equipo = e.getValue();

            if (equipo != null) {
                idField.setValue(equipo.getIdEquipos() != null ? equipo.getIdEquipos().toString() : "");
                codigoEquiposField.setValue(equipo.getCodigoEquipos() != null ? equipo.getCodigoEquipos() : "");
                nombreEquiposField.setValue(equipo.getNombreEquipos() != null ? equipo.getNombreEquipos() : "");
                categoriaField.setValue(equipo.getCategoria() != null ? equipo.getCategoria() : "");
                descripcionField.setValue(equipo.getDescripcion() != null ? equipo.getDescripcion() : "");
                estadoField.setValue(equipo.getEstado() != null ? equipo.getEstado() : "");
                precioField.setValue(equipo.getPrecio() != null ? equipo.getPrecio().toString() : "");
                stockField.setValue(equipo.getStock() != null ? equipo.getStock().toString() : "");
            }
        });

        Button btnCrear = new Button("Crear", e -> {
            Equipos equipo = new Equipos(codigoEquiposField.getValue(), nombreEquiposField.getValue(),
                categoriaField.getValue(), descripcionField.getValue(), estadoField.getValue(),
                parseDouble(precioField), parseInteger(stockField));

            crudService.guardar(equipo);
            refrescar.run();
            idField.setValue(equipo.getIdEquipos() != null ? equipo.getIdEquipos().toString() : "");
            Notification.show("Equipo guardado con ID: " + equipo.getIdEquipos());
        });
        btnCrear.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnConsultar = new Button("Consultar", e -> {
            Equipos equipo = crudService.buscarPorId(Equipos.class, parseId(idField));

            if (equipo != null) {
                grid.asSingleSelect().setValue(equipo);
                Notification.show("Encontrado: " + equipo.getNombreEquipos());
            } else {
                Notification.show("No encontrado");
            }
        });

        Button btnActualizar = new Button("Actualizar", e -> {
            Equipos equipo = crudService.buscarPorId(Equipos.class, parseId(idField));

            if (equipo != null) {
                equipo.setCodigoEquipos(codigoEquiposField.getValue());
                equipo.setNombreEquipos(nombreEquiposField.getValue());
                equipo.setCategoria(categoriaField.getValue());
                equipo.setDescripcion(descripcionField.getValue());
                equipo.setEstado(estadoField.getValue());
                equipo.setPrecio(parseDouble(precioField));
                equipo.setStock(parseInteger(stockField));

                crudService.actualizar(equipo);
                refrescar.run();
                Notification.show("Equipo actualizado");
            } else {
                Notification.show("ID no encontrado");
            }
        });

        Button btnEliminar = new Button("Eliminar", e -> {
            crudService.eliminar(Equipos.class, parseId(idField));
            refrescar.run();
            limpiar(idField, codigoEquiposField, nombreEquiposField, categoriaField,
                    descripcionField, estadoField, precioField, stockField);
            grid.asSingleSelect().clear();
            Notification.show("Equipo eliminado");
        });
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnLimpiar = new Button("Limpiar", e -> {
            limpiar(idField, codigoEquiposField, nombreEquiposField, categoriaField,
                    descripcionField, estadoField, precioField, stockField);
            grid.asSingleSelect().clear();
        });

        layout.add(form, crearAcciones(btnCrear, btnConsultar, btnActualizar, btnEliminar, btnLimpiar), grid);
        return layout;
    }

    // ==========================================
    // 2. CATEGORÃAS
    // ==========================================
    private Component crearSeccionCategorias() {
        VerticalLayout layout = new VerticalLayout();

        TextField idField = new TextField("ID");
        TextField nombreField = new TextField("Nombre");
        TextField descripcionField = new TextField("Descripción");

        FormLayout form = new FormLayout(idField, nombreField, descripcionField);

        Grid<Categoria> grid = new Grid<>(Categoria.class, false);
        grid.addColumn(Categoria::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Categoria::getNombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Categoria::getDescripcion).setHeader("Descripción").setAutoWidth(true);

        Runnable refrescar = () -> grid.setItems(crudService.listarTodos(Categoria.class));
        refrescar.run();

        grid.asSingleSelect().addValueChangeListener(e -> {
            Categoria c = e.getValue();

            if (c != null) {
                idField.setValue(c.getId() != null ? c.getId().toString() : "");
                nombreField.setValue(c.getNombre() != null ? c.getNombre() : "");
                descripcionField.setValue(c.getDescripcion() != null ? c.getDescripcion() : "");
            }
        });

        Button btnCrear = new Button("Crear", e -> {
            Categoria c = new Categoria(nombreField.getValue(), descripcionField.getValue());

            crudService.guardar(c);
            refrescar.run();
            idField.setValue(c.getId() != null ? c.getId().toString() : "");
            Notification.show("CategorÃ­a guardada con ID: " + c.getId());
        });
        btnCrear.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnConsultar = new Button("Consultar", e -> {
            Categoria c = crudService.buscarPorId(Categoria.class, parseId(idField));

            if (c != null) {
                grid.asSingleSelect().setValue(c);
                Notification.show("Encontrada: " + c.getNombre());
            } else {
                Notification.show("No encontrada");
            }
        });

        Button btnActualizar = new Button("Actualizar", e -> {
            Categoria c = crudService.buscarPorId(Categoria.class, parseId(idField));

            if (c != null) {
                c.setNombre(nombreField.getValue());
                c.setDescripcion(descripcionField.getValue());

                crudService.actualizar(c);
                refrescar.run();
                Notification.show("Categoría actualizada");
            } else {
                Notification.show("ID no encontrado");
            }
        });

        Button btnEliminar = new Button("Eliminar", e -> {
            crudService.eliminar(Categoria.class, parseId(idField));
            refrescar.run();
            limpiar(idField, nombreField, descripcionField);
            grid.asSingleSelect().clear();
            Notification.show("Categoría eliminada");
        });
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnLimpiar = new Button("Limpiar", e -> {
            limpiar(idField, nombreField, descripcionField);
            grid.asSingleSelect().clear();
        });

        layout.add(form, crearAcciones(btnCrear, btnConsultar, btnActualizar, btnEliminar, btnLimpiar), grid);
        return layout;
    }

    // ==========================================
    // 3. PRÃ‰STAMOS
    // ==========================================
    private Component crearSeccionPrestamos() {
        VerticalLayout layout = new VerticalLayout();
        TextField idField = new TextField("ID");
        TextField idUsuarioField = new TextField("ID Usuario");
        TextField idEquipoField = new TextField("ID Equipo");
        TextField rolField = new TextField("Rol");
        DateTimePicker fechaPrestamoField = new DateTimePicker("Fecha del préstamo");
        DateTimePicker fechaDevolucionField = new DateTimePicker("Fecha de devolución prevista");
        TextField estadoField = new TextField("Estado");

        FormLayout form = new FormLayout(idField, idUsuarioField, idEquipoField, rolField,
            fechaPrestamoField, fechaDevolucionField, estadoField);
        Grid<Prestamos> grid = new Grid<>(Prestamos.class, false);
        grid.addColumn(Prestamos::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Prestamos::getIdUsuario).setHeader("ID Usuario").setAutoWidth(true);
        grid.addColumn(Prestamos::getIdEquipo).setHeader("ID Equipo").setAutoWidth(true);
        grid.addColumn(Prestamos::getRol).setHeader("Rol").setAutoWidth(true);
        grid.addColumn(Prestamos::getFechaPrestamo).setHeader("Fecha del préstamo").setAutoWidth(true);
        grid.addColumn(Prestamos::getFechaDevolucionPrevista).setHeader("Fecha de devolución").setAutoWidth(true);
        grid.addColumn(Prestamos::getEstado).setHeader("Estado").setAutoWidth(true);

        Runnable refrescar = () -> grid.setItems(crudService.listarTodos(Prestamos.class));
        refrescar.run();
        grid.asSingleSelect().addValueChangeListener(e -> {
            Prestamos prestamo = e.getValue();
            if (prestamo != null) {
                idField.setValue(prestamo.getId() != null ? prestamo.getId().toString() : "");
                idUsuarioField.setValue(prestamo.getIdUsuario() != null ? prestamo.getIdUsuario().toString() : "");
                idEquipoField.setValue(prestamo.getIdEquipo() != null ? prestamo.getIdEquipo().toString() : "");
                rolField.setValue(prestamo.getRol() != null ? prestamo.getRol() : "");
                fechaPrestamoField.setValue(prestamo.getFechaPrestamo() != null
                    ? LocalDateTime.ofInstant(prestamo.getFechaPrestamo().toInstant(), ZoneId.systemDefault()) : null);
                fechaDevolucionField.setValue(prestamo.getFechaDevolucionPrevista() != null
                    ? LocalDateTime.ofInstant(prestamo.getFechaDevolucionPrevista().toInstant(), ZoneId.systemDefault()) : null);
                estadoField.setValue(prestamo.getEstado() != null ? prestamo.getEstado() : "");
            }
        });

        Button btnCrear = new Button("Crear", e -> {
            if (parseId(idUsuarioField) == null || parseId(idEquipoField) == null
                    || fechaPrestamoField.isEmpty() || fechaDevolucionField.isEmpty()) {
                Notification.show("Ingresa IDs válidos y completa las fechas");
                return;
            }
            Prestamos prestamo = new Prestamos(parseId(idUsuarioField), parseId(idEquipoField), rolField.getValue(),
                Date.from(fechaPrestamoField.getValue().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(fechaDevolucionField.getValue().atZone(ZoneId.systemDefault()).toInstant()), estadoField.getValue());
            crudService.guardar(prestamo);
            refrescar.run();
            idField.setValue(prestamo.getId() != null ? prestamo.getId().toString() : "");
            Notification.show("Préstamo guardado con ID: " + prestamo.getId());
        });
        btnCrear.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnConsultar = new Button("Consultar", e -> {
            Prestamos prestamo = crudService.buscarPorId(Prestamos.class, parseId(idField));
            if (prestamo != null) {
                grid.asSingleSelect().setValue(prestamo);
                Notification.show("Préstamo encontrado");
            } else Notification.show("No encontrado");
        });

        Button btnActualizar = new Button("Actualizar", e -> {
            Prestamos prestamo = crudService.buscarPorId(Prestamos.class, parseId(idField));
            if (prestamo == null) {
                Notification.show("ID no encontrado");
                return;
            }
            if (parseId(idUsuarioField) == null || parseId(idEquipoField) == null
                    || fechaPrestamoField.isEmpty() || fechaDevolucionField.isEmpty()) {
                Notification.show("Ingresa IDs válidos y completa las fechas");
                return;
            }
            prestamo.setIdUsuario(parseId(idUsuarioField));
            prestamo.setIdEquipo(parseId(idEquipoField));
            prestamo.setRol(rolField.getValue());
            prestamo.setFechaPrestamo(Date.from(fechaPrestamoField.getValue().atZone(ZoneId.systemDefault()).toInstant()));
            prestamo.setFechaDevolucionPrevista(Date.from(fechaDevolucionField.getValue().atZone(ZoneId.systemDefault()).toInstant()));
            prestamo.setEstado(estadoField.getValue());
            crudService.actualizar(prestamo);
            refrescar.run();
            Notification.show("Préstamo actualizado");
        });

        Button btnEliminar = new Button("Eliminar", e -> {
            crudService.eliminar(Prestamos.class, parseId(idField));
            refrescar.run();
            limpiar(idField, idUsuarioField, idEquipoField, rolField, estadoField);
            fechaPrestamoField.clear();
            fechaDevolucionField.clear();
            grid.asSingleSelect().clear();
            Notification.show("Préstamo eliminado");
        });
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnLimpiar = new Button("Limpiar", e -> {
            limpiar(idField, idUsuarioField, idEquipoField, rolField, estadoField);
            fechaPrestamoField.clear();
            fechaDevolucionField.clear();
            grid.asSingleSelect().clear();
        });
        layout.add(form, crearAcciones(btnCrear, btnConsultar, btnActualizar, btnEliminar, btnLimpiar), grid);
        return layout;
    }
    // ==========================================
    // 4. USUARIOS
    // ==========================================
    private Component crearSeccionUsuarios() {
        VerticalLayout layout = new VerticalLayout();
        TextField idField = new TextField("ID");
        TextField tipoDocumentoField = new TextField("Tipo de Documento");
        TextField numeroDocumentoField = new TextField("Número de Documento");
        TextField contrasenaHashField = new TextField("Contraseña Hash");
        TextField nombreField = new TextField("Nombre");
        TextField apellidoField = new TextField("Apellido");
        TextField emailField = new TextField("Email");
        TextField telefonoField = new TextField("Teléfono");
        TextField direccionField = new TextField("Dirección");
        TextField rolField = new TextField("Rol");
        TextField estadoField = new TextField("Estado");

        FormLayout form = new FormLayout(idField, tipoDocumentoField, numeroDocumentoField, nombreField, apellidoField, emailField, contrasenaHashField, telefonoField, direccionField, rolField, estadoField);

        Grid<Usuario> grid = new Grid<>(Usuario.class, false);
        grid.addColumn(Usuario::getIdUsuario).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Usuario::getTipoDocumento).setHeader("Tipo de Documento").setAutoWidth(true);
        grid.addColumn(Usuario::getNumeroDocumento).setHeader("Número de Documento").setAutoWidth(true);
        grid.addColumn(Usuario::getContrasenaHash).setHeader("Contraseña Hash").setAutoWidth(true);
        grid.addColumn(Usuario::getNombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Usuario::getApellido).setHeader("Apellido").setAutoWidth(true);
        grid.addColumn(Usuario::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(Usuario::getTelefono).setHeader("Teléfono").setAutoWidth(true);
        grid.addColumn(Usuario::getRol).setHeader("Rol").setAutoWidth(true);
        grid.addColumn(Usuario::getEstado).setHeader("Estado").setAutoWidth(true);

        Runnable refrescar = () -> grid.setItems(crudService.listarTodos(Usuario.class));
        refrescar.run();

        grid.asSingleSelect().addValueChangeListener(e -> {
            Usuario u = e.getValue();
            if (u != null) {
                idField.setValue(u.getIdUsuario() != null ? u.getIdUsuario().toString() : "");
                tipoDocumentoField.setValue(u.getTipoDocumento() != null ? u.getTipoDocumento() : "");
                numeroDocumentoField.setValue(u.getNumeroDocumento() != null ? u.getNumeroDocumento() : "");
                contrasenaHashField.setValue(u.getContrasenaHash() != null ? u.getContrasenaHash() : "");
                nombreField.setValue(u.getNombre() != null ? u.getNombre() : "");
                apellidoField.setValue(u.getApellido() != null ? u.getApellido() : "");
                emailField.setValue(u.getEmail() != null ? u.getEmail() : "");
                telefonoField.setValue(u.getTelefono() != null ? u.getTelefono() : "");
                rolField.setValue(u.getRol() != null ? u.getRol() : "");
                estadoField.setValue(u.getEstado() != null ? u.getEstado() : "");
            }
        });

        Button btnCrear = new Button("Crear", e -> {
            Usuario u = new Usuario(

                tipoDocumentoField.getValue(),
                numeroDocumentoField.getValue(),
                nombreField.getValue(),
                apellidoField.getValue(),
                emailField.getValue(),
                contrasenaHashField.getValue(),
                telefonoField.getValue(),
                rolField.getValue(),
                estadoField.getValue()
            );
            crudService.guardar(u);
            refrescar.run();
            idField.setValue(u.getIdUsuario() != null ? u.getIdUsuario().toString() : "");
            Notification.show("Usuario guardado con ID: " + u.getIdUsuario());
        });
        btnCrear.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnConsultar = new Button("Consultar", e -> {
            Usuario u = crudService.buscarPorId(Usuario.class, parseId(idField));
            if (u != null) {
                grid.asSingleSelect().setValue(u);
                Notification.show("Encontrado: " + u.getNombre() + " " + u.getApellido());
            } else {
                Notification.show("No encontrado");
            }
        });

        Button btnActualizar = new Button("Actualizar", e -> {
            Usuario u = crudService.buscarPorId(Usuario.class, parseId(idField));
            if (u != null) {
                u.setNombre(nombreField.getValue());
                u.setApellido(apellidoField.getValue());
                u.setNumeroDocumento(numeroDocumentoField.getValue());
                u.setEmail(emailField.getValue());
                u.setTelefono(telefonoField.getValue());     
                u.setRol(rolField.getValue());
                u.setEstado(estadoField.getValue());
                crudService.actualizar(u);
                refrescar.run();
                Notification.show("Usuario actualizado");
            } else {
                Notification.show("ID no encontrado");
            }
        });

        Button btnEliminar = new Button("Eliminar", e -> {
            crudService.eliminar(Usuario.class, parseId(idField));
            refrescar.run();
            limpiar(idField, tipoDocumentoField, numeroDocumentoField, nombreField, apellidoField, emailField, contrasenaHashField, telefonoField, direccionField, rolField, estadoField);
            Notification.show("Usuario eliminado");
        });
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnLimpiar = new Button("Limpiar", e -> {
            limpiar(idField, tipoDocumentoField, numeroDocumentoField, nombreField, apellidoField, emailField, contrasenaHashField, telefonoField, direccionField, rolField, estadoField);
            grid.asSingleSelect().clear();
        });

        layout.add(form, crearAcciones(btnCrear, btnConsultar, btnActualizar, btnEliminar, btnLimpiar), grid);
        return layout;
    }

    // ==========================================
    // MÃ‰TODOS AUXILIARES
    // ==========================================
    private Long parseId(TextField field) {
        try {
            return Long.parseLong(field.getValue().trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Double parseDouble(TextField field) {
        try {
            return Double.parseDouble(field.getValue().trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseInteger(TextField field) {
        try {
            return Integer.parseInt(field.getValue().trim());
        } catch (Exception e) {
            return null;
        }
    }

    private void limpiar(TextField... campos) {
        for (TextField c : campos) {
            c.clear();
        }
    }

    private HorizontalLayout crearAcciones(Button... botones) {
        HorizontalLayout layout = new HorizontalLayout(botones);
        layout.getStyle().set("flex-wrap", "wrap");
        return layout;
    }
}
