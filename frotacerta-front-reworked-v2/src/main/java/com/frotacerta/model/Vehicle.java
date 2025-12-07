package com.frotacerta.model;

import java.util.Objects;

public class Vehicle {
    private String id;
    private String placa;
    private String marca;
    private String modelo;
    private int ano;
    private boolean ativo;

    public Vehicle(String id, String placa, String marca, String modelo, int ano, boolean ativo) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.ativo = ativo;
    }

    public String getId() { return id; }
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAno() { return ano; }
    public boolean isAtivo() { return ativo; }

    public void setPlaca(String placa) { this.placa = placa; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setAno(int ano) { this.ano = ano; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return id + ";" + placa + ";" + marca + ";" + modelo + ";" + ano + ";" + (ativo ? "1" : "0");
    }

    public static Vehicle fromLine(String line) {
        String[] p = line.split(";");
        if (p.length < 6) return null;
        return new Vehicle(p[0], p[1], p[2], p[3], Integer.parseInt(p[4]), "1".equals(p[5]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle v = (Vehicle) o;
        return Objects.equals(id, v.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
