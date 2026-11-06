package com.example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.example.model.Categoria;
import com.example.model.Equipo;
import com.example.model.Prestamo;
import com.example.model.Usuario;
import com.example.service.CrudService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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

@PageTitle("Gestión CRUD - Sistema de Préstamo de Equipos")
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
        tabs.add("Categorías", crearSeccionCategorias());
        tabs.add("Préstamos", crearSeccionPrestamos());
        tabs.add("Usuarios", crearSeccionUsuarios());

        add(new H2("Sistema de Préstamo de Equipos - CRUD"), tabs);
    }

    // ==========================================
    // 1. EQUIPOS
    // ==========================================
    private Component crearSeccionEquipos() {
        VerticalLayout layout = new VerticalLayout();

        TextField idField = new TextField("ID");
        TextField codigoField = new TextField("Código");
        TextField nombreField = new TextField("Nombre");

        ComboBox<Categoria> categoriaField = new ComboBox<>("Categoría");
        categoriaField.setItems(crudService.listarTodos(Categoria.class));
        categoriaField.setItemLabelGenerator(Categoria::getNombre);

        TextField marcaField = new TextField("Marca");
        TextField descripcionField = new TextField("Descripción");
        TextField ubicacionField = new TextField("Ubicación");
        TextField estadoField = new TextField("Estado");

        FormLayout form = new FormLayout(
            idField, codigoField, nombreField, categoriaField,
            marcaField, descripcionField, ubicacionField, estadoField
        );

        Grid<Equipo> grid = new Grid<>(Equipo.class, false);
        grid.addColumn(Equipo::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Equipo::getCodigo).setHeader("Código").setAutoWidth(true);
        grid.addColumn(Equipo::getNombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(e -> e.getCategoria() != null ? e.getCategoria().getNombre() : "")
            .setHeader("Categoría").setAutoWidth(true);
        grid.addColumn(Equipo::getMarca).setHeader("Marca").setAutoWidth(true);
        grid.addColumn(Equipo::getDescripcion).setHeader("Descripción").setAutoWidth(true);
        grid.addColumn(Equipo::getUbicacion).setHeader("Ubicación").setAutoWidth(true);
        grid.addColumn(Equipo::getEstado).setHeader("Estado").setAutoWidth(true);

        Runnable refrescar = () -> grid.setItems(crudService.listarTodos(Equipo.class));
        refrescar.run();

        grid.asSingleSelect().addValueChangeListener(e -> {
            Equipo equipo = e.getValue();

            if (equipo != null) {
                idField.setValue(equipo.getId() != null ? equipo.getId().toString() : "");
                codigoField.setValue(equipo.getCodigo() != null ? equipo.getCodigo() : "");
                nombreField.setValue(equipo.getNombre() != null ? equipo.getNombre() : "");
                categoriaField.setValue(equipo.getCategoria());
                marcaField.setValue(equipo.getMarca() != null ? equipo.getMarca() : "");
                descripcionField.setValue(equipo.getDescripcion() != null ? equipo.getDescripcion() : "");
                ubicacionField.setValue(equipo.getUbicacion() != null ? equipo.getUbicacion() : "");
                estadoField.setValue(equipo.getEstado() != null ? equipo.getEstado() : "");
            }
        });

        Button btnCrear = new Button("Crear", e -> {
            Equipo equipo = new Equipo(
                codigoField.getValue(),
                nombreField.getValue(),
                categoriaField.getValue(),
                marcaField.getValue(),
                descripcionField.getValue(),
                ubicacionField.getValue(),
                estadoField.getValue()
            );

            crudService.guardar(equipo);
            refrescar.run();
            idField.setValue(equipo.getId() != null ? equipo.getId().toString() : "");
            Notification.show("Equipo guardado con ID: " + equipo.getId());
        });
        btnCrear.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnConsultar = new Button("Consultar", e -> {
            Equipo equipo = crudService.buscarPorId(Equipo.class, parseId(idField));

            if (equipo != null) {
                grid.asSingleSelect().setValue(equipo);
                Notification.show("Encontrado: " + equipo.getNombre());
            } else {
                Notification.show("No encontrado");
            }
        });

        Button btnActualizar = new Button("Actualizar", e -> {
            Equipo equipo = crudService.buscarPorId(Equipo.class, parseId(idField));

            if (equipo != null) {
                equipo.setCodigo(codigoField.getValue());
                equipo.setNombre(nombreField.getValue());
                equipo.setCategoria(categoriaField.getValue());
                equipo.setMarca(marcaField.getValue());
                equipo.setDescripcion(descripcionField.getValue());
                equipo.setUbicacion(ubicacionField.getValue());
                equipo.setEstado(estadoField.getValue());

                crudService.actualizar(equipo);
                refrescar.run();
                Notification.show("Equipo actualizado");
            } else {
                Notification.show("ID no encontrado");
            }
        });

        Button btnEliminar = new Button("Eliminar", e -> {
            crudService.eliminar(Equipo.class, parseId(idField));
            refrescar.run();
            limpiar(idField, codigoField, nombreField, marcaField,
                    descripcionField, ubicacionField, estadoField);
            categoriaField.clear();
            grid.asSingleSelect().clear();
            Notification.show("Equipo eliminado");
        });
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnLimpiar = new Button("Limpiar", e -> {
            limpiar(idField, codigoField, nombreField, marcaField,
                    descripcionField, ubicacionField, estadoField);
            categoriaField.clear();
            grid.asSingleSelect().clear();
        });

        layout.add(form, crearAcciones(btnCrear, btnConsultar, btnActualizar, btnEliminar, btnLimpiar), grid);
        return layout;
    }

    // ==========================================
    // 2. CATEGORÍAS
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
            Notification.show("Categoría guardada con ID: " + c.getId());
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
    // 3. PRÉSTAMOS
    // ==========================================
    private Component crearSeccionPrestamos() {
        VerticalLayout layout = new VerticalLayout();

        TextField idField = new TextField("ID");

        ComboBox<Usuario> usuarioField = new ComboBox<>("Usuario");
        usuarioField.setItems(crudService.listarTodos(Usuario.class));
        usuarioField.setItemLabelGenerator(u -> u.getNombre() + " " + u.getApellido());

        ComboBox<Equipo> equipoField = new ComboBox<>("Equipo");
        equipoField.setItems(crudService.listarTodos(Equipo.class));
        equipoField.setItemLabelGenerator(Equipo::getNombre);

        ComboBox<Usuario> administradorField = new ComboBox<>("Administrador");
        administradorField.setItems(crudService.listarTodos(Usuario.class));
        administradorField.setItemLabelGenerator(u -> u.getNombre() + " " + u.getApellido());

        DateTimePicker fechaPrestamoField = new DateTimePicker("Fecha del préstamo");
        DateTimePicker fechaDevolucionField = new DateTimePicker("Fecha de devolución prevista");

        TextField estadoField = new TextField("Estado");

        FormLayout form = new FormLayout(
            idField, usuarioField, equipoField, administradorField,
            fechaPrestamoField, fechaDevolucionField, estadoField
        );

        Grid<Prestamo> grid = new Grid<>(Prestamo.class, false);
        grid.addColumn(Prestamo::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(p -> p.getUsuario() != null
            ? p.getUsuario().getNombre() + " " + p.getUsuario().getApellido() : "")
            .setHeader("Usuario").setAutoWidth(true);
        grid.addColumn(p -> p.getEquipo() != null ? p.getEquipo().getNombre() : "")
            .setHeader("Equipo").setAutoWidth(true);
        grid.addColumn(p -> p.getAdministrador() != null
            ? p.getAdministrador().getNombre() + " " + p.getAdministrador().getApellido() : "")
            .setHeader("Administrador").setAutoWidth(true);
        grid.addColumn(Prestamo::getFechaPrestamo).setHeader("Fecha del préstamo").setAutoWidth(true);
        grid.addColumn(Prestamo::getFechaDevolucionPrevista)
            .setHeader("Fecha de devolución").setAutoWidth(true);
        grid.addColumn(Prestamo::getEstado).setHeader("Estado").setAutoWidth(true);

        Runnable refrescar = () -> grid.setItems(crudService.listarTodos(Prestamo.class));
        refrescar.run();

        grid.asSingleSelect().addValueChangeListener(e -> {
            Prestamo prestamo = e.getValue();

            if (prestamo != null) {
                idField.setValue(prestamo.getId() != null ? prestamo.getId().toString() : "");
                usuarioField.setValue(prestamo.getUsuario());
                equipoField.setValue(prestamo.getEquipo());
                administradorField.setValue(prestamo.getAdministrador());

                if (prestamo.getFechaPrestamo() != null) {
                    fechaPrestamoField.setValue(
                        LocalDateTime.ofInstant(
                            prestamo.getFechaPrestamo().toInstant(),
                            ZoneId.systemDefault()
                        )
                    );
                } else {
                    fechaPrestamoField.clear();
                }

                if (prestamo.getFechaDevolucionPrevista() != null) {
                    fechaDevolucionField.setValue(
                        LocalDateTime.ofInstant(
                            prestamo.getFechaDevolucionPrevista().toInstant(),
                            ZoneId.systemDefault()
                        )
                    );
                } else {
                    fechaDevolucionField.clear();
                }

                estadoField.setValue(prestamo.getEstado() != null ? prestamo.getEstado() : "");
            }
        });

        Button btnCrear = new Button("Crear", e -> {
            if (usuarioField.isEmpty() || equipoField.isEmpty()
                    || administradorField.isEmpty() || fechaPrestamoField.isEmpty()
                    || fechaDevolucionField.isEmpty()) {
                Notification.show("Completa todos los campos obligatorios");
                return;
            }

            Prestamo prestamo = new Prestamo(
                usuarioField.getValue(),
                equipoField.getValue(),
                administradorField.getValue(),
                Date.from(fechaPrestamoField.getValue().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(fechaDevolucionField.getValue().atZone(ZoneId.systemDefault()).toInstant()),
                estadoField.getValue()
            );

            crudService.guardar(prestamo);
            refrescar.run();
            idField.setValue(prestamo.getId() != null ? prestamo.getId().toString() : "");
            Notification.show("Préstamo guardado con ID: " + prestamo.getId());
        });
        btnCrear.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnConsultar = new Button("Consultar", e -> {
            Prestamo prestamo = crudService.buscarPorId(Prestamo.class, parseId(idField));

            if (prestamo != null) {
                grid.asSingleSelect().setValue(prestamo);
                Notification.show("Préstamo encontrado");
            } else {
                Notification.show("No encontrado");
            }
        });

        Button btnActualizar = new Button("Actualizar", e -> {
            Prestamo prestamo = crudService.buscarPorId(Prestamo.class, parseId(idField));

            if (prestamo != null) {
                if (usuarioField.isEmpty() || equipoField.isEmpty()
                        || administradorField.isEmpty() || fechaPrestamoField.isEmpty()
                        || fechaDevolucionField.isEmpty()) {
                    Notification.show("Completa todos los campos obligatorios");
                    return;
                }

                prestamo.setUsuario(usuarioField.getValue());
                prestamo.setEquipo(equipoField.getValue());
                prestamo.setAdministrador(administradorField.getValue());
                prestamo.setFechaPrestamo(Date.from(
                    fechaPrestamoField.getValue().atZone(ZoneId.systemDefault()).toInstant()
                ));
                prestamo.setFechaDevolucionPrevista(Date.from(
                    fechaDevolucionField.getValue().atZone(ZoneId.systemDefault()).toInstant()
                ));
                prestamo.setEstado(estadoField.getValue());

                crudService.actualizar(prestamo);
                refrescar.run();
                Notification.show("Préstamo actualizado");
            } else {
                Notification.show("ID no encontrado");
            }
        });

        Button btnEliminar = new Button("Eliminar", e -> {
            crudService.eliminar(Prestamo.class, parseId(idField));
            refrescar.run();
            limpiar(idField, estadoField);
            usuarioField.clear();
            equipoField.clear();
            administradorField.clear();
            fechaPrestamoField.clear();
            fechaDevolucionField.clear();
            grid.asSingleSelect().clear();
            Notification.show("Préstamo eliminado");
        });
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnLimpiar = new Button("Limpiar", e -> {
            limpiar(idField, estadoField);
            usuarioField.clear();
            equipoField.clear();
            administradorField.clear();
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
    // MÉTODOS AUXILIARES
    // ==========================================
    private Long parseId(TextField field) {
        try {
            return Long.parseLong(field.getValue().trim());
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
