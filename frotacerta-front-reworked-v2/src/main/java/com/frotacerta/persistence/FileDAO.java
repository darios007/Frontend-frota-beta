package com.frotacerta.persistence;

import com.frotacerta.model.Vehicle;
import com.frotacerta.model.TipoDespesa;
import com.frotacerta.model.Movimentacao;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FileDAO {
    private final Path base;

    public FileDAO(String baseDir) {
        this.base = Paths.get(baseDir);
        try {
            Files.createDirectories(base);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path p(String name) { return base.resolve(name); }

    // Vehicles
    public List<Vehicle> loadVehicles() {
        Path f = p("veiculos.txt");
        if (!Files.exists(f)) return new ArrayList<>();
        try {
            return Files.lines(f).map(Vehicle::fromLine).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveVehicles(List<Vehicle> list) {
        Path f = p("veiculos.txt");
        try {
            Files.write(f, list.stream().map(Vehicle::toString).collect(Collectors.toList()), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Tipos
    public List<TipoDespesa> loadTipos() {
        Path f = p("tipos_despesa.txt");
        if (!Files.exists(f)) return new ArrayList<>();
        try {
            return Files.lines(f).map(TipoDespesa::fromLine).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveTipos(List<TipoDespesa> list) {
        Path f = p("tipos_despesa.txt");
        try {
            Files.write(f, list.stream().map(TipoDespesa::toString).collect(Collectors.toList()), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Movimentacoes
    public List<Movimentacao> loadMovs() {
        Path f = p("movimentacoes.txt");
        if (!Files.exists(f)) return new ArrayList<>();
        try {
            return Files.lines(f).map(Movimentacao::fromLine).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveMovs(List<Movimentacao> list) {
        Path f = p("movimentacoes.txt");
        try {
            Files.write(f, list.stream().map(Movimentacao::toString).collect(Collectors.toList()), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
