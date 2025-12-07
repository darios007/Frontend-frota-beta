package com.frotacerta.controller;

import com.frotacerta.model.Vehicle;
import com.frotacerta.model.TipoDespesa;
import com.frotacerta.model.Movimentacao;
import com.frotacerta.persistence.FileDAO;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AppController {
    private final FileDAO dao;
    private List<Vehicle> vehicles;
    private List<TipoDespesa> tipos;
    private List<Movimentacao> movs;

    public AppController(String baseDir) {
        this.dao = new FileDAO(baseDir);
        loadAll();
    }

    public void loadAll() {
        vehicles = dao.loadVehicles();
        tipos = dao.loadTipos();
        movs = dao.loadMovs();
    }

    public List<Vehicle> getVehicles() { return vehicles; }
    public List<TipoDespesa> getTipos() { return tipos; }
    public List<Movimentacao> getMovs() { return movs; }

    public void addVehicle(Vehicle v) {
        vehicles.add(v);
        dao.saveVehicles(vehicles);
    }

    public void updateVehicles(List<Vehicle> list) {
        this.vehicles = list;
        dao.saveVehicles(list);
    }

    public void addTipo(TipoDespesa t) {
        tipos.add(t);
        dao.saveTipos(tipos);
    }

    public void addMov(Movimentacao m) {
        movs.add(m);
        dao.saveMovs(movs);
    }

    // Reports
    public List<Movimentacao> movsByVehicle(String idVeiculo) {
        return movs.stream().filter(m -> m.getIdVeiculo().equals(idVeiculo)).collect(Collectors.toList());
    }

    public double sumByMonth(int year, int month) {
        return movs.stream().filter(m -> m.getData().getYear() == year && m.getData().getMonthValue() == month)
                .mapToDouble(Movimentacao::getValor).sum();
    }

    public double sumFuelByMonth(int year, int month, String fuelTipoId) {
        return movs.stream().filter(m -> m.getData().getYear() == year && m.getData().getMonthValue() == month
                && m.getIdTipoDespesa().equals(fuelTipoId)).mapToDouble(Movimentacao::getValor).sum();
    }

    public double sumIpvaByYear(int year, String ipvaTipoId) {
        return movs.stream().filter(m -> m.getData().getYear() == year && m.getIdTipoDespesa().equals(ipvaTipoId))
                .mapToDouble(Movimentacao::getValor).sum();
    }

    public List<Vehicle> inactiveVehicles() {
        return vehicles.stream().filter(v -> !v.isAtivo()).collect(Collectors.toList());
    }

    public List<Movimentacao> multasByVehicleYear(String vehicleId, int year, String multaTipoId) {
        return movs.stream().filter(m -> m.getIdVeiculo().equals(vehicleId) && m.getData().getYear() == year
                && m.getIdTipoDespesa().equals(multaTipoId)).collect(Collectors.toList());
    }
}
