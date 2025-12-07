
package com.frotacerta.app;

import com.frotacerta.controller.AppController;
import com.frotacerta.model.Vehicle;
import com.frotacerta.model.Movimentacao;
import com.frotacerta.model.TipoDespesa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class MainApp {

    private final AppController controller;
    private final JFrame frame;
    private DefaultTableModel vehicleTableModel;
    private DefaultTableModel movTableModel;

    public MainApp(String baseDir) {
        controller = new AppController(baseDir);

        frame = new JFrame("Frota Certa - Sistema de Gestão da Frota");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1050, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Frota Certa - Sistema de Controle de Gastos da Frota", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(10,10,10,10));
        frame.add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 16));

        tabs.add("Veículos", buildVehiclesPanel());
        tabs.add("Movimentações", buildMovPanel());
        tabs.add("Relatórios", buildReportsPanel());

        frame.add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildVehiclesPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(new EmptyBorder(10,10,10,10));

        vehicleTableModel = new DefaultTableModel(new Object[]{"ID","Placa","Marca","Modelo","Ano","Ativo"},0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        JTable table = new JTable(vehicleTableModel);
        table.setRowHeight(28);
        refreshVehicles();

        JScrollPane sp = new JScrollPane(table);
        p.add(sp, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));

        JButton add = styledButton("Adicionar Veículo");
        add.addActionListener(e -> addVehicle());
        btns.add(add);

        JButton reload = styledButton("Recarregar");
        reload.addActionListener(e -> {
            controller.loadAll();
            refreshVehicles();
        });
        btns.add(reload);

        p.add(btns, BorderLayout.SOUTH);

        return p;
    }

    private JPanel buildMovPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(new EmptyBorder(10,10,10,10));

        movTableModel = new DefaultTableModel(new Object[]{"ID","Veículo","Tipo","Descrição","Data","Valor"},0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        JTable table = new JTable(movTableModel);
        table.setRowHeight(28);
        refreshMovs();

        JScrollPane sp = new JScrollPane(table);
        p.add(sp, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));

        JButton add = styledButton("Adicionar Movimentação");
        add.addActionListener(e -> addMov());
        btns.add(add);

        p.add(btns, BorderLayout.SOUTH);

        return p;
    }

    private JPanel buildReportsPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBorder(new EmptyBorder(10,10,10,10));

        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane sp = new JScrollPane(area);
        p.add(sp, BorderLayout.CENTER);

        JPanel btns = new JPanel(new GridLayout(3,2,10,10));

        JButton b1 = styledButton("Despesas por Veículo");
        b1.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(frame,"ID veículo:");
            if(id!=null){
                var list = controller.movsByVehicle(id);
                StringBuilder sb = new StringBuilder();
                list.forEach(m->sb.append(m.toString()).append("\n"));
                area.setText(sb.toString());
            }
        });
        btns.add(b1);

        JButton b2 = styledButton("Somatório Mensal");
        b2.addActionListener(e -> {
            String in = JOptionPane.showInputDialog(frame,"AAAA-MM:");
            if(in!=null && in.matches("\\d{4}-\\d{2}")){
                int y = Integer.parseInt(in.split("-")[0]);
                int m = Integer.parseInt(in.split("-")[1]);
                area.setText("Total: "+controller.sumByMonth(y,m));
            }
        });
        btns.add(b2);

        JButton b3 = styledButton("Combustível no Mês");
        b3.addActionListener(e -> {
            String tipo = JOptionPane.showInputDialog(frame,"ID Tipo Combustível:");
            String in = JOptionPane.showInputDialog(frame,"AAAA-MM:");
            if(tipo!=null && in!=null && in.matches("\\d{4}-\\d{2}")){
                int y = Integer.parseInt(in.split("-")[0]);
                int m = Integer.parseInt(in.split("-")[1]);
                area.setText("Total: "+controller.sumFuelByMonth(y,m,tipo));
            }
        });
        btns.add(b3);

        JButton b4 = styledButton("IPVA do Ano");
        b4.addActionListener(e -> {
            String tipo = JOptionPane.showInputDialog(frame,"ID Tipo IPVA:");
            String ano = JOptionPane.showInputDialog(frame,"AAAA:");
            if(tipo!=null && ano!=null && ano.matches("\\d{4}")){
                area.setText("Total: "+controller.sumIpvaByYear(Integer.parseInt(ano),tipo));
            }
        });
        btns.add(b4);

        JButton b5 = styledButton("Veículos Inativos");
        b5.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            controller.inactiveVehicles().forEach(v->sb.append(v.toString()).append("\n"));
            area.setText(sb.toString());
        });
        btns.add(b5);

        JButton b6 = styledButton("Multas por Ano");
        b6.addActionListener(e -> {
            String tipo = JOptionPane.showInputDialog(frame,"ID Tipo Multa:");
            String vid = JOptionPane.showInputDialog(frame,"ID Veículo:");
            String ano = JOptionPane.showInputDialog(frame,"AAAA:");
            if(tipo!=null && vid!=null && ano!=null){
                var list = controller.multasByVehicleYear(vid,Integer.parseInt(ano),tipo);
                StringBuilder sb = new StringBuilder();
                list.forEach(m->sb.append(m.toString()).append("\n"));
                area.setText(sb.toString());
            }
        });
        btns.add(b6);

        p.add(btns, BorderLayout.NORTH);

        return p;
    }

    private JButton styledButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setPreferredSize(new Dimension(240,50));
        return b;
    }

    private void refreshVehicles() {
        vehicleTableModel.setRowCount(0);
        for(Vehicle v: controller.getVehicles()){
            vehicleTableModel.addRow(new Object[]{
                    v.getId(), v.getPlaca(), v.getMarca(), v.getModelo(), v.getAno(), v.isAtivo()
            });
        }
    }

    private void refreshMovs() {
        movTableModel.setRowCount(0);
        for(Movimentacao m: controller.getMovs()){
            movTableModel.addRow(new Object[]{
                    m.getId(), m.getIdVeiculo(), m.getIdTipoDespesa(),
                    m.getDescricao(), m.getData(), m.getValor()
            });
        }
    }

    private void addVehicle() {
        JTextField placa = new JTextField();
        JTextField marca = new JTextField();
        JTextField modelo = new JTextField();
        JTextField ano = new JTextField();
        JCheckBox ativo = new JCheckBox("Ativo?", true);

        JPanel form = new JPanel(new GridLayout(6,2,10,10));
        form.add(new JLabel("Placa (AAA-0A00):"));
        form.add(placa);
        form.add(new JLabel("Marca:"));
        form.add(marca);
        form.add(new JLabel("Modelo:"));
        form.add(modelo);
        form.add(new JLabel("Ano:"));
        form.add(ano);
        form.add(new JLabel(""));
        form.add(ativo);

        if(JOptionPane.showConfirmDialog(frame,form,"Novo Veículo",
                JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {

            if(!Pattern.matches("[A-Z]{3}-\\d[A-Z]\\d{2}", placa.getText().toUpperCase())){
                JOptionPane.showMessageDialog(frame,"Placa inválida","Erro",JOptionPane.ERROR_MESSAGE);
                return;
            }

            try{
                int a = Integer.parseInt(ano.getText());
                Vehicle v = new Vehicle(UUID.randomUUID().toString(),
                        placa.getText().toUpperCase(), marca.getText(), modelo.getText(), a, ativo.isSelected());
                controller.addVehicle(v);
                refreshVehicles();
            }catch(Exception e){
                JOptionPane.showMessageDialog(frame,"Dados inválidos","Erro",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addMov() {
        List<Vehicle> veh = controller.getVehicles();
        List<TipoDespesa> tipos = controller.getTipos();

        if(veh.isEmpty() || tipos.isEmpty()){
            JOptionPane.showMessageDialog(frame,"Cadastre veículo e tipo primeiro!");
            return;
        }

        JComboBox<String> cbV = new JComboBox<>(veh.stream().map(Vehicle::getId).toArray(String[]::new));
        JComboBox<String> cbT = new JComboBox<>(tipos.stream().map(TipoDespesa::getId).toArray(String[]::new));

        JTextField desc = new JTextField();
        JTextField data = new JTextField(LocalDate.now().toString());
        JTextField valor = new JTextField();

        JPanel form = new JPanel(new GridLayout(6,2,10,10));
        form.add(new JLabel("Veículo (ID):"));
        form.add(cbV);
        form.add(new JLabel("Tipo (ID):"));
        form.add(cbT);
        form.add(new JLabel("Descrição:"));
        form.add(desc);
        form.add(new JLabel("Data:"));
        form.add(data);
        form.add(new JLabel("Valor:"));
        form.add(valor);

        if(JOptionPane.showConfirmDialog(frame,form,"Nova Movimentação",
                JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {

            try{
                Movimentacao m = new Movimentacao(
                        UUID.randomUUID().toString(),
                        (String)cbV.getSelectedItem(),
                        (String)cbT.getSelectedItem(),
                        desc.getText(),
                        LocalDate.parse(data.getText()),
                        Double.parseDouble(valor.getText())
                );
                controller.addMov(m);
                refreshMovs();
            }catch(Exception e){
                JOptionPane.showMessageDialog(frame,"Dados inválidos: "+e,"Erro",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args){
        String base = "data";
        if(args.length>0) base = args[0];
        MainApp app = new MainApp(base);
        SwingUtilities.invokeLater(app::show);
    }
}
