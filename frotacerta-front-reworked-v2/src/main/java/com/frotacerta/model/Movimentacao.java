package com.frotacerta.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Movimentacao {
    private String id;
    private String idVeiculo;
    private String idTipoDespesa;
    private String descricao;
    private LocalDate data;
    private double valor;
    private static final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;

    public Movimentacao(String id, String idVeiculo, String idTipoDespesa, String descricao, LocalDate data, double valor) {
        this.id = id;
        this.idVeiculo = idVeiculo;
        this.idTipoDespesa = idTipoDespesa;
        this.descricao = descricao;
        this.data = data;
        this.valor = valor;
    }

    public String getId() { return id; }
    public String getIdVeiculo() { return idVeiculo; }
    public String getIdTipoDespesa() { return idTipoDespesa; }
    public String getDescricao() { return descricao; }
    public LocalDate getData() { return data; }
    public double getValor() { return valor; }

    @Override
    public String toString() {
        return id + ";" + idVeiculo + ";" + idTipoDespesa + ";" + descricao + ";" + data.format(fmt) + ";" + valor;
    }

    public static Movimentacao fromLine(String line) {
        String[] p = line.split(";",6);
        if (p.length < 6) return null;
        return new Movimentacao(p[0], p[1], p[2], p[3], LocalDate.parse(p[4]), Double.parseDouble(p[5]));
    }
}
