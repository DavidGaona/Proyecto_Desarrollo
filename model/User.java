package model;

public class User {
        private String nombre_usuario;
        private String apellidos_usuario;
        private String documento_id_usuario;
        private short tipo_usuario;
        private Boolean estado_usuario;
        private String pass_usuario;

    public User(String nombre_usuario, String apellidos_usuario, String documento_id_usuario, short tipo_usuario, Boolean estado_usuario, String pass_usuario) {
        this.nombre_usuario = nombre_usuario;
        this.apellidos_usuario = apellidos_usuario;
        this.documento_id_usuario = documento_id_usuario;
        this.tipo_usuario = tipo_usuario;
        this.estado_usuario = estado_usuario;
        this.pass_usuario = pass_usuario;
    }

    public boolean isBlank(){ return nombre_usuario.isBlank() || apellidos_usuario.isBlank() || documento_id_usuario.isBlank() || pass_usuario.isBlank(); }


    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public String getApellidos_usuario() {
        return apellidos_usuario;
    }

    public String getDocumento_id_usuario() {
        return documento_id_usuario;
    }

    public short getTipo_usuario() {
        return tipo_usuario;
    }

    public Boolean getEstado_usuario() {
        return estado_usuario;
    }

    public String getPass_usuario() {
        return pass_usuario;
    }
}
