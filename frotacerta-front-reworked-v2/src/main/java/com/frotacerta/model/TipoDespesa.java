package com.frotacerta.model;

public class TipoDespesa {
    private String id;
    private String descricao;

    public TipoDespesa(String id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public String getId() { return id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return id + ";" + descricao;
    }

    public static TipoDespesa fromLine(String line) {
        String[] p = line.split(";", 2);
        if (p.length < 2) return null;
        return new TipoDespesa(p[0], p[1]);
    }
}
